package bakatxt.international;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import bakatxt.core.Database;

public class BakaTongue {
    private static final String BUNDLE_NAME = "bakatxt.international.BakaLanguage";
    private static final String SPACE = " ";

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
            case "wah lao" :
            case "singlish" :
                currentLocale = new Locale("en", "SG");
                break;
            case "english" :
                currentLocale = new Locale("en", "US");
                break;
            case "中文" :
            case "chinese" :
                currentLocale = new Locale("zh", "CN");
                break;
            case "한국어" :
            case "korean" :
                currentLocale = new Locale("ko", "KR");
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

            if (key.contains("COMMAND")) {
                if (isAcceptable(input, international)
                        && isSameCommand(input, international)) {
                    input = input.replace(international, english);
                }
            } else if (key.contains("USER_PROMPT")) {
                continue;
            } else {
                input = input.replace(international, english);
            }
        }

        return input;
    }

    private static boolean isSameCommand(String input, String international) {
        int commandLength = international.length();
        return input.substring(0, commandLength).equals(international);
    }

    private static boolean isAcceptable(String input, String international) {
        return input.length() >= international.length();
    }
}
