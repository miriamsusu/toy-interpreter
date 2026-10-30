package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
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

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getSymTable().put("a", new IntValue(0));
    }

    @Test
    void assignsConstantToDeclaredVariable() throws MyException {
        new AssignStmt("a", new ValueExp(new IntValue(5))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("a"));
    }

    @Test
    void assignsResultOfExpression() throws MyException {
        IStmt stmt = new AssignStmt("a",
                new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3))));
        stmt.execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("a"));
    }

    @Test
    void canUseVariableOnRightSide() throws MyException {
        state.getSymTable().update("a", new IntValue(4));
        IStmt stmt = new AssignStmt("a",
                new ArithExp(ArithOp.MUL, new VarExp("a"), new ValueExp(new IntValue(2))));
        stmt.execute(state);
        assertEquals(new IntValue(8), state.getSymTable().lookup("a"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        IStmt stmt = new AssignStmt("x", new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void typeMismatchThrows() {
        IStmt stmt = new AssignStmt("a", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void failedAssignmentLeavesValueUnchanged() throws MyException {
        IStmt stmt = new AssignStmt("a", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals(new IntValue(0), state.getSymTable().lookup("a"));
    }
}