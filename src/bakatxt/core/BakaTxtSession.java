package bakatxt.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class BakaTxtSession implements BakaTxtSessionInterface {

    // TODO Ensure filename input is correct
    // TODO Ensure database is closed
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

    Database database;

    public BakaTxtSession(String fileName) {
        database = new Database(fileName);
        _isDate = false;
        _isTime = false;
        _isVenue = false;
        _isDescription = false;
    }

    @Override
    public String add(String input) {
        // TODO Auto-generated method stub
        String str = input;

        if (str.contains("--")) {
            str.replace("-- ", "--");
            identifyDescription(str);
        }

        identifyDate(str);
        if (_date != null) {
            _isDate = true;
        }

        identifyTime(str);
        if (_time != null) {
            _isTime = true;
        }

        if (str.contains("@")) {
            str = str.replace("@ ", "@");
            identifyVenue(str);
        }

        identifyTitle(str);

        if (_title.equals("")) {
            return "Invalid add command, please add a title!";
        }

        Task task = new Task(_title);
        task.addDate(_date);
        task.addTime(_time);
        task.addVenue(_venue);
        task.addDescription(_description);
        if (!_isDate && !_isTime) {
            task.setFloating(true);
        }

        database.add(task);

        return task.toDisplayString();
    }

    private String replaceDateTimeDescription(String input) {
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
            String descriptionTemp = "--" + _description;
            inputTemp = inputTemp.replace(descriptionTemp, " ");
        }
        return inputTemp;
    }

    private String removePrepositions(String input) {
        String inputTemp = input;
        String part[] = inputTemp.trim().split(" ");
        if (part[part.length - 1].contains("on")
                || part[part.length - 1].contains("at")) {
            inputTemp = inputTemp.replace("on", "");
            inputTemp = inputTemp.replace("at", "");
        }
        return inputTemp;
    }

    private void identifyTitle(String input) {
        String newInput = replaceDateTimeDescription(input);
        String inputTemp = newInput;

        if (_isVenue) {
            String venueTemp = "@" + _venue;
            inputTemp = inputTemp.replace(venueTemp, " ");
        }

        _title = removePrepositions(inputTemp).trim();
    }

    private void identifyDescription(String input) {
        String[] part = input.split("--");
        _description = part[1].trim();
        _isDescription = true;
    }

    private void identifyVenue(String input) {
        String newInput = replaceDateTimeDescription(input);
        int index = newInput.indexOf('@') + 1;
        _isVenue = true;
        newInput = newInput.substring(index).trim();
        _venue = removePrepositions(newInput).trim();
    }

    private void identifyDate(String input) {
        Parser parser = new Parser();
        String[] temp = input.split(" ");
        String newDate;
        String[] dateFragment;

        try {
            for (int i = 0; i < temp.length; i++) {
                String messageFragment = temp[i];
                String originalFragment = temp[i];
                // dd/MM/YY or dd/MM/YYYY or dd/MM
                if (messageFragment
                        .matches("(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-]((19|2[01])\\d\\d)")
                        || messageFragment
                                .matches("(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-](\\d\\d)")
                        || messageFragment
                                .matches("(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])")) {

                    // dd/MM
                    if (messageFragment.length() <= 5
                            && messageFragment.length() > 2) {
                        messageFragment = messageFragment + "-2014";
                    }

                    dateFragment = messageFragment.split("[/-]");
                    if (dateFragment[2].length() == 2) {
                        dateFragment[2] = "20" + dateFragment[2];
                    }
                    newDate = dateFragment[2] + "-" + dateFragment[1] + "-"
                            + dateFragment[0];

                    input = input.replace(originalFragment, newDate);
                    _originalDigitDateFormat = originalFragment;
                }
            }

            List<DateGroup> dateGroup = parser.parse(input);
            Date date = dateGroup.get(0).getDates().get(0);
            _originalDateFormat = dateGroup.get(0).getText();
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
            String output = DATE_FORMAT.format(date);

            _date = output;
        } catch (Exception ex) {
            _date = null;
        }
    }

    private void identifyTime(String input) {
        try {
            if (_originalDigitDateFormat != null) {
                input = input.replace(_originalDigitDateFormat, " ");
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
        return null;
    }

    @Override
    public String display(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String display() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sort() {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFileName() {
        return database.getFileName();
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
