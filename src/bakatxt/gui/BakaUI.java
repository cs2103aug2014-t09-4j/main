// @author A0116538A

package bakatxt.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import bakatxt.core.BakaProcessor;
import bakatxt.core.Task;

/**
 * BakaUI is the "main window" of the GUI for BakaTXT. Since we are doing a
 * custom
 * window design, BakaUI is actually completely transparent. We will instead
 * draw
 * a BakaPanel that fills the BakaUI window, and treat it as our actual window.
 *
 */
public class BakaUI extends JFrame {

    private static BakaUI _bakaUI;
    private static BakaPanel _bakaPanel;
    private static BakaProcessor _bakaProcessor;

    /**
     * @param thisSession
     *            refers to the logic module we are interacting with
     */
    private BakaUI(BakaProcessor bakaProcessor) {
        _bakaProcessor = bakaProcessor;
        initUI();
    }

    /**
     * This method initializes the GUI and updates it when necessary
     */
    public static void startGui(BakaProcessor bakaProcessor) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bakaUI = new BakaUI(bakaProcessor);
                _bakaUI.setVisible(true);
                processInput();
            }
        });
    }

    // TODO remove our dependency for this method
    /**
     * This method listens for input from the GUI and does the following when
     * the
     * return key is pressed:
     *
     * 1. Highlight all the text in the input box (to make it trivial for the
     * user
     * to input new commands)
     * 2. Passes the input to the logic module to process it
     * 3. Lastly, updates the contents of the GUI to fit the command
     *
     * @deprecated use the getInput and updateUI methods instead at the logic
     *             module
     */
    @Deprecated
    public static void processInput() {

        Input input = _bakaPanel.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input.selectAll();
                _bakaProcessor.executeCommand(input.getText());
                updateUI(_bakaProcessor.getAllTasks());
            }
        });
    }

    /**
     * @return the text in the input box
     */
    public static String getInputText() {
        return _bakaPanel.getInput().getText();
    }

    /**
     * update the contents of the GUI
     */
    public static void updateUI(LinkedList<Task> tasks) {

        assert (tasks != null) : "tasks must not be null";

        _bakaPanel.refreshContents(tasks);
        _bakaUI.pack();
        _bakaUI.setLocationRelativeTo(null);
    }

    protected static BakaUI getWindow() {
        return _bakaUI;
    }

    /**
     * This method draws the BakaPanel and sets the window as transparent and
     * centered.
     *
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
        setWindowLocation();
        setAlwaysOnTop(false);
        setTitle("Baka TX");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMouseActions();
    }

    private static void setMouseActions() {
        MouseActions mouseActions = new MouseActions(_bakaPanel);
        _bakaPanel.addMouseListener(mouseActions);
        _bakaPanel.addMouseMotionListener(mouseActions);
    }

    /**
     * Set the window position to a quarter below the top of the screen
     */
    private static void setWindowLocation() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        _bakaUI.setLocation(dim.width/2-_bakaUI.getSize().width/2, dim.height/4);
    }
}
