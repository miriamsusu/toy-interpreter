package interpreter.model.type;

import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeTest {

    @Test
    void defaultValueIsNullAddressWithTheInnerType() {
        assertEquals(new RefValue(0, new IntType()), new RefType(new IntType()).defaultValue());
    }

    @Test
    void nestedRefTypesWithTheSameInnerTypeAreEqual() {
        assertEquals(new RefType(new RefType(new IntType())), new RefType(new RefType(new IntType())));
    }

    @Test
    void refRefIntDiffersFromRefInt() {
        assertNotEquals(new RefType(new RefType(new IntType())), new RefType(new IntType()));
    }

    @Test
    void refIntDiffersFromRefBool() {
        assertNotEquals(new RefType(new IntType()), new RefType(new BoolType()));
    }

    @Test
    void refValueHasARefTypeOfItsLocationType() {
        assertEquals(new RefType(new IntType()), new RefValue(1, new IntType()).getType());
        assertEquals(new RefType(new RefType(new IntType())),
                new RefValue(2, new RefType(new IntType())).getType());
    }

    @Test
    void toStringShowsTheNesting() {
        assertEquals("Ref(Ref(int))", new RefType(new RefType(new IntType())).toString());
        assertEquals("(2, Ref(int))", new RefValue(2, new RefType(new IntType())).toString());
    }
}
