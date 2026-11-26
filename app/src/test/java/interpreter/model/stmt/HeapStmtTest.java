package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);   // Ref int v
        new VarDeclStmt("n", new IntType()).execute(state);                // int n
    }

    private static ValueExp num(int n) { return new ValueExp(new IntValue(n)); }


    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void secondNewAllocatesANewAddress() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new NewStmt("v", num(30)).execute(state);
        assertEquals(new RefValue(2, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));   // the old location is still there
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void newOfARefRefStoresTheReference() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new VarDeclStmt("a", new RefType(new RefType(new IntType()))).execute(state);
        new NewStmt("a", new VarExp("v")).execute(state);
        assertEquals(new RefValue(2, new RefType(new IntType())), state.getSymTable().lookup("a"));
        assertEquals(new RefValue(1, new IntType()), state.getHeap().get(2));
    }

    @Test
    void newOfAnUndeclaredVariableThrows() {
        assertThrows(StatementException.class, () -> new NewStmt("x", num(1)).execute(state));
    }

    @Test
    void newOfANonReferenceVariableThrows() {
        assertThrows(StatementException.class, () -> new NewStmt("n", num(1)).execute(state));
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
        assertTrue(state.getHeap().getContent().isEmpty());   // nothing was allocated
    }


    @Test
    void writeChangesTheHeapLocationButNotTheVariable() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new WriteHeapStmt("v", num(30)).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void writeToAnUndeclaredVariableThrows() {
        assertThrows(StatementException.class, () -> new WriteHeapStmt("x", num(1)).execute(state));
    }

    @Test
    void writeToANonReferenceVariableThrows() {
        assertThrows(StatementException.class, () -> new WriteHeapStmt("n", num(1)).execute(state));
    }

    @Test
    void writeToAnAddressNotInTheHeapThrows() {
        // v is only declared: (0, int), and address 0 is never allocated
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", num(1)).execute(state));
    }

    @Test
    void writeOfAValueOfTheWrongTypeThrows() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", exp).execute(state));
        assertEquals(new IntValue(20), state.getHeap().get(1));   // unchanged
    }
}
