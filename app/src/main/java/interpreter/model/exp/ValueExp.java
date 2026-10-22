package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public record ValueExp() implements IExp{
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        return null;
    }
}
