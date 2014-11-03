package bakatxt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bakatxt.core.BakaParser;
import bakatxt.core.Database;
import bakatxt.core.Task;
import bakatxt.core.UserAction;
import bakatxt.core.UserEditStatus;
import bakatxt.core.UserEditTask;

public class UserInputTest {

    private Task dummy;
    private BakaParser _parser;
    private Database _database;

    @Before
    public void setUp() throws Exception {
        _parser = new BakaParser();
        _database = Database.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        _database.close();
    }

    @Test
    public void testAdd() {
        dummy = _parser.add("add testAdd command ");
        UserAction command = new UserAction("add", dummy);

        command.execute();
        LinkedList<Task> tasks = _database.getAllTasks();
        assertTrue(tasks.contains(dummy));

        command.undo();
        tasks = _database.getAllTasks();
        assertFalse(tasks.contains(dummy));
    }

    @Test
    public void testDelete() {
        dummy = _parser.add("add testDelete command tonight");
        UserAction command = new UserAction("delete", dummy);

        command.execute();
        LinkedList<Task> tasks = _database.getAllTasks();
        assertFalse(tasks.contains(dummy));

        command.undo();
        tasks = _database.getAllTasks();
        assertTrue(tasks.contains(dummy));
    }

    @Test
    public void testEdit() {
        dummy = _parser.add("add editing1");
        Task edit = _parser.add("add editing2");
        UserAction command = new UserEditTask("edit", dummy, edit);

        command.execute();
        LinkedList<Task> tasks = _database.getAllTasks();
        assertFalse(tasks.contains(dummy));

        command.undo();
        tasks = _database.getAllTasks();
        assertTrue(tasks.contains(dummy));
    }

    @Test
    public void testDone() {
        dummy = _parser.add("add done 1pm tomorrow");
        // _database.add(dummy);
        UserAction command = new UserEditStatus("done", dummy, true);

        command.execute();
        LinkedList<Task> tasks = _database.getAllTasks();
        assertTrue(tasks.contains(dummy));

        command.undo();
        tasks = _database.getAllTasks();
        assertTrue(tasks.contains(dummy));
    }
}
