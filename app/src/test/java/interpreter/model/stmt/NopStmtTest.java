package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();   // remove the placeholder program
        state.getSymTable().put("a", new IntValue(3));
        state.getOut().add(new IntValue(7));
    }

    @Test
    void returnsTheSameState() throws MyException {
        assertSame(state, new NopStmt().execute(state));
    }

    @Test
    void leavesExeStackUnchanged() throws MyException {
        new NopStmt().execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void leavesSymTableUnchanged() throws MyException {
        new NopStmt().execute(state);
        assertEquals(new IntValue(3), state.getSymTable().lookup("a"));
        assertFalse(state.getSymTable().isDefined("b"));
    }

    @Test
    void leavesOutUnchanged() throws MyException {
        new NopStmt().execute(state);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(7), state.getOut().get(0));
    }
}