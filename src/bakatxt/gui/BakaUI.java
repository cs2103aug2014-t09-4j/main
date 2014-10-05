package bakatxt.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import bakatxt.core.BakaTxtMain;

public class BakaUI extends JFrame {

    private static BakaPanel _baka;

    public BakaUI() {
        initUI();
    }

    public static void startGui() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BakaUI baka = new BakaUI();
                baka.setVisible(true);
                processInput();
            }
        });
    }

    public static void processInput() {

        Input input = _baka.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BakaTxtMain.executeCommand(input.getText());
            }
        });
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
