//@author A0116320Y
package bakatxt.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BakaLogFormatter extends Formatter {

    private static final int MAX_BUFFER = 1000;
    private static final String DATE_FORMAT = "HH:mm:ss:SSS";
    private static final String SPACE = " ";
    private static final String COLON = ":";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        StringBuffer output = new StringBuffer(MAX_BUFFER);

        output.append(calculateDate(record.getMillis()));
        output.append(SPACE);
        output.append(record.getSourceClassName());
        output.append(SPACE);
        output.append(record.getSourceMethodName());
        output.append(LINE_SEPARATOR + "\t");
        output.append(record.getLevel());
        output.append(COLON + SPACE);
        output.append(formatMessage(record));
        output.append(LINE_SEPARATOR);

        return output.toString();
    }

    private static String calculateDate(long ms) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date rawDate = new Date(ms);
        return dateFormat.format(rawDate);
    }

    @Override
    public String getHead(Handler handler) {
        Date now = new Date();
        return "Session: " + now.toString() + LINE_SEPARATOR;
    }

    @Override
    public String getTail(Handler h) {
        return "End Session" + LINE_SEPARATOR + LINE_SEPARATOR;
    }
}
