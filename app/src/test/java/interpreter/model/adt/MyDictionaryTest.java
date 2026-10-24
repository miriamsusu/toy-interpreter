package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MyDictionaryTest {
   MyDictionary<Integer, Integer> dict;

   @BeforeEach
    void setUp(){
       dict=new MyDictionary<Integer, Integer>();
   }

   @Test
    void putTest() throws ADTException{
       dict.put(1,2);
       assertEquals(2,dict.lookup(1));
   }

   @Test
   void lookupOfMissingKeyThrows() {
       assertThrows(ADTException.class, () -> dict.lookup(99));
   }

   @Test
    void updateTest() throws ADTException{
       dict.put(1,2);
       dict.put(2,0);
       dict.update(1,3);
       assertEquals(3,dict.lookup(1));
   }

   @Test
    void updateThrowsExceptionTest(){
       assertThrows(ADTException.class, () -> dict.update(99,0));
   }

   @Test
    void isDefinedTest(){
       dict.put(1,2);
       assertTrue(dict.isDefined(1));
   }
}
