package com.chessgear.game;

/**
 * Object representation of a chess piece.
 * Created by Ran on 10/8/2015.
 */
public class Piece {

    /**
     * TYpe of piece.
     */
    private PieceType type;

    /**
     * Onwer of the piece.
     */
    private Player owner;

    /**
     * Constructor for Piece object.
     * @param type
     * @param owner
     */
    public Piece(PieceType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }
}
