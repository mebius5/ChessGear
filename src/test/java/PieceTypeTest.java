/**
 * Created by Grady Xiao on 10/24/15.
 * JUnit Test for PieceType.java
 */
import org.junit.Test;
import static org.junit.Assert.*;
import com.chessgear.game.PieceType;

public class PieceTypeTest {
    @Test
    public void testParseCharacter(){
        assertEquals(PieceType.parseCharacter('p'), PieceType.PAWN);
        assertEquals(PieceType.parseCharacter('n'), PieceType.KNIGHT);
        assertEquals(PieceType.parseCharacter('b'), PieceType.BISHOP);
        assertEquals(PieceType.parseCharacter('r'), PieceType.ROOK);
        assertEquals(PieceType.parseCharacter('q'), PieceType.QUEEN);
        assertEquals(PieceType.parseCharacter('k'), PieceType.KING);
    }

    @Test
    public void testGetFENChar(){
        assertEquals(PieceType.PAWN.getFENChar(),'p');
        assertEquals(PieceType.KNIGHT.getFENChar(),'n');
        assertEquals(PieceType.BISHOP.getFENChar(),'b');
        assertEquals(PieceType.ROOK.getFENChar(),'r');
        assertEquals(PieceType.QUEEN.getFENChar(),'q');
        assertEquals(PieceType.KING.getFENChar(),'k');
    }
}
