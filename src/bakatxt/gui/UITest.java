//@author A0116538A

package bakatxt.gui;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.awt.AWTException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakatxt.test.BakaBot;

@SuppressWarnings("boxing")
public class UITest {

    // Bot will help us automate typing & mouse movement
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
        _bot.mouseToInputBox();
    }

    @After
    public void tearDown() throws Exception {
        BakaBot.botTearDown();
    }

    //TODO
    @Test
    public void testTaskDisplay() throws AWTException {
        _bot.inputThisString(BakaBot.DISPLAY);
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
        _bot.inputThisString("display");
        final int iterations = UIHelper.SCREEN_SIZE.height / 100;
        for (int i = 0; i < iterations; i++) {
            _bot.inputThisString(BakaBot.ADD + "some task" + i);
            BakaBot.waitAWhile(BakaBot.WAIT_MEDIUM);
        }
        assertThat(getScrollPaneHeight(), is(equalTo(UIHelper.WINDOW_Y)));
    }

    /**
     * Ensure that the width of the program does not change
     * @throws AWTException
     */
    private static void testUnchangedWidth() throws AWTException {
        _bot.inputThisString(BakaBot.ADD + BakaBot.SAMPLE_FOX);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
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
        _bot.inputThisString(BakaBot.DISPLAY);
        for (int i = 0; i < 100; i++) {
            _bot.enter();
        }
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        assertThat(BakaUI.getPanel().getInput().getLocation(), is(equalTo(UIHelper.INPUT_LOCATION)));
    }


    private static int getScrollPaneHeight() {
        return BakaUI.getPanel().getScrollPane().getHeight();
    }
}

