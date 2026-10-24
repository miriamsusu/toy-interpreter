package interpreter.model.exp;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class VarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() throws MyException {
        symTable = new MyDictionary<>();
        symTable.put("a", new IntValue(7));
    }

    @Test
    void returnsValueOfDefinedVariable() throws MyException {
        assertEquals(new IntValue(7), new VarExp("a").eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        assertThrows(ExpressionException.class, () -> new VarExp("x").eval(symTable));
    }
}
