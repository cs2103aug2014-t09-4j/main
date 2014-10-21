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

import bakatxt.log.BakaLogger;

public class Database implements DatabaseInterface {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String SPACE = " ";

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
        try (BufferedReader inputStream = Files.newBufferedReader(_userFile,
                CHARSET_DEFAULT)) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                } else if (line.contains(TAG_DONE) && _removeDone) {
                    Task doneTask = new Task(line);
                    doneTask.setDeleted(true);
                    addTaskToMap(doneTask);
                } else if (line.contains(TAG_TITLE)) {
                    Task task = new Task(line);
                    addTaskToMap(task);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            LOGGER.severe("bakaMap update failed");
        }
    }

    private void addTaskToMap(Task task) {
        LOGGER.fine("add to bakaMap");
        String key = task.getKey();

        if (!_bakaMap.containsKey(key)) {
            _bakaMap.put(key, new LinkedList<Task>());
        }
        LinkedList<Task> target = _bakaMap.get(key);
        target.add(task);
    }

    @Override
    public String setFile(String fileName) {
        setEnvironment(fileName);
        String output = String.format(MESSAGE_FILE_CHANGE, getFileName());
        return output;
    }

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

    @Override
    public boolean add(Task task) {
        LOGGER.info("add task initialized");
        Task toAdd = new Task(task);
        toAdd.setDeleted(false);
        if (isExisting(toAdd)) {
            return false;
        }
        addTaskToMap(toAdd);
        return dirtyWrite(toAdd.toString());
    }

    private boolean isExisting(Task task) {
        String key = task.getKey();
        LinkedList<Task> target = _bakaMap.get(key);
        if (target == null || !target.contains(task)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Task task) {
        LOGGER.info("delete task initialized");
        String key = task.getKey();
        LinkedList<Task> target = _bakaMap.get(key);
        if (target == null) {
            return false;
        }
        boolean isRemoved = target.remove(task);
        if (isRemoved) {
            task.setDeleted(true);
            addTaskToMap(task);
            dirtyWrite(task.toString());
        } else {
            return isRemoved;
        }
        return updateFile();
    }

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
        writeFileComments();
        return writeLinesToFile();
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
            tempFile = Files.createTempFile("bakatxt", "old");
            Files.copy(_userFile, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOGGER.warning("Temp creation failed");
        }
        LOGGER.info("Temp creation completed");
    }

    @Override
    public boolean setDone(Task task, boolean isDone) {
        LOGGER.info("done status change initialized");
        delete(task);
        task.setDone(isDone);
        return add(task);
    }

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
            result = _bakaMap.get(key);
        }
        return result;
    }

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
        for (String key : _sortedKeys) {
            if (!key.contains(TAG_DELETED)) {
                for (Task task : _bakaMap.get(key)) {
                    task.setDeleted(true);
                }
            }
        }
        updateFile();
        updateMemory();
    }

}
