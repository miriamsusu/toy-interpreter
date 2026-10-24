package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MyListTest {
    MyList<Integer> l;
    @BeforeEach
    void setUp(){
        l= new MyList<>();
    }

    @Test
    void addTest(){
        l.add(1);
        l.add(2);
        assertEquals(2,l.size());
    }

    @Test
    void getTest() throws ADTException {
        l.add(1);
        l.add(0);
        l.add(2);

        assertEquals(0,l.get(1));
    }

    @Test
    void getThrowsExceptionTest(){
        assertThrows(ADTException.class, ()->l.get(5));
    }

    @Test
    void sizeTest(){
        assertEquals(0,l.size());
    }
}
