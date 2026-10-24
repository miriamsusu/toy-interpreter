package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    private static IExp bool(boolean b) { return new ValueExp(new BoolValue(b)); }

    @Test
    void andOfTrueAndTrueIsTrue() throws MyException {
        IExp e = new LogicExp(bool(true), bool(true), LogicOp.AND);
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void andOfTrueAndFalseIsFalse() throws MyException {
        IExp e = new LogicExp(bool(true), bool(false), LogicOp.AND);
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void orOfTrueAndFalseIsTrue() throws MyException {
        IExp e = new LogicExp(bool(true), bool(false), LogicOp.OR);
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void orOfFalseAndFalseIsFalse() throws MyException {
        IExp e = new LogicExp(bool(false), bool(false), LogicOp.OR);
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void nonBooleanFirstOperandThrows() {
        IExp e = new LogicExp(new ValueExp(new IntValue(1)), bool(true), LogicOp.AND);
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }

    @Test
    void nonBooleanSecondOperandThrows() {
        IExp e = new LogicExp(bool(true), new ValueExp(new IntValue(1)), LogicOp.OR);
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }
}
