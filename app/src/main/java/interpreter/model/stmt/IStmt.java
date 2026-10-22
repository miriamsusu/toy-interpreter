package interpreter.model.stmt;

import interpreter.model.PrgState;

public interface IStmt {
    PrgState execute(PrgState prgState);
}
