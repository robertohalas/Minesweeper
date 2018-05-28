package minesweeper.core;

import java.io.Serializable;
import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field implements Serializable{

    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;
    /**
     * Number of tiles in certain state *
     */
    private int stateCount;

    /**
     * Constructor.
     *
     * @param rowCount row count
     * @param columnCount column count
     * @param mineCount mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];
        generate();
        stateCount = 0;
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row row number
     * @param column column number
     */
    private void openTilesWithMines() {
        if (this.getState() == GameState.FAILED) {
            for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    if (tiles[row][column] instanceof Mine && tiles[row][column].getState() == Tile.State.CLOSED) {
                        tiles[row][column].setState(Tile.State.OPEN);
                    }
                    if (tiles[row][column] instanceof Clue && tiles[row][column].getState() == Tile.State.MARKED) {
                        tiles[row][column].setState(Tile.State.WRONG_MARKED);
                    }
                }
            }
        }
        else if (this.getState() == GameState.SOLVED){
           for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    if (tiles[row][column] instanceof Clue && tiles[row][column].getState() == Tile.State.CLOSED) {
                        tiles[row][column].setState(Tile.State.OPEN);
                    }
                    if (tiles[row][column] instanceof Mine && tiles[row][column].getState() == Tile.State.CLOSED) {
                        tiles[row][column].setState(Tile.State.MARKED);
                    }
                }
            } 
        }
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        final Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if (tile instanceof Mine) {
                state = GameState.FAILED;
                openTilesWithMines();
                return;
            }
            if (tile instanceof Clue && this.countAdjacentMines(row, column) == 0) {
                this.openAdjacentTiles(row, column);
            }

            if (isSolved()) {
                state = GameState.SOLVED;
                openTilesWithMines();
            }
        }
    }

    /**
     * Marks tile at specified indeces.
     *
     * @param row row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        final Tile tile = tiles[row][column];

        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
        } else if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.QUEST);
        } else if (tile.getState() == Tile.State.QUEST) {
            tile.setState(Tile.State.CLOSED);
        }
    }

    /**
     * Get tile row.
     *
     * @return the rowCount
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Get tile column.
     *
     * @return the columnCount
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Get mine count.
     *
     * @return the mineCount
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Get tile state.
     *
     * @return the state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Get field tile at specific location.
     *
     * @param row tile row
     * @param column tile column
     * @return Tile specific field tile
     */
    public Tile getTile(int row, int column) {
        if (row >= 0 && row < this.getRowCount() && column >= 0 && column < this.getColumnCount()) {
            return tiles[row][column];
        } else {
            throw new IllegalArgumentException("Wrong argument row/column!");
        }
    }

    /**
     *
     * @return Tile specific field tile
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        generateMines();
        fillWithClues();
    }

    /**
     * Generate random mines.
     */
    private void generateMines() {
        Random random = new Random();
        for (int i = 0; i < mineCount; i++) {
            int ranRow = random.nextInt(rowCount);
            int ranColumn = random.nextInt(columnCount);
            if (tiles[ranRow][ranColumn] == null) {
                tiles[ranRow][ranColumn] = new Mine();
            }
            else
                i--;
        }

    }

    /**
     * Fill with clues - field tile which not contain mine just only number of
     * adjecent mines.
     */
    private void fillWithClues() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (!(tiles[row][column] instanceof Mine)) {
                    tiles[row][column] = new Clue(countAdjacentMines(row, column));
                }
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {

        if (mineCount == rowCount * columnCount - this.getNumberOfOpen(Tile.State.OPEN)) {
            return true;
        }

        return false;
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the
     * field.
     *
     * @param row row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    public int countAdjacentMines(int row, int column) {
        int count = 0;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    /**
     * Returns number of tiles in certain state.
     *
     * @return int
     * @param state
     */
    private int getNumberOfOpen(Tile.State state) {

        stateCount = 0;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                if (getTile(row, col).getState() == state) {
                    stateCount++;
                }
            }
        }

        return stateCount;
    }

    /**
     * Open adjacent tile if tile has no clue.
     *
     * @param row row of tile
     * @param column column of tile
     */
    private void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                int actColumn = column + columnOffset;
                if (actRow >= 0 && actRow < getRowCount() && actColumn >= 0 && actColumn < getColumnCount()) {
                    if (tiles[actRow][actColumn] instanceof Clue
                            && tiles[actRow][actColumn].getState() == Tile.State.CLOSED) {

                        tiles[actRow][actColumn].setState(Tile.State.OPEN);
                        try {
                            if (this.countAdjacentMines(actRow, actColumn) == 0) {
                                this.openAdjacentTiles(actRow, actColumn);

                            }
                        } catch (java.lang.StackOverflowError e) {
                            System.out.println(e);
                        }

                    }
                }
            }
        }
    }

    /**
     * Get number of remaining mines.
     *
     * @return number of remaining mines
     */
    public int getRemainingMineCount() {
        return mineCount - getNumberOfOpen(Tile.State.MARKED);
    }
}
