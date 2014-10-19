//@author A0116538A

package bakatxt.gui;

import static java.awt.event.KeyEvent.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.junit.Before;
import org.junit.Test;

import bakatxt.core.BakaProcessor;

public class UITest {

    private static Robot bot;

    @Before
    public void setUp() throws Exception {
        BakaUI.startGui(new BakaProcessor());
        bot = new Robot();
    }

    //TODO
    @Test
    public void testTaskDisplay() throws AWTException {
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
     * moves the cursor to the input box and simulate a keyboard typing the string s
     *
     * @param s is the string to be typed
     */
    private static void inputThisString(String s) {
        mouseToInputBox();
        typeThis(s);
        waitAWhile();
        enter();
    }

    /**
     * moves the cursor to the BakaTxt input box and clicks it
     */
    private static void mouseToInputBox() {
        bot.mouseMove(UIHelper.WINDOW_LOCATION.x + 20,UIHelper.WINDOW_LOCATION.y + 20);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitAWhile();
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * Tells the process to wait waitTime milliseconds
     */
    private static void waitAWhile() {
        final int waitTime = 100;
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
            waitAWhile();
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
                throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    /**
     * Types the char based on what keypresses are needed to type it
     *
     * @param keyCodes is the keyCodes needed to be activated
     */
    private static void typingThisChar(int... keyCodes) {
        for (int i = 0; i < keyCodes.length; i++) {
            bot.keyPress(keyCodes[i]);
        }
        waitAWhile();
        for (int i = 0; i < keyCodes.length; i++) {
            bot.keyRelease(keyCodes[i]);
        }
    }
}
