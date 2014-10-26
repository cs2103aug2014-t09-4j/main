package bakatxt.international;

import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import bakatxt.core.Database;
import bakatxt.core.Task;

public class BakaTongue {
    private static final String BUNDLE_NAME = "bakatxt.international.BakaLanguage";
    private static final String SPACE = " ";
    private static final String HEADER = "LANGUAGE";

    private static final String[] LANGUAGES = { "English", "中文", "한국어" };

    private static Locale currentLocale = new Locale("en", "US");
    private static ResourceBundle resBundle = ResourceBundle.getBundle(
            BUNDLE_NAME, currentLocale);

    private BakaTongue() {
    }

    public static String getString(String key) {
        try {
            return resBundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static void setLanguage(String language) {
        switch (language.toLowerCase().trim()) {
            case "english" :
            case "1" :
                currentLocale = new Locale("en", "US");
                break;
            case "中文" :
            case "chinese" :
            case "2" :
                currentLocale = new Locale("zh", "CN");
                break;
            case "한국어" :
            case "korean" :
            case "3" :
                currentLocale = new Locale("ko", "KR");
                break;
            case "wah lao" :
            case "singlish" :
                currentLocale = new Locale("en", "SG");
                break;
            case "hodor" :
                currentLocale = new Locale("zz", "ZZ");
                break;
            default :
                currentLocale = new Locale("en", "US");
        }
        resBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        Database.getInstance().updateLocale(currentLocale.toString());
    }

    public static void setLanguage(String lang, String region) {
        currentLocale = new Locale(lang.trim(), region.trim());
        resBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    public static String toEnglish(String input) {
        Set<String> keys = resBundle.keySet();

        Locale target = new Locale("en", "US");
        ResourceBundle resTarget = ResourceBundle
                .getBundle(BUNDLE_NAME, target);
        for (String key : keys) {
            String international = resBundle.getString(key);
            String english = resTarget.getString(key) + SPACE;

            int commandLength = international.length();
            if (key.contains("COMMAND")) {
                if (isAcceptable(input, commandLength)
                        && isSameCommand(input, international, commandLength)) {
                    input = input.substring(0, commandLength).toUpperCase()
                            + input.substring(commandLength);

                    input = input.replace(international, english);
                }
            } else if (key.contains("USER_PROMPT") || key.contains("ALERT")) {
                continue;
            } else {
                input = input.replace(international, english);
            }
        }
        return input;
    }

    private static boolean isSameCommand(String input, String international,
            int commandLength) {
        return input.substring(0, commandLength).toUpperCase()
                .equals(international);
    }

    private static boolean isAcceptable(String input, int commandLength) {
        return input.length() >= commandLength;
    }

    public static LinkedList<Task> languageChoices() {
        LinkedList<Task> choices = new LinkedList<Task>();
        for (int i=0; i<LANGUAGES.length; i++) {
            Task language = new Task(LANGUAGES[i]);
            language.setDate(HEADER);
            choices.add(language);
        }
        return choices;
    }
}
