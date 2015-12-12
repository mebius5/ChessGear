package com.chessgear.game;

/**
 * Enumeration of the two classes.
 * Created by Ran on 10/8/2015.
 */
public enum Player {

    WHITE, BLACK;

    /**
     * Returns the reverse of the current player.
     * @return If black, returns white. If white, returns black.
     */
    public Player toggle() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }

}
