//@author A0115160X
package bakatxt.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bakatxt.core.BakaParser;
import bakatxt.core.Database;
import bakatxt.core.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BakaTxtAutomatedTest {

    private static BakaBot _bot;
    private static Database _database;
    private static BakaParser _parser;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        _bot = new BakaBot();
        BakaBot.botOneTimeSetUp();
        _parser = new BakaParser();
        _database = Database.getInstance();
    }

    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        BakaBot.botOneTimeTearDown();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        BakaBot.botTearDown();
    }

    @Test
    public void test1Add() {
        String input = BakaBot.ADD
                + "Lunch tomorrow from 2pm to 3pm @mac -- with project members";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_VERY_LONG);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTasksWithDate(_parser.getDate("tomorrow"));
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void test2Add2() {
        String input = "Lunch and mugging date with friends day after tomorrow 12pm @NTU";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database
                .getTaskWithTitle("Lunch and mugging date with friends");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void test3Edit() {
        _bot.inputThisString(BakaBot.DISPLAY);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.EDIT + "2");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // title
        _bot.inputThisString("");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // venue
        _bot.inputThisString("");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // date
        _bot.inputThisString("");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // start time
        _bot.inputThisString("1pm");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // end time
        _bot.inputThisString("6pm");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        // description
        _bot.inputThisString("");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database
                .getTaskWithTitle("Lunch and mugging date with friends");
        Task expectedTask = expected.get(0);
        assertEquals(expectedTask.getTime(), "1300");
        assertEquals(expectedTask.getEndTime(), "1800");
    }

    @Test
    public void test4UndoRedo() {
        _bot.inputThisString(BakaBot.SHOW);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.CLEAR);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.UNDO);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.REDO);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.UNDO);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("Lunch");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser
                .add("Lunch tomorrow from 2pm to 3pm @mac -- with project members"));
        output.add(_parser
                .add("Lunch and mugging date with friends day after tomorrow 1pm to 6pm @NTU"));
        assertEquals(expected, output);
    }

    @Test
    public void test5Add3() {
        String input = BakaBot.ADD + "Study for EE2020 tomorrow @Com1";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("EE2020");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void test6Add4() {
        String input = "Study for CS2103T next week from 9am to 6pm @UTown";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("CS2103T");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void test7Add5() {
        String input = BakaBot.ADD
 + "Study EE2021 on 1/12 9am @Home";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("EE2021");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void test8Display() {
        _bot.inputThisString(BakaBot.DISPLAY + "today");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.SHOW + "tomorrow");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.VIEW + "week");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.DISPLAY + "next week");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.DISPLAY);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);

    }

    @Test
    public void test9Delete() {
        _bot.inputThisString(BakaBot.DELETE + "1");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
    }

    @Test
    public void test9Search() {
        _bot.inputThisString(BakaBot.SEARCH + "study");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("study");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add("Study for EE2020 tomorrow @Com1"));
        output.add(_parser
                .add("Study for CS2103T next week from 9am to 6pm @UTown"));
        output.add(_parser.add("Study EE2021 on 1/12 9am @Home"));
        assertEquals(expected, output);
    }
    
    @Test
    public void testt1Edit2() {
        _bot.inputThisString(BakaBot.EDIT + "2 @Com 1 -- Print notes");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("CS2103T");
        Task expectedTask = expected.get(0);
        assertEquals(expectedTask.getVenue(), "Com 1");
        assertEquals(expectedTask.getDescription(), "Print notes");
        
    }

    @Test
    public void testt2Delete2() {
        _bot.inputThisString(BakaBot.REMOVE + "1");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
    }

    @Test
    public void testt3Add6() {
        _bot.inputThisString(BakaBot.DISPLAY);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        String input = BakaBot.ADD + "Breakfast today 6am @home";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);

        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("Breakfast");
        Task expectedTask = expected.get(0);
        assertEquals(expectedTask.getDescription(), "Overdue since: ["
                + _parser.getDate("today") + "]");

    }

    @Test
    public void testt4Delete3() {
        _bot.inputThisString(BakaBot.DISPLAY);
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        _bot.inputThisString(BakaBot.DELETE + "1 to 2");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getAllTasks();
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add("Lunch and mugging date with friends day after tomorrow 1pm to 6pm @NTU"));
        output.add(_parser.add("Study EE2021 on 1/12 9am @Home"));
        assertEquals(expected, output);

    }
    
    @Test 
    public void testt5Done() {
        _bot.inputThisString(BakaBot.DONE + "1");
        BakaBot.waitAWhile(BakaBot.WAIT_LONG);
        
        Task expected = new Task();
        _database.updateDoneView(true);
        expected = _database.getAllTasks().get(1);
        Task output = new Task();
        output = _parser.add("Lunch and mugging date with friends day after tomorrow 1pm to 6pm @NTU");
        output.setDone(true);        
        assertEquals(expected, output);
    }
}
