package minesweeper.core;

import java.io.Serializable;

/**
 * Tile of a field.
 */
public abstract class Tile implements Serializable{

    /**
     * Tile states.
     */
    public enum State {

        /**
         * Open tile.
         */
        OPEN,
        /**
         * Closed tile.
         */
        CLOSED,
        /**
         * Marked tile.
         */
        MARKED,
        /**
         * Questioned tile
         */
        QUEST,
        /**
         * Wrong marked tile.
         */
        WRONG_MARKED,
    }

    /**
     * Tile state.
     */
    private State state = State.CLOSED;

    /**
     * Returns current state of this tile.
     *
     * @return current state of this tile
     */
    public State getState() {
        return state;
    }

    /**
     * Sets current current state of this tile.
     *
     * @param state current state of this tile
     */
    void setState(State state) {
        this.state = state;
    }
}
