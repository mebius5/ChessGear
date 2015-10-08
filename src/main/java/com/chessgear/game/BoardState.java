package com.chessgear.game;

/**
 * Board state class.
 * Created by Ran on 10/8/2015.
 */
public class BoardState {

    /**
     * Array of all pieces in board.
     * Indexing is zero-indexed, [x][y] - i.e. b3 = [1][2], c7 = [2][6]
     */
    private Piece[][] pieces;

    /**
     * Flag indicating whether or not castling queenside for white is legal.
     */
    private boolean canWhiteCastleQueenSide;

    /**
     * Flag indicating whether or not castling kingside for white is legal.
     */
    private boolean canWhiteCastleKingside;

    /**
     * Flag indicating whether or not castling queenside for black is legal.
     */
    private boolean canBlackCastleQueenSide;

    /**
     * Flag indicating whether or not castling kingside for black is legal.
     */
    private boolean canBlackCastleKingSide;

    /**
     * If the last move was a double pawn push, this square indicates the en passant capture square.
     */
    private Square enPassantTarget;

    /**
     * Counts the # of half moves since the last pawn push or piece capture.
     */
    private int halfMoveCounter;

    /**
     * Counts the # of full moves elapsed since beginning of game.
     */
    private int fullMoveCounter;

    /**
     * Returns the Forsyth-Edwards Notation for the current board state.
     * @return FEN string representation of current board state.
     */
    public String toFEN() {
        return "";// TODO
    }

}
