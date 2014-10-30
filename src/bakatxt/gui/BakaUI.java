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
import javax.swing.SwingUtilities;

import bakatxt.core.BakaProcessor;
import bakatxt.core.Task;

/**
 * BakaUI is the "main window" of the GUI for BakaTXT. Since we are doing a custom
 * window design, BakaUI is actually completely transparent. We will instead draw
 * a BakaPanel that fills the BakaUI window, and treat it as our actual window.
 */
public class BakaUI extends JFrame {

    private static BakaUI _bakaUI;
    private static BakaPanel _bakaPanel;
    private static BakaProcessor _bakaProcessor;
    private static boolean _isNewTask = false;

    /**
     * @param thisSession
     *        refers to the logic module we are interacting with
     */
    private BakaUI(BakaProcessor bakaProcessor) {
        _bakaProcessor = bakaProcessor;
        initUI();
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
                boolean isSuccessful = _bakaProcessor.executeCommand(input.getText());
                _bakaPanel.animateInputBox(isSuccessful);
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
    protected static BakaUI getWindow() {
        return _bakaUI;
    }

    /**
     * @return the Jpanel with all the content
     */
    protected static BakaPanel getPanel() {
        return _bakaPanel;
    }

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
        setTitle("Baka TX");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMouseActions();
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

    /**
     * Allow mouse movement of window from any part of the window
     * @deprecated
     * TODO remove by 0.5
     */
    @Deprecated
    private static void setMouseActions() {
        MouseActions mouseActions = new MouseActions(_bakaPanel);
        _bakaPanel.addMouseListener(mouseActions);
        _bakaPanel.addMouseMotionListener(mouseActions);
    }
}
