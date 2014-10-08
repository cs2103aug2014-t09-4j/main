package bakatxt.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
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

public class Database implements DatabaseInterface {

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final String SPACE = " ";

    private static final String MESSAGE_FILE_CHANGE = "File changed. Current filename is \"%1$s\".";
    private static final String MESSAGE_OUTPUT_FILENAME = "Filename: %1$s"
            + LINE_SEPARATOR;
    private static final String MESSAGE_OUTPUT_TOTAL_COUNT = "No. of tasks: %1$s"
            + LINE_SEPARATOR;
    private static final String MESSAGE_OUTPUT_DONE_COUNT = "No. of tasks completed: %1$s"
            + LINE_SEPARATOR;

    private static final String TAG_OPEN = "[";
    private static final String TAG_CLOSE = "]";
    private static final String TAG_TITLE = TAG_OPEN + "TITLE" + TAG_CLOSE;

    private static final String TASK_TOTAL = TAG_OPEN + "TOTAL COUNT"
            + TAG_CLOSE + SPACE;
    private static final String TASK_DONE = TAG_OPEN + "DONE COUNT" + TAG_CLOSE
            + SPACE;

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

    private static final int MAX_TASKS_TO_DISPLAY = 5;
    private static final int CONTENT_INDEX = 1;

    private static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
    private static final byte[] EMPTY_BYTE = {};
    private static final OpenOption[] OPEN_OPTIONS = {
            StandardOpenOption.CREATE, StandardOpenOption.APPEND };

    private static BigInteger taskCount;
    private static BigInteger taskDone;

    private Path _userFile;
    private String _previousTask;
    private BufferedWriter _outputStream;
    private HashMap<String, LinkedList<Task>> _bakaMap;
    private TreeSet<String> _sortedKeys;
    private boolean _removeDone;

    public Database(String fileName) {
        setEnvironment(fileName);
    }

    private void setEnvironment(String fileName) {
        initializeFilePath(fileName);
        initializeOutputStream();
        initializeVariables();
    }

    private void initializeVariables() {
        taskCount = taskDone = BigInteger.ZERO;
        updateMemory();
        _previousTask = null;
        _removeDone = false;
    }

    private void initializeFilePath(String fileName) {
        _userFile = Paths.get(fileName);
    }

    private void initializeOutputStream() {
        try {
            _outputStream = Files.newBufferedWriter(_userFile, CHARSET_DEFAULT,
                    OPEN_OPTIONS);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateMemory() {
        _bakaMap = new HashMap<String, LinkedList<Task>>();
        try (BufferedReader inputStream = Files.newBufferedReader(_userFile,
                CHARSET_DEFAULT)) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                if (line.isEmpty() || line.contains(TAG_DELETED)
                        || (line.contains(TASK_DONE) && _removeDone)) {
                    continue;
                } else if (line.contains(TAG_TITLE)) {
                    Task task = new Task(line);
                    addTaskToMap(task);
                } else if (line.contains(TASK_TOTAL)) {
                    String count = line.split(TAG_CLOSE)[CONTENT_INDEX].trim();
                    taskCount = new BigInteger(count.trim());
                } else if (line.contains(TASK_DONE)) {
                    String count = line.split(TAG_CLOSE)[CONTENT_INDEX].trim();
                    taskDone = new BigInteger(count.trim());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addTaskToMap(Task task) {
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

    @Override
    public String getTotalCount() {
        return taskCount.toString();
    }

    @Override
    public String getDoneCount() {
        return taskDone.toString();
    }

    @Override
    public String getUndoneCount() {
        return taskCount.subtract(taskDone).toString();
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
        String lineTwo = String.format(MESSAGE_OUTPUT_TOTAL_COUNT,
                taskCount.toString());
        String lineThree = String.format(MESSAGE_OUTPUT_DONE_COUNT,
                taskDone.toString());
        return lineOne + lineTwo + lineThree;
    }

    @Override
    public Task getPrevious() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean add(Task task) {
        task.setDeleted(false);
        addTaskToMap(task);

        if (task.isDone()) {
            taskDone = taskDone.add(BigInteger.ONE);
            String count = TASK_DONE + taskDone.toString();
            dirtyWrite(count);
        }

        taskCount = taskCount.add(BigInteger.ONE);
        String count = TASK_TOTAL + taskCount.toString();

        dirtyWrite(count);
        return dirtyWrite(task.toString());
    }

    @Override
    public boolean delete(Task task) {
        String key = task.getKey();
        LinkedList<Task> target = _bakaMap.get(key);
        if (target == null) {
            return false;
        }
        boolean isRemoved = target.remove(task);
        if (isRemoved) {
            task.setDeleted(true);
            addTaskToMap(task);

            if (task.isDone()) {
                taskDone = taskDone.subtract(BigInteger.ONE);
                String count = TASK_DONE + taskDone.toString();
                dirtyWrite(count);
            }

            taskCount = taskCount.subtract(BigInteger.ONE);
            String count = TASK_TOTAL + taskCount.toString();

            dirtyWrite(task.toString());
            dirtyWrite(count);
        } else {
            return isRemoved;
        }
        return updateFile();
    }

    @Override
    public void close() {
        updateFile();
        try {
            _outputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean updateFile() {
        tempCreation();
        resetFile();
        writeFileComments();
        writeFinalCounters();
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
            ex.printStackTrace();
        }
    }

    private void writeFinalCounters() {
        String counter = TASK_TOTAL + taskCount.toString();
        try {
            _outputStream.write(counter);
            _outputStream.newLine();
            counter = TASK_DONE + taskDone.toString();
            _outputStream.write(counter);
            _outputStream.newLine();
            _outputStream.newLine();
            _outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean writeLinesToFile() {
        try {
            sort();
            for (String key : _sortedKeys) {
                if (key.contains(TAG_DONE) && _removeDone) {
                    continue;
                }
                if (key.contains(TAG_DELETED)) {
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
            ex.printStackTrace();
            return false;
        }
    }

    private void resetFile() {
        try {
            Files.write(_userFile, EMPTY_BYTE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void tempCreation() {
        // copy userFile into tempFile
        Path tempFile;
        try {
            tempFile = Files.createTempFile("bakatxt", "old");
            Files.copy(_userFile, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean setDone(Task task, boolean isDone) {
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
                if (taskTitle.equals(title.toLowerCase())) {
                    result.add(task);
                }
            }
        }

        // for demo purposes
        if (result.size() > MAX_TASKS_TO_DISPLAY) {
            LinkedList<Task> truncated = new LinkedList<Task>();
            for (int i = 0; i < MAX_TASKS_TO_DISPLAY; i++) {
                truncated.add(result.get(i));
            }
            result = truncated;
        }
        for (int i = 0; i < result.size(); i++) {
            Task task = new Task(result.get(i).toString());
            String taskTitle = task.getTitle();
            task.addTitle(i + 1 + ". " + taskTitle);
            result.set(i, task);
        }
        // end demo code

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

        // for demo purposes
        if (result.size() > MAX_TASKS_TO_DISPLAY) {
            LinkedList<Task> truncated = new LinkedList<Task>();
            for (int i = 0; i < MAX_TASKS_TO_DISPLAY; i++) {
                truncated.add(result.get(i));
            }
            result = truncated;
        }
        // end demo code

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

        // for demo purposes
        if (all.size() > MAX_TASKS_TO_DISPLAY) {
            LinkedList<Task> truncated = new LinkedList<Task>();
            for (int i = 0; i < MAX_TASKS_TO_DISPLAY; i++) {
                truncated.add(all.get(i));
            }
            all = truncated;
        }
        // end demo code

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
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void resetCounters() {
        taskCount = BigInteger.ZERO;
        taskDone = BigInteger.ZERO;
        for (Map.Entry<String, LinkedList<Task>> entry : _bakaMap.entrySet()) {
            String key = entry.getKey();
            if (!key.contains(TAG_DELETED)) {
                String size = String.valueOf(entry.getValue().size());
                taskCount = taskCount.add(new BigInteger(size));
            }
            if (key.contains(TAG_DONE)) {
                String size = String.valueOf(entry.getValue().size());
                taskDone = taskDone.add(new BigInteger(size));
            }
        }
    }

    @Override
    public void removeDone() {
        taskCount = taskCount.subtract(taskDone);
        taskDone = BigInteger.ZERO;
        _removeDone = true;
        updateFile();
        updateMemory();
        _removeDone = false;
    }

    @Override
    public void clear() {
        resetFile();
        updateMemory();
    }

}
