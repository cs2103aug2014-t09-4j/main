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
        HELP,
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
        SEARCH,
        DONE,
        EXIT
    }

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = new BakaParser();
        _displayTasks = _database.getAllUndoneTasks();
        _ra = new ReverseAction();
    }

    private void displayTask(String input) {
        input = input.trim();
        if (input.contains(SPACE)) {
            String content = _parser.getString(input);

            if (content.equals("week")) {
                _displayTasks = _database.getWeekTasks();
            } else {
                String currentDate = _parser.getDate(content);
                _displayTasks = _database.getTasksWithDate(currentDate);
            }
        } else {
            _displayTasks = _database.getAllUndoneTasks();
        }

    }

    private void searchTask(String input) {
        String title = _parser.getString(input).trim();
        _displayTasks = _database.getTaskWithTitle(title);
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }

    private void clearTask(String command) {
        UserAction inputCmd;
        inputCmd = new UserClear(command);
        _ra.execute(inputCmd);
        _database.getAllUndoneTasks();
    }

    public boolean executeCommand(String input) {

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
            return true;
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
            case HELP :
                showHelp();
                return true;

            case ADD :
                return addTask(input, command);

            case REMOVE :
            case DELETE :
                return deleteTask(input, command);

            case SHOW :
            case DISPLAY :
                displayTask(input);
                return true;

            case CLEAR :
                clearTask(command);
                break;

            case EDIT :
                return editTask(input, command);

            case UNDO :
                return undoTask();

            case REDO :
                return redoTask();

            case LANGUAGE :
                languageSelector(input);
                return true;

            case EXIT :
                _database.close();
                System.exit(0);
                return true;

            case SEARCH :
                searchTask(input);
                return true;

            case DONE :
                return markDoneTask(input, command);

            case DEFAULT :
                return addTaskWithNoCommandWord(input);

            default :
                return false;
        }

        _displayTasks = _database.getAllUndoneTasks();
        return true;
    }

    private boolean undoTask() {
        boolean isSuccessful = _ra.undo();
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }

    private boolean redoTask() {
        boolean isSuccessful = _ra.redo();
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }

    private void showHelp() {
        _displayTasks = new LinkedList<Task>();
        for (CommandType c : CommandType.values()) {
            String cmd = c.toString();
            if (cmd.equals("DEFAULT") || cmd.equals("HELP")) {
                continue;
            }
            String languageKey = "COMMAND_" + cmd;
            Task command = new Task(BakaTongue.getString(languageKey));
            command.setDate(BakaTongue.getString("COMMAND_HELP"));
            _displayTasks.add(command);
        }
    }

    private void languageSelector(String input) {
        input = input.trim();
        if (input.contains(SPACE)) {
            BakaTongue.setLanguage(_parser.getString(input));
            BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
            _displayTasks = _database.getAllUndoneTasks();
        } else {
            _choosingLanguage = true;
            _displayTasks = BakaTongue.languageChoices();
            BakaUI.setAlertMessageText(BakaTongue
                    .getString("MESSAGE_LANGUAGE_CHOICE"));
        }
    }

    private boolean addTaskWithNoCommandWord(String input) {
        UserAction inputCmd;
        Task task;
        task = _parser.add(COMMAND_ADD + SPACE + input);
        inputCmd = new UserAction(COMMAND_ADD, task);
        boolean isSuccessful = _ra.execute(inputCmd);
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }

    private boolean editTask(String input, String command) {
        boolean isSuccessful = false;
        if (editStage == 0) {
            editStage = 6;
            String index = _parser.getString(input).trim();
            int trueIndex = Integer.valueOf(index.trim()) - 1;
            _displayTasks = _database.getAllUndoneTasks();
            editTask = _displayTasks.get(trueIndex);
            originalTask = new Task(editTask);

            BakaUI.setInputBoxText(editTask.getTitle());
            BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                    + BakaTongue.getString("ALERT_EDIT_TITLE"));
            isSuccessful = true;
        } else {
            input = _parser.getString(input);
            switch (editStage) {
                case 6 :
                    isSuccessful = editTitle(input);
                    break;
                case 5 :
                    isSuccessful = editVenue(input);
                    break;
                case 4 :
                    isSuccessful = editDate(input);
                    break;
                case 3 :
                    isSuccessful = editStartTime(input);
                    break;
                case 2 :
                    isSuccessful = editEndTime(input);
                    break;
                case 1 :
                    isSuccessful = editDescription(input, command);
                    break;
                default :
                    break;
            }
            editStage--;
        }
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }

    private boolean editDescription(String input, String command) {
        UserAction inputCmd;
        String nextStagePrompt;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_DESCRIPTION").toLowerCase())) {
            input = null;
        }
        editTask.setDescription(input);

        inputCmd = new UserEditTask(command, originalTask, editTask);
        boolean isSuccessful = _ra.execute(inputCmd);
        nextStagePrompt = "";
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
        return isSuccessful;
    }

    private boolean editStartTime(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_START_TIME").toLowerCase())) {
            input = null;
        }
        parsedDateTime = _parser.getTime(input);
        editTask.setTime(parsedDateTime);

        nextStagePrompt = editTask.getEndTime();
        if (nextStagePrompt.equals(STRING_NULL)) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_END_TIME");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_END_TIME"));
        return true;
    }

    private boolean editEndTime(String input) {

        String nextStagePrompt;
        String parsedDateTime;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_END_TIME").toLowerCase())) {
            input = null;
        }
        parsedDateTime = _parser.getTime(input);
        editTask.setEndTime(parsedDateTime);

        nextStagePrompt = editTask.getDescription();
        if (nextStagePrompt == null || nextStagePrompt.trim().isEmpty()) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_DESCRIPTION");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_DESCRIPTION"));
        return true;
    }

    private boolean editDate(String input) {
        String nextStagePrompt;
        String parsedDateTime;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_DATE").toLowerCase())) {
            input = null;
        }
        parsedDateTime = _parser.getDate(input);
        editTask.setDate(parsedDateTime);

        nextStagePrompt = editTask.getTime();
        if (nextStagePrompt.equals(STRING_NULL)) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_START_TIME");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_START_TIME"));
        return true;
    }

    private boolean editVenue(String input) {
        String nextStagePrompt;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_VENUE").toLowerCase())) {
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
        return true;
    }

    private boolean editTitle(String input) {
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
        return true;
    }

    private boolean deleteTask(String input, String command) {
        UserAction inputCmd;
        Task task;
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
        _displayTasks = _database.getAllUndoneTasks();
        boolean isSuccessful = true;
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            task = _displayTasks.get(trueIndex - 1);
            inputCmd = new UserAction(command, task);
            isSuccessful = isSuccessful && _ra.execute(inputCmd);
        }
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;

    }

    private boolean markDoneTask(String input, String command) {
        Task task;
        UserAction inputCmd;
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
        _displayTasks = _database.getAllUndoneTasks();
        boolean isSuccessful = true;
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            task = _displayTasks.get(trueIndex - 1);
            inputCmd = new UserEditStatus(command, task, true);
            isSuccessful = isSuccessful && _ra.execute(inputCmd);

        }
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }

    private boolean addTask(String input, String command) {
        UserAction inputCmd;
        Task task;
        task = _parser.add(input);
        inputCmd = new UserAction(command, task);
        boolean isSuccessful = _ra.execute(inputCmd);
        _displayTasks = _database.getAllUndoneTasks();
        return isSuccessful;
    }
}
