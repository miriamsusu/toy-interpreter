package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private StringValue fileName;
    private ValueExp file;

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n8\n");
        fileName = new StringValue(f.toString());
        file = new ValueExp(fileName);
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @AfterEach
    void closeOpenFiles() throws IOException {
        // on Windows an open reader stops @TempDir from deleting the folder
        for (BufferedReader reader : state.getFileTable().toMap().values())
            reader.close();
    }

    private ValueExp fileIn(String name, String content) throws IOException {
        Path f = dir.resolve(name);
        Files.writeString(f, content);
        return new ValueExp(new StringValue(f.toString()));
    }

    @Test
    void openAddsTheFileToTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));
    }

    @Test
    void anEqualFileNameFindsTheOpenFile() throws MyException {
        // a different StringValue object with the same text must find the same FileTable entry
        new OpenRFileStmt(file).execute(state);
        var sameName = new ValueExp(new StringValue(fileName.val()));
        new ReadFileStmt(sameName, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));
        assertThrows(StatementException.class, () -> new OpenRFileStmt(sameName).execute(state));
    }

    @Test
    void openWithNonStringExpressionThrows() {
        IStmt open = new OpenRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> open.execute(state));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void openingAMissingFileThrows() {
        var missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class, () -> new OpenRFileStmt(missing).execute(state));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    // ---------- readFile (S9) ----------

    @Test
    void readGivesTheNumbersInOrder() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(8), state.getSymTable().lookup("x"));
    }

    @Test
    void readGivesZeroAtTheEndOfTheFile() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);   // 7
        new ReadFileStmt(file, "x").execute(state);   // 8
        new ReadFileStmt(file, "x").execute(state);   // end of file
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readFromAnEmptyFileGivesZero() throws Exception {
        ValueExp empty = fileIn("empty.in", "");
        new OpenRFileStmt(empty).execute(state);
        new ReadFileStmt(empty, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readIntoUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "y").execute(state));
    }

    @Test
    void readIntoNonIntVariableThrows() throws MyException {
        new VarDeclStmt("s", new StringType()).execute(state);
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "s").execute(state));
    }

    @Test
    void readFromAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void readOfANonIntegerLineThrows() throws Exception {
        ValueExp bad = fileIn("bad.in", "abc\n");
        new OpenRFileStmt(bad).execute(state);
        assertThrows(StatementException.class, () -> new ReadFileStmt(bad, "x").execute(state));
    }

    @Test
    void readWithNonStringExpressionThrows() {
        IStmt read = new ReadFileStmt(new ValueExp(new IntValue(1)), "x");
        assertThrows(StatementException.class, () -> read.execute(state));
    }


    @Test
    void closeRemovesTheFileFromTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(fileName));
    }

    @Test
    void closingAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void closeWithNonStringExpressionThrows() {
        IStmt close = new CloseRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> close.execute(state));
    }

    @Test
    void fileCanBeOpenedAgainAfterClosing() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));
    }
}