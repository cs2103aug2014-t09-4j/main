//@author A0116538A

package bakatxt.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import bakatxt.core.BakaProcessor;
import bakatxt.core.Task;
import bakatxt.gui.look.UIHelper;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

/**
 * BakaUI is the "main window" of the GUI for BakaTXT. Since we are doing a custom
 * window design, BakaUI is actually completely transparent. We will instead draw
 * a BakaPanel that fills the BakaUI window, and treat it as our actual window.
 *
 */
public class BakaUI extends JFrame {

    private static BakaUI _bakaUI;
    private static BakaPanel _bakaPanel;
    private static BakaProcessor _bakaProcessor;
    private static boolean _isNewTask = false;

    private static final String INITIAL_DISPLAY = "display today";
    private static final String HOTKEY = "control SPACE";

    private static final Provider provider = Provider.getCurrentProvider(true);

    /**
     * @param thisSession
     *        refers to the logic module we are interacting with
     */
    private BakaUI(BakaProcessor bakaProcessor) {
        _bakaProcessor = bakaProcessor;
        initUI();
        setupHotkey();
    }

    /**
     * This method initializes the GUI and updates it when necessary
     */
    public static void startGui(BakaProcessor bakaProcessor) {
        _bakaUI = new BakaUI(bakaProcessor);
        _bakaUI.setVisible(true);
        _bakaUI.setLocation(UIHelper.WINDOW_LOCATION);
        setInitialFocus();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setInitialDisplay();
                processInput();
            }
        });
    }

    /**
     * This method listens for input from the GUI and does the following when the
     * return key is pressed:
     *
     * 1. Highlight all the text in the input box (to make it trivial for the user
     * to input new commands)
     * 2. Passes the input to the logic module to process it
     * 3. Lastly, updates the contents of the GUI to fit the command
     */
    public static void processInput() {

        Input input = _bakaPanel.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input.selectAll();
                String s = input.getText();
                shouldAnimate(processInput(s));
                updateUI(_bakaProcessor.getAllTasks());
            }
        });
    }

    /**
     * update the contents of the GUI
     */
    public static void updateUI(LinkedList<Task> tasks) {

        assert (tasks != null) : "tasks must not be null";

        _bakaPanel.refreshContents(tasks);
        _bakaUI.setLocation(UIHelper.WINDOW_LOCATION);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bakaUI.setPreferredSize(new Dimension(UIHelper.WINDOW_X,
                        _bakaPanel.getPreferredSize().height));
                _bakaUI.pack();
            }
        });
    }

    /**
     * Set the text within the input box
     *
     * @param s
     *        the string to be put in the input box
     */
    public static void setInputBoxText(String s) {
        _bakaPanel.setInputBoxText(s);
        _bakaPanel.getInput().selectAll();
    }

    /**
     * Set the text in the alert message box
     *
     * @param s
     *        the string to put in the alert message box
     */
    public static void setAlertMessageText(String s) {
        _bakaPanel.updateAlertMessageText(s);
    }

    /**
     * @return the JFrame window
     */
    public static BakaUI getWindow() {
        return _bakaUI;
    }

    /**
     * @return the Jpanel with all the content
     */
    public static BakaPanel getPanel() {
        return _bakaPanel;
    }

    /**
     * @return
     */
    protected static boolean isNewTask() {
        return _isNewTask;
    }

    /**
     * This method draws the BakaPanel and sets the window as transparent and
     * centered.
     */
    private void initUI() {
        try {
            _bakaPanel = new BakaPanel(_bakaProcessor.getAllTasks());
        } catch (NullPointerException e) {
            throw new Error("bakaProcessor.getAllTasks() is null");
        }
        setUndecorated(true);
        setBackground(UIHelper.TRANSPARENT);
        setContentPane(_bakaPanel);
        pack();
        setAlwaysOnTop(false);
        setTitle("BakaTxt");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Set the input box to be immediately selected on start up
     */
    private static void setInitialFocus() {
        _bakaUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened( WindowEvent e ){
                _bakaPanel.getInput().requestFocus();
            }
        });
    }

    private static void setInitialDisplay() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                processInput(INITIAL_DISPLAY);
                updateUI(_bakaProcessor.getAllTasks());
            }
        });
    }

    private static boolean processInput(String inputText) {
        return _bakaProcessor.executeCommand(inputText);
    }

    private static void shouldAnimate(boolean shouldAnimate) {
        _bakaPanel.animateInputBox(shouldAnimate);
    }

    private static void setupHotkey() {
        final HotKeyListener listener = new HotKeyListener() {
            @Override
            public void onHotKey(final HotKey hotKey) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        _bakaUI.setAlwaysOnTop(true);
                        _bakaUI.toFront();
                        _bakaUI.requestFocus();
                        _bakaUI.setAlwaysOnTop(false);
                    }
                });
            }
        };
        provider.reset();
        provider.register(KeyStroke.getKeyStroke(HOTKEY), listener);
    }
}
