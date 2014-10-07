//@author A0116538A

package bakatxt.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import bakatxt.core.BakaTxtMain;

// TODO comments
public class BakaUI extends JFrame {

    private static BakaPanel _baka;

    public BakaUI(BakaTxtMain thisSession) {
        initUI(thisSession);
    }

    public static void startGui() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                BakaTxtMain thisSession = new BakaTxtMain();
                BakaUI baka = new BakaUI(thisSession);
                baka.setVisible(true);
                processInput(thisSession);
            }
        });
    }

    public static void processInput(BakaTxtMain thisSession) {

        Input input = _baka.getInput();
        input.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input.selectAll();
                BakaTxtMain.executeCommand(input.getText());
                _baka.setContents(thisSession);
            }
        });
    }

    private void initUI(BakaTxtMain thisSession) {
        _baka = new BakaPanel(thisSession);
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
