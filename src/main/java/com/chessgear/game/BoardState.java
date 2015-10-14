package com.chessgear.game;

import java.util.List;

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
     * Default constructor.
     */
    public BoardState() {

    }

    /**
     * Sets the boardstate to the starting position.
     */
    public void setToDefaultPosition() {

    }

    /**
     * Returns the Forsyth-Edwards Notation for the current board state.
     * @return FEN string representation of current board state.
     */
    public String toFEN() {
        return "";// TODO
    }

    /**
     * Returns the new boardstate after a move was done to it.
     * NOTE: This boardstate remains unchanged! We do not do a modification in place!
     * We clone the boardstate, then do a new move.
     * @param m Move to execute.
     * @return
     */
    public BoardState doMove(Move m) {
        // TODO
    }

    /**
     * Clone constructor.
     * @return A deep copy of this object.
     */
    public BoardState clone() {
        BoardState cloneBoardState = new BoardState();
    }

    /**
     * Returns a list of all pieces owned by a particular player on board of the given type.
     * @param player Player's pieces who we should look through.
     * @param type Type of piece we should look for.
     * @return A list of all pieces owned by the specified player, of the specified type.
     */
    List<Piece> getAllPiecesOfType(Player player, PieceType type) {

        // TODO

    }

    /**
     *
     * @param origin
     * @param target
     * @return
     */
    public boolean canMakeMove(Square origin, Square target) {

        // NOTE: We must check for absolute pins!

    }
}
