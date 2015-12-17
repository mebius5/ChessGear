package com.chessgear.game;

/**
 * Object representaiton of a player
 * Enum class with the different player types
 */
public enum Player {

    WHITE, BLACK;

    /**
     * Returns the reverse of the current player.
     * @return If black, returns white. If white, returns black.
     */
    public final Player toggle() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }

}
