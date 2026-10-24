package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public class NopStmt implements IStmt{
    @Override
    public PrgState execute(PrgState prgState) throws MyException {
        return prgState;
    }
    @Override
    public String toString(){
        return "nop";
    }
}
