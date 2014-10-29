package bakatxt.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bakatxt.core.BakaParser;
import bakatxt.core.Database;
import bakatxt.core.Task;

public class BakaParserAutomatedTest {

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
    public void testAddInputCase() {
        String input = BakaBot.ADD + "lUNCH today 2pm";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_VERY_LONG);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("lUNCH");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddNumbersTitle() {
        String input = BakaBot.ADD + "0123456789";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("0123456789");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddLongTitle() {
        String input = BakaBot.ADD
                + " this is a super super long title for testing purposes idk where to stoppppp";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database
                .getTaskWithTitle("this is a super super long title for testing purposes idk where to stoppppp");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat1() {
        String input = BakaBot.ADD + "2103tzx";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("2103tzx");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat2() {
        String input = BakaBot.ADD + "8z";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("8z");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat3() {
        String input = BakaBot.ADD + "edit1";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("edit1");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenue() {
        String input = BakaBot.ADD + "do cs2101 assignment @ computing at nus";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("do cs2101 assignment");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddSpecialVenue() {
        String input = BakaBot.ADD + "discuss 2103t with members @com1";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("discuss 2103t with members");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDescription() {
        String input = BakaBot.ADD
                + "do research -- about inefficient solid waste management";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("do research");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTmr() {
        String input = BakaBot.ADD + "cut hair tomorrow";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("cut hair");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTonight() {
        String input = BakaBot.ADD + "dinner tonight";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("dinner");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateNoon() {
        String input = BakaBot.ADD + "brunch at noon";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("brunch");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateNextWeek() {
        String input = BakaBot.ADD + "go sentosa next week";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go sentosa");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateNextFriday() {
        String input = BakaBot.ADD + "presentation next friday";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("presentation");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateNextMonth() {
        String input = BakaBot.ADD + "go cafe next month";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go cafe");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat1() {
        String input = BakaBot.ADD + "teabreak 20/12/2014";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("teabreak");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat2() {
        String input = BakaBot.ADD + "buy trackpad 21/11";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("buy trackpad");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat3() {
        String input = BakaBot.ADD + "see doctor on 1/12/14";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("see doctor");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat4() {
        String input = BakaBot.ADD + "go shopping on 21-12";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go shopping");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat5() {
        String input = BakaBot.ADD + "go eat supper on 4-12-2014";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go eat supper");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat6() {
        String input = BakaBot.ADD + "meeting friends 15-1-15";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("meeting friends");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat1() {
        String input = BakaBot.ADD + "go library 20/1/15 2pm";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go library");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat2() {
        String input = BakaBot.ADD + "class 6/2/15 9am";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("class");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat3() {
        String input = BakaBot.ADD + "work on 27/12 1200hours";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("work");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat4() {
        String input = BakaBot.ADD + "swimming with sis 28/12 13:00";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("swimming with sis");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat5() {
        String input = BakaBot.ADD + "submit homework 18/11 1200h";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("submit homework");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat1() {
        String input = BakaBot.ADD
                + "google hangouts next week 9pm to 11pm @home";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("google hangouts");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat2() {
        String input = BakaBot.ADD
                + "lunch buffet tomorrow 1300h - 1500h @ town";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("lunch buffet");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat3() {
        String input = BakaBot.ADD + "studyyy today @ school 13:00 - 6pm";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("studyyy");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateVenue() {
        String input = BakaBot.ADD
                + "lunchhh 2/3/15 @ school or somewhere else";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("lunchhh");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateVenuePrepo() {
        String input = BakaBot.ADD + "eat gyoza on 1/1/15 at @jurong";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("eat gyoza");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDate() {
        String input = BakaBot.ADD + "english class @ engine e3 21/11";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("english class");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    public void testAddSpecialVenueDate() {
        String input = BakaBot.ADD + "buy bread @ buona vista 15/12";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("buy bread");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDatePrepo() {
        String input = BakaBot.ADD + "lab at @ engine e3 on at 16/11";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("lab");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenue() {
        String input = BakaBot.ADD + "hola 3/8/15 3pm @ school";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("hola");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenuePrepo() {
        String input = BakaBot.ADD + "booooo on 6/3/15 at 3pm at @ school";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("booooo");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTime() {
        String input = BakaBot.ADD + "lunch date @school or smth 5/8/2015 12pm";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("lunch date");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTimePrepo() {
        String input = BakaBot.ADD
                + "take a walk at on @nearby home or smth on 13/5/2015 at on 5pm";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("take a walk");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenue() {
        String input = BakaBot.ADD + "rest 3pm 8-2-15 @ school wooo";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("rest");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenuePrepo() {
        String input = BakaBot.ADD + "go out at on 10am on 8-12-14 @ town wooo";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("go out");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueTimeDatePrepo() {
        String input = BakaBot.ADD + "buy book @somewhere la 12pm 1-12";
        _bot.inputThisString(input);
        BakaBot.waitAWhile(BakaBot.WAIT_SHORT);
        LinkedList<Task> expected = new LinkedList<Task>();
        expected = _database.getTaskWithTitle("buy book");
        LinkedList<Task> output = new LinkedList<Task>();
        output.add(_parser.add(input));
        assertEquals(expected, output);
    }
}
