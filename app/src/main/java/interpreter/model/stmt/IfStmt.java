package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {

    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        IValue val = cond.eval(prgState.getSymTable());
        if (!val.getType().equals(new BoolType())){
            throw new StatementException("Not a bool type");
        }
        boolean b = ((BoolValue) val).val();
        prgState.getExeStack().push(b ? thenS : elseS);
        return prgState;
    }

    @Override
    public String toString() {
        return "if (" + cond + ") then (" + thenS + ") else (" + elseS + ")";
    }
}
