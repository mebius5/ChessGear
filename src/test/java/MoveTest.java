/**
 * Created by Grady Xiao on 10/24/15.
 */
import com.chessgear.game.Move;
import com.chessgear.game.PieceType;
import com.chessgear.game.Player;
import com.chessgear.game.Square;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {
    @Test
    public void testPawnPushDetector() {
        Move d4 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        assertTrue(d4.isPawnPush());
        assertEquals(d4.getEnPassantTarget(), new Square("d3"));
    }
}
