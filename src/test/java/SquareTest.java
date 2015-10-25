import static org.junit.Assert.*;

import com.chessgear.game.Square;
import org.junit.Test;

/**
 * Test cases for square class.
 * Created by Ran on 10/18/2015.
 */
public class SquareTest {

    @Test
    public void stringConversionTest() {
        Square a1 = new Square(0, 0);
        Square d4 = new Square(3, 3);

        assertEquals(a1.toString(), "a1");
        assertEquals(d4.toString(), "d4");
    }

    @Test
    public void stringConstructorTest() {
        Square a1 = new Square("a1");
        Square d4 = new Square("d4");
        assertEquals(a1.toString(), "a1");
        assertEquals(d4.toString(), "d4");
    }

    @Test
    public void getXYTest(){
        Square test = new Square("a1");
        assertEquals(test.getX(), 0);
        assertEquals(test.getY(), 0);
    }

    @Test
    public void squareEqualsTest(){
        Square test = new Square("b3");
        Square same = new Square("b3");
        Square different = new Square("c3");
        assertTrue(test.equals(same));
        assertFalse(test.equals(different));
    }

}
