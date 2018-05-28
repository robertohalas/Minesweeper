package minesweeper;

/**
 * Class just sets connections and SQL constants.
 */
public class DatabaseSetting {

    /**
     * Driver name
     */
    public static final String DRIVER_CLASS = "org.apache.derby.jdbc.ClientDriver";
    /**
     * Hostname URL
     */
    public static final String URL = "jdbc:derby://localhost/minesweeper";
    /**
     * User name for auth
     */
    public static final String USER = "minesweeper";
    /**
     * User password for auth
     */
    public static final String PASSWORD = "minesweeper";
    /**
     * SQL for creating database table
     */
    public static final String QUERY_CREATE_BEST_TIMES = "CREATE TABLE player_time (name VARCHAR(128) NOT NULL, best_time INT NOT NULL, level VARCHAR(128) NOT NULL)";
    /**
     * Adds the player score to database
     */
    public static final String QUERY_ADD_BEST_TIME = "INSERT INTO player_time (name, best_time, level) VALUES (?, ?, ?)";
    /**
     * Select all rows from table player_time
     */
    public static final String QUERY_SELECT_BEST_TIMES = "SELECT name, best_time, level FROM player_time";
    /**
     * Select all rows from table player_time ordered ascending
     */
    public static final String QUERY_SELECT_BEAT_BEST_TIME = "SELECT name, best_time, level FROM player_time ORDER BY best_time ASC";

    /**
     * Empty constructor.
     */
    private DatabaseSetting() {
    }
}
