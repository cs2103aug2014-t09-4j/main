package bakatxt.core;

import java.util.Scanner;

public class BakaProcessorCLI {

    private static Scanner _sc;
    private static BakaProcessor _processor;

    public BakaProcessorCLI() {
        _sc = new Scanner(System.in);
        _processor = new BakaProcessor();

    }

    public static void main(String[] args) {

        BakaProcessorCLI thisSession = new BakaProcessorCLI();

        while (true) {
            String input = _sc.nextLine();
            _processor.executeCommand(input);
            for (int i = 0; i < _processor.getAllTasks().size(); i++) {
                System.out.println(_processor.getAllTasks().get(i));
            }
        }
    }
}
