package bakatxt.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class BakaUI extends JFrame {

    public BakaUI() {
        initUI();
    }

    public static String getInput() {
        return Input.getInput();
    }

    public static void startGui() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BakaUI baka = new BakaUI();
                baka.setVisible(true);
            }
        });
    }

    private void initUI() {
        setUndecorated(true);
        setBackground(UIHelper.TRANSPARENT);
        setContentPane(new BakaPanel());
        pack();
        setLocationRelativeTo(null); // centers the window on screen
        setAlwaysOnTop(true);
        setTitle("Baka TX");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
