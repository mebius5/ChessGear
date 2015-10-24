package com.chessgear.game;

import java.util.Scanner;

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
     * Location of the piece.
     */
    private Square location;

    /**
     * Constructor for Piece object.
     * @param type Type of piece
     * @param owner Owner of piece
     * @param location Location of piece
     */
    public Piece(PieceType type, Player owner, Square location) {
        this.type = type;
        this.owner = owner;
        this.location = location;
    }

    /**
     * Accessor for piece type.
     * @return Type of piece.
     */
    public PieceType getType() {
        return this.type;
    }

    /**
     * Accessor for owner.
     * @return Player that owns this piece
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Clone method for Piece.
     * @return Deep copy of this piece.
     */
    public Piece clone() {
        Piece clone = new Piece(this.type, this.owner, this.location);
        return clone;
    }

    /**
     * Mutator for location.
     * @param target New location of piece.
     */
    public void setLocation(Square target) {
        this.location = target;
    }

    /**
     * Returns the FEN character representation of this piece.
     * @return FEN character representation of this piece.
     */
    public char getFENChar() {
        switch(this.getOwner()) {
            case WHITE:
                return Character.toUpperCase(this.getType().getFENChar());
            case BLACK:
                return this.getType().getFENChar();
            default:
                return 0;
        }
    }

    /**
     * Accessor for the location of the piece.
     * @return Location of piece.
     */
    public Square getLocation() {
        return this.location;
    }

    /**
     * Mutator for piece type.
     * @param type Type of piece.
     */
    public void setPieceType(PieceType type) {
        this.type = type;
    }

}
