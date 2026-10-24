package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public record PrintStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        IValue val=exp.eval(prgState.getSymTable());
        prgState.getOut().add(val);
        return prgState;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}
