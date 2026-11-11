package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @TempDir
    Path dir;   // a new empty folder for every test

    private String logFile() {
        return dir.resolve("log.txt").toString();
    }

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        new Controller(new Repository(state, logFile()), false).allStep();   // display off
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example1HasFinalSymTable() throws MyException {
        PrgState state = run(Examples.EX1);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithAnEmptyFilePrintsZeroTwice() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example5PrintsFive() throws MyException {
        PrgState state = run(Examples.EX5);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void logFileHasTheInitialStateAndOneStatePerStep() throws Exception {
        run(Examples.EX1);   // 5 steps: two CompStmt splits, the declaration, the assignment, the print
        String log = Files.readString(dir.resolve("log.txt"));
        assertEquals(6, log.split("ExeStack:", -1).length - 1);
    }

    @Test
    void oneStepExecutesOnlyTheTopStatement() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        Controller controller = new Controller(new Repository(state, logFile()), false);
        controller.oneStep(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();   // empty the stack
        Controller controller = new Controller(new Repository(state, logFile()), false);
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertEquals("program state stack is empty", ex.getMessage());
    }

    @Test
    void errorInProgramIsPropagated() {
        IStmt bad = new AssignStmt("undeclared", new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> run(bad));
    }
}
