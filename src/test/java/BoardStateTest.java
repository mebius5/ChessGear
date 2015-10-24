import com.chessgear.game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Boardstate unit tests.
 * Created by Ran on 10/21/2015.
 */
public class BoardStateTest {

    private BoardState defaultBoardState;

    @Before
    public void intialize() {
        defaultBoardState = new BoardState();
        defaultBoardState.setToDefaultPosition();
    }

    @Test
    public void testFEN() {
        assertEquals(defaultBoardState.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void testGetPieceAt() {
        List<Piece> whiteKingList = defaultBoardState.getAllPiecesOfType(Player.WHITE, PieceType.KING);
        assertEquals(whiteKingList.size(), 1);
        assertEquals(whiteKingList.get(0).getLocation(), new Square("e1"));
    }

    @Test
    public void testDoMove() {

        Move d4 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        BoardState pawnPushed = defaultBoardState.doMove(d4);
        assertEquals(pawnPushed.toFEN(), "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");

    }

}
