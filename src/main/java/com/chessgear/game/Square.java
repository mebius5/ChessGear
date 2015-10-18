package com.chessgear.game;

/**
 * Object representation of a square on the chessboard.
 * Created by Ran on 10/8/2015.
 */
public class Square {

    /**
     * Indicates file of square.
     */
    private int x;

    /**
     * Indicates rank of square.
     */
    private int y;

    /**
     * Constructs a square.
     * @param x
     * @param y
     */
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns string of location.
     * @return
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        char file = (char)('a' + this.x);
        result.append(file);
        result.append(this.y + 1);

        return result.toString();
    }

}
