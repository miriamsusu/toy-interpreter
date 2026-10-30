package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private static PrgState newState(IStmt program) {
        return PrgState.of(program);
    }

    private static PrgState run(IStmt program) throws MyException {
        PrgState state = newState(program);
        new Controller(new Repository(state), false).allStep();   // display off
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.ex1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.ex2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.ex3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example1HasFinalSymTable() throws MyException {
        PrgState state = run(Examples.ex1);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void oneStepExecutesOnlyTheTopStatement() throws MyException {
        PrgState state = newState(new NopStmt());
        Controller controller = new Controller(new Repository(state), false);
        controller.oneStep(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = newState(new NopStmt());
        state.getExeStack().pop();   // empty the stack
        Controller controller = new Controller(new Repository(state), false);
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertEquals("program state stack is empty", ex.getMessage());
    }

    @Test
    void errorInProgramIsPropagated() {
        IStmt bad = new AssignStmt("undeclared", new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> run(bad));
    }
}