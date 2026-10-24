package interpreter.repository;

import interpreter.model.PrgState;

import java.util.List;

public class Repository implements IRepository{
    List<PrgState> states;
    public Repository(PrgState state){
        states.add(state);
    }
    @Override
    public PrgState getCrtPrg() {
        return states.get(0);
    }
}
