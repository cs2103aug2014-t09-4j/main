package bakatxt.core;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Scanner;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static Scanner _sc;
    private static BakaProcessor _processor;

    public BakaTxtMain() {

        _sc = new Scanner(System.in);
        _processor = new BakaProcessor();
    }

    public static void main(String[] args) throws IOException {

        if (GraphicsEnvironment.isHeadless()) {
            BakaTxtMain thisSession = new BakaTxtMain();

            while (true) {
                String input = _sc.nextLine();
                String result = _processor.executeCommand(input);
                System.out.println(result);
            }
        }
        BakaUI.startGui();
    }

}
