package bakatxt.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import bakatxt.core.BakaParser;
import bakatxt.core.Task;

public class BakaParserTest {
    private static BakaParser _parser;

    @Before
    public void setUp() throws Exception {
        _parser = new BakaParser();
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
    public void testAddTitleFormat4() {
        String input = "add CS2101 OP2 Conference @Computing tomorrow";
        Task output = _parser.add(input);
        Task expected = new Task("CS2101 OP2 Conference");
        expected.setDate(_parser.getDate("tomorrow"));
        expected.setVenue("Computing");
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
        String input = "add lunch -- woohoo 14/12/14 2pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDescription("woohoo 14/12/14 2pm");
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
    public void testAddDateToday() {
        String input = "add do nothing today 23:59";
        Task output = _parser.add(input);
        Task expected = new Task("do nothing");
        expected.setDate(_parser.getDate("today"));
        expected.setTime("2359");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTmr() {
        String input = "add cut hair tomorrow";
        Task output = _parser.add(input);
        Task expected = new Task("cut hair");
        expected.setDate(_parser.getDate("tomorrow"));
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTonight() {
        String input = "add dinner tonight";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate(_parser.getDate("tonight"));
        expected.setTime("1900");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateNoon() {
        String input = "add lunch at noon";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate(_parser.getDate("noon"));
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat1() {
        String input = "add dinner 20/12/2014";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate("2014-12-20");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat2() {
        String input = "add buy trackpad 29/11";
        Task output = _parser.add(input);
        Task expected = new Task("buy trackpad");
        expected.setDate("2014-11-29");
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
        String input = "add lunch 21-12";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-21");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat5() {
        String input = "add dinner 4-1-2015";
        Task output = _parser.add(input);
        Task expected = new Task("dinner");
        expected.setDate("2015-01-04");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateFormat6() {
        String input = "add meeting friends 15-4-15";
        Task output = _parser.add(input);
        Task expected = new Task("meeting friends");
        expected.setDate("2015-04-15");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat1() {
        String input = "add go shopping 7/12 2pm";
        Task output = _parser.add(input);
        Task expected = new Task("go shopping");
        expected.setDate("2014-12-07");
        expected.setTime("1400");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat2() {
        String input = "add class 6/12 9am";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-12-06");
        expected.setTime("0900");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat3() {
        String input = "add class 6/12 1200hours";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-12-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat4() {
        String input = "add class 6/12 12:00";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-12-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeFormat5() {
        String input = "add class 6/12 1200h";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2014-12-06");
        expected.setTime("1200");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat1() {
        String input = "add lunch buffet tomorrow from 1pm to 3pm @town";
        Task output = _parser.add(input);
        Task expected = new Task("lunch buffet");
        expected.setDate(_parser.getDate("tomorrow"));
        expected.setTime("1300");
        expected.setEndTime("1500");
        expected.setVenue("town");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat2() {
        String input = "add google hangouts from next week 9pm to 11pm @ home";
        Task output = _parser.add(input);
        Task expected = new Task("google hangouts");
        expected.setDate(_parser.getDate("next week"));
        expected.setTime("2100");
        expected.setEndTime("2300");
        expected.setVenue("home");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat3() {
        String input = "add google hangouts tomorrow 1pm to 3pm @home";
        Task output = _parser.add(input);
        Task expected = new Task("google hangouts");
        expected.setDate(_parser.getDate("tomorrow"));
        expected.setTime("1300");
        expected.setEndTime("1500");
        expected.setVenue("home");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat4() {
        String input = "add google hangouts tomorrow @ home 1300hours to 3pm";
        Task output = _parser.add(input);
        Task expected = new Task("google hangouts");
        expected.setDate(_parser.getDate("tomorrow"));
        expected.setTime("1300");
        expected.setEndTime("1500");
        expected.setVenue("home");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat5() {
        String input = "add google hangouts @ home tomorrow 1300h to 15:00";
        Task output = _parser.add(input);
        Task expected = new Task("google hangouts");
        expected.setDate(_parser.getDate("tomorrow"));
        expected.setTime("1300");
        expected.setEndTime("1500");
        expected.setVenue("home");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDurationFormat6() {
        String input = "add google hangouts 13:00 - 15:00 @home";
        Task output = _parser.add(input);
        Task expected = new Task("google hangouts");
        expected.setDate(_parser.getDate("today"));
        expected.setTime("1300");
        expected.setEndTime("1500");
        expected.setVenue("home");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateVenue() {
        String input = "add lunch 2/12 @ school or somewhere else";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-02");
        expected.setVenue("school or somewhere else");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateVenuePrepo() {
        String input = "add lunch on 2/12 at @jurong";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-02");
        expected.setVenue("jurong");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDate() {
        String input = "add class @ engine e3 1/5/15";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2015-05-01");
        expected.setVenue("engine e3");
        assertEquals(expected, output);
    }

    @Test
    public void testAddSpecialVenueDate() {
        String input = "add lunch @ buona vista 15/2/15";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2015-02-15");
        expected.setVenue("buona vista");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDatePrepo() {
        String input = "add class at @ engine e3 on at 15/2/15";
        Task output = _parser.add(input);
        Task expected = new Task("class");
        expected.setDate("2015-02-15");
        expected.setVenue("engine e3");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenue() {
        String input = "add lunch 3/12 3pm @ school";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-03");
        expected.setTime("1500");
        expected.setVenue("school");
        assertEquals(expected, output);
    }

    @Test
    public void testAddDateTimeVenuePrepo() {
        String input = "add lunch on 3/12 at 3pm at @ school";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-03");
        expected.setTime("1500");
        expected.setVenue("school");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTime() {
        String input = "add lunch @school or smth 3/3/2015 3pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2015-03-03");
        expected.setTime("1500");
        expected.setVenue("school or smth");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueDateTimePrepo() {
        String input = "add lunch at on @school or smth on 3/3/2015 at on 3pm";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2015-03-03");
        expected.setTime("1500");
        expected.setVenue("school or smth");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenue() {
        String input = "add lunch 3pm 3-3-15 @ school wooo";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2015-03-03");
        expected.setTime("1500");
        expected.setVenue("school wooo");
        assertEquals(expected, output);
    }

    @Test
    public void testAddTimeDateVenuePrepo() {
        String input = "add lunch at on 3pm on 3-3-15 @ school wooo";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2015-03-03");
        expected.setTime("1500");
        expected.setVenue("school wooo");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueTimeDate() {
        String input = "add lunch @somewhere la 12pm 3-12";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-03");
        expected.setTime("1200");
        expected.setVenue("somewhere la");
        assertEquals(expected, output);
    }

    @Test
    public void testAddVenueTimeDatePrepo() {
        String input = "add lunch at on @somewhere la on 12pm 3-12";
        Task output = _parser.add(input);
        Task expected = new Task("lunch");
        expected.setDate("2014-12-03");
        expected.setTime("1200");
        expected.setVenue("somewhere la");
        assertEquals(expected, output);
    }

    @Test
    public void testGetString() {
        String input = "add testing 123";
        String output = _parser.getString(input);
        String expected = "testing 123";
        assertEquals(expected, output);
    }

    @Test
    public void testGetCommand() {
        String input = "add testing 123";
        String output = _parser.getCommand(input);
        String expected = "ADD";
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

    @Test
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
    public void testIndexRange3() {
        String input = "3, 6, 10";
        ArrayList<Integer> output = _parser.getIndexList(input);
        ArrayList<Integer> expected = new ArrayList<Integer>();
        expected.add(3);
        expected.add(6);
        expected.add(10);
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

    @Test
    public void testGetDate1() {
        String input = "24/5";
        String output = _parser.getDate(input);
        String expected = "2014-05-24";
        assertEquals(expected, output);
    }

    @Test
    public void testGetDate2() {
        String input = "tomorrow";
        String output = _parser.getDate(input);
        String expected = _parser.getDate("one day later");
        assertEquals(expected, output);
    }

    @Test
    public void testGetDate3() {
        String input = "tonight";
        String output = _parser.getDate(input);
        String expected = _parser.getDate("today");
        assertEquals(expected, output);
    }

    @Test
    public void testGetDate4() {
        String input = "2-5";
        String output = _parser.getDate(input);
        String expected = "2014-05-02";
        assertEquals(expected, output);
    }

    @Test
    public void testGetDate5() {
        String input = "24/5/2014";
        String output = _parser.getDate(input);
        String expected = "2014-05-24";
        assertEquals(expected, output);
    }

    @Test
    public void testGetFormatDate1() {
        String input = "2014-10-29";
        String output = _parser.getFormattedDate(input);
        String expected = "Wednesday, 29 October 2014";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime1() {
        String input = "9pm";
        String output = _parser.getTime(input);
        String expected = "2100";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime2() {
        String input = "5:15";
        String output = _parser.getTime(input);
        String expected = "0515";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime3() {
        String input = "tonight";
        String output = _parser.getTime(input);
        String expected = "1900";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime4() {
        String input = "noon";
        String output = _parser.getTime(input);
        String expected = "1200";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime5() {
        String input = "0900h";
        String output = _parser.getTime(input);
        String expected = "0900";
        assertEquals(expected, output);
    }

    @Test
    public void testGetTime6() {
        String input = "7am";
        String output = _parser.getTime(input);
        String expected = "0700";
        assertEquals(expected, output);
    }
}

