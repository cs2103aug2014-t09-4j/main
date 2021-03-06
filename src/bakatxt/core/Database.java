//@author A0116320Y
package bakatxt.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import bakatxt.gui.look.ThemeReader;
import bakatxt.international.BakaTongue;
import bakatxt.log.BakaLogger;

public class Database implements DatabaseInterface {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String SPACE = " ";

    private static final String STRING_TODAY = "today";
    private static final String STRING_DAY = "day";

    private static final String MESSAGE_OUTPUT_FILENAME = "Filename: %1$s"
            + LINE_SEPARATOR;

    private static final String TAG_OPEN = "[";
    private static final String TAG_CLOSE = "]";
    private static final String TAG_TITLE = TAG_OPEN + "TITLE" + TAG_CLOSE;

    private static final String FILE_COMMENT = TAG_OPEN + "-" + TAG_CLOSE
            + SPACE;
    private static final String FILE_HEADER = FILE_COMMENT
            + "BakaTXT Human-Readable Human-Fixable Database";
    private static final String FILE_VERSION = FILE_COMMENT + "alpha v0.0";
    private static final String FILE_WARNING = FILE_COMMENT
            + "Each task has to be in the same line";

    private static final String LOCALE_FILE = TAG_OPEN + "LOCALE" + TAG_CLOSE;
    private static final String LOCALE_DEFAULT = "en_US";

    private static final String TAG_DELETED = "9999";
    private static final String TAG_DONE = "5000";
    private static final String TAG_FLOATING = "0000";

    private static final String VIEW_DONE = TAG_OPEN + "VIEW DONE" + TAG_CLOSE;
    private static final String THEME = TAG_OPEN + "THEME" + TAG_CLOSE;

    private static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
    private static final byte[] EMPTY_BYTE = {};
    private static final OpenOption[] OPEN_OPTIONS = {
            StandardOpenOption.CREATE, StandardOpenOption.APPEND };

    private static final Logger LOGGER = Logger.getLogger(Database.class
            .getName());

    private static Database _database = null;

    private Path _userFile;
    private BufferedWriter _outputStream;
    private HashMap<String, LinkedList<Task>> _bakaMap;
    private TreeSet<String> _sortedKeys;
    private boolean _isRemoveDeleted;
    private String _localeString;
    private BakaParser _parser;
    private boolean _isViewDone;
    private String _theme;

    private Database(String fileName) {
        assert (_database == null);
        try {
            BakaLogger.setup();
        } catch (Exception ex) {
            BakaLogger.setup(true);
        } finally {
            LOGGER.setLevel(Level.INFO);
            setEnvironment(fileName);
            LOGGER.info("Database setup completed");
        }
    }

    /**
     * Returns a single shared instance of Database to enable reading and
     * writing to the storage text file.
     * 
     * @return a singleton instance of Database
     */
    public static Database getInstance() {
        if (_database == null) {
            _database = new Database("BakaStorage.txt");
        }
        return _database;
    }

    /**
     * Set up the output buffer and initialize instance variables.
     * 
     * @param fileName
     *            storage file containing the tasks and settings
     */
    private void setEnvironment(String fileName) {
        initializeFilePath(fileName);
        initializeOutputStream();
        initializeVariables();
    }

    /**
     * Initialize all the variables of the <code>Database</code> singleton
     */
    private void initializeVariables() {
        _parser = new BakaParser();
        _localeString = LOCALE_DEFAULT;
        _isRemoveDeleted = true;
        _isViewDone = false;
        _theme = "themes/DarkAsMySoul.bakaTheme";
        updateMemory();
    }

    /**
     * Convert the filename of the storage to a <code>Path</code> and set it to
     * the instance attribute.
     * 
     * @param fileName
     *            storage file containing the tasks and settings
     */
    private void initializeFilePath(String fileName) {
        _userFile = Paths.get(fileName);
    }

    /**
     * Setup of the output file writer to enable writing of information to the
     * storage file.
     */
    private void initializeOutputStream() {
        try {
            _outputStream = Files.newBufferedWriter(_userFile, CHARSET_DEFAULT,
                    OPEN_OPTIONS);
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
        }
    }

    /**
     * This will check all possible writable potential storage files and set it
     * as the default writable file. If there is none found, the last file in
     * this search sequence will be duplicated into a new writable file.
     * 
     * This method is iterative and will end when it reaches a count of
     * Integer.MAX_VALUE. This is highly not possible and illogical, unless
     * there is a specific blocking set in place.
     */
    private void iterativeRunAndSet() {
        String prefix = "BakaStorage";
        String suffix = ".txt";
        for (int i = 0; i <= Integer.MAX_VALUE; i++) {
            String potentialFile = prefix + i + suffix;
            Path potential = Paths.get(potentialFile);
            if (potential.toFile().exists()) {
                _userFile = potential;
                if (potential.toFile().canWrite()) {
                    changeFile(potential);
                    break;
                }
            } else {
                changeFile(potential);
                break;
            }
        }
    }

    /**
     * Duplicate the file that can be read into a possible potential target
     * file. Write stream is then pointed to this new file.
     * 
     * @param potential
     *            Path containing a potential file location
     */
    private void changeFile(Path potential) {
        Path temp = tempCreation();
        try {
            Files.copy(temp, potential,
                    StandardCopyOption.REPLACE_EXISTING);
            _userFile = potential;
            _outputStream = Files.newBufferedWriter(_userFile,
                    CHARSET_DEFAULT, OPEN_OPTIONS);
        } catch (IOException ex) {
            LOGGER.severe(stackTraceString(ex));
        }
    }

    /**
     * Packages the <code>exception</code> stack trace into a
     * <code>String</code> for logging.
     * 
     * @param thrown
     *            <code>exception</code> thrown
     * 
     * @return <code>String</code> containing the stack trace information
     */
    private static String stackTraceString(Throwable thrown) {
        StringWriter converted = new StringWriter();
        PrintWriter printWriter = new PrintWriter(converted);
        thrown.printStackTrace(printWriter);
        return converted.toString();
    }

    /**
     * Reads the storage file through a buffered input stream into a
     * <code>HashMap</code> containing <code>LinkedList</code> of tasks to their
     * relevant keys. User settings such as language and themes are also
     * applied.
     */
    private void updateMemory() {
        LOGGER.info("bakaMap update initialized");
        _bakaMap = new HashMap<String, LinkedList<Task>>();
        try (BufferedReader inputStream = Files.newBufferedReader(_userFile,
                CHARSET_DEFAULT)) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                } else if (line.contains(VIEW_DONE)) {
                    if (line.contains("true")) {
                        _isViewDone = true;
                    } else {
                        _isViewDone = false;
                    }
                } else if (line.contains(LOCALE_FILE)) {
                    settingLanguage(line);
                } else if (line.contains(THEME)) {
                    settingTheme(line);
                } else if (line.contains(TAG_TITLE)) {
                    Task task = new Task(line);
                    if (task.isDeleted()) {
                        removeEquivalentTask(task);
                    } else {
                        addTaskToMap(task);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
        }
    }

    /**
     * Removes any task that is actually deleted but not updated in the app
     * database properly due to improper exit.
     * 
     * @param task
     */
    private void removeEquivalentTask(Task task) {
        task.setDeleted(false);
        LinkedList<Task> target = _bakaMap.get(task.getKey());
        if (target != null) {
            target.remove(task);
        }
    }

    /**
     * Set the language of <code>BakaTxt</code> based on the settings inside the
     * storage file.
     * 
     * @param line
     *            containing the language preference in the storage file
     */
    private void settingLanguage(String line) {
        _localeString = line.substring(line.indexOf(TAG_CLOSE) + 1);
        String[] localeArgs = _localeString.split("_");
        try {
        BakaTongue.setLanguage(localeArgs[0], localeArgs[1]);
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
        }
    }

    /**
     * Set the theme of <code>BakaTxt</code> based on the settings inside the
     * storage file.
     * 
     * @param line
     *            containing the theme preference in the storage file
     */
    private void settingTheme(String line) {
        _theme = line.substring(line.indexOf(TAG_CLOSE) + 1).trim();
        Path themePath = Paths.get(_theme);
        ThemeReader.setTheme(themePath);
    }

    /**
     * Adds a <code>Task</code> that is changed or new into the
     * <code>HashMap</code> in memory. Repeated <code>Tasks</code> will not be
     * added.
     * 
     * @param task
     *            that is to be added or has been changed
     * @return <code>true</code> if task is added, <code>false</code> otherwise.
     */
    private boolean addTaskToMap(Task task) {
        LOGGER.fine("add to bakaMap");
        String key = task.getKey();
        if (isExisting(task)) {
            return false;
        }
        if (!_bakaMap.containsKey(key)) {
            _bakaMap.put(key, new LinkedList<Task>());
        }
        LinkedList<Task> target = _bakaMap.get(key);
        return target.add(task);
    }

    /**
     * Returns the relative file name of the storage file where IO is done.
     * 
     * @return <code>String</code> of the text file
     */
    @Override
    public String getFileName() {
        return _userFile.toString();
    }

    /**
     * Updates the instance attribute of a <code>TreeSet</code> containing the
     * sorted keys of the <code>HashMap</code>. <code>Task</code> in
     * <code>LinkedList</code> in the <code>HashMap</code> are also sorted in a
     * chronological order as determined by <code>compareTo</code> of the
     * <code>Task</code>.
     */
    private void sort() {
        _sortedKeys = new TreeSet<String>(_bakaMap.keySet());
        for (Map.Entry<String, LinkedList<Task>> entry : _bakaMap.entrySet()) {
            LinkedList<Task> today = entry.getValue();
            Collections.sort(today);
        }
    }

    /**
     * Output the storage filename as a formatted <code>String</code>
     * 
     * @return <code>String</code> containing the filename of the storage file.
     */
    @Override
    public String toString() {
        String lineOne = String.format(MESSAGE_OUTPUT_FILENAME, getFileName());
        return lineOne;
    }

    /**
     * Returns a boolean to specify if a task is added.
     * Adds a non-deleted unique task to the HashMap in the memory and writes a
     * copy of the task into the storage file.
     * 
     * @param task
     *            a specific task to add
     * @return <code>true</code> if task is written to the storage file
     */
    @Override
    public boolean add(Task task) {
        LOGGER.info("add task initialized");
        if (task.getTitle().isEmpty()) {
            return false;
        }
        Task toAdd = new Task(task);
        toAdd.setDeleted(false);
        boolean isAdded = addTaskToMap(toAdd);
        if (isAdded) {
            dirtyWrite(toAdd.toString());
        }
        return isAdded;
    }

    /**
     * Checks if the task already exists in the <code>HashMap</code> by looking
     * into the associated <code>key</code> value.
     * 
     * @param task
     *            to be checked;
     * @return <code>true</code> if the task already exist, else
     *         <code>false</code>
     */
    private boolean isExisting(Task task) {
        String key = task.getKey();
        LinkedList<Task> target = _bakaMap.get(key);
        if (target == null || !target.contains(task)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a boolean to specify if a task is deleted.
     * Deletes a task from the HashMap in the memory and refreshes the storage
     * file with the existing copy in the memory.
     * 
     * @param task
     *            a specific task to delete
     * @return <code>true</code> if task is deleted from the memory
     */
    @Override
    public boolean delete(Task task) {
        LOGGER.info("delete task initialized");

        // updateFile();
        updateMemory();

        String key = task.getKey();
        LinkedList<Task> target = _bakaMap.get(key);
        if (target == null) {
            return false;
        }

        boolean isRemoved = target.remove(task);
        if (isRemoved) {
            Task toDelete = new Task(task);
            toDelete.setDeleted(true);
            addTaskToMap(toDelete);
            dirtyWrite(toDelete.toString());
        }

        // updateFile();
        return isRemoved;
    }

    /**
     * Writes the content of the HashMap into the storage file. Storage file is
     * created if it does not exists. Closes BufferedWriter stream and set the
     * Database instance to null. Activity logging is also stopped.
     */
    @Override
    public void close() {
        LOGGER.info("end Database initialized");
        updateFile();
        try {
            _outputStream.close();
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
        }
        _database = null;
        BakaLogger.teardown();
    }

    /**
     * Writes the preset comments, preferences and content of the
     * <code>HashMap</code> into the storage file that is empty.
     * 
     * @return <code>true</code> if the tasks are written to the storage file,
     *         <code>false</code> otherwise.
     */
    private boolean updateFile() {
        LOGGER.info("update file initialized");
        // tempCreation();
        resetFile();
        writeFileComments();
        writeSettings();
        return writeLinesToFile();
    }

    /**
     * Writes the user preferences into the file based on the attributes stored
     * in the instance.
     */
    private void writeSettings() {
        try {
            _outputStream.write(LOCALE_FILE + SPACE + _localeString.trim());
            _outputStream.newLine();
            _outputStream.write(VIEW_DONE + SPACE + _isViewDone);
            _outputStream.newLine();
            _outputStream.write(THEME + SPACE + _theme.trim());
            _outputStream.newLine();
            _outputStream.newLine();
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
            updateFile();
        }
    }

    /**
     * Writes the preset storage file comments to the storage file.
     */
    private void writeFileComments() {
        try {
            _outputStream.write(FILE_HEADER);
            _outputStream.newLine();
            _outputStream.write(FILE_VERSION);
            _outputStream.newLine();
            _outputStream.write(FILE_WARNING);
            _outputStream.newLine();
            _outputStream.newLine();
            _outputStream.flush();
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
            updateFile();
        }
    }

    /**
     * Writes <code>Task</code> from the <code>HashMap</code> into the storage
     * file. Deleted <code>Task</code> are determined to be written based on the
     * <code>boolean _isRemovedDeleted</code>.
     * 
     * @return <code>true</code> if the content of the <code>HashMap</code> can
     *         be written to the storage file, <code>false</code> if an
     *         exception arises.
     */
    private boolean writeLinesToFile() {
        LOGGER.info("write to file initialized");
        try {
            sort();
            for (String key : _sortedKeys) {
                if (_isRemoveDeleted && key.contains(TAG_DELETED)) {
                    continue;
                }
                LinkedList<Task> listToWrite = _bakaMap.get(key);
                for (Task task : listToWrite) {
                    _outputStream.write(task.toString());
                    _outputStream.newLine();
                }
                _outputStream.newLine();
                _outputStream.flush();
            }
            return true;
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
            updateFile();
            return false;
        }
    }

    /**
     * Reset the file by overwriting the content with an empty <code>byte</code>
     */
    private void resetFile() {
        LOGGER.info("reset file initialized");
        try {
            Files.write(_userFile, EMPTY_BYTE);
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
        }
    }

    /**
     * Creates a temporary copy of the storage file in the same folder as the
     * storage file.
     * 
     * @return Path of the temp file created
     */
    private Path tempCreation() {
        // copy userFile into tempFile
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile(_userFile.toAbsolutePath()
                    .getParent(), "Baka", ".archive.txt");
            Files.copy(_userFile, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();
        } catch (Exception ex) {
            LOGGER.warning("Temp creation failed");
        }
        LOGGER.info("Temp creation completed");
        return tempFile;
    }

    /**
     * Sets a specific task's done status based on the specified boolean. This
     * is implemented by deleting the original copy of the task from the memory
     * and storage file, before adding an updated copy back to memory and
     * storage file.
     * 
     * @param task
     *            a task to change the status
     * @param isDone
     *            a boolean to specify the status of the task to be set
     * 
     * @return <code>true</code> if the updated task is written to the storage
     *         file
     */
    @Override
    public boolean setDone(Task task, boolean isDone) {
        LOGGER.info("done status change initialized");
        delete(task);
        task.setDone(isDone);
        return add(task);
    }

    /**
     * Returns a <code>LinkedList</code> containing all the non-deleted tasks in
     * the memory with titles containing the specified string.
     * 
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order. Listing of done
     * tasks is determined by the user preference as stated in
     * <code>_isViewDone</code>.
     * 
     * @param title
     *            full or partial <code>String</code> of a title
     * @return <code>LinkedList</code> containing all the existing tasks with
     *         the title containing the <code>String</code> input
     */
    @Override
    public LinkedList<Task> getTaskWithTitle(String title) {
        LinkedList<Task> result = new LinkedList<Task>();
        sort();
        for (String key : _sortedKeys) {
            if (key.contains(TAG_DELETED)) {
                continue;
            }
            if (_isViewDone == false && key.contains(TAG_DONE)) {
                continue;
            }

            LinkedList<Task> today = _bakaMap.get(key);
            for (Task task : today) {
                String taskTitle = task.getTitle().toLowerCase();
                if (taskTitle.contains(title.toLowerCase())) {
                    result.add(task);
                }
            }
        }

        Collections.sort(result);
        return result;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the non-deleted
     * tasks in the memory with the specified date.
     * 
     * The list is sorted in chronological order. Listing of done tasks is
     * determined by the user preference as stated in <code>_isViewDone</code>.
     * 
     * @param key
     *            <code>String</code> containing the date
     * 
     * @return <code>LinkedList</code> containing all the tasks in
     *         specified date or floating tasks if specified date is
     *         <code>null</code>
     */
    @Override
    public LinkedList<Task> getTasksWithDate(String key) {
        LinkedList<Task> result = new LinkedList<Task>();
        String date;
        if (key == null || key.equals("null")) {
            date = TAG_FLOATING + SPACE + "null";
        } else {
            date = key;
        }

        updateTasksList(result, date);
        Collections.sort(result);
        return result;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the tasks in
     * the memory.
     * 
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order. Listing of done
     * tasks is determined by the user preference as stated in
     * <code>_isViewDone</code>.
     * 
     * @return <code>LinkedList</code> containing all the tasks
     */
    @Override
    public LinkedList<Task> getAllTasks() {
        LinkedList<Task> tasks = new LinkedList<Task>();
        sort();
        for (String key : _sortedKeys) {
            if (!key.contains(TAG_DELETED)) {
                if (!_isViewDone && key.contains(TAG_DONE)) {
                    continue;
                }
                LinkedList<Task> target = _bakaMap.get(key);
                tasks.addAll(target);
            }
        }
        return tasks;
    }

    /**
     * Add all tasks in a <code>HashMap</code> key into the specified
     * <code>LinkedList</code>. Adds done tasks if required.
     * 
     * @param tasks
     *            the target <code>LinkedList</code>
     * @param key
     *            of the values in the <code>HashMap</code> to be added to the
     *            target <code>LinkedList</code>.
     */
    private void updateTasksList(LinkedList<Task> tasks, String key) {
        LinkedList<Task> today = _bakaMap.get(key);
        if (today != null && !today.isEmpty()) {
            tasks.addAll(today);
        }
        if (_isViewDone) {
            today = _bakaMap.get(TAG_DONE + SPACE + key);
            if (today != null && !today.isEmpty()) {
                tasks.addAll(today);
            }
        }
    }

    /**
     * Returns a <code>LinkedList</code> containing all the tasks for the
     * next 7 days, including today.
     * 
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order. Listing of done
     * tasks is determined by the user preference as stated in
     * <code>_isViewDone</code>.
     * 
     * @return <code>LinkedList</code> containing all the tasks in 7 days
     */
    @Override
    public LinkedList<Task> getWeekTasks() {
        LinkedList<Task> thisWeek = new LinkedList<Task>();
        LinkedList<String> dates = new LinkedList<String>();

        String[] intString = { "one", "two", "three", "four", "five", "six" };
        String day = _parser.getDate(STRING_TODAY);
        dates.add(day);
        for (int i = 0; i < 6; i++) {
            day = _parser.getDate(intString[i] + SPACE + STRING_DAY);
            dates.add(day);
        }

        for (String date : dates) {
            updateTasksList(thisWeek, date);
        }

        Collections.sort(thisWeek);
        return thisWeek;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the done tasks.
     * 
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order.
     * 
     * @return <code>LinkedList</code> containing all the done tasks.
     */
    @Override
    public LinkedList<Task> getDoneTasks() {
        LinkedList<Task> tasks = new LinkedList<Task>();
        sort();
        for (String key : _sortedKeys) {
            if (!key.contains(TAG_DELETED)) {
                if (key.contains(TAG_DONE)) {
                    LinkedList<Task> source = _bakaMap.get(key);
                    tasks.addAll(source);
                }
            }
        }

        Collections.sort(tasks);
        return tasks;
    }

    /**
     * Sets a specific task's floating status based on the specified boolean.
     * This is implemented by deleting the original task from the memory
     * and storage file, before adding an updated copy back to memory and
     * storage file.
     * 
     * @param task
     *            a task to change the status
     * @param isFloating
     *            a boolean to specify the status of the task to be set
     * 
     * @return <code>true</code> if the updated task is written to the storage
     *         file
     */
    @Override
    public boolean setFloating(Task task, boolean isFloating) {
        LOGGER.info("set floating status initialized");
        delete(task);
        task.setFloating(isFloating);
        add(task);
        return updateFile();
    }

    /**
     * Writes the line to the storage file without any form of post-processing.
     * This ensures that the information is added to the storage file once it is
     * received by <code>Database</code>.
     * 
     * @param line
     *            containing the relevant information to be written
     * @return <code>true</code> if the information can be written to the
     *         storage file, <code>false</code> if the storage file cannot be
     *         written to.
     */
    private boolean dirtyWrite(String line) {
        try {
            _outputStream.write(line);
            _outputStream.newLine();
            _outputStream.flush();
            return true;
        } catch (Exception ex) {
            LOGGER.severe(stackTraceString(ex));
            iterativeRunAndSet();
            dirtyWrite(line);
            return false;
        }
    }

    /**
     * Writes the current locale used by <code>BakaTxt</code> into the storage
     * file to enable persistence.
     * 
     * @param locale
     *            current locale used
     */
    @Override
    public void updateLocale(String locale) {
        LOGGER.info("locale write initialized");
        _localeString = locale.trim();
        dirtyWrite(LOCALE_FILE + SPACE + _localeString);
    }

    /**
     * Writes the current theme used by <code>BakaTxt</code> into the storage
     * file to enable persistence.
     * 
     * @param theme
     *            current theme used
     */
    @Override
    public void updateTheme(String theme) {
        LOGGER.info("theme write initialized");
        _theme = theme.trim();
        dirtyWrite(THEME + SPACE + _theme);
    }

    /**
     * Writes the current view option for done tasks into the storage file to
     * enable persistence.
     * 
     * @param isViewingDone
     *            is <code>true</code> to show done, <code>false</code>
     *            otherwise.
     */
    @Override
    public void updateDoneView(boolean isViewingDone) {
        LOGGER.info("view done write initialized");
        _isViewDone = isViewingDone;
        dirtyWrite(VIEW_DONE + SPACE + _isViewDone);
    }
}
