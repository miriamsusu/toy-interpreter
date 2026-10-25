package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();   // remove the placeholder program
    }

    @Test
    void printsConstantToOut() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(5))).execute(state);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void printsBoolValue() throws MyException {
        new PrintStmt(new ValueExp(new BoolValue(true))).execute(state);
        assertEquals(new BoolValue(true), state.getOut().get(0));
    }

    @Test
    void printsValueOfVariable() throws MyException {
        state.getSymTable().put("v", new IntValue(2));
        new PrintStmt(new VarExp("v")).execute(state);
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void printsResultOfExpression() throws MyException {
        new PrintStmt(new ArithExp(ArithOp.ADD,
                new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3)))).execute(state);
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void appendsInOrder() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(1))).execute(state);
        new PrintStmt(new ValueExp(new IntValue(2))).execute(state);
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getOut().get(1));
    }

    @Test
    void errorInExpressionIsPropagated() {
        IStmt stmt = new PrintStmt(new VarExp("undefined"));
        assertThrows(ExpressionException.class, () -> stmt.execute(state));
        assertEquals(0, state.getOut().size());
    }
}