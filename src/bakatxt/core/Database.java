//@author A0116320Y
package bakatxt.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

import bakatxt.international.BakaTongue;
import bakatxt.log.BakaLogger;

public class Database implements DatabaseInterface {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String SPACE = " ";

    private static final String STRING_TODAY = "today";
    private static final String STRING_DAY = "day";

    private static final String MESSAGE_FILE_CHANGE = "File changed. Current filename is \"%1$s\".";
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

    private static final String FILE_LOCALE = TAG_OPEN + "LOCALE" + TAG_CLOSE;

    private static final String DEFAULT_LOCALE = "en_US";

    private static final String TAG_DELETED = "9999";
    private static final String TAG_DONE = "5000";
    private static final String TAG_FLOATING = "0000";

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
    private boolean _removeDone;
    private boolean _removeDeleted;
    private String _localeString;
    private BakaParser _parser;

    private Database(String fileName) {
        assert (_database == null);
        try {
            BakaLogger.setup();
        } catch (SecurityException | IOException ex) {
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
            _database = new Database("mytestfile.txt");
        }
        return _database;
    }

    private void setEnvironment(String fileName) {
        initializeFilePath(fileName);
        initializeOutputStream();
        initializeVariables();
    }

    private void initializeVariables() {
        _parser = new BakaParser();
        _localeString = DEFAULT_LOCALE;
        updateMemory();
        _removeDone = false;
        _removeDeleted = true;
    }

    private void initializeFilePath(String fileName) {
        _userFile = Paths.get(fileName);
    }

    private void initializeOutputStream() {
        try {
            _outputStream = Files.newBufferedWriter(_userFile, CHARSET_DEFAULT,
                    OPEN_OPTIONS);
        } catch (IOException ex) {
            LOGGER.severe("BufferedWriter failed");
        }
    }

    private void updateMemory() {
        LOGGER.info("bakaMap update initialized");
        _bakaMap = new HashMap<String, LinkedList<Task>>();
        String today = _parser.getDate(STRING_TODAY);
        try (BufferedReader inputStream = Files.newBufferedReader(_userFile,
                CHARSET_DEFAULT)) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                } else if (line.contains(FILE_LOCALE)) {
                    updateLanguage(line);
                } else if (line.contains(TAG_DONE) && _removeDone) {
                    deleteDoneTask(line);
                } else if (line.contains(TAG_TITLE)) {
                    Task task = new Task(line);
                    setOverdueToFloating(today, task);
                    addTaskToMap(task);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            LOGGER.severe("bakaMap update failed");
        }
    }

    @SuppressWarnings("static-method")
    private void setOverdueToFloating(String today, Task task) {
        String taskDate = task.getDate();
        if (!task.isDone() && !taskDate.equals("null")) {
            if (today.compareTo(taskDate) > 0) {
                task.setFloating(true);
            }
        }
    }

    private void deleteDoneTask(String line) {
        Task doneTask = new Task(line);
        doneTask.setDeleted(true);
        addTaskToMap(doneTask);
    }

    private void updateLanguage(String line) {
        _localeString = line.substring(line.indexOf(TAG_CLOSE) + 1);
        String[] localeArgs = _localeString.split("_");
        BakaTongue.setLanguage(localeArgs[0], localeArgs[1]);
    }

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

    @Override
    public String setFile(String fileName) {
        setEnvironment(fileName);
        String output = String.format(MESSAGE_FILE_CHANGE, getFileName());
        return output;
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

    private void sort() {
        _sortedKeys = new TreeSet<String>(_bakaMap.keySet());
        for (Map.Entry<String, LinkedList<Task>> entry : _bakaMap.entrySet()) {
            LinkedList<Task> today = entry.getValue();
            Collections.sort(today);
        }
    }

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

        updateFile();
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

        updateFile();
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        _database = null;
        BakaLogger.teardown();
    }

    private boolean updateFile() {
        LOGGER.info("update file initialized");
        // tempCreation();
        resetFile();
        writeLocale();
        writeFileComments();
        return writeLinesToFile();
    }

    private void writeLocale() {
        try {
            _outputStream.write(FILE_LOCALE + SPACE + _localeString.trim());
            _outputStream.newLine();
        } catch (IOException ex) {
            LOGGER.severe("unable to write to file!");
        }
    }

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
        } catch (IOException ex) {
            LOGGER.severe("unable to write to file!");
        }
    }

    private boolean writeLinesToFile() {
        LOGGER.info("write to file initialized");
        try {
            sort();
            for (String key : _sortedKeys) {
                if (_removeDone && key.contains(TAG_DONE)) {
                    continue;
                } else if (_removeDeleted && key.contains(TAG_DELETED)) {
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
        } catch (IOException ex) {
            LOGGER.severe("unable to write to file!");
            return false;
        }
    }

    private void resetFile() {
        LOGGER.info("reset file initialized");
        try {
            Files.write(_userFile, EMPTY_BYTE);
        } catch (IOException ex) {
            LOGGER.severe("file reset failed");
        }
    }

    private void tempCreation() {
        // copy userFile into tempFile
        Path tempFile;
        try {
            tempFile = Files.createTempFile(_userFile.toAbsolutePath()
                    .getParent(), "Baka", ".archive.txt");
            updateFile();
            Files.copy(_userFile, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOGGER.warning("Temp creation failed");
        }
        LOGGER.info("Temp creation completed");
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
     * followed by tasks with dates in chronological order.
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
            LinkedList<Task> today = _bakaMap.get(key);
            for (Task task : today) {
                String taskTitle = task.getTitle().toLowerCase();
                if (taskTitle.contains(title.toLowerCase())) {
                    result.add(task);
                }
            }
        }
        return result;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the undone non-deleted
     * tasks in the memory with the specified date.
     * 
     * The list is sorted in chronological order.
     * 
     * @param key
     *            <code>String</code> containing the date
     * 
     * @return <code>LinkedList</code> containing all the undone tasks in
     *         specified date or floating tasks if specified date is
     *         <code>null</code>
     */
    @Override
    public LinkedList<Task> getTasksWithDate(String key) {
        LinkedList<Task> result = new LinkedList<Task>();
        if (key == null) {
            for (Map.Entry<String, LinkedList<Task>> entry : _bakaMap
                    .entrySet()) {
                if (entry.getKey().contains(TAG_FLOATING)) {
                    result.addAll(entry.getValue());
                }
            }
        } else if (_bakaMap.containsKey(key)) {
            result.addAll(_bakaMap.get(key));
        }
        return result;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the non-deleted tasks in
     * the memory.
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order.
     * 
     * @return <code>LinkedList</code> containing all the existing tasks
     */
    @Override
    public LinkedList<Task> getAllTasks() {
        LinkedList<Task> all = new LinkedList<Task>();
        sort();
        for (String key : _sortedKeys) {
            if (key.contains(TAG_DELETED)) {
                continue;
            }
            all.addAll(_bakaMap.get(key));
        }
        return all;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the undone tasks in
     * the memory.
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order.
     * 
     * @return <code>LinkedList</code> containing all the undone tasks
     */
    @Override
    public LinkedList<Task> getAllUndoneTasks() {
        LinkedList<Task> undone = new LinkedList<Task>();
        sort();
        for (String key : _sortedKeys) {
            if (!key.contains(TAG_DONE) && !key.contains(TAG_DELETED)) {
                LinkedList<Task> today = _bakaMap.get(key);
                undone.addAll(today);
            }
        }
        return undone;
    }

    /**
     * Returns a <code>LinkedList</code> containing all the undone tasks for the
     * next 7 days, including today.
     * 
     * The list is sorted with the floating tasks first in alphabetical order
     * followed by tasks with dates in chronological order.
     * 
     * @return <code>LinkedList</code> containing all the undone tasks in 7 days
     */
    @Override
    public LinkedList<Task> getWeekTasks() {
        LinkedList<Task> thisWeek = new LinkedList<Task>();

        String today = _parser.getDate(STRING_TODAY);
        LinkedList<Task> day = _bakaMap.get(today);
        if (day != null) {
            thisWeek.addAll(day);
        }

        for (int i = 1; i < 7; i++) {
            String date = _parser.getDate(i + SPACE + STRING_DAY);
            day = _bakaMap.get(date);
            if (day != null) {
                thisWeek.addAll(day);
            }
        }

        return thisWeek;
    }

    /**
     * Sets a specific task's floating status based on the specified boolean.
     * This is implemented by deleting the original task from the memory
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
    public boolean setFloating(Task task, boolean isFloating) {
        LOGGER.info("set floating status initialized");
        delete(task);
        task.setFloating(isFloating);
        add(task);
        return updateFile();
    }

    private boolean dirtyWrite(String line) {
        try {
            _outputStream.write(line);
            _outputStream.newLine();
            _outputStream.flush();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Removes tasks that are done from the memory and the storage file. This
     * method is not persistent. Subsequent tasks that are marked as done are
     * still kept in the storage file and memory until this method is called
     * again.
     */
    @Override
    public void removeDone() {
        LOGGER.info("delete done initialized");
        _removeDone = true;
        updateFile();
        updateMemory();
        _removeDone = false;
    }

    @Override
    public void clear() {
        tempCreation();
        resetFile();
        updateMemory();
    }

    /**
     * Writes the current locale used by <code>BakaTxt</code> into the storage
     * file to enable persistence
     * 
     * @param locale
     *            current locale used
     */
    @Override
    public void updateLocale(String locale) {
        LOGGER.info("locale write initialized");
        _localeString = locale.trim();
        dirtyWrite(FILE_LOCALE + SPACE + _localeString);
    }
}
