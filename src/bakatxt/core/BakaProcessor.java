package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

import bakatxt.gui.BakaUI;

public class BakaProcessor {

    private static final String ALERT_EDIT = "Edit Mode: ";

    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;
    private static ReverseAction ra = new ReverseAction();

    private static Integer editStage = 0;
    private static Task originalTask;
    private static Task editTask;

    enum CommandType {
        ADD,
        DELETE,
        SHOW,
        DISPLAY,
        CLEAR,
        DEFAULT,
        REMOVE,
        EDIT,
        UNDO,
        REDO,
        EXIT
    }

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = new BakaParser();
        _displayTasks = _database.getAllTasks();
    }

    public void displayTask() {
        _displayTasks = _database.getAllTasks();
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }

    public void clearTask() {
        _database.clear();
        _displayTasks = _database.getAllTasks();
    }

    public String executeCommand(String input) {

        String command = _parser.getCommand(input);
        CommandType commandType;

        /*
         * The command word will be stored as a string and it will get
         * CommandType value but will return DEFAULT if a wrong command is
         * entered
         */
        try {
            commandType = CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandType = CommandType.DEFAULT;
        }

        switch (commandType) {

            case ADD :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    addTask(input, command);
                }
                break;

            case REMOVE :
            case DELETE :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    deleteTask(input, command);
                }
                break;

            case SHOW :
            case DISPLAY :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    displayTask();
                }
                break;

            case CLEAR :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    clearTask();
                }
                break;

            case EDIT :
                editTask(input, command);
                break;

            case UNDO :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    ra.undo();
                }
                break;

            case REDO :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    ra.redo();
                }
                break;

            case EXIT :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    _database.close();
                    System.exit(0);
                }
                break;

            case DEFAULT :
                addTaskWithNoCommandWord(input);
                break;

            default :
                break;
        }

        _displayTasks = _database.getAllTasks();
        return null;
    }

    private void addTaskWithNoCommandWord(String input) {

        UserInput inputCmd;
        Task task;
        if (editStage > 0) {
            executeCommand("edit " + input);
        } else {
            task = _parser.add("add " + input);
            inputCmd = new UserInput("add", task);
            ra.execute(inputCmd);
        }
    }

    private void editTask(String input, String command) {
        UserInput inputCmd;
        String nextStagePrompt;
        String parsedDateTime;
        if (editStage == 0) {
            editStage = 5;
            String index = _parser.getString(input).trim();
            int trueIndex = Integer.valueOf(index.trim()) - 1;
            _displayTasks = _database.getAllTasks();
            editTask = _displayTasks.get(trueIndex);
            originalTask = new Task(editTask);

            BakaUI.setInputBoxText(editTask.getTitle());
            BakaUI.setAlertMessageText(ALERT_EDIT + "TITLE");
        } else {
            input = _parser.getString(input);
            switch (editStage) {
                case 5 :
                    editTitle(input);
                    break;
                case 4 :
                    editVenue(input);
                    break;
                case 3 :
                    editDate(input);
                    break;
                case 2 :
                    editTime(input);
                    break;
                case 1 :
                    editDescription(input, command);
                    break;
                default :
                    break;
            }
            editStage--;
        }
    }

    private void editDescription(String input, String command) {
        UserInput inputCmd;
        String nextStagePrompt;
        if (input.equals("Add description?")) {
            input = null;
        }
        editTask.setDescription(input);

        inputCmd = new UserInput(command, originalTask, editTask);
        ra.execute(inputCmd);
        nextStagePrompt = "";
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText("Welcome to BakaTXT! For help please type help in the box above");
    }

    private void editTime(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.equals("Add a time?")) {
            input = null;
        }
        parsedDateTime = _parser.getTime(input);
        editTask.setTime(parsedDateTime);

        nextStagePrompt = editTask.getDescription();
        if (nextStagePrompt == null) {
            nextStagePrompt = "Add description?";
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(ALERT_EDIT + "DESCRIPTION");
    }

    private void editDate(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.equals("Add a date?")) {
            input = null;
        }
        parsedDateTime = _parser.getDate(input);
        editTask.setDate(parsedDateTime);

        nextStagePrompt = editTask.getTime();
        if (nextStagePrompt.equals("null")) {
            nextStagePrompt = "Add a time?";
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(ALERT_EDIT + "TIME");
    }

    private void editVenue(String input) {
        String nextStagePrompt;
        if (input.equals("Add a venue?")) {
            input = null;
        }
        editTask.setVenue(input);

        nextStagePrompt = editTask.getDate();
        if (nextStagePrompt.equals("null")) {
            nextStagePrompt = "Add a date?";
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(ALERT_EDIT + "DATE");
    }

    private void editTitle(String input) {
        String nextStagePrompt;
        if (input.trim().isEmpty()) {
            editTask.setTitle(originalTask.getTitle());
        } else {
            editTask.setTitle(input);
        }

        nextStagePrompt = editTask.getVenue();
        if (nextStagePrompt.equals("null")) {
            nextStagePrompt = "Add a venue?";
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(ALERT_EDIT + "VENUE");
    }

    private void deleteTask(String input, String command) {
        UserInput inputCmd;
        Task task;
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
        _displayTasks = _database.getAllTasks();
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            task = _displayTasks.get(trueIndex - 1);
            inputCmd = new UserInput(command, task);
            ra.execute(inputCmd);
        }
    }

    private void addTask(String input, String command) {
        UserInput inputCmd;
        Task task;
        task = _parser.add(input);
        inputCmd = new UserInput(command, task);
        ra.execute(inputCmd);
        _database.getAllTasks();
    }
}
