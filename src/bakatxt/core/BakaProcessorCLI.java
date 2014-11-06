//@author A0116014Y
package bakatxt.core;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;

import bakatxt.gui.BakaUI;
import bakatxt.gui.look.ThemeReader;
import bakatxt.international.BakaTongue;

public class BakaProcessorCLI {

    private static String COMMAND_EDIT = "EDIT";
    private static String COMMAND_ADD = "ADD";
    private static String SPACE = " ";
    private static String STRING_NULL = "null";
    private static String STRING_DEFAULT_VIEW = "DISPLAY today";
    private static String WRONG_COMMAND_FORMAT = "Wrong command format";
    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;
    private static ReverseAction _ra;

    private static Integer _editStage = 0;
    private static Task _originalTask;
    private static Task _editTask;

    private static boolean _choosingLanguage = false;

    private static String _previousAction;

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
        FIND,
        VIEW,
        DONE,
        THEME,
        EXIT
    }

    public BakaProcessorCLI() {
        _database = Database.getInstance();
        _parser = new BakaParser();
        _displayTasks = _database.getTasksWithDate(_parser.getDate("today"));
        _ra = new ReverseAction();
        _previousAction = STRING_DEFAULT_VIEW;
    }

    private boolean displayTask(String input) {
        input = input.trim();
        if (input.contains(SPACE)) {
            String content = _parser.getString(input);

            switch (content) {
                case "all" :
                    _displayTasks = _database.getAllTasks();
                    System.out.println(_displayTasks);
                    System.out.println("Displaying all tasks");
                    break;
                case "week" :
                    _displayTasks = _database.getWeekTasks();
                    System.out.println(_displayTasks);
                    System.out.println("Displaying This Week's Tasks");
                    break;
                default :
                    String currentDate = _parser.getDate(content);
                    _displayTasks = _database.getTasksWithDate(currentDate);
                    System.out.println(_displayTasks);
                    System.out
                            .println("Displaying " + currentDate + "'s Tasks");
            }
        } else {
            _displayTasks = _database.getAllTasks();
            System.out.println(_displayTasks);
            System.out.println("Displaying all tasks");
        }

        if (_displayTasks.isEmpty()) {
            System.out.println(WRONG_COMMAND_FORMAT);
            return false;
        }
        return true;
    }

    private void searchTask(String input) {
        String title = _parser.getString(input).trim();
        _displayTasks = _database.getTaskWithTitle(title);
        System.out.println(_displayTasks);
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }

    private boolean clearTask(String input, String command) {
        UserAction inputCmd;
        boolean isSuccessful = true;
        String date = null;

        input = input.trim();
        if (input.contains(SPACE)) {
            date = _parser.getString(input);
            date = _parser.getDate(date);
        } else {
            setToPreviousView();
            if (_previousAction.contains(SPACE)) {
                date = _parser.getString(_previousAction);
                date = _parser.getDate(date);
            } else {
                date = "all";
            }
        }
        inputCmd = new UserClear(command, date);
        isSuccessful = _ra.execute(inputCmd);
        setToPreviousView();
        System.out.println("Tasks cleared");
        return isSuccessful;
    }

    /**
     * Takes in a String and execute the command with the content. First, the
     * command will be taken parsed to get the command word and it will follow
     * the switch statement to do execute its command.
     *
     * @param str
     *            is the <code>String</code> containing the command and details
     * @return boolean, true if the user entered a valid command false if the
     *         user entered an invalid one
     */

    public boolean executeCommand(String input) {
        boolean isSuccessful = true;

        if (_choosingLanguage) {
            isSuccessful = BakaTongue.setLanguage(input);
            BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
            _choosingLanguage = false;
            input = "display";
        }

        input = BakaTongue.toEnglish(input);

        if (input.toLowerCase().equals("show done")) {
            _database.updateDoneView(true);
            return true;
        } else if (input.toLowerCase().equals("hide done")) {
            _database.updateDoneView(false);
            return true;
        }

        String command = _parser.getCommand(input);

        if (!command.equals(COMMAND_EDIT) && _editStage > 0) {
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
                break;

            case ADD :
                isSuccessful = addTask(input, command);
                break;

            case REMOVE :
            case DELETE :
                isSuccessful = deleteTask(input);
                break;

            case VIEW :
            case SHOW :
            case DISPLAY :
                _previousAction = input.trim();
                // catches unsuccessful tasks that invoke displayTasks();
                isSuccessful = isSuccessful && displayTask(input);
                break;

            case CLEAR :
                isSuccessful = clearTask(input, command);
                break;

            case EDIT :
                isSuccessful = editTask(input, command);
                break;

            case UNDO :
                isSuccessful = undoTask();
                break;

            case REDO :
                isSuccessful = redoTask();
                break;

            case LANGUAGE :
                isSuccessful = languageSelector(input);
                break;

            case EXIT :
                _database.close();
                System.exit(0);
                break;

            case FIND :
            case SEARCH :
                searchTask(input);
                _previousAction = input.trim();
                break;

            case DONE :
                isSuccessful = markDoneTask(input, command);
                break;

            case THEME :
                isSuccessful = setTheme(input);
                break;

            case DEFAULT :
                isSuccessful = addTaskWithNoCommandWord(input);
                break;

            default :
                return false;
        }
        return isSuccessful;
    }

    private boolean undoTask() {
        boolean isSuccessful = _ra.undo();
        setToPreviousView();
        System.out.println(_displayTasks);
        System.out.println("Undo Successful");
        return isSuccessful;
    }

    private boolean redoTask() {
        boolean isSuccessful = _ra.redo();
        setToPreviousView();
        System.out.println(_displayTasks);
        System.out.println("Redo Successful");
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
            command.setDone(true);
            command.setDate(BakaTongue.getString("COMMAND_HELP"));
            _displayTasks.add(command);
            System.out.println(command);
        }
    }

    private boolean languageSelector(String input) {
        input = input.trim();
        boolean isSuccessful = true;
        if (input.contains(SPACE)) {
            isSuccessful = BakaTongue.setLanguage(_parser.getString(input));
            BakaUI.setAlertMessageText(BakaTongue.getString("MESSAGE_WELCOME"));
            setToPreviousView();
        } else {
            _choosingLanguage = true;
            _displayTasks = BakaTongue.languageChoices();
            System.out.println(_displayTasks);
            BakaUI.setAlertMessageText(BakaTongue
                    .getString("MESSAGE_LANGUAGE_CHOICE"));
        }
        System.out.println("Language Changed");
        return isSuccessful;
    }

    private boolean addTaskWithNoCommandWord(String input) {
        UserAction inputCmd;
        Task task;
        task = _parser.add(COMMAND_ADD + SPACE + input);
        inputCmd = new UserAction(COMMAND_ADD, task);
        boolean isSuccessful = _ra.execute(inputCmd);
        _previousAction = "display " + task.getDate();
        setToPreviousView();
        System.out.println(_displayTasks);
        System.out.println("Task added");
        return isSuccessful;
    }

    private boolean editTask(String input, String command) {
        boolean isSuccessful = false;
        setToPreviousView();
        if (_editStage == 0) {
            input = _parser.getString(input).trim();
            if (input.contains(SPACE)) {
                isSuccessful = editInstant(input, command);
            } else {
                int trueIndex = Integer.valueOf(input) - 1;
                if (trueIndex < 0 || trueIndex >= _displayTasks.size()) {
                    return false;
                }
                isSuccessful = editStages(trueIndex);
            }
        } else {
            isSuccessful = editStageActions(input, command, isSuccessful);
        }
        setToPreviousView();
        return isSuccessful;
    }

    private boolean editStageActions(String input, String command,
            boolean isSuccessful) {
        input = _parser.getString(input);
        switch (_editStage) {
            case 6 :
                isSuccessful = editTitle(input);
                System.out.println(_displayTasks);
                break;
            case 5 :
                isSuccessful = editVenue(input);
                System.out.println(_displayTasks);
                break;
            case 4 :
                isSuccessful = editDate(input);
                System.out.println(_displayTasks);
                break;
            case 3 :
                isSuccessful = editStartTime(input);
                System.out.println(_displayTasks);
                break;
            case 2 :
                isSuccessful = editEndTime(input);
                System.out.println(_displayTasks);
                break;
            case 1 :
                isSuccessful = editDescription(input, command);
                System.out.println(_displayTasks);
                break;
            default :
                break;
        }
        _editStage--;
        return isSuccessful;
    }

    private boolean editStages(int trueIndex) {
        boolean isSuccessful;
        _editTask = _displayTasks.get(trueIndex);
        _originalTask = new Task(_editTask);

        BakaUI.setInputBoxText(_editTask.getTitle());
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_TITLE"));
        isSuccessful = true;
        _editStage = 6;
        return isSuccessful;
    }

    private boolean editInstant(String input, String command) {
        boolean isSuccessful;
        String index = _parser.getCommand(input);
        int trueIndex = Integer.valueOf(index) - 1;
        if (trueIndex < 0 || trueIndex >= _displayTasks.size()) {
            return false;
        }
        String content = _parser.getString(input);
        Task edits = _parser.add(content);
        _editTask = _displayTasks.get(trueIndex);
        _originalTask = new Task(_editTask);
        _editTask.merge(edits);

        UserAction inputCmd = new UserEditTask(command, _originalTask,
                _editTask);
        isSuccessful = _ra.execute(inputCmd);

        if (isSuccessful) {
            _previousAction = "display " + _editTask.getDate();
        }
        return isSuccessful;
    }

    private boolean editDescription(String input, String command) {
        UserAction inputCmd;
        String nextStagePrompt;
        if (input.toLowerCase().equals(
                BakaTongue.getString("USER_PROMPT_DESCRIPTION").toLowerCase())) {
            input = null;
        }
        _editTask.setDescription(input);

        inputCmd = new UserEditTask(command, _originalTask, _editTask);
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
        _editTask.setTime(parsedDateTime);

        nextStagePrompt = _editTask.getEndTime();
        if (nextStagePrompt == null || nextStagePrompt.equals(STRING_NULL)) {
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
        _editTask.setEndTime(parsedDateTime);

        nextStagePrompt = _editTask.getDescription();
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
        _editTask.setDate(parsedDateTime);

        nextStagePrompt = _editTask.getTime();
        if (nextStagePrompt == null || nextStagePrompt.equals(STRING_NULL)) {
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
        _editTask.setVenue(input);

        nextStagePrompt = _editTask.getDate();
        if (nextStagePrompt == null || nextStagePrompt.equals(STRING_NULL)) {
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
            _editTask.setTitle(_originalTask.getTitle());
        } else {
            _editTask.setTitle(input);
        }

        nextStagePrompt = _editTask.getVenue();
        if (nextStagePrompt == null || nextStagePrompt.equals(STRING_NULL)) {
            nextStagePrompt = BakaTongue.getString("USER_PROMPT_VENUE");
        }
        BakaUI.setInputBoxText(nextStagePrompt);
        BakaUI.setAlertMessageText(BakaTongue.getString("ALERT_EDIT_MODE")
                + BakaTongue.getString("ALERT_EDIT_VENUE"));
        return true;
    }

    private boolean deleteTask(String input) {
        UserAction inputCmd;
        Task task;
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);

        if (_previousAction == null) {
            System.out.println("Nothing to delete");
            return false;

        }

        setToPreviousView();
        boolean isSuccessful = true;
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            if (trueIndex < 1 || trueIndex > _displayTasks.size()) {
                return false;
            }
            task = _displayTasks.get(trueIndex - 1);
            inputCmd = new UserAction("delete", task);
            isSuccessful = isSuccessful && _ra.execute(inputCmd);
            System.out.println(_displayTasks);
            System.out.println("Tasks" + content + "deleted");
        }
        setToPreviousView();

        return isSuccessful;
    }

    private void setToPreviousView() {
        String previousCommand = _parser.getCommand(_previousAction);
        switch (previousCommand.toLowerCase()) {
            case "display" :
            case "show" :
            case "view" :
                displayTask(_previousAction);
                break;
            case "search" :
            case "find" :
                searchTask(_previousAction);
                break;
            default :
                _displayTasks = _database.getAllTasks();
        }
    }

    private boolean markDoneTask(String input, String command) {
        Task task;
        UserAction inputCmd;
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
        setToPreviousView();
        boolean isSuccessful = true;
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            task = _displayTasks.get(trueIndex - 1);
            inputCmd = new UserEditStatus(command, task, true);
            isSuccessful = isSuccessful && _ra.execute(inputCmd);

        }
        setToPreviousView();
        System.out.println(_displayTasks);
        System.out.println("Task marked done");
        return isSuccessful;
    }

    private boolean addTask(String input, String command) {
        UserAction inputCmd;
        Task task;
        task = _parser.add(input);
        inputCmd = new UserAction(command, task);
        boolean isSuccessful = _ra.execute(inputCmd);
        _previousAction = "display " + task.getDate();
        _displayTasks = _database.getTasksWithDate(task.getDate());
        System.out.println("Task added");
        return isSuccessful;
    }

    private boolean setTheme(String theme) {
        theme = _parser.getString(theme).trim();
        try {
            @SuppressWarnings("unused")
            ThemeReader newTheme = new ThemeReader("./" + theme);
            setToPreviousView();
            return true;
        } catch (NoSuchFileException e) {
            setToPreviousView();
            return false;
        }
    }
}
