package com.chessgear.game;

/**
 * Object Representation of PieceType
 * Enumeration of all Chess piece types.
 * Contains functions for PGN and fen
 */
public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    /**
     * Returns the PieceType encoded by a character containing its FEN notation.
     * @param c Character encoding a piece type.
     * @return Piece type.
     */
    public static PieceType parseCharacter(char c) {
        switch (Character.toLowerCase(c)) {
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
    public final char getFENChar() {
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
