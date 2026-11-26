package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

    @Test
    void equalStringsAreEqual() {
        assertEquals(new StringValue("test.in"), new StringValue("test.in"));
    }

    @Test
    void equalStringsHaveTheSameHashCode() {
        assertEquals(new StringValue("test.in").hashCode(), new StringValue("test.in").hashCode());
    }

    @Test
    void differentStringsAreNotEqual() {
        assertNotEquals(new StringValue("a.in"), new StringValue("b.in"));
    }

    @Test
    void typeIsString() {
        assertEquals(new StringType(), new StringValue("x").getType());
    }

    @Test
    void toStringAddsQuotes() {
        assertEquals("\"test.in\"", new StringValue("test.in").toString());
    }
}
