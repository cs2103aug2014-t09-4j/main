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
        task = new Task("New Task");
    }

    @Test
    public void testTaskOutput() {
        task.addDate("2014-05-02");
        task.addTime("2230");
        task.setDone(true);
        String expected = "\t[TITLE] New Task \n" + "\t[DATE] 2014-05-02 \n"
                + "\t[TIME] 2230 \n" + "\t[VENUE] null \n"
                + "\t[DESCRIPTION] null \n" + "\t[DONE] true \n"
                + "\t[FLOATING] false \n\n";
        String output = task.toDisplayString();
        System.out.println(output);
        assertEquals(expected, output);
    }

    @Test
    public void testTaskFileOutput() {
        task.addDate("2014-05-02");
        task.addTime("2230");
        task.setDone(true);
        task.setFloating(true);
        task.setDeleted(true);

        String output = task.toString();
        String expected = "[9999 5000 0000 2014-05-02 2230] [TITLE] New Task "
                + "[DATE] 2014-05-02 [TIME] 2230 [VENUE] null "
                + "[DONE] true [FLOATING] true " + "[DELETED] true "
                + "[DESCRIPTION] null ";
        System.out.println(output);
        assertEquals(expected, output);
    }

    @Test
    public void testTaskFromDatabaseString() {
        String databaseStr = "[9999 5000 2014-05-02 2230] [TITLE] New Task [DATE] 2014-05-02 [TIME] 2230 [VENUE] null [DONE] true [FLOATING] false [DELETED] true [DESCRIPTION] null ";
        Task task = new Task(databaseStr);
        assertTrue(task.isDone());
        assertFalse(task.isFloating());
        assertEquals("null", task.getVenue());
        assertEquals(databaseStr, task.toString());
    }
}