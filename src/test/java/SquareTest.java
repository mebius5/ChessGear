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

}
