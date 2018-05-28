package minesweeper;

import java.io.*;

/**
 * The class maintain main settings of the game.
 */
public class Settings implements Serializable {

    /**
     * Number of rows in field
     */
    private final int rowCount;
    /**
     * Number of columns in field
     */
    private final int columnCount;
    /**
     * Number of mines in field
     */
    private final int mineCount;
    /**
     * Quocient which describes the difficulty
     */
    private final int levelQuocient;
    /**
     * Name of the level
     */
    private String levelName;
    /**
     * Beginner settings
     */
    public static final Settings BEGINNER = new Settings(9, 9, 10);
    /**
     * Intermediate settings
     */
    public static final Settings INTERMEDIATE = new Settings(16, 16, 40);
    /**
     * Expert settings
     */
    public static final Settings EXPERT = new Settings(16, 30, 99);
    /**
     * Name of setting file
     */
    private static final String SETTING_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";

    /**
     * Constructor initializes private variables and count from parameters the
     * game quocient.
     *
     * @param rowCount amount of rows in field
     * @param columnCount amount of columns in field
     * @param mineCount amount of mines in field
     */
    public Settings(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        this.levelQuocient = rowCount * columnCount;

    }

    /**
     * Returns the level name.
     *
     * @return String level name
     */
    public String getLevelName() {
        if (levelQuocient < 100) {
            levelName = "BEGINNER";
            return levelName;
        }

        if (levelQuocient > 100 && levelQuocient < 400) {
            levelName = "INTERMEDIATE";
            return levelName;
        }

        if (levelQuocient > 400) {
            levelName = "EXPERT";
            return levelName;
        }

        return null;
    }

    /**
     * Returns the number of rows.
     *
     * @return the rowCount
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Returns the number of columns
     *
     * @return the columnCount
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returs the number of mines
     *
     * @return the mineCount
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Detects if the objects are same.
     *
     * @param o object
     * @return boolean returns true of objects are equal
     */
    public boolean equals(Object o) {
        if (this.getColumnCount() != ((Settings) o).getColumnCount()) {
            return false;
        }

        if (this.getRowCount() != ((Settings) o).getRowCount()) {
            return false;
        }

        if (this.getMineCount() != ((Settings) o).getMineCount()) {
            return false;
        }

        return true;
    }

    /**
     * Hashcode for this objects. Return value is counted from columns, rows and
     * mines numbers.
     *
     * @return int value of this object
     */
    public int hashCode() {
        return this.getRowCount() * this.getColumnCount() * this.getMineCount();
    }

    /**
     * Save settings object to the file or write the error statement.
     */
    public void save() {
        try {
            FileOutputStream file = new FileOutputStream(SETTING_FILE);
            try {
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(this);
            } catch (IOException e) {
                System.out.println("Cant save the settings!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cant open the file!");
        }
    }

    /**
     * Load settings object from the file. If the exception was occured then
     * write error statement and sets the default settings object.
     *
     * @return Settings loaded settings objects
     */
    public static Settings load() {
        try {
            FileInputStream file = new FileInputStream(SETTING_FILE);
            try {
                ObjectInputStream in = new ObjectInputStream(file);
                try {
                    Settings result = (Settings) in.readObject();
                    return result;
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                    return BEGINNER;
                }
            } catch (IOException e) {
                System.out.println(e);
                return BEGINNER;
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
            return BEGINNER;
        }
    }
}
