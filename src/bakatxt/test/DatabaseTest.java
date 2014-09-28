package bakatxt.test;
import static org.junit.Assert.assertEquals;
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
        database = new Database("mytestfile.txt");
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testSetFile() {
        String expected = "File changed. Current filename is \"mytestfile2.txt\".";
        String output = database.setFile("mytestfile2.txt");
        assertEquals(expected, output);
    }

    @Test
    public void testAddAndDeleteTask() {
        Task task = new Task("helloWorld!");
        task.addDate("2014-05-02");
        task.addTime("2230");
        task.setDone(true);
        assertTrue(database.add(task));
        assertTrue(database.delete(task));
    }

    @Test
    public void testDeleteTaskFalse() {
        Task task = new Task("helloWorld!    ");
        task.addDate("2014-05-02");
        task.addTime("2230");
        task.setDone(true);
        assertFalse(database.delete(task));
    }

    @Test
    public void testSetTaskDone() {
        Task task = new Task("set done!");
        database.add(task);
        assertTrue(database.setDone(task));
    }

    @Test
    public void testAddFloating() {
        Task task = new Task("This is floating!");
        task.setFloating(true);
        assertTrue(database.add(task));
    }

    @Test
    public void testGetTasks() {
        Task task1 = new Task("task1");
        task1.addDate("2014-03-02");
        database.add(task1);
        Task task2 = new Task("task2");
        task2.addDate("2014-12-14");
        database.add(task2);
        Task task3 = new Task("task3");
        task3.setDone(true);
        task3.setFloating(true);
        database.add(task3);
        LinkedList<Task> result = database.getTasks("2014-03-02");
        assertTrue(result.contains(task1));
        result = database.getTasks("2014-12-14");
        assertFalse(result.contains(task1));
        result = database.getAllUndoneTasks();
        assertFalse(result.contains(task3));
        result = database.getAllTasks();
        assertTrue(result.contains(task2));
        result = database.getTasks(null);
        assertTrue(result.contains(task3));
    }

    @Test
    public void testDatabaseFunctions() {
        database.resetCounters();
        database.removeDone();
    }
}
