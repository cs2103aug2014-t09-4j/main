//@author A0116320Y
package bakatxt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bakatxt.core.Database;
import bakatxt.core.Task;

public class DatabaseTest {

    private static Database database;
    private static Path temp;

    @BeforeClass
    public static void setUp() throws Exception {
        temp = Files.createTempFile(Paths.get("BakaStorage.txt")
                .toAbsolutePath().getParent(),
                null, null);
        temp.toFile().deleteOnExit();
        Files.copy(Paths.get("BakaStorage.txt"), temp,
                StandardCopyOption.REPLACE_EXISTING);
        database = Database.getInstance();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        database.close();
        Files.delete(Paths.get(database.getFileName()));
        Files.copy(temp, Paths.get("BakaStorage.txt"),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testAddAndDeleteTask() {
        Task task = new Task("add test!");
        task.setDate("3015-05-02");
        task.setTime("2230");

        database.add(task);
        LinkedList<Task> tasks = database.getTaskWithTitle("add test");
        assertTrue(tasks.contains(task));

        database.delete(task);
        tasks = database.getTaskWithTitle("add test");
        assertFalse(tasks.contains(task));
    }

    @Test
    public void testDeleteTaskDontExist() {
        Task task = new Task("delete test!");
        task.setDate("2014-05-02");
        task.setTime("2230");
        assertFalse(database.delete(task));
    }

    @Test
    public void testSetTaskDone() {
        Task task = new Task("set done test!");
        database.add(task);
        database.setDone(task, true);
        task.setDone(true);
        LinkedList<Task> tasks = database.getAllTasks();
        assertTrue(tasks.contains(task));
    }

    @Test
    public void testSetFloating() {
        Task task = new Task("set floating test!");
        task.setDate("2014-05-02");
        task.setTime("2230");
        database.add(task);
        database.setFloating(task, true);
        task.setFloating(true);
        LinkedList<Task> tasks = database.getAllTasks();
        assertTrue(tasks.contains(task));
    }

    @Test
    public void testGetTasks() {
        database.updateDoneView(false);
        Task task1 = new Task("task1");
        task1.setDate("2015-03-02");
        database.add(task1);
        Task task2 = new Task("task2");
        task2.setDate("2015-12-14");
        database.add(task2);
        Task task3 = new Task("task3");
        task3.setDone(true);
        task3.setFloating(true);
        database.add(task3);
        LinkedList<Task> result = database.getTasksWithDate(task1.getDate());
        assertTrue(result.contains(task1));
        result = database.getTasksWithDate("2014-12-14");
        assertFalse(result.contains(task1));
        result = database.getAllTasks();
        assertFalse(result.contains(task3));
        result = database.getAllTasks();
        assertTrue(result.contains(task2));
        result = database.getTasksWithDate(null);
        assertFalse(result.contains(task3));
        database.updateDoneView(true);
    }

    @Test
    public void testDatabaseOverflow() {
        for (int year = 2014; year < 3014; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= 31; day++) {
                    String date = year
                            + ((month < 10) ? "-0" + month : "-" + month)
                            + ((day < 10) ? "-0" + day : "-" + day);
                    Task task = new Task(date);
                    task.setDate(date);
                    database.add(task);
                }
            }
        }
    }

    @Test
    public void testRemoveDone() {
        Task task = new Task("remove done test!");
        task.setDone(true);

        database.add(task);
        LinkedList<Task> tasks = database.getAllTasks();
        assertTrue(tasks.contains(task));
    }

    @Test
    public void testGetTaskWithTitle() {
        Task task = new Task("title");
        database.add(task);
        LinkedList<Task> tasks = database.getTaskWithTitle("title");
        assertTrue(tasks.contains(task));
    }

    // This is a equivalence test case for adding tasks
    @Test
    public void testIsExisting() {
        Task task = new Task("titleExists");
        assertTrue(database.add(task));
        assertFalse(database.add(task));
    }
}
