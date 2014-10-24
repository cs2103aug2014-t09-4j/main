package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

import bakatxt.gui.BakaUI;
import bakatxt.international.BakaTongue;

public class BakaProcessor {

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
        LANGUAGE,
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

        input = BakaTongue.toEnglish(input);

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

            case LANGUAGE :
                if (editStage > 0) {
                    executeCommand("edit " + input);
                } else {
                    BakaTongue.setLanguage(_parser.getString(input));
                    BakaUI.setAlertMessageText(BakaTongue
                            .getString("MESSAGE_WELCOME"));
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
            BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                    + BakaTongue.getString("ALERT_EDIT_TITLE"));
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
        if (input.equals(BakaTongue.getString("USER_PROMPT_DESCRIPTION"))) {
            input = null;
        }
        editTask.setDescription(input);

        inputCmd = new UserInput(command, originalTask, editTask);
        ra.execute(inputCmd);
        nextStagePrompt = "";
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
    }

    private void editTime(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.equals(BakaTongue.getString("USER_PROMPT_TIME"))) {
            input = null;
        }
        parsedDateTime = _parser.getTime(input);
        editTask.setTime(parsedDateTime);

        nextStagePrompt = editTask.getDescription();
        if (nextStagePrompt == null) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_DESCRIPTION");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_DESCRIPTION"));
    }

    private void editDate(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.equals(BakaTongue.getString("USER_PROMPT_DATE"))) {
            input = null;
        }
        parsedDateTime = _parser.getDate(input);
        editTask.setDate(parsedDateTime);

        nextStagePrompt = editTask.getTime();
        if (nextStagePrompt.equals("null")) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_TIME");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_TIME"));
    }

    private void editVenue(String input) {
        String nextStagePrompt;
        if (input.equals(BakaTongue.getString("USER_PROMPT_VENUE"))) {
            input = null;
        }
        editTask.setVenue(input);

        nextStagePrompt = editTask.getDate();
        if (nextStagePrompt.equals("null")) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_DATE");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_DATE"));
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
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_VENUE");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_VENUE"));
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
