package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadHeapExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
        int address = heap.allocate(new IntValue(20));                  // 1 -> 20
        symTable.put("v", new RefValue(address, new IntType()));        // v -> (1, int)
    }

    @Test
    void readsTheValueAtTheAddress() throws MyException {
        assertEquals(new IntValue(20), new ReadHeapExp(new VarExp("v")).eval(symTable, heap));
    }

    @Test
    void readsThroughTwoReferences() throws MyException {
        int address = heap.allocate(new RefValue(1, new IntType()));    // 2 -> (1, int)
        symTable.put("a", new RefValue(address, new RefType(new IntType())));
        IExp e = new ReadHeapExp(new ReadHeapExp(new VarExp("a")));
        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void canBeUsedInArithmetic() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5)));
        assertEquals(new IntValue(25), e.eval(symTable, heap));
    }

    @Test
    void nonReferenceExpressionThrows() {
        IExp e = new ReadHeapExp(new ValueExp(new IntValue(1)));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void addressNotInTheHeapThrows() {
        symTable.put("u", new RefValue(0, new IntType()));               // declared, never allocated
        IExp e = new ReadHeapExp(new VarExp("u"));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void toStringIsRH() {
        assertEquals("rH(v)", new ReadHeapExp(new VarExp("v")).toString());
    }
}
