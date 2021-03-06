package com.chessgear.game;

import org.junit.Test;

import static org.junit.Assert.*;

/***
 * Test for Move class
 */
public class MoveTest {
    @Test
    public void testMoveConstructor(){
        Player whoMoved = Player.WHITE;
        PieceType pieceType = PieceType.PAWN;
        Square origin = new Square("e7");
        Square destination = new Square("e8");
        PieceType promotionType = PieceType.QUEEN;
        Move e4 = new Move(whoMoved, pieceType, origin, destination, false, promotionType);
        assertEquals(e4.getWhoMoved(), Player.WHITE);
        assertEquals(e4.getPieceType(),pieceType);
        assertEquals(e4.getOrigin(), origin);
        assertEquals(e4.getDestination(), destination);
        assertFalse(e4.isCastling());
        assertEquals(e4. getPromotionType(), promotionType);
    }

    @Test
    public void testPawnPushDetector() {
        Move d4 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        assertTrue(d4.isPawnPush());
        assertEquals(d4.getEnPassantTarget(), new Square("d3"));
    }

    @Test
    public void testToString(){
        Move d4 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        assertEquals(d4.toString(),"d2d4");
    }

    @Test
    public void testEquals(){
        Move move1 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        Move move2 = new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null);
        assertTrue(move1.equals(move2));
        assertFalse(move1.equals(new String("2")));
    }
}
