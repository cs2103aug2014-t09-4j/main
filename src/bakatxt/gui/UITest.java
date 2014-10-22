//@author A0116538A

package bakatxt.gui;

import static java.awt.event.KeyEvent.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakatxt.core.BakaProcessor;

@SuppressWarnings("boxing")
public class UITest {

    // Bot will help us automate typing & mouse movement
    private static Robot _bot;

    // places to store and backup our test files
    private static final Path TEST_FILE_INITIALIZE =
            new File("./src/bakatxt/test/.UItestfile").toPath();
    private static final Path TEST_FILE = new File("./mytestfile.txt").toPath();
    private static final Path TEST_FILE_SAVE = new File("./mytestfile.txt.bak").toPath();

    private static final String ADD = "add ";
    private static final String DELETE = "delete ";
    private static final String EDIT = "edit ";
    private static final String DISPLAY = "display ";

    private static final int WAIT_SHORT = 25;
    private static final int WAIT_MEDIUM = 200;
    private static final int WAIT_LONG = 2000;

    // some sample strings
    private static final String SAMPLE_FOX = "the quick brown fox jumps over "
                                          + "the lazy dog 1234567890";
    private static final String SAMPLE_ZOMBIES = "PAINFUL ZOMBIES QUICKLY WATCH A"
                                                + "JINXED GRAVEYARD";

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        saveOldFile();
        initializeTestFile();
    }

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        restoreTestFile();
    }

    @Before
    public void setUp() throws Exception {
        BakaUI.startGui(new BakaProcessor());
        _bot = new Robot();
        initializeTestFile();
        mouseToInputBox();
    }

    @After
    public void tearDown() throws Exception {
        waitAWhile(WAIT_LONG);
    }

    //TODO
    @Test
    public void testTaskDisplay() throws AWTException {
        inputThisString(DISPLAY);
    }

    //TODO
    @Test
    public void testTaskAdd() throws AWTException {
    }

    //TODO
    @Test
    public void testTaskDelete() throws AWTException {
    }

    //TODO
    @Test
    public void testTaskEdit() throws AWTException {
    }

    /**
     * This method tests to make sure that the UI conforms to the correct size
     * at any point
     * @throws AWTException
     */
    @Test
    public void testSizeOfUIElements() throws AWTException {
        testMinHeightOfUIElements();
        // boundary case for the positive partition
        testMaxHeightOfUIElements();
        testUnchangedWidth();
    }

    /**
     * Ensure that the program is actually visible
     * @throws AWTException
     */
    private static void testMinHeightOfUIElements() throws AWTException {
        assertThat(BakaUI.getPanel().getHeight(), is(greaterThan(0)));
    }


    /**
     * Ensure that the program height is not too long
     * @throws AWTException
     */
    private static void testMaxHeightOfUIElements() throws AWTException {
        inputThisString("display");
        final int iterations = UIHelper.SCREEN_SIZE.height / 100;
        for (int i = 0; i < iterations; i++) {
            inputThisString(ADD + "some task" + i);
            waitAWhile(WAIT_MEDIUM);
        }
        assertThat(getScrollPaneHeight(), is(equalTo(UIHelper.WINDOW_Y)));
    }

    /**
     * Ensure that the width of the program does not change
     * @throws AWTException
     */
    private static void testUnchangedWidth() throws AWTException {
        inputThisString(ADD + SAMPLE_FOX);
        waitAWhile(WAIT_LONG);
        assertThat(BakaUI.getPanel().getWidth(), is(equalTo(UIHelper.WINDOW_X)));
    }

    /**
     * Tests to make sure that the locations of the elements do not move to an
     * unspecified location
     * @throws AWTException
     */
    @Test
    public void testLocationOfUIElements() throws AWTException {
        testLocationOfInputBox();
    }

    /**
     * the input box moves about a lot, hence, we need to ensure that it does not
     * move out of position
     */
    private static void testLocationOfInputBox() {
        inputThisString(DISPLAY);
        for (int i = 0; i < 100; i++) {
            enter();
        }
        waitAWhile(WAIT_LONG);
        assertThat(BakaUI.getPanel().getInput().getLocation(), is(equalTo(UIHelper.INPUT_LOCATION)));
    }


    private static int getScrollPaneHeight() {
        return BakaUI.getPanel().getScrollPane().getHeight();
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
     * moves the cursor to the input box and simulate a keyboard typing the string s
     *
     * @param s is the string to be typed
     */
    private static void inputThisString(String s) {
        typeThis(s);
        waitAWhile(WAIT_SHORT);
        enter();
    }

    /**
     * moves the cursor to the BakaTxt input box and clicks it
     */
    private static void mouseToInputBox() {
        _bot.mouseMove(UIHelper.WINDOW_LOCATION.x + 20,UIHelper.WINDOW_LOCATION.y + 20);
        _bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitAWhile(WAIT_SHORT);
        _bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * Tells the process to wait waitTime milliseconds
     * @param waitTime is the time in miliseconds to wait
     */
    private static void waitAWhile(final int waitTime) {
        try{
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Simulates an enter key being pressed
     */
    private static void enter() {
        typeThis("\n");
    }

    /**
     * Types a string using keyboard inputs
     * @param toBeTyped is the string to be typed
     */
    private static void typeThis(CharSequence toBeTyped) {
        for (int i = 0; i < toBeTyped.length(); i++) {
            char character = toBeTyped.charAt(i);
            typeThisChar(character);
            waitAWhile(WAIT_SHORT);
        }
    }

    /**
     * types a character, note, only what is on a standard keyboard layout, no unicode
     * @param character is the character to be typed
     */
    private static void typeThisChar(char character) {
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
    private static void typingThisChar(int... keyCodes) {
        for (int i = 0; i < keyCodes.length; i++) {
            _bot.keyPress(keyCodes[i]);
        }
        waitAWhile(WAIT_SHORT);
        for (int i = 0; i < keyCodes.length; i++) {
            _bot.keyRelease(keyCodes[i]);
        }
    }
}
