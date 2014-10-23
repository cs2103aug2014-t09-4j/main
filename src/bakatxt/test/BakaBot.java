//@author A0116538A

package bakatxt.test;

import static java.awt.event.KeyEvent.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bakatxt.core.BakaProcessor;
import bakatxt.gui.BakaUI;
import bakatxt.gui.UIHelper;

/**
 * This class is a helper class to automate test cases for JUnit on the GUI.
 *
 */
public class BakaBot extends Robot {

    // places to store and backup our test files
    private static final Path TEST_FILE_INITIALIZE =
            new File("./src/bakatxt/test/.UItestfile").toPath();
    private static final Path TEST_FILE = new File("./mytestfile.txt").toPath();
    private static final Path TEST_FILE_SAVE = new File("./mytestfile.txt.bak").toPath();

    // commands
    public static final String ADD = "add ";
    public static final String DELETE = "delete ";
    public static final String EDIT = "edit ";
    public static final String DISPLAY = "display ";

    public static final int WAIT_SHORT = 25;
    public static final int WAIT_MEDIUM = 200;
    public static final int WAIT_LONG = 2000;

    // some sample strings
    public static final String SAMPLE_FOX = "the quick brown fox jumps over "
                                          + "the lazy dog 1234567890";
    public static final String SAMPLE_ZOMBIES = "PAINFUL ZOMBIES QUICKLY WATCH A"
                                                + "JINXED GRAVEYARD";

    public BakaBot() throws AWTException {
        super();
    }

    /**
     * Call this in the @BeforeClass method in your JUnit test. Sets up the files
     * for testing while retaining the old files.
     * @throws IOException
     */
    public static void botOneTimeSetUp() throws IOException {
        saveOldFile();
        initializeTestFile();
    }

    /**
     * Call this in the @AfterClass method in your JUnit test. Restores the old
     * task database file used before the test.
     * @throws IOException
     */
    public static void botOneTimeTearDown() throws IOException {
        restoreTestFile();
    }

    /**
     * Call this in the @Before method in your JUnit test. Starts the GUI and
     * reinitializes the test files if needed.
     * @throws IOException
     */
    public static void botSetUp() throws IOException {
        BakaUI.startGui(new BakaProcessor());
        initializeTestFile();
    }

    /**
     * Call this in the @Before method in your JUnit test. Pauses for 2 seconds
     * between each test to prevent interference between each test.
     */
    public static void botTearDown() {
        waitAWhile(WAIT_LONG);
    }

    /**
     * moves the cursor to the input box and simulate a keyboard typing the string
     * s
     * @param s is the string to be typed
     */
    public void inputThisString(String s) {
        typeThis(s);
        waitAWhile(WAIT_SHORT);
        enter();
    }

    /**
     * moves the cursor to the BakaTxt input box and clicks it
     */
    public void mouseToInputBox() {
        mouseMove(UIHelper.WINDOW_LOCATION.x + 20,UIHelper.WINDOW_LOCATION.y + 20);
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitAWhile(WAIT_SHORT);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * Tells the process to wait waitTime milliseconds
     * @param waitTime is the time in miliseconds to wait
     */
    public static void waitAWhile(final int waitTime) {
        try{
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Simulates an enter key being pressed
     */
    public void enter() {
        typeThis("\n");
    }

    /**
     * Types a string using keyboard inputs
     * @param toBeTyped is the string to be typed
     */
    public void typeThis(CharSequence toBeTyped) {
        for (int i = 0; i < toBeTyped.length(); i++) {
            char character = toBeTyped.charAt(i);
            typeThisChar(character);
            waitAWhile(WAIT_SHORT);
        }
    }

    private static void saveOldFile() throws IOException {
        Files.deleteIfExists(TEST_FILE_SAVE);
        Files.copy(TEST_FILE, TEST_FILE_SAVE);
    }

    private static void initializeTestFile() throws IOException {
        Files.deleteIfExists(TEST_FILE);
        Files.copy(TEST_FILE_INITIALIZE, TEST_FILE);
    }

    private static void restoreTestFile() throws IOException {
        Files.deleteIfExists(TEST_FILE);
        Files.copy(TEST_FILE_SAVE, TEST_FILE);
        Files.deleteIfExists(TEST_FILE_SAVE);
    }

    /**
     * types a character, note, only what is on a standard keyboard layout, no unicode
     * @param character is the character to be typed
     */
    private void typeThisChar(char character) {
        switch (character) {
            case 'a' :
                typingThisChar(VK_A);
                break;
            case 'b' :
                typingThisChar(VK_B);
                break;
            case 'c' :
                typingThisChar(VK_C);
                break;
            case 'd' :
                typingThisChar(VK_D);
                break;
            case 'e' :
                typingThisChar(VK_E);
                break;
            case 'f' :
                typingThisChar(VK_F);
                break;
            case 'g' :
                typingThisChar(VK_G);
                break;
            case 'h' :
                typingThisChar(VK_H);
                break;
            case 'i' :
                typingThisChar(VK_I);
                break;
            case 'j' :
                typingThisChar(VK_J);
                break;
            case 'k' :
                typingThisChar(VK_K);
                break;
            case 'l' :
                typingThisChar(VK_L);
                break;
            case 'm' :
                typingThisChar(VK_M);
                break;
            case 'n' :
                typingThisChar(VK_N);
                break;
            case 'o' :
                typingThisChar(VK_O);
                break;
            case 'p' :
                typingThisChar(VK_P);
                break;
            case 'q' :
                typingThisChar(VK_Q);
                break;
            case 'r' :
                typingThisChar(VK_R);
                break;
            case 's' :
                typingThisChar(VK_S);
                break;
            case 't' :
                typingThisChar(VK_T);
                break;
            case 'u' :
                typingThisChar(VK_U);
                break;
            case 'v' :
                typingThisChar(VK_V);
                break;
            case 'w' :
                typingThisChar(VK_W);
                break;
            case 'x' :
                typingThisChar(VK_X);
                break;
            case 'y' :
                typingThisChar(VK_Y);
                break;
            case 'z' :
                typingThisChar(VK_Z);
                break;
            case 'A' :
                typingThisChar(VK_SHIFT, VK_A);
                break;
            case 'B' :
                typingThisChar(VK_SHIFT, VK_B);
                break;
            case 'C' :
                typingThisChar(VK_SHIFT, VK_C);
                break;
            case 'D' :
                typingThisChar(VK_SHIFT, VK_D);
                break;
            case 'E' :
                typingThisChar(VK_SHIFT, VK_E);
                break;
            case 'F' :
                typingThisChar(VK_SHIFT, VK_F);
                break;
            case 'G' :
                typingThisChar(VK_SHIFT, VK_G);
                break;
            case 'H' :
                typingThisChar(VK_SHIFT, VK_H);
                break;
            case 'I' :
                typingThisChar(VK_SHIFT, VK_I);
                break;
            case 'J' :
                typingThisChar(VK_SHIFT, VK_J);
                break;
            case 'K' :
                typingThisChar(VK_SHIFT, VK_K);
                break;
            case 'L' :
                typingThisChar(VK_SHIFT, VK_L);
                break;
            case 'M' :
                typingThisChar(VK_SHIFT, VK_M);
                break;
            case 'N' :
                typingThisChar(VK_SHIFT, VK_N);
                break;
            case 'O' :
                typingThisChar(VK_SHIFT, VK_O);
                break;
            case 'P' :
                typingThisChar(VK_SHIFT, VK_P);
                break;
            case 'Q' :
                typingThisChar(VK_SHIFT, VK_Q);
                break;
            case 'R' :
                typingThisChar(VK_SHIFT, VK_R);
                break;
            case 'S' :
                typingThisChar(VK_SHIFT, VK_S);
                break;
            case 'T' :
                typingThisChar(VK_SHIFT, VK_T);
                break;
            case 'U' :
                typingThisChar(VK_SHIFT, VK_U);
                break;
            case 'V' :
                typingThisChar(VK_SHIFT, VK_V);
                break;
            case 'W' :
                typingThisChar(VK_SHIFT, VK_W);
                break;
            case 'X' :
                typingThisChar(VK_SHIFT, VK_X);
                break;
            case 'Y' :
                typingThisChar(VK_SHIFT, VK_Y);
                break;
            case 'Z' :
                typingThisChar(VK_SHIFT, VK_Z);
                break;
            case '0' :
                typingThisChar(VK_0);
                break;
            case '1' :
                typingThisChar(VK_1);
                break;
            case '2' :
                typingThisChar(VK_2);
                break;
            case '3' :
                typingThisChar(VK_3);
                break;
            case '4' :
                typingThisChar(VK_4);
                break;
            case '5' :
                typingThisChar(VK_5);
                break;
            case '6' :
                typingThisChar(VK_6);
                break;
            case '7' :
                typingThisChar(VK_7);
                break;
            case '8' :
                typingThisChar(VK_8);
                break;
            case '9' :
                typingThisChar(VK_9);
                break;
            case '-' :
                typingThisChar(VK_MINUS);
                break;
            case '@' :
                typingThisChar(VK_AT);
                break;
            case '\n' :
                typingThisChar(VK_ENTER);
                break;
            case '/' :
                typingThisChar(VK_SLASH);
                break;
            case ' ' :
                typingThisChar(VK_SPACE);
                break;
            default :
                throw new IllegalArgumentException("Cannot type: " + character);
        }
    }

    /**
     * Types the char based on what keypresses are needed to type it
     *
     * @param keyCodes is the keyCodes needed to be activated
     */
    private void typingThisChar(int... keyCodes) {
        for (int i = 0; i < keyCodes.length; i++) {
            keyPress(keyCodes[i]);
        }
        waitAWhile(WAIT_SHORT);
        for (int i = 0; i < keyCodes.length; i++) {
            keyRelease(keyCodes[i]);
        }
    }
}
