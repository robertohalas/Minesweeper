package minesweeper;

import minesweeper.core.Field;

/**
 * Implementing an interface allows a class to starts new game and update
 * player's field.
 */
public interface UserInterface {

    /**
     * Starts new game.
     *
     * @param field field of mines and clues
     */
    void newGameStarted(Field field);

    /**
     * Updates game field and show current state of tiles in field.
     */
    void update();

}
