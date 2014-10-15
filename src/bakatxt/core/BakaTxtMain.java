package bakatxt.core;

import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.Scanner;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static final String MESSAGE_WELCOME = "Welcome To BakaTxt!";
    private static final String MESSAGE_BYEBYE = "Bye bye!";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";
    private static final String MESSAGE_INVALID_COMMAND = "Invalid Command!";

    private static Scanner _sc;
    private static BakaParser _parser;
    private static Database _database;
    private static LinkedList<Task> _displayTasks;
    private static BakaProcessor _processor;

    public BakaTxtMain() {

        _sc = new Scanner(System.in);
        _parser = BakaParser.getInstance();
        _database = Database.getInstance();
        _displayTasks = _database.getAllTasks();
        _processor = new BakaProcessor();
    }

    public static void main(String[] args) {

        if (GraphicsEnvironment.isHeadless()) {
            BakaTxtMain thisSession = new BakaTxtMain();

            System.out.println(MESSAGE_WELCOME);

            while (true) {
                System.out.print(MESSAGE_ENTER_COMMAND);
                String input = _sc.nextLine();
                String result = _processor.executeCommand(input);
                System.out.println(result);
            }
        }
        BakaUI.startGui();
    }

}
