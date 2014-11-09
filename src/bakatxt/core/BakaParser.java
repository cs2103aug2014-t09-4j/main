//@author A0115160X
package bakatxt.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class BakaParser implements BakaParserInterface {

    private static final String STRING_EMPTY = "";
    private static final String STRING_SPACE = " ";
    private static final String STRING_ADD = "@";
    private static final String STRING_DOUBLE_DASH = "--";
    private static final String STRING_COMMA = ",";
    private static final String STRING_AT = "at";
    private static final String STRING_ON = "on";
    private static final String STRING_TO = "to";
    private static final String STRING_FROM = "from";
    private static final String STRING_NOW = "now";
    private static final String STRING_DASH = "-";
    private static final String STRING_YEAR = "2014";
    private static final String STRING_YEAR_FRAG = "20";
    private static final String STRING_CANT_PARSE = "vista";
    private static final String STRING_REPLACEMENT = "\\/ista";
    private static final String STRING_TMR = "tomorrow";
    private static final String STRING_TODAY = "today";
    private static final String STRING_TONIGHT = "tonight";
    private static final String STRING_TONIGHT_TIME = "1900";

    private static final String DATE_FORMAT_DDMMYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-](\\d\\d)";
    private static final String DATE_FORMAT_DDMMYYYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-]((19|2[01])\\d\\d)";
    private static final String DATE_FORMAT_DDMM_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])";
    private static final String DATE_FORMAT_DIVIDER_REGEX = "[/-]";
    private static final String DATE_FORMAT_STANDARD = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HHmm";
    private static final String DATE_FORMAT_SPECIAL = "EEEE, dd MMMM YYYY";
    private static final String DISABLE_NUMBER_REGEX = "\\d{3,}?";
    private static final String DISABLE_PARSING_REGEX = "(([0-2]\\d[0-5]\\d)|(\\d{1,2}))[^h]";
    private static final String DISABLE_FAKE_TIME_REGEX = "\\D\\S+\\d";
    private static final String DISABLE_FAKE_DIGIT_REGEX = "\\d{1,2}?";

    private static boolean _isDate;
    private static boolean _isTime;
    private static boolean _isVenue;
    private static boolean _isDescription;
    private static boolean _isExceptionString;
    private static String _title;
    private static String _date;
    private static String _time;
    private static String _endTime;
    private static String _venue;
    private static String _description;
    private static String _inputDate;
    private static String _inputTime;
    private static String _inputDateThatCantParse;
    private static ArrayList<String> _inputThatCantParse;

    public BakaParser() {
        resetDetails();
    }


    /**
     * Takes in a String and parse it. First, the description will be
     * identified, followed by the date, time, venue and title.
     * 
     * @param str
     *            is the <code>String</code> containing the command and details
     *            to be parsed
     * @return task containing the parsed information
     */
    @Override
    public Task add(String str) {
        resetDetails();
        String firstWord = getFirstWord(str);
        if (firstWord.toLowerCase().equals("add")) {
            str = str.replaceFirst(firstWord, STRING_EMPTY).trim();
        }

        if (str.contains(STRING_DOUBLE_DASH)) {
            str = str.replace(STRING_DOUBLE_DASH + STRING_SPACE,
                    STRING_DOUBLE_DASH);
            identifyDescription(str);
            str = replaceDescription(str);
        }

        if (str.contains(STRING_CANT_PARSE)) {
            str = str.replaceAll(STRING_CANT_PARSE, STRING_REPLACEMENT);
            _isExceptionString = true;
        }

        if (str.contains(STRING_ADD)) {
            str = str.replace(STRING_ADD + STRING_SPACE, STRING_ADD);
        }

        identifyDate(str);
        if (_date != null) {
            _isDate = true;
        }

        identifyTime(str);
        if (_time != null) {
            checkWithCurrentTime(_time);
            if (_time != null) {
                _isTime = true;
            }
        }
        
        if (_isExceptionString) {
            str = str.replaceAll(STRING_REPLACEMENT, STRING_CANT_PARSE);
        }

        if (str.contains(STRING_ADD)) {
            identifyVenue(str);
        }
       
        identifyTitle(str);

        Task task = new Task(_title);
        task.setDate(_date);
        if (_time != null) {
            String[] part = _time.split(STRING_DASH);
            _time = part[0].trim();
            if (part.length > 1) {
                _endTime = part[1].trim();
            }
        }
        task.setTime(_time);
        task.setEndTime(_endTime);
        task.setVenue(_venue);
        task.setDescription(_description);
        if (!_isDate && !_isTime) {
            task.setFloating(true);
        }

        return task;
    }

    /**
     * 
     * @param input
     *            is the <code>String</code> containing the command and details
     *            to be parsed
     * @return a <code>String</code> of the command or first word
     */
    private static String getFirstWord(String input) {
        String[] part = input.split(STRING_SPACE);
        return part[0].trim();
    }

    /**
     * Set all the global variables to <code>null</code>
     */
    private static void resetDetails() {
        _date = null;
        _time = null;
        _endTime = null;
        _venue = null;
        _description = null;
        _inputThatCantParse = new ArrayList<String>();
    }

    /**
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     * @return a <code>String</code> of details without the date and time
     */
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

    /**
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     * @return a <code>String</code> of details without the unnecessary
     *         prepositions at the end
     */
    private static String removePrepositions(String input) {
        input = input.replace("\\s+", "\\s");
        String[] part = input.trim().split(STRING_SPACE);
        String[] prepoList = { "at", "on", "from" };

        do {
            int lastIndex = part.length - 1;
            for (int i = 0; i < prepoList.length; i++) {
                if (part[lastIndex].equals(prepoList[i])) {
                    input = input.substring(0,
                            input.length() - prepoList[i].length()).trim();
                }
            }
            part = input.split(STRING_SPACE);

        } while (part[part.length - 1].equals(STRING_AT)
                || part[part.length - 1].equals(STRING_ON)
                || part[part.length - 1].equals(STRING_FROM));

        return input;
    }

    /**
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     * @return a <code>String</code> of details without the description
     */
    private static String replaceDescription(String input) {
        if (_isDescription) {
            String descriptionTemp = STRING_DOUBLE_DASH + _description;
            input = input.replace(descriptionTemp, STRING_SPACE).trim();
        }
        return input;
    }

    /**
     * Title is parsed from the details.
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     */
    private static void identifyTitle(String input) {
        input = replaceDateTimeDescription(input);

        if (_isVenue) {
            String venueTemp = STRING_ADD + _venue;
            input = input.replace(venueTemp, STRING_SPACE);
        }
        input = input.trim();
        if (input.isEmpty()) {
            _title = input;
        } else {
            _title = removePrepositions(input).trim();
        }
        _title = _title.replaceAll("\\s+", STRING_SPACE);
    }

    /**
     * Description is parsed from the details.
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     */
    private static void identifyDescription(String input) {
        String[] part = input.split(STRING_DOUBLE_DASH);
        if (part.length == 0) {
            _description = null;
        } else {
        _description = part[1].trim();
        }
        _isDescription = true;
    }

    /**
     * Venue is parsed from the details.
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     */
    private static void identifyVenue(String input) {
        input = replaceDateTimeDescription(input);
        int index = input.indexOf(STRING_ADD) + 1;
        _isVenue = true;
        input = input.substring(index).trim();
        _venue = removePrepositions(input).trim();
    }

    /**
     * Date is parsed from the details using a modified natty parser.
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     */
    private static void identifyDate(String input) {
        Parser parser = new Parser();
        String[] temp = input.split(STRING_SPACE);
        String newDate;
        String[] dateFragment;

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
                    messageFragment = messageFragment + STRING_DASH
                            + STRING_YEAR;
                }
                dateFragment = messageFragment.split(DATE_FORMAT_DIVIDER_REGEX);
                if (dateFragment[2].length() == 2) {
                    dateFragment[2] = STRING_YEAR_FRAG + dateFragment[2];
                }
                newDate = dateFragment[2] + STRING_DASH + dateFragment[1]
                        + STRING_DASH + dateFragment[0];

                input = input.replace(originalFragment, newDate);
                _inputDateThatCantParse = originalFragment;
            }

            if (input.contains(STRING_TMR)) {
                _inputDateThatCantParse = STRING_TMR;
            }
            if (input.contains(STRING_TODAY)) {
                _inputDateThatCantParse = STRING_TODAY;
            }

            if (messageFragment.matches(DISABLE_NUMBER_REGEX)
                    || messageFragment.matches(DISABLE_PARSING_REGEX)
                    || messageFragment.matches(DISABLE_FAKE_TIME_REGEX)
                    || messageFragment.matches(DISABLE_FAKE_DIGIT_REGEX)) {
                _inputThatCantParse.add(originalFragment);
                String[] inputPart = input.split(STRING_SPACE);

                input = STRING_EMPTY;
                for (int j = 0; j < inputPart.length; j++) {
                    if (inputPart[j].equals(originalFragment)) {
                        inputPart[j] = STRING_EMPTY;
                    }
                    input += inputPart[j] + STRING_SPACE;
                }
            }
        }

        List<DateGroup> dateGroup = parser.parse(input);
        if (dateGroup.size() > 0) {
            Date date = dateGroup.get(0).getDates().get(0);
            _inputDate = dateGroup.get(0).getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    DATE_FORMAT_STANDARD);
            String output = dateFormat.format(date);

            _date = output;
        } else {
            _date = null;
        }
    }

    /**
     * Time is parsed from the details using a modified natty parser.
     * 
     * @param input
     *            is a <code>String</code> of details to be parsed
     */
    private static void identifyTime(String input) {
        Parser parser = new Parser();

        if (_inputDateThatCantParse != null) {
            input = input.replace(_inputDateThatCantParse, STRING_SPACE);
        }

        String[] inputPart = input.split(STRING_SPACE);
        input = STRING_EMPTY;
        for (int i = 0; i < inputPart.length; i++) {
            for (int j = 0; j < _inputThatCantParse.size(); j++) {
                if (inputPart[i].equals(_inputThatCantParse.get(j))) {
                    inputPart[i] = STRING_EMPTY;
                }
            }
            input += inputPart[i] + STRING_SPACE;
        }

        if (input.contains(STRING_TONIGHT)) {
            input = input.replace(STRING_TONIGHT, STRING_TONIGHT_TIME);
        }

        List<DateGroup> dateGroup = parser.parse(input);
        if (dateGroup.size() > 0) {

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
            SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
            String outputStart = timeFormat.format(timeStart);

            if (timeEnd != null) {
                String outputEnd = timeFormat.format(timeEnd);
                output = outputStart + STRING_SPACE + STRING_DASH
                        + STRING_SPACE + outputEnd;
            } else {
                output = outputStart;
            }

            _time = output;
        } else {
            _time = null;
        }
    }

    /**
     * Set the time to <code>null</code> if the current time is parsed due to
     * the natty parser.
     * 
     * @param input
     *            is a <code>String</code> of time in HHmm format
     */
    private static void checkWithCurrentTime(String input) {
        Parser parser = new Parser();

        List<DateGroup> dateGroup = parser.parse(STRING_NOW);
        Date date = dateGroup.get(0).getDates().get(0);
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String output = timeFormat.format(date);

        if (input.equals(output)) {
            _time = null;
        } else {
            dateGroup = parser.parse("1 minute later");
            Date newDate = dateGroup.get(0).getDates().get(0);
            String newOutput = timeFormat.format(newDate);
            if (input.equals(newOutput)) {
                _time = null;
            }
        }
    }

    /**
     * @return <code>String</code> of current time in HHmm format.
     */
    public static String getCurrentTime() {
        Parser parser = new Parser();

        List<DateGroup> dateGroup = parser.parse(STRING_NOW);
        Date date = dateGroup.get(0).getDates().get(0);
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String output = timeFormat.format(date);

        return output;
    }

    /**
     * @param input
     *            a <code>String</code> that contains a command and details.
     * @return <code>String</code> of details without the command or the
     *         original <code>String</code> input if there are no spaces.
     */
    @Override
    public String getString(String input) {
        int index = input.indexOf(STRING_SPACE);
        input = input.substring(index).trim();
        return input;
    }

    /**
     * @param input
     *            a <code>String</code> that contains a command and details.
     * @return <code>String</code> of the command without the details or a
     *         <code>String</code> of the first word of the input.
     */
    @Override
    public String getCommand(String input) {
        if (input == null || input.isEmpty()) {
            return STRING_EMPTY;
        }
        String[] part = input.split(STRING_SPACE);
        return part[0].toUpperCase();
    }

    /**
     * Creates an <code>ArrayList</code> of <code>Integer</code> from the input
     * <code>String</code>. When there is more than one index, the indices are
     * identified by a comma, a space, a dash or "to".
     * 
     * @param input
     *            a <code>String</code> containing the indices or a range of
     *            indices.
     * @return an <code>ArrayList</code> of the indices.
     */
    @Override
    public ArrayList<Integer> getIndexList(String input) {
        input = input.trim();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] num;

        if (input.contains(STRING_DASH) || input.contains(STRING_TO)) {
            input = input.replaceAll("\\s+", STRING_EMPTY);
            if (input.contains(STRING_DASH)) {
                num = input.split(STRING_DASH);
            } else {
                num = input.split(STRING_TO);
            }
            int firstIndex = Integer.valueOf(num[0]);
            int lastIndex = Integer.valueOf(num[1]);
            for (int i = firstIndex; i <= lastIndex; i++) {
                list.add(i);
            }
        } else {
            if (input.contains(STRING_COMMA)) {
                num = input.split(STRING_COMMA);
            } else {
                num = input.split("\\s+");
            }
            for (int i = 0; i < num.length; i++) {
                list.add(Integer.valueOf(num[i].trim()));
            }

        }
        return list;
    }

    /**
     * Takes in a String and parse the date.
     * 
     * @param input
     *            a <code>String</code> containing a date.
     * @return a <code>String</code> of the date in YYYY-MM-dd format or
     *         <code>null</code> when the input cannot be parsed.
     */
    @Override
    public String getDate(String input) {
        String date;
        if (input != null) {
            identifyDate(input);
            date = _date;
            _date = null;
        } else {
            date = null;
        }
        return date;
    }

    /**
     * Takes in a String of a specific date format and parse the date into
     * another format.
     * 
     * @param input
     *            a <code>String</code> containing a date in YYYY-MM-dd format.
     * @return a <code>String</code> of the date in EEEE, dd MMMM YYYY format or
     *         <code>null</code> when the input cannot be parsed.
     */
    @Override
    public String getFormattedDate(String input) {
        String formattedDate;
        if (input != null) {
            Parser parser = new Parser();
            List<DateGroup> dateGroup = parser.parse(input);
            if (dateGroup.size() > 0) {
                Date date = dateGroup.get(0).getDates().get(0);
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                    DATE_FORMAT_SPECIAL);
                String output = DATE_FORMAT.format(date);

                formattedDate = output;
            } else {
                formattedDate = null;
            }
        } else {
            formattedDate = null;
        }
        return formattedDate;
    }

    /**
     * Takes in a String and parse the time.
     * 
     * @param input
     *            a <code>String</code> containing a time.
     * @return a <code>String</code> of the time in HHmm, 24 hours format or
     *         <code>null</code> when the input cannot be parsed.
     */
    @Override
    public String getTime(String input) {
        String time;
        if (input != null) {
            identifyTime(input);
            time = _time;
            _time = null;
        } else {
            time = null;
        }
        return time;
    }
}
