package com.chessgear.game; /**
 * Created by Grady Xiao on 10/24/15.
 * JUnit Test for Piece.java
 */
import com.chessgear.game.PieceType;
import com.chessgear.game.Player;
import com.chessgear.game.Square;
import org.junit.Test;
import static org.junit.Assert.*;
import com.chessgear.game.Piece;

public class PieceTest {
    @Test
    public void testPieceConstructor(){
        PieceType type = PieceType.BISHOP;
        Player owner = Player.BLACK;
        Square location = new Square("e4");
        Piece test = new Piece(type, owner, location);
        assertEquals(test.getType(),type);
        assertEquals(test.getOwner(),owner);
        assertEquals(test.getLocation(),location);
    }

    @Test
    public void testGetFENChar(){
        PieceType type = PieceType.BISHOP;
        Square location = new Square("e4");
        Piece test = new Piece(type, Player.BLACK, location);
        assertEquals(test.getFENChar(),'b');
        Piece test2 = new Piece(type, Player.WHITE, location);
        assertEquals(test2.getFENChar(),'B');
    }

    @Test
    public void testClone(){
        PieceType type = PieceType.BISHOP;
        Player owner = Player.BLACK;
        Square location = new Square("e4");
        Piece original = new Piece(type, owner, location);
        Piece clone = original.clone();
        assertEquals(original.getType(),clone.getType());
        assertEquals(original.getOwner(), clone.getOwner());
        assertEquals(original.getLocation(),clone.getLocation());
        assertEquals(original.getFENChar(),clone.getFENChar());
    }

    @Test
    public void testSetPiece(){
        PieceType type = PieceType.PAWN;
        Player owner = Player.BLACK;
        Square location = new Square("e4");
        Piece test = new Piece(type, owner, location);
        test.setPieceType(PieceType.QUEEN);
        assertEquals(test.getType(),PieceType.QUEEN);
    }

    @Test
    public void testSetLocation(){
        PieceType type = PieceType.PAWN;
        Player owner = Player.BLACK;
        Square location = new Square("e4");
        Piece test = new Piece(type, owner, location);
        Square newLocation = new Square("e5");
        test.setLocation(newLocation);
        assertEquals(test.getLocation(),newLocation);
    }

}
