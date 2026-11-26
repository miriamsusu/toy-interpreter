package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.IExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {
    private PrgState state;

    // while (v > 0) (print(v); v = v - 1)
    private final IExp cond = new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0)));
    private final IStmt body = new CompStmt(new PrintStmt(new VarExp("v")),
            new AssignStmt("v", new ArithExp(ArithOp.SUB, new VarExp("v"), new ValueExp(new IntValue(1)))));
    private final IStmt loop = new WhileStmt(cond, body);

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();   // remove the placeholder program
        new VarDeclStmt("v", new IntType()).execute(state);
    }

    /** Runs the loop to the end, one statement at a time, like Controller.allStep. */
    private void runLoopFrom(int start) throws MyException {
        state.getSymTable().update("v", new IntValue(start));
        state.getExeStack().push(loop);
        while (!state.getExeStack().isEmpty())
            state.getExeStack().pop().execute(state);
    }

    @Test
    void trueConditionPushesTheLoopThenTheBody() throws MyException {
        state.getSymTable().update("v", new IntValue(1));
        loop.execute(state);
        assertEquals(body, state.getExeStack().pop());   // body on top
        assertEquals(loop, state.getExeStack().pop());   // the loop under it
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());          // nothing has run yet
    }

    @Test
    void falseConditionPushesNothing() throws MyException {
        loop.execute(state);                             // v = 0
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void loopThatRunsZeroTimes() throws MyException {
        runLoopFrom(0);
        assertEquals(0, state.getOut().size());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopThatRunsOnce() throws MyException {
        runLoopFrom(1);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void loopThatRunsSeveralTimes() throws MyException {
        runLoopFrom(3);
        assertEquals(3, state.getOut().size());
        assertEquals(new IntValue(3), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getOut().get(1));
        assertEquals(new IntValue(1), state.getOut().get(2));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void nonBooleanConditionThrows() {
        var bad = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> bad.execute(state));
    }

    @Test
    void conditionCanBeABooleanConstant() throws MyException {
        new WhileStmt(new ValueExp(new BoolValue(false)), body).execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void toStringIsInfix() {
        assertEquals("(while ((v > 0)) (print(v); v = (v - 1)))", loop.toString());
    }
}
