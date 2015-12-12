package com.chessgear.game;

import com.chessgear.data.PGNParser;
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

    @Test
    public void testFENConstructor() {
        BoardState test = new BoardState("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        assertEquals(test.toFEN(), "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
    }

    @Test
    public void testIsBlocked() {
        assertTrue(defaultBoardState.isBlocked(new Square("a1"), new Square("a3")));
        assertTrue(defaultBoardState.isBlocked(new Square("a1"), new Square("c3")));
        assertFalse(defaultBoardState.isBlocked(new Square("a2"), new Square("b3")));
        assertFalse(defaultBoardState.isBlocked(new Square("b8"), new Square("c7")));
        assertTrue(defaultBoardState.isBlocked(new Square("b8"), new Square("d6")));
        assertFalse(defaultBoardState.isBlocked(new Square("c3"), new Square("e3")));
    }

    @Test
    public void testCanMakeMove() {

        assertTrue(defaultBoardState.canMakeMove(new Square("e2"), new Square("e4")));
        assertTrue(defaultBoardState.canMakeMove(new Square("a2"), new Square("a3")));
        assertTrue(defaultBoardState.canMakeMove(new Square("b1"), new Square("c3")));
        assertFalse(defaultBoardState.canMakeMove("c1", "d5"));
        assertFalse(defaultBoardState.canMakeMove("a1", "d1"));
    }

    @Test
    public void testGetPieceByTarget() {

        Piece p = defaultBoardState.getPieceByTarget(PieceType.PAWN, Player.WHITE, new Square("e4"), (char)0, -1);
        assertEquals(p.getLocation(), new Square("e2"));
        p = defaultBoardState.getPieceByTarget(PieceType.KNIGHT, Player.WHITE, new Square("c3"), (char)0, -1);
        assertEquals(p.getLocation(), "b1");

        BoardState testBoardState = new BoardState("r1bqkb1r/pppp1ppp/2n2n2/8/2BpP3/8/PPPN1PPP/R1BQK1NR w KQkq - 1 5");
        p = testBoardState.getPieceByTarget(PieceType.KNIGHT, Player.WHITE, new Square("f3"), 'd', -1);
        assertEquals(p.getLocation(), "d2");
        p = testBoardState.getPieceByTarget(PieceType.KNIGHT, Player.WHITE, new Square("f3"), 'g', -1);
        assertEquals(p.getLocation(), "g1");
        p = testBoardState.getPieceByTarget(PieceType.KNIGHT, Player.WHITE, new Square("f3"), (char)0, 1);
        assertEquals(p.getLocation(), "g1");
        p = testBoardState.getPieceByTarget(PieceType.KNIGHT, Player.WHITE, new Square("f3"), (char)0, 2);
        assertEquals(p.getLocation(), "d2");



    }

    @Test
    public void testEnPassant() {
        BoardState testBoardState = new BoardState("r1bqkbnr/ppppp1pp/2n5/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3");
        Piece p = testBoardState.getPieceByTarget(PieceType.PAWN, Player.WHITE, new Square("f6"), (char)0, -1);
        assertEquals(p.getLocation(), "e5");
    }

    @Test
    public void failureCasesTest() {
        /*
        BoardState testBoardState = new BoardState("r4rk1/pp3pbp/4p1p1/3q4/3P4/4BP2/PP1Q1P1P/R4R1K w - - 0 17");
        Piece qd2 = testBoardState.getPieceAt(new Square("d2"));
        assertNotNull(qd2);
        assertTrue(testBoardState.canMakeMove(new Square("d2"), new Square("e2")));
        Piece p = testBoardState.getPieceByTarget(PieceType.QUEEN, Player.WHITE, new Square("e2"), (char)0, -1);
        assertEquals(p.getLocation(), "d2");
        */

        BoardState failureCase2 = new BoardState("2R2rk1/pp3pbp/4p1p1/8/3P4/4BP2/qP2QP1P/5R1K b - - 0 19");
        List<Piece> candidates = failureCase2.getAllPiecesOfType(Player.BLACK, PieceType.ROOK);

        Player active = Player.BLACK;
        String s = "Rc8";
        PieceType type = PGNParser.getPieceType(s);
        Square target = PGNParser.extractTarget(s);
        char fileDisambiguation = PGNParser.getFileDisambiguation(s);
        int rankDisambiguation = PGNParser.getRankDisambiguation(s);
        Piece p = failureCase2.getPieceByTarget(type, active, target, fileDisambiguation, rankDisambiguation);
        Square origin = p.getLocation();
        PieceType promotionType = PGNParser.getPromotionType(s);
        Move m = new Move(active, type, origin, target, false, promotionType);
    }

    @Test
    public void testEquals() {
        assertTrue(this.defaultBoardState.equals(new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")));
    }

}
