package com.chessgear.game;

/**
 * Enumeration of all Chess piece types.
 * Created by Ran on 10/8/2015.
 */
public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    /**
     * Returns lowercase FEN character notation of this piece type.
     * @return
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
