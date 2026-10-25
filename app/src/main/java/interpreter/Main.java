package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;

public class Main {
    public static void main(String[] args) {
        int i = 1;
        for (IStmt program : Examples.ALL) {
            System.out.println("===== Example " + i++ + ": " + program);
            try {
                PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
                Controller controller = new Controller(new Repository(state),true);
                controller.allStep();
                System.out.println("Output: " + state.getOut());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}