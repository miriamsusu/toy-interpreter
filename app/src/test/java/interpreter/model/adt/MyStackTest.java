package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyStackTest {
    private IStack<Integer> stack;

    @BeforeEach
    void setUp(){
        stack= new MyStack<>();
    }

    @Test
    void newStackEmptyTest(){
        assertTrue(stack.isEmpty());
    }

    @Test
    void pushNotEmptyTest(){
        stack.push(1);
        assertFalse(stack.isEmpty());
    }
    @Test
    void popTest(){
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void exceptionTest(){
        var ex = assertThrows(ADTException.class, () -> stack.pop());
        assertEquals("pop from an empty stack", ex.getMessage());
    }
}
