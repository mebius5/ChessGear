package com.chessgear.game;

/**
 * Move class.
 * Created by Ran on 10/8/2015.
 */
public class Move {

    /**
     * Whose move was it.
     */
    private Player whoMoved;

    /**
     * What type of piece was moved.
     */
    private PieceType pieceType;

    /**
     * Origin square.
     */
    private Square origin;

    /**
     * Destination square.
     */
    private Square destination;

    /**
     * Null if not pawn promotion, else, type of piece promoted to.
     */
    private PieceType promotionType;

    /**
     * Flag that indicates whether the move is a castling move.
     */
    private boolean castling;

}
