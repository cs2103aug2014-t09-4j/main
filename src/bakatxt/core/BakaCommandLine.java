//@author A0116320Y
package bakatxt.core;

import java.util.LinkedList;
import java.util.Scanner;

public class BakaCommandLine {

    private static final String MESSAGE_WELCOME = "Welcome to BakaTxt Commandline!";
    private static final String MESSAGE_COMMAND = "Command: ";
    private static final String MESSAGE_NO_TASK = "No task to display!";
    private static final String MESSAGE_ERROR = "An error has occurred! Please try again!";

    private static Scanner _sc;
    private static BakaProcessor _processor;

    public BakaCommandLine() {
        _sc = new Scanner(System.in);
        _processor = new BakaProcessor(false);
    }

    public static void startCli() {

        BakaCommandLine thisSession = new BakaCommandLine();

        String input;
        boolean isSuccessful;

        System.out.println(MESSAGE_WELCOME);
        _processor.executeCommand("display today");
        printTasks();

        do {
            System.out.println();
            System.out.print(MESSAGE_COMMAND);
            input = _sc.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            isSuccessful = _processor.executeCommand(input);
            if (!isSuccessful) {
                System.out.println(MESSAGE_ERROR);
            } else {
                printTasks();
            }
        } while (!input.equals("exit"));
    }

    private static void printTasks() {
        LinkedList<Task> tasks = _processor.getAllTasks();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%4d. ", i + 1);
            System.out.println(tasks.get(i).toDisplayString());
        }

        if (tasks.isEmpty()) {
            System.out.println(MESSAGE_NO_TASK);
        }
    }
}
