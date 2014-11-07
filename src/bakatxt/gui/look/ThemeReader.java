//@author A0116538A

package bakatxt.gui.look;

import static java.lang.Integer.valueOf;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;

import bakatxt.core.Database;
import bakatxt.core.Task;
import bakatxt.international.BakaTongue;

/**
 * This class controls the colors as well as the fonts of the GUI from the theme
 *
 */
@SuppressWarnings("boxing")
public class ThemeReader {

    private static final String EXT = ".bakaTheme";
    private static final String HEADER = "THEMES";

    /**
     * The font to use for the GUI
     */
    private static String _typeface = "Arial";

    /**
     * Colors for the boxes in the GUI
     */
    private static Color _input = new Color(100, 100, 100);
    private static Color _panel = new Color(66, 66, 66);
    private static Color _taskLight = new Color(48, 48, 48);
    private static Color _taskDark = new Color(36, 36, 36);
    private static Color _scroll = new Color(0, 0, 0, 100);
    private static Color _selection = new Color(0, 0, 0, 150);

    /**
     * Color, font size, and font type of various elements in the GUI
     */
    private static BakaTheme _date = new BakaTheme(new Color(253, 184, 19),
                                                   Font.PLAIN, 12);
    private static BakaTheme _alert = new BakaTheme(new Color(250, 250, 250),
                                                    Font.PLAIN, 12);
    private static BakaTheme _location = new BakaTheme(new Color(227, 122, 37),
                                                       Font.BOLD, 14);
    private static BakaTheme _number = new BakaTheme(new Color(80, 80, 80),
                                                     Font.PLAIN, 16);
    private static BakaTheme _title = new BakaTheme(new Color(239, 62, 47),
                                                    Font.BOLD, 18);
    private static BakaTheme _interact = new BakaTheme(new Color(228, 224, 227),
                                                        Font.PLAIN, 24);
    private static BakaTheme _default = new BakaTheme(new Color(228, 224, 227),
                                                      Font.PLAIN, 12);

    //@author A0116320Y
    public static boolean setTheme(String input) {
        // input is asserted to be a selected choice
        input = input.trim();
        LinkedList<Task> choices = themeChoices();
        Path themePath = null;
        try {
            int index = Integer.valueOf(input) - 1;
            if (index >= choices.size() || index < 0) {
                return false;
            }
            themePath = Paths
                    .get("themes", choices.get(index).getTitle() + EXT);
            if (Files.exists(themePath)) {
                readFile(themePath);
            } else {
                return false;
            }
        } catch (NumberFormatException ex) {

        }
        if (themePath != null) {
            Database.getInstance().updateTheme(themePath.toString());
            return true;
        }
        return false;
    }

    public static boolean setTheme(Path themePath) {
        if (Files.exists(themePath)) {
            readFile(themePath);
            return true;
        }
        return false;
    }

    public static LinkedList<Task> themeChoices() {
        Path dir = Paths.get("themes/").toAbsolutePath();
        if (!dir.toFile().exists()) {
            try {
                Files.createDirectories(dir);
            } catch (Exception ex) {
            }
        }
        populateDefaultThemes();
        File[] files = dir.toFile().listFiles();
        LinkedList<Task> names = new LinkedList<Task>();
        for (File f : files) {
            if (f.isFile() && f.toString().contains(EXT)) {
                String displayString = f.getName();
                displayString = displayString.replace(EXT, "");
                Task themeDisplay = new Task(displayString);
                themeDisplay.setDate(BakaTongue.getString(HEADER));
                names.add(themeDisplay);
            }
        }
        Collections.sort(names);
        return names;
    }

    private static void populateDefaultThemes() {
        Charset charset = Charset.forName("UTF-8");
        OpenOption[] options = { StandardOpenOption.CREATE };
        populateDarkAsMySoulTheme(charset, options);
        populateWhiteSpaceTheme(charset, options);
    }

    private static void populateWhiteSpaceTheme(Charset charset,
            OpenOption[] options) {
        Path defaultTheme = Paths.get("themes/", "WhiteSpace.bakaTheme");
        try (BufferedWriter outputStream = Files.newBufferedWriter(
                defaultTheme, charset, options)) {
            outputStream.write("TYPEFACE = Arial");
            outputStream.newLine();
            outputStream.write("INPUT = (255,255,255)");
            outputStream.newLine();
            outputStream.write("PANEL = (250,250,250)");
            outputStream.newLine();
            outputStream.write("TASK1 = (235,235,235)");
            outputStream.newLine();
            outputStream.write("TASK2 = (225,225,225)");
            outputStream.newLine();
            outputStream.write("SCROLLBAR = (0,0,0,150)");
            outputStream.newLine();
            outputStream.write("SELECTION = (100,100,100)");
            outputStream.newLine();
            outputStream.write("DATE = (30,30,30) & PLAIN & 12");
            outputStream.newLine();
            outputStream.write("ALERT = (50,50,50) & PLAIN & 14");
            outputStream.newLine();
            outputStream.write("LOCATION = (150,150,150) & BOLD & 14");
            outputStream.newLine();
            outputStream.write("NUMBER = (70,70,70) & PLAIN & 16");
            outputStream.newLine();
            outputStream.write("TITLE = (20,20,20) & BOLD & 16");
            outputStream.newLine();
            outputStream.write("INTERACT = (0,0,0) & PLAIN & 24");
            outputStream.newLine();
            outputStream.write("DEFAULT = (60,60,60) & PLAIN & 12");
            outputStream.newLine();
        } catch (IOException ex) {
            // not going to do anything
        }
    }

    private static void populateDarkAsMySoulTheme(Charset charset,
            OpenOption[] options) {
        Path defaultTheme = Paths.get("themes/", "DarkAsMySoul.bakaTheme");
        try (BufferedWriter outputStream = Files.newBufferedWriter(
                defaultTheme, charset, options)) {
            outputStream.write("TYPEFACE = Arial");
            outputStream.newLine();
            outputStream.write("INPUT = (100,100,100)");
            outputStream.newLine();
            outputStream.write("PANEL = (66,66,66)");
            outputStream.newLine();
            outputStream.write("TASK1 = (48,48,48)");
            outputStream.newLine();
            outputStream.write("TASK2 = (36,36,36)");
            outputStream.newLine();
            outputStream.write("SCROLLBAR = (0,0,0,100)");
            outputStream.newLine();
            outputStream.write("SELECTION = (0,0,0,150)");
            outputStream.newLine();
            outputStream.write("DATE = (253,184,19) & PLAIN & 12");
            outputStream.newLine();
            outputStream.write("ALERT = (250,250,250) & PLAIN & 12");
            outputStream.newLine();
            outputStream.write("LOCATION = (227,122,37) & BOLD & 14");
            outputStream.newLine();
            outputStream.write("NUMBER = (80,80,80) & PLAIN & 16");
            outputStream.newLine();
            outputStream.write("TITLE = (239,62,47) & BOLD & 18");
            outputStream.newLine();
            outputStream.write("INTERACT = (228,224,227) & PLAIN & 24");
            outputStream.newLine();
            outputStream.write("DEFAULT = (228,224,227) & PLAIN & 12");
            outputStream.newLine();
        } catch (IOException ex) {
            // not going to do anything
        }
    }

    //@author A0116538A
    /**
     * @return the typeface to be used in the GUI
     */
    public static String getTypeface() {
        return _typeface;
    }

    /**
     * @return the color of the input box to be used
     */
    public static Color getInputColor() {
        return _input;
    }

    /**
     * @return the color of the main background to be used
     */
    public static Color getPanelColor() {
        return _panel;
    }

    /**
     * @return the lighter color of the task box to be used
     */
    public static Color getTaskLightColor() {
        return _taskLight;
    }

    /**
     * @return the darker color of the task box to be used
     */
    public static Color getTaskDarkColor() {
        return _taskDark;
    }

    /**
     * @return the color of the scrollbar to be used
     */
    public static Color getScrollColor() {
        return _scroll;
    }

    /**
     * @return the text selection color to be used
     */
    public static Color getSelectionColor() {
        return _selection;
    }

    /**
     * @return the style of the date text to be used
     */
    public static BakaTheme getDateTheme() {
        return _date;
    }

    /**
     * @return the style of the alert text to be used
     */
    public static BakaTheme getAlertTheme() {
        return _alert;
    }

    /**
     * @return the style of the location text to be used
     */
    public static BakaTheme getLocationTheme() {
        return _location;
    }

    /**
     * @return the style of the task number to be used
     */
    public static BakaTheme getNumberTheme() {
        return _number;
    }

    /**
     * @return the style of the title of the task to be used
     */
    public static BakaTheme getTitleTheme() {
        return _title;
    }

    /**
     * @return the style of the text in the input box to be used
     */
    public static BakaTheme getInteractTheme() {
        return _interact;
    }

    /**
     * @return th style of any other text in the program to be used
     */
    public static BakaTheme getDefaultTheme() {
        return _default;
    }

    /**
     * reads the theme from a file
     *
     * @param filePath
     *        the path to the file to be used
     */
    private static void readFile(Path filePath) {
        try (BufferedReader inputStream = Files.newBufferedReader(filePath,
                                          Charset.forName("UTF-8"))) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                setThemes(line);
            }
        } catch (IOException ex) {
            // TODO log printStackTrace()
        }
    }

    /**
     * sets the new theme from the theme file, if information is missing,
     * that information is set to the last one used
     *
     * @param line
     *        is each line of the theme file
     */
    private static void setThemes(String line) {
        try {
            String[] thisTheme = line.split("=");
            if (thisTheme[1] == null) {
                return;
            }
            thisTheme[0] = thisTheme[0].trim();
            thisTheme[1] = thisTheme[1].trim();
            switch (thisTheme[0].toUpperCase()) {
                case "TYPEFACE" :
                    setTypeface(thisTheme[1]);
                    break;
                case "INPUT" :
                    setInput(thisTheme[1]);
                    break;
                case "PANEL" :
                    setPanel(thisTheme[1]);
                    break;
                case  "TASK1" :
                    setTaskLight(thisTheme[1]);
                    break;
                case "TASK2" :
                    setTaskDark(thisTheme[1]);
                    break;
                case "SCROLLBAR" :
                    setScroll(thisTheme[1]);
                    break;
                case "SELECTION" :
                    setSelection(thisTheme[1]);
                    break;
                case "DATE" :
                    setDate(thisTheme[1]);
                    break;
                case "ALERT" :
                    setAlert(thisTheme[1]);
                    break;
                case "LOCATION" :
                    setLocation(thisTheme[1]);
                    break;
                case "NUMBER" :
                    setNumber(thisTheme[1]);
                    break;
                case "TITLE" :
                    setTitle(thisTheme[1]);
                    break;
                case "INTERACT" :
                    setInteract(thisTheme[1]);
                    break;
                case "DEFAULT" :
                    setDefault(thisTheme[1]);
                    break;
                default :
                    break;
            }
        } catch (NullPointerException e) {
        }
    }

    private static void setTypeface(String typeface) {
        _typeface = typeface;
    }

    private static void setInput(String input) {
        _input = setToOriginalIfNull(_input, readColor(input));
    }

    private static void setPanel(String panel) {
        _panel = setToOriginalIfNull(_panel, readColor(panel));
    }

    private static void setTaskLight(String taskLight) {
        _taskLight = setToOriginalIfNull(_taskLight, readColor(taskLight));
    }

    private static void setTaskDark(String taskDark) {
        _taskDark = setToOriginalIfNull(_taskDark, readColor(taskDark));
    }

    private static void setScroll(String scroll) {
        _scroll = setToOriginalIfNull(_scroll, readColor(scroll));
    }

    private static void setSelection(String selection) {
        _selection = setToOriginalIfNull(_selection, readColor(selection));
    }

    private static void setDate(String date) {
        _date = setToOriginalIfNull(_date, readTheme(date));
    }

    private static void setAlert(String alert) {
        _alert = setToOriginalIfNull(_alert, readTheme(alert));
    }

    private static void setLocation(String location) {
        _location = setToOriginalIfNull(_location, readTheme(location));
    }

    private static void setNumber(String number) {
        _number = setToOriginalIfNull(_number, readTheme(number));
    }

    private static void setTitle(String title) {
        _title = setToOriginalIfNull(_title, readTheme(title));
    }

    private static void setInteract(String interact) {
        _interact = setToOriginalIfNull(_interact, readTheme(interact));
    }

    private static void setDefault(String defaultText) {
        _default = setToOriginalIfNull(_default, readTheme(defaultText));
    }

    /**
     * reads a string which specifies a BakaTheme
     *
     * @param theme
     *        is the BakaTheme in the form of a string
     * @return the BakaTheme from the string
     */
    private static BakaTheme readTheme(String theme) {
        try {
            String[] bakaTheme = theme.split("&");
            if (bakaTheme.length == 3) {
                return stringToTheme(bakaTheme[0], bakaTheme[1], bakaTheme[2]);
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * reads a string which specifies a color
     *
     * @param color
     *        is the color in the form of a string
     * @return the color from the string
     */
    private static Color readColor(String color) {
        try {
            color = trimString(color);
            String[] rgba = color.split(",");
            if (rgba.length == 3) {
                return stringToColor(rgba[0], rgba[1], rgba[2]);
            } else if (rgba.length == 4) {
                return stringToColor(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * reads a string which specifies a font type
     *
     * @param fontType
     *        is the font type in the form of a string
     * @return the font type from the string
     */
    private static Integer readFontType(String fontType) {
        switch (fontType.trim()) {
            case "PLAIN" :
                return Font.PLAIN;
            case "BOLD" :
                return Font.BOLD;
            case "ITALIC" :
                return Font.ITALIC;
            default :
                return null;
        }
    }

    /**
     * reads a string which specifies a font size
     *
     * @param fontSize
     *        is the size of the font in the form of a string
     * @return the font size from the string
     */
    private static Integer readFontSize(String fontSize) {
        try {
            return valueOf(fontSize.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * decide whether or not to set the theme of the element to what was used
     * previously based on null values detected
     *
     * @param original
     *        the initial BakaTheme
     * @param newTheme
     *        the new BakaTheme
     * @return the BakaTheme without null values
     */
    private static BakaTheme setToOriginalIfNull(BakaTheme original,
                                                 BakaTheme newTheme) {
        if (newTheme == null) {
            return original;
        }
        newTheme.setColor(setToOriginalIfNull(original.getColor(),
                                               newTheme.getColor()));
        newTheme.setFontSize(setToOriginalIfNull(original.getFontSize(),
                                                  newTheme.getFontSize()));
        newTheme.setFontType(setToOriginalIfNull(original.getFontType(),
                                                  newTheme.getFontType()));

        return newTheme;
    }

    /**
     * decide whether or not to set the color of the element to what was used
     * previously based on null values detected
     *
     * @param original
     *        the initial Color
     * @param newColor
     *        the new Color
     * @return the Color without null values
     */
    private static Color setToOriginalIfNull(Color original, Color newColor) {
        if (newColor == null) {
            return original;
        }
        return newColor;
    }

    /**
     * decide whether or not to set the integer of the element to what was used
     * previously based on null values detected
     *
     * @param original
     *        the initial int
     * @param newInt
     *        the new integer
     * @return the int without null values
     */
    private static int setToOriginalIfNull(int original, Integer newInt) {
        if (newInt == null) {
            return original;
        }
        return newInt;
    }

    private static BakaTheme stringToTheme(String color, String fontType,
                                           String fontSize) {
        return new BakaTheme(readColor(color), readFontType(fontType),
                             readFontSize(fontSize));
    }

    /**
     * Trim the first and last character in a string, in our case, '(' and ')'
     *
     * @param string
     *        the string we are trimming the characters
     * @return the string after trimming
     */
    private static String trimString(String string) {
        string = string.trim();
        return string.substring(1, string.length() - 1);
    }

    /**
     * takes a Color in the form of a string and change it to an object, returning
     * null if the color is invalid
     *
     * @param red
     *        red component of Color
     * @param green
     *        green component of Color
     * @param blue
     *        blue component of Color
     * @return the new color or null if it is an invalid color
     */
    private static Color stringToColor(String red, String green, String blue) {
        return stringToColor(red, green, blue, "255");
    }

    /**
     * takes a Color in the form of a string and change it to an object, returning
     * null if the color is invalid
     *
     * @param red
     *        red component of Color
     * @param green
     *        green component of Color
     * @param blue
     *        blue component of Color
     * @param alphs
     *        alpha component of Color
     * @return the new color or null if it is an invalid color
     */
    private static Color stringToColor(String red, String green, String blue,
                                       String alpha) {
        try {
            return new Color(valueOf(red), valueOf(green), valueOf(blue),
                             valueOf(alpha));
        } catch (NumberFormatException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
