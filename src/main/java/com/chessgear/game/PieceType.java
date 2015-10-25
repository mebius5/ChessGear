package com.chessgear.game;

import com.chessgear.data.PGNParseException;

/**
 * Enumeration of all Chess piece types.
 * Created by Ran on 10/8/2015.
 */
public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    /**
     * Returns the PieceType encoded by a character containing its FEN notation.
     * @param c Character encoding a piece type.
     * @return Piece type.
     */
    public static PieceType parseCharacter(char c) {
        switch (c) {
            case 'p':
                return PAWN;
            case 'n':
                return KNIGHT;
            case 'b':
                return BISHOP;
            case 'r':
                return ROOK;
            case 'q':
                return QUEEN;
            case 'k':
                return KING;
            default:
                return PAWN;
        }
    }

    /**
     * Returns lowercase FEN character notation of this piece type.
     * @return Lowercase FEN character notation of the piece type.
     */
    public char getFENChar() {
        switch (this) {
            case PAWN:
                return 'p';
            case KNIGHT:
                return 'n';
            case BISHOP:
                return 'b';
            case ROOK:
                return 'r';
            case QUEEN:
                return 'q';
            case KING:
                return 'k';
            default:
                return 0;
        }
    }
}
