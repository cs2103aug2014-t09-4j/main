package bakatxt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bakatxt.core.Database;
import bakatxt.core.Task;

public class DatabaseTest {

    private Database database;

    @Before
    public void setUp() throws Exception {
        database = Database.getInstance();
        database.setFile("aloha.txt");
        System.out.println(database.getFileName());
        Task task = new Task("beforeTask");
        database.add(task);
    }

    @After
    public void tearDown() {
        System.out.println(database);
        database.clear();
        database.close();
    }

    // @Test
    // public void testSetFile() {
    // String expected =
    // "File changed. Current filename is \"mytestfile2.txt\".";
    // String output = database.setFile("mytestfile2.txt");
    // assertEquals(expected, output);
    // }

    @Test
    public void testAddAndDeleteTask() {
        Task task = new Task("add test!");
        task.setDate("2014-05-02");
        task.setTime("2230");

        database.add(task);
        LinkedList<Task> tasks = database.getAllTasks();
        assertTrue(tasks.contains(task));

        database.delete(task);
        tasks = database.getAllTasks();
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
        Task task1 = new Task("task1");
        task1.setDate("2014-03-02");
        database.add(task1);
        Task task2 = new Task("task2");
        task2.setDate("2014-12-14");
        database.add(task2);
        Task task3 = new Task("task3");
        task3.setDone(true);
        task3.setFloating(true);
        database.add(task3);
        LinkedList<Task> result = database.getTasksWithDate("2014-03-02");
        assertTrue(result.contains(task1));
        result = database.getTasksWithDate("2014-12-14");
        assertFalse(result.contains(task1));
        result = database.getAllUndoneTasks();
        assertFalse(result.contains(task3));
        result = database.getAllTasks();
        assertTrue(result.contains(task2));
        result = database.getTasksWithDate(null);
        assertTrue(result.contains(task3));
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
        database.removeDone();
        LinkedList<Task> tasks = database.getAllTasks();
        assertFalse(tasks.contains(task));
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
        Task task = new Task("title");
        assertTrue(database.add(task));
        assertFalse(database.add(task));
    }
}
