package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();   // remove the placeholder program
    }

    @Test
    void declaresVariable() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        assertTrue(state.getSymTable().isDefined("a"));
    }

    @Test
    void intDefaultsToZero() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("a"));
    }

    @Test
    void boolDefaultsToFalse() throws MyException {
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("b"));
    }

    @Test
    void redeclaredVariableThrows() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        IStmt again = new VarDeclStmt("a", new BoolType());
        assertThrows(StatementException.class, () -> again.execute(state));
    }

    @Test
    void failedRedeclarationKeepsOldValue() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        state.getSymTable().update("a", new IntValue(5));
        IStmt again = new VarDeclStmt("a", new IntType());
        assertThrows(StatementException.class, () -> again.execute(state));
        assertEquals(new IntValue(5), state.getSymTable().lookup("a"));
    }

    @Test
    void declaresSeveralVariables() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertTrue(state.getSymTable().isDefined("a"));
        assertTrue(state.getSymTable().isDefined("b"));
    }
}