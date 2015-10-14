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

}
