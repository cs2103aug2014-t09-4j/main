package bakatxt.core;

import java.util.Scanner;

public class BakaCommandLine {

    private static Scanner _sc;
    private static BakaProcessor _processor;

    public BakaCommandLine(BakaProcessor processor) {
        _sc = new Scanner(System.in);
        _processor = processor;

    }

    public static void startCli(BakaProcessor processor) {

        BakaCommandLine thisSession = new BakaCommandLine(processor);

        while (true) {
            String input = _sc.nextLine();
            _processor.executeCommand(input);
            for (int i = 0; i < _processor.getAllTasks().size(); i++) {
                System.out.println(_processor.getAllTasks().get(i)
                        .toDisplayString());
            }
        }
    }
}
