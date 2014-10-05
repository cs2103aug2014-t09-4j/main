package bakatxt.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import bakatxt.core.BakaTxtMain;
import bakatxt.core.BakaTxtSession;
import bakatxt.core.Task;

public class BakaUI extends JFrame {

    private static BakaPanel _baka;
    private static LinkedList<Task> _tasks;

    public BakaUI() {
        initUI();
    }

    public static void startGui(BakaTxtSession session) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BakaUI baka = new BakaUI();
                baka.setVisible(true);
                processInput(session);
            }
        });
    }

    public static void processInput(BakaTxtSession session) {

        Input input = _baka.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input.selectAll();
                BakaTxtMain.executeCommand(input.getText());
                _tasks = session.getTasks();
            }
        });
    }

    protected static LinkedList<Task> getTasks() {
        return _tasks;
    }

    private void initUI() {
        _baka = new BakaPanel();
        setUndecorated(true);
        setBackground(UIHelper.TRANSPARENT);
        setContentPane(_baka);
        pack();
        setLocationRelativeTo(null); // centers the window on screen
        setAlwaysOnTop(true);
        setTitle("Baka TX");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
