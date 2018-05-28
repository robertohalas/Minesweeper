package minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

/**
 * Player times.
 */
public class BestTimes implements Iterable<BestTimes.PlayerTime> {

    /**
     * List of best player times.
     */
    private List<PlayerTime> playerTimes = new ArrayList<PlayerTime>();

    /**
     * Returns an iterator over a set of best times.
     *
     * @return an iterator
     */
    public Iterator<PlayerTime> iterator() {
        return playerTimes.iterator();
    }

    /**
     * Adds player time to best times.
     *
     * @param name name to the player
     * @param time player time in seconds
     * @param level name to the level
     * @throws java.sql.SQLException
     */
    public void addPlayerTime(String name, int time, String level) throws SQLException {
        PlayerTime player = new PlayerTime(name, time, level);
        playerTimes.add(player);
        Collections.sort(playerTimes);
        insertToDB(player);
    }

    /**
     * Insert into database players scores or write error statement of error
     * occured.
     *
     * @param playerTime
     */
    private void insertToDB(PlayerTime playerTime) throws SQLException {
        try {
            Class.forName(DatabaseSetting.DRIVER_CLASS);
            try {
                Connection connection = DriverManager.getConnection(DatabaseSetting.URL, DatabaseSetting.USER, DatabaseSetting.PASSWORD);

                java.sql.Statement stm = connection.createStatement();
                try {
                    stm.executeUpdate(DatabaseSetting.QUERY_CREATE_BEST_TIMES);
                } catch (Exception e) {

                }
                stm.close();

                PreparedStatement pstm = connection.prepareStatement(DatabaseSetting.QUERY_ADD_BEST_TIME);
                pstm.setString(1, playerTime.getName());
                pstm.setInt(2, playerTime.getTime());
                pstm.setString(3, playerTime.getLevel());
                try {
                    pstm.execute();
                    pstm.close();
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Cant write to database: " + e.getMessage());
                }

            } catch (SQLException e) {
                System.out.println("SQL error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

    }

    /**
     * Select all rows from table player_time from database.
     */
    private void selectFromDB() {
        try {
            Class.forName(DatabaseSetting.DRIVER_CLASS);

            try {
                Class.forName(DatabaseSetting.DRIVER_CLASS);
                Connection connection = DriverManager.getConnection(DatabaseSetting.URL,
                        DatabaseSetting.USER, DatabaseSetting.PASSWORD);
                java.sql.Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery(DatabaseSetting.QUERY_SELECT_BEST_TIMES);

                playerTimes.clear();

                while (rs.next()) {
                    if (rs.getString(3).equalsIgnoreCase(Minesweeper.getInstance().getSetting().getLevelName())) {
                        PlayerTime pt = new PlayerTime(rs.getString(1), rs.getInt(2), rs.getString(3));
                        playerTimes.add(pt);
                        Collections.sort(playerTimes);
                    }
                }
                stm.close();
                connection.close();

            } catch (SQLException e) {

                System.out.println("SQL error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    /**
     * Select best time from database.
     *
     * @return int best time in seconds selected from database
     */
    public int selectBestTimeFromDB() {
        try {
            Class.forName(DatabaseSetting.DRIVER_CLASS);

            try {
                Class.forName(DatabaseSetting.DRIVER_CLASS);
                Connection connection = DriverManager.getConnection(DatabaseSetting.URL,
                        DatabaseSetting.USER, DatabaseSetting.PASSWORD);
                java.sql.Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery(DatabaseSetting.QUERY_SELECT_BEAT_BEST_TIME);

                while (rs.next()) {
                    if (rs.getString(3).equalsIgnoreCase(Minesweeper.getInstance().getSetting().getLevelName())) {
                        return rs.getInt(2);
                    }
                }

                stm.close();
                connection.close();

                return 0;

            } catch (SQLException e) {

                System.out.println("SQL error: " + e.getMessage());
                return 0;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    public String toString() {
        Formatter f = new Formatter();

        selectFromDB();

        for (int i = 0; i < playerTimes.size(); i++) {
            PlayerTime player = playerTimes.get(i);
            f.format("%d. %s %d %s\n", i + 1, player.name, player.time, player.level);
        }

        return f.toString();

    }

    /**
     * Clears playerTimes object.
     */
    private void reset() {
        playerTimes.clear();
    }

    /**
     * Player time.
     */
    public static class PlayerTime implements Comparable<PlayerTime> {

        /**
         * Player name.
         */
        private final String name;

        /**
         * Playing time in seconds.
         */
        private final int time;
        /**
         * Level name
         */
        private final String level;

        /**
         * Constructor.
         *
         * @param name player name
         * @param time playing game time in seconds
         * @param level level name
         */
        public PlayerTime(String name, int time, String level) {
            this.name = name;
            this.time = time;
            this.level = level;
        }

        /**
         * Returns name of player.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns player time.
         *
         * @return the time
         */
        public int getTime() {
            return time;
        }

        /**
         * Compare two objects
         *
         * @param o compared object
         * @return int result of comparing
         */
        @Override
        public int compareTo(PlayerTime o) {

            if (this.time < o.time) {
                return -1;
            } else if (this.time > o.time) {
                return 1;
            } else {
                return 0;
            }

        }

        /**
         * Get game's level
         *
         * @return the level
         */
        public String getLevel() {
            return Minesweeper.getInstance().getSetting().getLevelName();
        }
    }
}
