package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap=new MyHeap();
    }

    private static IExp num(int n) { return new ValueExp(new IntValue(n)); }

    private IValue eval(RelOp op, int left, int right) throws MyException {
        return new RelationalExp(op, num(left), num(right)).eval(symTable,heap);
    }

    @Test
    void lessThan() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.LT, 1, 2));
        assertEquals(new BoolValue(false), eval(RelOp.LT, 2, 2));
    }

    @Test
    void lessOrEqual() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.LE, 2, 2));
        assertEquals(new BoolValue(false), eval(RelOp.LE, 3, 2));
    }

    @Test
    void equal() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.EQ, 4, 4));
        assertEquals(new BoolValue(false), eval(RelOp.EQ, 4, 5));
    }

    @Test
    void notEqual() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.NE, 4, 5));
        assertEquals(new BoolValue(false), eval(RelOp.NE, 4, 4));
    }

    @Test
    void greaterThan() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.GT, 3, 2));
        assertEquals(new BoolValue(false), eval(RelOp.GT, 2, 2));
    }

    @Test
    void greaterOrEqual() throws MyException {
        assertEquals(new BoolValue(true), eval(RelOp.GE, 2, 2));
        assertEquals(new BoolValue(false), eval(RelOp.GE, 1, 2));
    }

    @Test
    void worksWithVariables() throws MyException {
        symTable.put("a", new IntValue(5));
        symTable.put("b", new IntValue(7));
        IExp e = new RelationalExp(RelOp.LT, new VarExp("a"), new VarExp("b"));
        assertEquals(new BoolValue(true), e.eval(symTable,heap));
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp e = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable,heap));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp e = new RelationalExp(RelOp.LT, num(1), new ValueExp(new BoolValue(true)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable,heap));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void toStringIsInfix() {
        assertEquals("(1 <= 2)", new RelationalExp(RelOp.LE, num(1), num(2)).toString());
    }
}
