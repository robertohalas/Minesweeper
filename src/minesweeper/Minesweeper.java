package minesweeper;

import minesweeper.consoleui.ConsoleUI;
import minesweeper.core.Field;
import minesweeper.swingui.SwingUI;

/**
 * Main application class.
 */
public class Minesweeper {

    /**
     * User interface.
     */
    private UserInterface userInterface;
    /**
     * Starting time
     */
    private long startMillis;
    /**
     * Players best times
     */
    private BestTimes bestTimes;
    /**
     * Instance of current object - Minesweeper
     */
    private static Minesweeper instance;
    /**
     * Game settings
     */
    private Settings setting;
    /**
     * Default user interface
     */
    private static String DEFAULT_UI = "swing";
    /**
     * Custom user interface
     */
    private static String CUSTOM_UI = null;

    /**
     * Constructor.
     */
    private Minesweeper() {
        instance = this;
        bestTimes = new BestTimes();
        this.setting = Settings.load();
        if (CUSTOM_UI == null) {
            userInterface = create(DEFAULT_UI);
        } else {
            userInterface = create(CUSTOM_UI);
        }
        newGame();
    }

    /**
     * Starts new game and starts timer.
     */

    public void newGame() {
        Field field = new Field(setting.getRowCount(), setting.getColumnCount(), setting.getMineCount());
        /*Field field;
        startMillis = System.currentTimeMillis();
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + System.getProperty("file.separator") + "save.settings"))) {
            field = (Field) oos.readObject();
        } catch (Exception e) {
            System.out.println("Cant open the file!");
            field = new Field(setting.getRowCount(), setting.getColumnCount(), setting.getMineCount());
        }*/
        startMillis = System.currentTimeMillis();
        userInterface.newGameStarted(field);

    }

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            CUSTOM_UI = args[0];
        }
        new Minesweeper();
    }

    /**
     * Elapsed time from begging to now in seconds.
     *
     * @return current time in millis
     */
    public int getPlayingSeconds() {
        return Math.round((System.currentTimeMillis() - startMillis) / 1000);
    }

    /**
     * This field gets best times.
     *
     * @return the bestTimes
     */
    public BestTimes getBestTimes() {
        return bestTimes;
    }

    /**
     * Return instance of current object - Minesweeper.
     *
     * @return the instance
     */
    public static Minesweeper getInstance() {
        if (instance == null) {
            return new Minesweeper();
        } else {
            return instance;
        }
    }

    /**
     * Load the settings of the game
     *
     * @return the setting
     */
    public Settings getSetting() {
        return this.setting;
    }

    /**
     * Save settings of the game.
     *
     * @param setting the setting to set
     */
    public void setSetting(Settings setting) {
        this.setting = setting;
        this.setting.save();
    }

    /**
     * Create new instance of UI based on parameter. This method throws runtime
     * exception if no parameter or empty parameter was set.
     *
     * @param name the name of user interface
     * @return new interface
     */
    private UserInterface create(String name) {
        if (name.equalsIgnoreCase("swing")) {
            return new SwingUI();
        } else if (name.equalsIgnoreCase("console")) {
            return new ConsoleUI();
        } else {

            throw new RuntimeException("No valid UI specified");
        }
    }
}
