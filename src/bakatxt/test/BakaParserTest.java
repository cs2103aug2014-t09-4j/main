package bakatxt.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import bakatxt.core.BakaParser;
import bakatxt.core.Task;

public class BakaParserTest {
    BakaParser _parser;

    @Before
    public void setUp() throws Exception {
        _parser = BakaParser.getInstance();
    }

    @Test
    public void testInputCase() {
        String input = "adD HELLO testinG @ scHOOl";
        Task output = _parser.add(input);
        Task expected = new Task("HELLO testinG");
        expected.setVenue("scHOOl");
        assertEquals(expected, output);
    }

    @Test
    public void testAddWordTitle() {
        String input = "add hello";
        Task output = _parser.add(input);
        Task expected = new Task("hello");
        assertEquals(expected, output);
    }

    @Test
    public void testAddLongWordTitle() {
        String input = "add lunch with project members";
        Task output = _parser.add(input);
        Task expected = new Task("lunch with project members");
        assertEquals(expected, output);
    }

    // @Test
    // public void testAddNumberTitle() {
    // String input = "add go to com 1 after class";
    // Task output = _parser.add(input);
    // Task expected = new Task("go to com 1 after class");
    // assertEquals(expected, output);
    // }

    @Test
    public void testAddLongNumberTitle() {
        String input = "add 012345";
        Task output = _parser.add(input);
        Task expected = new Task("012345");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat1() {
        String input = "add 2103tzx";
        Task output = _parser.add(input);
        Task expected = new Task("2103tzx");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat2() {
        String input = "add 8z";
        Task output = _parser.add(input);
        Task expected = new Task("8z");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTitleFormat3() {
        String input = "add edit1";
        Task output = _parser.add(input);
        Task expected = new Task("edit1");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenue() {
        String input = "add discuss 2103t with members @school";
        Task output = _parser.add(input);
        Task expected = new Task("discuss 2103t with members");
        expected.setVenue("school");
        assertEquals(expected, output);
    }

    @Test
    public void testAddLongVenue() {
        String input = "add do cs2101 assignment @ seletar hill at yck";
        Task output = _parser.add(input);
        Task expected = new Task("do cs2101 assignment");
        expected.setVenue("seletar hill at yck");
        assertEquals(expected, output);
    }

    @Test
    public void testAddSpecialVenue() {
        String input = "add discuss 2103t with members @edit1";
        Task output = _parser.add(input);
        Task expected = new Task("discuss 2103t with members");
        expected.setVenue("edit1");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDescription() {
        String input = "add lunch -- woohoo 14/2/14 2pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDescription("woohoo 14/2/14 2pm");
        assertEquals(expected, output);
    }

    @Test
    public void testAddLongDescription() {
        String input = "add do research --about inefficient solid waste management";
        Task output = _parser.add(input);
        Task expected = new Task("do research");
        expected.setDescription("about inefficient solid waste management");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTmr() {
        String input = "add cut hair tomorrow";
        Task output = _parser.add(input);
        Task expected = new Task("cut hair");
        expected.setDate("2014-10-16");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTonight() {
        String input = "add dinner tonight";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate("2014-10-15");
        expected.setTime("1900");
        assertEquals(expected, output);
    }

    // @Test
    // public void testAddDateAfternoon() {
    // String input = "add tea time afternoon";
    // Task output = _parser.add(input);
    // Task expected = new Task("tea time");
    // expected.setDate("2014-10-15");
    // expected.setTime("1300");
    // assertEquals(expected, output);
    // }

    @Test
    public void testAddDateNoon() {
        String input = "add lunch at noon";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-10-15");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat1() {
        String input = "add dinner 20/11/2014";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate("2014-11-20");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat2() {
        String input = "add buy trackpad 5/11";
        Task output = _parser.add(input);
        Task expected = new Task("buy trackpad");
        expected.setDate("2014-11-05");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat3() {
        String input = "add see doctor 1/12/14";
        Task output = _parser.add(input);
        Task expected = new Task("see doctor");
        expected.setDate("2014-12-01");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat4() {
        String input = "add lunch 21-10";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-10-21");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat5() {
        String input = "add dinner 4-12-2014";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate("2014-12-04");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat6() {
        String input = "add meeting friends 15-4-14";
        Task output = _parser.add(input);
        Task expected = new Task("meeting friends");
        expected.setDate("2014-04-15");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat1() {
        String input = "add go shopping 7/3 2 pm";
        Task output = _parser.add(input);
        Task expected = new Task("go shopping");
        expected.setDate("2014-03-07");
        expected.setTime("1400");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat2() {
        String input = "add class 6/11 9am";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-11-06");
        expected.setTime("0900");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat3() {
        String input = "add class 6/11 1200hours";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-11-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat4() {
        String input = "add class 6/11 12:00";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-11-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat5() {
        String input = "add class 6/11 1200h";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-11-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    // @Test
    // public void testAddTimeDuration() {
    // String input = "add class 6/11 1200h";
    // Task output = _parser.add(input);
    // Task expected = new Task("class");
    // expected.setDate("2014-11-06");
    // expected.setTime("1200");
    // assertEquals(expected, output);
    // }

    @Test
    public void testAddDateVenue() {
        String input = "add lunch 2/3 @ school or somewhere else";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-03-02");
        expected.setVenue("school or somewhere else");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateVenuePrepo() {
        String input = "add lunch on 2/3 at @jurong";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-03-02");
        expected.setVenue("jurong");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDate() {
        String input = "add class @ engine e3 15/10";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-10-15");
        expected.setVenue("engine e3");
        assertEquals(expected, output);
    }

    @Test
    public void testAddSpecialVenueDate() {
        String input = "add lunch @ buona vista 15/10";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-10-15");
        expected.setVenue("buona vista");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDatePrepo() {
        String input = "add class at @ engine e3 on at 15/10";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-10-15");
        expected.setVenue("engine e3");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenue() {
        String input = "add lunch 3/8 3pm @ school";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenuePrepo() {
        String input = "add lunch on 3/8 at 3pm at @ school";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTime() {
        String input = "add lunch @school or smth 3/8/2014 3pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school or smth");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTimePrepo() {
        String input = "add lunch at on @school or smth on 3/8/2014 at on 3pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school or smth");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenue() {
        String input = "add lunch 3pm 3-8-14 @ school wooo";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school wooo");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenuePrepo() {
        String input = "add lunch at on 3pm on 3-8-14 @ school wooo";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1500");
        expected.setVenue("school wooo");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueTimeDate() {
        String input = "add lunch @somewhere la 12pm 3-8";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1200");
        expected.setVenue("somewhere la");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueTimeDatePrepo() {
        String input = "add lunch at on @somewhere la on 12pm 3-8";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-08-03");
        expected.setTime("1200");
        expected.setVenue("somewhere la");
        assertEquals(expected, output);
    }

    @Test
    public void testIndexRange1() {
        String input = "3 to 10";
        ArrayList<Integer> output = _parser.getIndexList(input);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        for (int i = 3; i < 11; i++) {
            expected.add(i);
        }
        assertEquals(expected, output);
    }

    public void testIndexRange2() {
        String input = "1-20";
        ArrayList<Integer> output = _parser.getIndexList(input);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        for (int i = 1; i < 21; i++) {
            expected.add(i);
        }
        assertEquals(expected, output);
    }

    @Test
    public void testIndex() {
        String input = "2";
        ArrayList<Integer> output = _parser.getIndexList(input);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(2);
        assertEquals(expected, output);
    }
}
