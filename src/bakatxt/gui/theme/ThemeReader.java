//@author A0116538A

package bakatxt.gui.theme;

import static java.lang.Integer.valueOf;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("boxing")
public class ThemeReader {

    private static String _typeface = "Helvetica Neue";

    private static Color _input = new Color(100, 100, 100);
    private static Color _panel = new Color(66, 66, 66);
    private static Color _taskLight = new Color(48, 48, 48);
    private static Color _taskDark = new Color(36, 36, 36);
    private static Color _scroll = new Color(0, 0, 0, 100);

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
    private static BakaTheme _default = new BakaTheme(new Color(228, 224, 227),
                                                      Font.PLAIN, 12);

    public ThemeReader() {
        //readFile("DarkAsMySoul");
    }

    public static String getTypeface() {
        return _typeface;
    }

    public static Color getInputColor() {
        return _input;
    }

    public static Color getPanelColor() {
        return _panel;
    }

    public static Color getTaskLightColor() {
        return _taskLight;
    }

    public static Color getTaskDarkColor() {
        return _taskDark;
    }

    public static Color getScrollColor() {
        return _scroll;
    }

    public static BakaTheme getDateTheme() {
        return _date;
    }

    public static BakaTheme getAlertTheme() {
        return _alert;
    }

    public static BakaTheme getLocationTheme() {
        return _location;
    }

    public static BakaTheme getNumberTheme() {
        return _number;
    }

    public static BakaTheme getTitleTheme() {
        return _title;
    }

    public static BakaTheme getDefaultTheme() {
        return _default;
    }

    private static void readFile(String file) {
        Path filePath = Paths.get(file);
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
            ex.printStackTrace();
        }
    }

    private static void setThemes(String line) {
        try {
            String[] thisTheme = line.split("=");
            if (thisTheme[1] == null) {
                return;
            }
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
                case "TITLE" :
                    setDate(thisTheme[1]);
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

    private static void setDefault(String defaultText) {
        _default = setToOriginalIfNull(_default, readTheme(defaultText));
    }

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

    private static Color readColor(String color) {
        try {
            color = trimBrackets(color);
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

    private static Integer readFontType(String fontType) {
        switch (fontType) {
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

    private static Integer readFontSize(String fontSize) {
        try {
            return valueOf(fontSize);
        } catch (NumberFormatException e) {
            return null;
        }
    }

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

    private static Color setToOriginalIfNull(Color original, Color newColor) {
        if (newColor == null) {
            return original;
        }
        return original;
    }

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

    private static String trimBrackets(String color) {
        return color.substring(1, color.length() - 2);
    }

    private static Color stringToColor(String red, String green, String blue) {
        try {
            return new Color(valueOf(red), valueOf(green), valueOf(blue));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Color stringToColor(String red, String green, String blue,
                                       String alpha) {
        try {
            return new Color(valueOf(red), valueOf(green), valueOf(blue),
                             valueOf(alpha));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
