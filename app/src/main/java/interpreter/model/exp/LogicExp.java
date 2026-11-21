package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(IExp left, IExp right, LogicOp op) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable,  IHeap heap) throws MyException {
        IValue v1 = left.eval(symTable,heap);
        if (!v1.getType().equals(new BoolType()))
            throw new ExpressionException("first operand " + left + " is not a boolean");
        IValue v2 = right.eval(symTable,heap);
        if (!v2.getType().equals(new BoolType()))
            throw new ExpressionException("second operand " + right + " is not a boolean");
        boolean b1 = ((BoolValue) v1).val();
        boolean b2 = ((BoolValue) v2).val();

        if(op==LogicOp.AND){
            return new BoolValue(b1 && b2);
        }
        else
        {
            return new BoolValue(b1 || b2);
        }
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}
