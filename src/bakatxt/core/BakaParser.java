package bakatxt.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class BakaParser implements BakaParserInterface {

    // private static final String MESSAGE_ADD_NO_TITLE =
    // "Invalid add command, please add a title!";
    // private static final String MESSAGE_EMPTY_FILE = "The file is empty!";

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
    private static final String NUMBER_REGEX = "\\d{3,}?";
    private static final String DISABLE_PARSING_REGEX = "(([0-2]\\d[0-5]\\d)|(\\d{1,2}))[a-z]";

    private static BakaParser _parser = null;
    private static boolean _isDate;
    private static boolean _isTime;
    private static boolean _isVenue;
    private static boolean _isDescription;
    private static String _title;
    private static String _date;
    private static String _time;
    private static String _venue;
    private static String _description;
    private static String _inputDate;
    private static String _inputTime;
    private static String _inputDateThatCantParse;
    private static String _inputThatCantParse;

    public BakaParser() {
        _isDate = false;
        _isTime = false;
        _isVenue = false;
        _isDescription = false;
    }

    public static BakaParser getInstance() {
        if (_parser == null) {
            _parser = new BakaParser();
        }
        return _parser;
    }

    @Override
    public Task add(String input) {
        // TODO Auto-generated method stub
        String str = input;
        str = str.replaceFirst("add", STRING_EMPTY).trim();

        if (str.contains(STRING_DASH)) {
            str = str.replace(STRING_DASH + STRING_SPACE, STRING_DASH);
            identifyDescription(str);
            str = replaceDescription(str);
        }

        if (str.contains(STRING_ADD)) {
            str = str.replace(STRING_ADD + STRING_SPACE, STRING_ADD);
            identifyVenue(str);
            str = replaceVenue(str);
        }

        identifyDate(str);
        if (_date != null) {
            _isDate = true;
        }

        identifyTime(str);
        if (_time != null) {
            _isTime = true;
        }

        identifyTitle(str);

        if (_title.equals(STRING_EMPTY)) {
            // TODO return MESSAGE_ADD_NO_TITLE;
        }

        Task task = new Task(_title);
        task.setDate(_date);
        task.setTime(_time);
        task.setVenue(_venue);
        task.setDescription(_description);
        if (!_isDate && !_isTime) {
            task.setFloating(true);
        }
        resetDetails();

        return task;
    }

    private static void resetDetails() {
        _date = null;
        _time = null;
        _venue = null;
        _description = null;
    }

    private static String replaceDateTimeDescription(String input) {

        if (_isDate) {
            input = input.replace(_inputDate, STRING_SPACE);
            if (_inputDateThatCantParse != null) {
                input = input.replace(_inputDateThatCantParse, STRING_SPACE);
            }
        }
        if (_isTime) {
            input = input.replace(_inputTime, STRING_SPACE);
        }
        if (_isDescription) {
            input = replaceDescription(input);
        }
        return input;
    }

    private static String removePrepositions(String input) {
        input = input.replace("\\s+", "\\s");
        String[] part = input.trim().split(STRING_SPACE);

        do {
            int lastIndex = part.length - 1;
            if (part[lastIndex].equals(STRING_AT)
                    || part[lastIndex].equals(STRING_ON)) {
                input = input.substring(0, input.length() - 2).trim();
            }
            part = input.split(STRING_SPACE);
        } while (part[part.length - 1].equals(STRING_AT)
                || part[part.length - 1].equals(STRING_ON));

        return input;
    }

    private static String replaceDescription(String input) {
        if (_isDescription) {
            String descriptionTemp = STRING_DASH + _description;
            input = input.replace(descriptionTemp, STRING_SPACE).trim();
        }
        return input;
    }

    private static String replaceVenue(String input) {
        if (_isVenue) {
            String venueTemp = STRING_ADD + _venue;
            input = input.replace(venueTemp, STRING_SPACE).trim();
        }
        return input;
    }

    private static void identifyTitle(String input) {
        input = replaceDateTimeDescription(input).trim();
        input = replaceVenue(input);

        if (input.isEmpty()) {
            _title = input;
        } else {
            _title = removePrepositions(input).trim();
        }
    }

    private static void identifyDescription(String input) {
        String[] part = input.split(STRING_DASH);
        _description = part[1].trim();
        _isDescription = true;
    }

    private static void identifyVenue(String input) {
        input = replaceDateTimeDescription(input);
        int index = input.indexOf(STRING_ADD) + 1;
        _isVenue = true;
        input = input.substring(index).trim();
        _venue = removePrepositions(input).trim();
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
                    _inputDateThatCantParse = originalFragment;
                }

                if (input.contains("tomorrow")) {
                    _inputDateThatCantParse = "tomorrow";
                }
                if (input.contains("today")) {
                    _inputDateThatCantParse = "today";
                }

                if (messageFragment.matches(NUMBER_REGEX)
                        || messageFragment.matches(DISABLE_PARSING_REGEX)) {
                    _inputThatCantParse = originalFragment;
                    input = input.replace(originalFragment, STRING_SPACE);
                }
            }

            List<DateGroup> dateGroup = parser.parse(input);
            Date date = dateGroup.get(0).getDates().get(0);
            _inputDate = dateGroup.get(0).getText();
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
            if (_inputDateThatCantParse != null) {
                input = input.replace(_inputDateThatCantParse, STRING_SPACE);
            }

            if (_inputThatCantParse != null) {
                input = input.replace(_inputThatCantParse, STRING_SPACE);
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
            _inputTime = dateGroup.get(0).getText();
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
    public String getString(String input) {
        // TODO remove first word and return the title
        int index = input.indexOf(STRING_SPACE);
        input = input.substring(index);
        return input;
    }

    @Override
    public Task display(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCommand(String input) {
        String[] part = input.split(STRING_SPACE);
        return part[0].toUpperCase();
    }

    // public LinkedList<Task> getTasks() {
    // return _database.getAllTasks();
    // }
}
