package minesweeper.consoleui;

import minesweeper.UserInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import minesweeper.Minesweeper;
import minesweeper.core.Clue;
import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Mine;
import minesweeper.core.Tile;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {

    /**
     * Playing field.
     */
    private Field field;

    /**
     * Input reader.
     */
    private final BufferedReader input;

    public ConsoleUI() {
        this.input = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads line of text from the reader.
     *
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Starts the game.
     *
     * @param field field of mines and clues
     */
    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        do {
            if (field.getState() == GameState.SOLVED) {
                System.out.println("Hra bola uspesne ukoncena!");
                System.exit(0);
            }

            update();

            if (field.getState() == GameState.FAILED) {
                System.out.println("Prehral si");
                System.exit(0);
                return;
            }

            try {
                processInput();
            } catch (WrongFormatException ex) {
                Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }

    /**
     * Updates user interface - prints the field.
     */
    @Override

    public void update() {
        System.out.println("Remaining mines: " + field.getRemainingMineCount());
        System.out.println("Time: " + Minesweeper.getInstance().getPlayingSeconds() + " seconds");

        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.print(" " + column);
        }
        System.out.println();

        for (int row = 0; row < field.getRowCount(); row++) {

            System.out.print((char) (65 + row) + " ");

            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);

                if (tile.getState() == Tile.State.MARKED) {
                    System.out.print("M ");
                }

                if (tile.getState() == Tile.State.CLOSED) {
                    System.out.print("- ");
                }

                if (tile.getState() == Tile.State.OPEN) {

                    if (tile instanceof Mine) {
                        System.out.print("X ");
                    }

                    if (tile instanceof Clue) {
                        System.out.print(field.countAdjacentMines(row, column) + " ");
                    }
                }

                if (column + 1 == field.getColumnCount()) {
                    System.out.println();
                }
            }
        }
    }

    /**
     * Processes user input. Reads line from console and does the action on a
     * playing field according to input string.
     */
    private void processInput() throws WrongFormatException {

        Pattern patternOpen = Pattern.compile("O([A-I])([0-8])");
        Pattern patternMarked = Pattern.compile("M([A-I])([0-8])");
        Pattern patternExit = Pattern.compile("X");

        String userinput = readLine();

        Matcher matcherOpen = patternOpen.matcher(userinput.toUpperCase());
        Matcher matcherMarked = patternMarked.matcher(userinput.toUpperCase());
        Matcher matcherExit = patternExit.matcher(userinput.toUpperCase());

        boolean action = matcherOpen.matches();
        if (action == true) {
            int row = matcherOpen.group(1).charAt(0) - 65;
            int column = Integer.parseInt(matcherOpen.group(2));

            field.openTile(row, column);
            this.newGameStarted(field);
        } else {
            boolean action2 = matcherMarked.matches();
            if (action2 == true) {
                int row = matcherMarked.group(1).charAt(0) - 65;
                int column = Integer.parseInt(matcherMarked.group(2));

                field.markTile(row, column);
                this.newGameStarted(field);
            } else {
                boolean exit = matcherExit.matches();
                if (exit == true) {
                    System.out.println("Game ended");
                    System.exit(0);
                } else {
                    throw new WrongFormatException("\nBad request. Plaese try in these formats: \n"
                            + "mark tile: M(A-I)(0-8) \n"
                            + "open tile: O(A-I)(0-8) \n"
                            + "quit game: X\n");
                }
            }
        }

    }
}
