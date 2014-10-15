//@author A0116538A

package bakatxt.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import bakatxt.core.BakaTxtMain;
import bakatxt.core.Task;

/**
 * BakaUI is the "main window" of the GUI for BakaTXT. Since we are doing a custom
 * window design, BakaUI is actually completely transparent. We will instead draw
 * a BakaPanel that fills the BakaUI window, and treat it as our actual window.
 *
 */
public class BakaUI extends JFrame {

    private static BakaPanel _baka;
    private static BakaTxtMain _thisSession;

    /**
     * @param thisSession refers to the logic module we are interacting with
     */
    private BakaUI(BakaTxtMain thisSession) {
        _thisSession = thisSession;
        initUI();
    }

    /**
     * This method initializes the GUI and updates it when necessary
     */
    public static void startGui() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BakaTxtMain thisSession = new BakaTxtMain();
                BakaUI baka = new BakaUI(thisSession);
                baka.setVisible(true);
                processInput();
            }
        });
    }

    //TODO remove our dependency for this method
    /**
     * This method listens for input from the GUI and does the following when the
     * return key is pressed:
     *
     * 1. Highlight all the text in the input box (to make it trivial for the user
     *    to input new commands)
     * 2. Passes the input to the logic module to process it
     * 3. Lastly, updates the contents of the GUI to fit the command
     *
     * @deprecated use the getInput and updateUI methods instead at the logic module
     */
    @Deprecated
    public static void processInput() {

        Input input = _baka.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input.selectAll();
                BakaTxtMain.executeCommand(input.getText());
                _baka.setContents(_thisSession.getAllTasks());
            }
        });
    }

    /**
     *@return the text in the input box
     */
    public static String getInputText() {
        return _baka.getInput().getText();
    }

    /**
     * update the contents of the GUI
     */
    public static void updateUI(LinkedList<Task> tasks) {
        _baka.setContents(tasks);
    }

    /**
     * This method draws the BakaPanel and sets the window as transparent, centered,
     * unmovable, and always on top.
     *
     */
    private void initUI() {
        _baka = new BakaPanel(_thisSession.getAllTasks());
        setUndecorated(true);
        setBackground(UIHelper.TRANSPARENT);
        setContentPane(_baka);
        pack();
        setLocationRelativeTo(null); // centers the window on screen
        setAlwaysOnTop(false);
        setTitle("Baka TX");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MouseActions mouseActions = new MouseActions(_baka);
        _baka.addMouseListener(mouseActions);
        _baka.addMouseMotionListener(mouseActions);
    }
}
