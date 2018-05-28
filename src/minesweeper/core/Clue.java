package minesweeper.core;

/**
 * Clue tile.
 */
public class Clue extends Tile {

    /**
     * Value of the clue.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param value value of the clue
     */
    public Clue(int value) {
        this.value = value;
    }

    /**
     * Return value of te clue.
     *
     * @return int clue value
     */
    public int getValue() {
        return value;
    }

}
