package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * GameTree builder helper class.
 */
public final class GameTreeBuilder {

    private List<BoardState> boardStateList;
    private List<Move> whiteHalfMoves;
    private List<Move> blackHalfMoves;
    private List<GameTreeNode> nodes;

    /**
     * Constructor. We pass the list of boardstates encountered over the course of a game, a list of white's half moves, and a list of black's half moves.
     * @param boardStateList List of board states.
     * @param whiteHalfMoves List of white's half moves.
     * @param blackHalfMoves List of black's half moves.
     */
    public GameTreeBuilder(List<BoardState> boardStateList, List<Move> whiteHalfMoves, List<Move> blackHalfMoves) {
        this.boardStateList = boardStateList;
        this.whiteHalfMoves = whiteHalfMoves;
        this.blackHalfMoves = blackHalfMoves;
    }

    /**
     * Constructor with PGN parser as input.
     * @param parser PGN Parser.
     * @throws PGNParseException if invalid PGN detected
     */
    public GameTreeBuilder(PGNParser parser) throws PGNParseException{
        this(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
    }

    /**
     * Output of our class. Returns a list of nodes that represents the game.
     * @return List of GameTreeNodes containing the boardstates of the game and the last moves made.
     */
    public List<GameTreeNode> getListOfNodes() {
        // If we've already computed the list of nodes, then return that.
        if (this.nodes != null) return this.nodes;

        this.nodes = new ArrayList<>();
        int counter = 0;
        for (BoardState b : this.boardStateList) {
            // Let the data structure assign ids. For now, set to 0.
            GameTreeNode currentNode = new GameTreeNode(0);
            currentNode.setBoardState(b);
            if (counter != 0) {
                int index = (counter - 1) / 2;
                // White's last move was this.
                if (counter % 2 != 0) {
                    currentNode.setLastMoveMade(this.whiteHalfMoves.get(index));
                } else {
                    currentNode.setLastMoveMade(this.blackHalfMoves.get(index));
                }
            }
            this.nodes.add(currentNode);
            counter++;
        }

        return this.nodes;
    }
    
}
