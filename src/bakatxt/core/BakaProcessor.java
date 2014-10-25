package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

import bakatxt.gui.BakaUI;
import bakatxt.international.BakaTongue;

public class BakaProcessor {

    private static String COMMAND_EDIT = "EDIT";
    private static String COMMAND_ADD = "ADD";
    private static String SPACE = " ";
    private static String STRING_NULL = "null";

    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;
    private static ReverseAction _ra;

    private static Integer editStage = 0;
    private static Task originalTask;
    private static Task editTask;

    private static boolean _choosingLanguage = false;

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
        _ra = new ReverseAction();
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

    public void executeCommand(String input) {

        if (_choosingLanguage) {
            BakaTongue.setLanguage(input);
            BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
            _choosingLanguage = false;
            input = "display";
        }

        input = BakaTongue.toEnglish(input);

        String command = _parser.getCommand(input);

        if (!command.equals(COMMAND_EDIT) && editStage > 0) {
            executeCommand(COMMAND_EDIT + SPACE + input);
            return;
        }

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
                addTask(input, command);
                break;

            case REMOVE :
            case DELETE :
                deleteTask(input, command);
                break;

            case SHOW :
            case DISPLAY :
                displayTask();
                break;

            case CLEAR :
                clearTask();
                break;

            case EDIT :
                editTask(input, command);
                break;

            case UNDO :
                _ra.undo();
                break;

            case REDO :
                _ra.redo();
                break;

            case LANGUAGE :
                languageSelector(input);
                return;

            case EXIT :
                _database.close();
                System.exit(0);
                break;

            case DEFAULT :
                addTaskWithNoCommandWord(input);
                break;

            default :
                break;
        }

        displayTask();
    }

    private void languageSelector(String input) {
        input = input.trim();
        if (input.contains(SPACE)) {
            BakaTongue.setLanguage(_parser.getString(input));
            BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
            _displayTasks = _database.getAllTasks();
        } else {
            _choosingLanguage = true;
            _displayTasks = BakaTongue.languageChoices();
            BakaUI.setAlertMessageText(BakaTongue
                    .getString("MESSAGE_LANGUAGE_CHOICE"));
        }
    }

    private void addTaskWithNoCommandWord(String input) {
        UserInput inputCmd;
        Task task;
        task = _parser.add(COMMAND_ADD + SPACE + input);
        inputCmd = new UserInput(COMMAND_ADD, task);
        _ra.execute(inputCmd);
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
        _ra.execute(inputCmd);
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
        if (nextStagePrompt.equals(STRING_NULL)) {
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
        if (nextStagePrompt.equals(STRING_NULL)) {
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
        if (nextStagePrompt.equals(STRING_NULL)) {
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
            _ra.execute(inputCmd);
        }
    }

    private void addTask(String input, String command) {
        UserInput inputCmd;
        Task task;
        task = _parser.add(input);
        inputCmd = new UserInput(command, task);
        _ra.execute(inputCmd);
        _database.getAllTasks();
    }
}
