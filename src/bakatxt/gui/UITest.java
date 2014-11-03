//@author A0116538A

package bakatxt.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakatxt.test.BakaBot;

/**
 * This class is an automated test case for GUI elements
 *
 */
@SuppressWarnings("boxing")
public class UITest {

    /**
     *  _bot will help us automate typing & mouse movement
     */
    private static BakaBot _bot;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        _bot = new BakaBot();
        BakaBot.botOneTimeSetUp();
    }

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        BakaBot.botOneTimeTearDown();
    }

    @Before
    public void setUp() throws Exception {
        BakaBot.botSetUp();
    }

    @After
    public void tearDown() throws Exception {
        BakaBot.botTearDown();
    }

    /**
     * Ensure that the program is actually visible
     * Boundary case for the negative partition.
     *
     * @throws AWTException
     */
    @Test
    public void testMinHeightOfUIElements() throws AWTException {
        assertThat(BakaUI.getPanel().getHeight(), is(greaterThan(0)));
    }


    /**
     * Ensure that the program height is not too long.
     * Boundary case for the positive partition.
     *
     * @throws AWTException
     */
    @Test
    public void testMaxHeightOfUIElements() throws AWTException {
        showAllTasks();
        addALotOfTasks();
        assertThat(getScrollPaneHeight(), is(equalTo(UIHelper.WINDOW_Y)));
    }

    /**
     * Ensure that the width of the program does not change
     * @throws AWTException
     */
    @Test
    public void testUnchangedWidth() throws AWTException {
        _bot.inputThisString(BakaBot.ADD + BakaBot.SAMPLE_FOX
                             + BakaBot.SAMPLE_ZOMBIES);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        assertThat(BakaUI.getPanel().getWidth(), is(equalTo(UIHelper.WINDOW_X)));
    }

    /**
     * The input box moves about a lot if the input fails, hence, we need to
     * ensure that it does not move out of position
     *
     * @throws AWTException
     */
    @Test
    public void testFailedInput() throws AWTException {
        _bot.inputThisString(BakaBot.ADD + BakaBot.ADD);
        spamEnter();
        BakaBot.waitAWhile(BakaBot.WAIT_VERY_LONG);
        assertThat(getInputBoxLocation(), is(equalTo(UIHelper.INPUT_LOCATION)));
    }

    /**
     * The input box changes color on successful input, we need to make sure that
     * the color returns to it's normal state upon animation completion
     *
     * @throws AWTException
     */
    @Test
    public void testSuccessfulInput() throws AWTException {
        showAllTasks();
        spamEnter();
        BakaBot.waitAWhile(BakaBot.WAIT_VERY_LONG);
        assertThat(getInputBoxColor(), is(equalTo(UIHelper.INPUT_COLOR)));
    }

    /**
     * Types 'display ' and enter
     */
    private static void showAllTasks() {
        _bot.inputThisString(BakaBot.DISPLAY);
    }

    /**
     * Adds enough tasks such that the scroll bar 'appears'
     */
    private static void addALotOfTasks() {
        final int iterations = UIHelper.SCREEN_SIZE.height / 100;
        for (int i = 0; i < iterations; i++) {
            _bot.inputThisString(BakaBot.ADD + "some task" + i);
            BakaBot.waitAWhile(BakaBot.WAIT_MEDIUM);
        }
    }

    /**
     * Hits the enter key. A lot.
     */
    private static void spamEnter() {
        for (int i = 0; i < 100; i++) {
            _bot.enter();
        }
    }

    /**
     * @return the height of the scrollpane
     */
    private static int getScrollPaneHeight() {
        return BakaUI.getPanel().getScrollPane().getHeight();
    }

    /**
     * @return the relative location of the input box
     */
    private static Point getInputBoxLocation() {
        return BakaUI.getPanel().getInput().getLocation();
    }

    /**
     * @return the color of the input box
     */
    private static Color getInputBoxColor() {
        return BakaUI.getPanel().getInput().getBackground();
    }
}
