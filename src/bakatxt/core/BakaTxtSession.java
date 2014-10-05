package bakatxt.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class BakaTxtSession {

    private static final String MESSAGE_ADD_NO_TITLE = "Invalid add command, please add a title!";
    private static final String MESSAGE_EMPTY_FILE = "The file is empty!";

    private static final String STRING_EMPTY = "";
    private static final String STRING_SPACE = " ";
    private static final String STRING_ADD = "@";
    private static final String STRING_DASH = "--";
    private static final String STRING_AT = "at";
    private static final String STRING_ON = "on";
    private static final String STRING_DASH_DATE = "-";
    private static final String STRING_YEAR = "2014";
    private static final String STRING_YEAR_FRAG = "20";

    private static final String DATE_FORMAT_DDMMYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-](\\d\\d)";
    private static final String DATE_FORMAT_DDMMYYYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-]((19|2[01])\\d\\d)";
    private static final String DATE_FORMAT_DDMM_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])";
    private static final String DATE_FORMAT_DIVIDER_REGEX = "[/-]";
    private static final String DATE_FORMAT_STANDARD = "yyyy-MM-dd";

    private static boolean _isDate;
    private static boolean _isTime;
    private static boolean _isVenue;
    private static boolean _isDescription;
    private static String _title;
    private static String _date;
    private static String _time;
    private static String _venue;
    private static String _description;
    private static String _originalDateFormat;
    private static String _originalTimeFormat;
    private static String _originalDigitDateFormat;
    private static Database _database;

    public BakaTxtSession(String fileName) {
        _database = new Database(fileName);
        _isDate = false;
        _isTime = false;
        _isVenue = false;
        _isDescription = false;
    }

    @Override
    public String add(String input) {
        // TODO Auto-generated method stub
        String str = input;

        if (str.contains(STRING_DASH)) {
            str = str.replace(STRING_DASH + STRING_SPACE, STRING_DASH);
            identifyDescription(str);
            str = replaceDescription(str);
        }

        identifyDate(str);
        if (_date != null) {
            _isDate = true;
        }

        identifyTime(str);
        if (_time != null) {
            _isTime = true;
        }

        if (str.contains(STRING_ADD)) {
            str = str.replace(STRING_ADD + STRING_SPACE, STRING_ADD);
            identifyVenue(str);
        }

        identifyTitle(str);

        if (_title.equals(STRING_EMPTY)) {
            return MESSAGE_ADD_NO_TITLE;
        }

        Task task = new Task(_title);
        task.addDate(_date);
        task.addTime(_time);
        task.addVenue(_venue);
        task.addDescription(_description);
        if (!_isDate && !_isTime) {
            task.setFloating(true);
        }
        _database.add(task);
        resetDetails();

        return task.toDisplayString();
    }

    private static void resetDetails() {
        _date = null;
        _time = null;
        _venue = null;
        _description = null;
    }

    private static String replaceDateTimeDescription(String input) {
        String inputTemp = input;
        if (_isDate) {
            inputTemp = inputTemp.replace(_originalDateFormat, " ");
            if (_originalDigitDateFormat != null) {
                inputTemp = inputTemp.replace(_originalDigitDateFormat, " ");
            }
        }
        if (_isTime) {
            inputTemp = inputTemp.replace(_originalTimeFormat, " ");
        }
        if (_isDescription) {
            inputTemp = replaceDescription(input);
        }
        return inputTemp;
    }

    private static String removePrepositions(String input) {
        String inputTemp = input.replace("\\s+", "\\s");
        String[] part = inputTemp.trim().split(STRING_SPACE);

        do {
            int lastIndex = part.length - 1;
            if (part[lastIndex].equals(STRING_AT)
                    || part[lastIndex].equals(STRING_ON)) {
                inputTemp = inputTemp.substring(0, inputTemp.length() - 2)
                        .trim();
            }
            part = inputTemp.split(STRING_SPACE);
        } while (part[part.length - 1].equals(STRING_AT)
                || part[part.length - 1].equals(STRING_ON));

        return inputTemp;
    }

    private static String replaceDescription(String input) {
        if (_isDescription) {
            String descriptionTemp = "--" + _description;
            input = input.replace(descriptionTemp, " ").trim();
        }
        return input;
    }

    private static void identifyTitle(String input) {
        String newInput = replaceDateTimeDescription(input);
        String inputTemp = newInput;

        if (_isVenue) {
            String venueTemp = STRING_ADD + _venue;
            inputTemp = inputTemp.replace(venueTemp, STRING_SPACE);
        }
        inputTemp = inputTemp.trim();
        if (inputTemp.isEmpty()) {
            _title = inputTemp;
        } else {
            _title = removePrepositions(inputTemp).trim();
        }
    }

    private static void identifyDescription(String input) {
        String[] part = input.split(STRING_DASH);
        _description = part[1].trim();
        _isDescription = true;
    }

    private static void identifyVenue(String input) {
        String newInput = replaceDateTimeDescription(input);
        int index = newInput.indexOf(STRING_ADD) + 1;
        _isVenue = true;
        newInput = newInput.substring(index).trim();
        _venue = removePrepositions(newInput).trim();
    }

    private static void identifyDate(String input) {
        Parser parser = new Parser();
        String[] temp = input.split(STRING_SPACE);
        String newDate;
        String[] dateFragment;

        try {
            for (int i = 0; i < temp.length; i++) {
                String messageFragment = temp[i];
                String originalFragment = temp[i];
                // dd/MM/YY or dd/MM/YYYY or dd/MM
                if (messageFragment.matches(DATE_FORMAT_DDMMYYYY_REGEX)
                        || messageFragment.matches(DATE_FORMAT_DDMMYY_REGEX)
                        || messageFragment.matches(DATE_FORMAT_DDMM_REGEX)) {

                    // dd/MM
                    if (messageFragment.length() <= 5
                            && messageFragment.length() > 2) {
                        messageFragment = messageFragment + STRING_DASH_DATE
                                + STRING_YEAR;
                    }

                    dateFragment = messageFragment
                            .split(DATE_FORMAT_DIVIDER_REGEX);
                    if (dateFragment[2].length() == 2) {
                        dateFragment[2] = STRING_YEAR_FRAG + dateFragment[2];
                    }
                    newDate = dateFragment[2] + STRING_DASH_DATE
                            + dateFragment[1] + STRING_DASH_DATE
                            + dateFragment[0];

                    input = input.replace(originalFragment, newDate);
                    _originalDigitDateFormat = originalFragment;
                }

                if (input.contains("tomorrow")) {
                    _originalDigitDateFormat = "tomorrow";
                }
                if (input.contains("today")) {
                    _originalDigitDateFormat = "today";
                }
            }

            List<DateGroup> dateGroup = parser.parse(input);
            Date date = dateGroup.get(0).getDates().get(0);
            _originalDateFormat = dateGroup.get(0).getText();
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                    DATE_FORMAT_STANDARD);
            String output = DATE_FORMAT.format(date);

            _date = output;
        } catch (Exception ex) {
            _date = null;
        }
    }

    private static void identifyTime(String input) {
        try {
            if (_originalDigitDateFormat != null) {
                input = input.replace(_originalDigitDateFormat, STRING_SPACE);
            }

            Parser parser = new Parser();
            List<DateGroup> dateGroup = parser.parse(input);
            Date timeStart = null;
            Date timeEnd = null;
            String output;

            if (dateGroup.get(0).getDates().size() == 1) {
                timeStart = dateGroup.get(0).getDates().get(0);
            } else {
                timeStart = dateGroup.get(0).getDates().get(0);
                timeEnd = dateGroup.get(0).getDates().get(1);
            }
            _originalTimeFormat = dateGroup.get(0).getText();
            SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmm");
            String outputStart = TIME_FORMAT.format(timeStart);
            if (timeEnd != null) {
                String outputEnd = TIME_FORMAT.format(timeEnd);
                output = outputStart + " - " + outputEnd;
            } else {
                output = outputStart;
            }

            _time = output;
        } catch (Exception ex) {
            _time = null;
        }
    }

    @Override
    public String delete(String input) {
        // TODO Auto-generated method stub
        // TODO Get a specific task to delete
        // TODO main function to filter for exact title to delete
        return null;
    }

    @Override
    public String display(String input) {
        // TODO Auto-generated method stub
        if (input.isEmpty()) {
            return display();
        }
        LinkedList<Task> selectedTasks = _database.getTaskWithTitle(input);
        return toOutputString(selectedTasks);
    }

    @Override
    public String display() {
        // TODO Auto-generated method stub
        LinkedList<Task> allTasks = _database.getAllTasks();
        return toOutputString(allTasks);
    }

    private String toOutputString(LinkedList<Task> tasks) {
        String output = new String();
        if (tasks.isEmpty()) {
            output = MESSAGE_EMPTY_FILE;
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                output += (i + 1) + ". " + tasks.get(i).toDisplayString();
            }
        }
        return output;
    }

    @Override
    public void sort() {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit() {
        _database.close();
    }

    @Override
    public String getFileName() {
        return _database.getFileName();
    }

    @Override
    public String getPrevious() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTotal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUndone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String done(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTask(String title) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteDone() {
        // TODO Auto-generated method stub
        return null;
    }

}
