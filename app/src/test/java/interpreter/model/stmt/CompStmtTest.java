package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {
    private PrgState state;
    private final IStmt first = new VarDeclStmt("v", new IntType());
    private final IStmt second = new PrintStmt(new ValueExp(new IntValue(1)));

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();   // remove the placeholder program
    }

    @Test
    void pushesFirstOnTopOfSecond() throws MyException {
        new CompStmt(first, second).execute(state);
        assertEquals(first, state.getExeStack().pop());
        assertEquals(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void doesNotExecuteEitherStatement() throws MyException {
        new CompStmt(first, second).execute(state);
        assertFalse(state.getSymTable().isDefined("v"));
        assertEquals(0, state.getOut().size());
    }

    @Test
    void splitsOnlyTheOuterComposition() throws MyException {
        IStmt inner = new CompStmt(second, new NopStmt());
        new CompStmt(first, inner).execute(state);
        assertEquals(first, state.getExeStack().pop());
        assertEquals(inner, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void returnsTheSameState() throws MyException {
        assertSame(state, new CompStmt(first, second).execute(state));
    }
}