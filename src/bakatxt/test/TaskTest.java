package bakatxt.test;

import static org.junit.Assert.assertEquals;
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
        String expected = "[TITLE] New Task \n" + "[DATE] 2014-05-02 \n"
                + "[TIME] 2230 \n" + "[VENUE] null \n"
                + "[DESCRIPTION] null \n" + "[DONE] true \n"
                + "[FLOATING] false \n";
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
        String databaseStr = "[9999 5000 0000 2014-05-02 2230] [TITLE] New Task [DATE] 2014-05-02 [TIME] 2230 [VENUE] null [DONE] true [FLOATING] true [DELETED] true [DESCRIPTION] null ";
        Task task = new Task(databaseStr);
        assertTrue(task.isDone());
        assertTrue(task.isFloating());
        assertEquals("null", task.getVenue());
        assertEquals(databaseStr, task.toString());
    }
}