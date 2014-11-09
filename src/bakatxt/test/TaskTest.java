//@author A0116320Y
package bakatxt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import bakatxt.core.Task;

public class TaskTest {

    Task task;

    @Before
    public void setUp() throws Exception {
        task = new Task();
        task.setTitle("New Task");
    }

    @Test
    public void testTaskOutput() {
        task.setDate("2014-05-02");
        task.setTime("2230");
        task.setDone(true);
        String expected = "[TITLE] New Task [DATE] 2014-05-02 [TIME] 2230 ";
        String output = task.toDisplayString();
        assertEquals(expected, output);
    }

    @Test
    public void testTaskFileOutput() {
        task.setDate("2014-05-02");
        task.setTime("2230");
        task.setDone(true);
        task.setFloating(true);
        task.setDeleted(true);

        String output = task.toString();
        String expected = "[9999 5000 0000 null null] [TITLE] New Task "
                + "[DATE] null [TIME] null [ENDTIME] null [VENUE] null "
                + "[DONE] true [FLOATING] true " + "[DELETED] true "
                + "[DESCRIPTION] null ";
        assertEquals(expected, output);
    }

    @Test
    public void testTaskFromDatabaseString() {
        String databaseStr = "[9999 5000 2014-05-02 2230] [TITLE] New Task [DATE] 2014-05-02 [TIME] 2230 [ENDTIME] null [VENUE] null [DONE] true [FLOATING] false [DELETED] true [DESCRIPTION] null ";
        Task task = new Task(databaseStr);
        assertTrue(task.isDone());
        assertFalse(task.isFloating());
        assertEquals("null", task.getVenue());
        assertEquals(databaseStr, task.toString());
    }

    // This is an equivalence partition test case for task creation
    @Test
    public void testInvalidDatabaseString() {
        String fake = "[TITLE] fakeee";
        Task task = new Task(fake);
        assertEquals(null, task.getTitle());
        String real = "[9999 5000 0000 null null] [TITLE] New Task "
                + "[DATE] null [TIME] null [ENDTIME] null [VENUE] null "
                + "[DONE] true [FLOATING] true " + "[DELETED] true "
                + "[DESCRIPTION] null ";
        task = new Task(real);
        assertEquals(task.toString(), real);
    }

    // merging tasks
    @Test
    public void testMerging() {
        Task task = new Task("hello World");
        task.setTime("2000");
        task.setEndTime("2100");
        Task mergeThis = new Task();
        mergeThis.setTime("1900");
        mergeThis.setDate("2014-12-14");
        mergeThis.setDescription("aloha");
        mergeThis.setVenue("nowhere");
        task.merge(mergeThis);
    }
}