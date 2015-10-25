import com.chessgear.game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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

        Move e4 = new Move(Player.WHITE, PieceType.PAWN, new Square("e2"), new Square("e4"), false, null);
        BoardState white1 = defaultBoardState.doMove(e4);
        assertEquals(white1.toFEN(), "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Move e5 = new Move(Player.BLACK, PieceType.PAWN, new Square("e7"), new Square("e5"), false, null);
        BoardState black1 = white1.doMove(e5);
        assertEquals(black1.toFEN(), "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2");
        Move nf3 = new Move(Player.WHITE, PieceType.KNIGHT, new Square("g1"), new Square("f3"), false, null);
        BoardState white2 = black1.doMove(nf3);
        assertEquals(white2.toFEN(), "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
        Move nf6 = new Move(Player.BLACK, PieceType.KNIGHT, new Square("g8"), new Square("f6"), false, null);
        BoardState black2 = white2.doMove(nf6);
        assertEquals(black2.toFEN(), "rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3");
        Move be2 = new Move(Player.WHITE, PieceType.BISHOP, new Square("f1"), new Square("e2"), false, null);
        BoardState white3 = black2.doMove(be2);
        assertEquals(white3.toFEN(), "rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQK2R b KQkq - 3 3");
        Move be7 = new Move(Player.BLACK, PieceType.BISHOP, new Square("f8"), new Square("e7"), false, null);
        BoardState black3 = white3.doMove(be7);
        assertEquals(black3.toFEN(), "rnbqk2r/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQK2R w KQkq - 4 4");
        Move oo = new Move(Player.WHITE, PieceType.KING, new Square("e1"), new Square("g1"), true, null);
        BoardState white4 = black3.doMove(oo);
        assertEquals(white4.toFEN(), "rnbqk2r/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 b kq - 5 4");
        Move rg8 = new Move(Player.BLACK, PieceType.ROOK, new Square("h8"), new Square("g8"), false, null);
        BoardState black4 = white4.doMove(rg8);
        assertEquals(black4.toFEN(), "rnbqk1r1/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 w q - 6 5");

    }

}
