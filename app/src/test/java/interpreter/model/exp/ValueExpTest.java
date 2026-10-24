package interpreter.model.exp;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ValueExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void returnsIntConstant() throws MyException {
        assertEquals(new IntValue(3), new ValueExp(new IntValue(3)).eval(symTable));
    }

    @Test
    void returnsBoolConstant() throws MyException {
        assertEquals(new BoolValue(false), new ValueExp(new BoolValue(false)).eval(symTable));
    }
}
