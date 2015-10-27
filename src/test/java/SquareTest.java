import static org.junit.Assert.*;

import com.chessgear.game.Square;
import org.junit.Test;

/**
 * JUnit Test for Square.java
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
    public void getXYDisplacementTest() {
        Square test = new Square("a1");
        Square test2 = new Square("a3");
        assertEquals(test.getXDisplacement(test2),0);
        assertEquals(test.getYDisplacement(test2),2);
    }

    @Test
    public void isTest(){
        Square test = new Square("a1");
        Square sameFile = new Square("a5");
        Square sameRank = new Square("c1");
        Square sameDiagonal = new Square("d4");
        assertTrue(test.isOnSameFile(sameFile));
        assertTrue(test.isOnSameRank(sameRank));
        assertTrue(test.isOnDiagonal(sameDiagonal));
        assertFalse(test.isOnSameFile(sameRank));
        assertFalse(test.isOnSameRank(sameFile));
        assertFalse(test.isOnDiagonal(sameRank));
    }

    @Test
    public void squareEqualsTest(){
        Square test = new Square("b3");
        Square sameSquare = new Square("b3");
        String sameString = "b3";
        Square differentSquare = new Square("c3");
        String differentString = "c3";
        assertTrue(test.equals(sameSquare));
        assertTrue(test.equals(sameString));
        assertFalse(test.equals(differentSquare));
        assertFalse(test.equals(differentString));
    }

}
