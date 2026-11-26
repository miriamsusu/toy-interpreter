package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsOneTwoThree() {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void getReturnsTheAllocatedValue() {
        int address = heap.allocate(new IntValue(10));
        assertEquals(new IntValue(10), heap.get(address));
    }

    @Test
    void getOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(1));
    }

    @Test
    void addressZeroIsNeverAllocated() {
        heap.allocate(new IntValue(10));
        assertFalse(heap.isDefined(0));
        assertThrows(ADTException.class, () -> heap.get(0));
    }

    @Test
    void updateChangesTheValue() {
        int address = heap.allocate(new IntValue(10));
        heap.update(address, new IntValue(11));
        assertEquals(new IntValue(11), heap.get(address));
    }

    @Test
    void updateOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(5, new IntValue(1)));
    }

    @Test
    void isDefinedOnlyForAllocatedAddresses() {
        int address = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(address));
        assertFalse(heap.isDefined(address + 1));
    }

    @Test
    void getContentIsAReadOnlyCopy() {
        heap.allocate(new IntValue(10));
        Map<Integer, ?> content = heap.getContent();
        assertEquals(Map.of(1, new IntValue(10)), content);
        assertThrows(UnsupportedOperationException.class, () -> content.remove(1));
    }

    @Test
    void setContentReplacesTheLocationsButAddressesAreNotReused() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        heap.setContent(Map.of(2, new IntValue(20)));   // location 1 collected
        assertFalse(heap.isDefined(1));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }
}
