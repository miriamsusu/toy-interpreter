package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private static String run(IStmt program) {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        Controller controller = new Controller(new Repository(state));
        controller.setDisplayFlag(false);
        controller.allStep();
        return state.getOut().toString();
    }

    @Test void examplesProduceExpectedOutput() {
        assertEquals("[2]", run(Examples.ALL.get(0)));
        assertEquals("[18]", run(Examples.ALL.get(1)));
        assertEquals("[2]", run(Examples.ALL.get(2)));
    }
}
