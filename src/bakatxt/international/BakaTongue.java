//@author A0116320Y
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

    private static final String[] LANGUAGES = { "English", "中文", "한국어", "HINDI" };

    private static Locale currentLocale = new Locale("en", "US");
    private static ResourceBundle resBundle = ResourceBundle.getBundle(
            BUNDLE_NAME, currentLocale);

    /**
     * Retrieves the information from the specified resource bundle
     * 
     * @param key
     *            of the <code>String</code> to be retrieved
     * @return <code>String</code> from the resource bundle, or
     *         <code>!KEY!</code> if the key does not exists.
     */
    public static String getString(String key) {
        try {
            return resBundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Sets the resource bundle according to the language specified.
     * 
     * @param language
     *            <code>String</code> chosen by the user
     * 
     * @return <code>true</code> if the language specified exists and is
     *         applied, <code>false</code> when language does not exist and
     *         default English is applied.
     */
    public static boolean setLanguage(String language) {
        boolean isSuccessful = true;
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
            case "hindi" :
            case "4" :
                currentLocale = new Locale("hi", "IN");
                break;
            case "wah lao" :
            case "singlish" :
                currentLocale = new Locale("en", "SG");
                break;
            default :
                currentLocale = new Locale("en", "US");
                isSuccessful = false;
        }
        resBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        Database.getInstance().updateLocale(currentLocale.toString());
        return isSuccessful;
    }

    /**
     * Enable setting of language directly based on specifying locale and
     * region. Used to set language from preferences stored in the storage file.
     * 
     * @param lang
     *            <code>String</code> containing locale language code
     * @param region
     *            <code>String</code> containing locale region code
     */
    public static void setLanguage(String lang, String region) {
        currentLocale = new Locale(lang.trim(), region.trim());
        resBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Enables pseudo translation of user commands by direct translation of user
     * command input from other languages to English. Only replaces identifying
     * elements such as commands and delimiters.
     * 
     * @param input
     *            <code>String</code> containing the user command
     * @return <code>String</code> containing the original input with replaced
     *         <code>String</code> of identifiers.
     */
    public static String toEnglish(String input) {
        Set<String> keys = resBundle.keySet();

        Locale target = new Locale("en", "US");

        if (target.equals(currentLocale)) {
            return input;
        }

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

    /**
     * Checks if the command identified is in English.
     * 
     * @param input
     *            from the user containing the Command <code>String</code>
     * @param international
     *            Command from the English resource bundle
     * @param commandLength
     *            <code>length</code> of the Command <code>String</code>
     * 
     * @return <code>true</code> if the Commands are equal, <code>false</code>
     *         otherwise.
     */
    private static boolean isSameCommand(String input, String international,
            int commandLength) {
        return input.substring(0, commandLength).toUpperCase()
                .equals(international);
    }

    /**
     * Checks if the input is of sufficient length
     * 
     * @param input
     *            to be checked
     * @param commandLength
     *            length of the command to be checked against
     * 
     * @return <code>true</code> if acceptable, <code>false</code> otherwise
     */
    private static boolean isAcceptable(String input, int commandLength) {
        return input.length() >= commandLength;
    }

    /**
     * Packages the languages into a <code>LinkedList</code> to be displayed to
     * the user as choices.
     * 
     * @return <code>LinkedList</code> containing pseudo-Tasks with information
     *         about the languages.
     */
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
