package bakatxt.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class BakaLogger {

    private static FileHandler _fileTxt;
    private static BakaLogFormatter _bakaFormat;
    private static Logger _logger;

    public static void setup() throws SecurityException, IOException {
        _logger = Logger.getLogger("");

        Handler[] handlers = _logger.getHandlers();
        for (Handler handler : handlers) {
            _logger.removeHandler(handler);
        }

        _fileTxt = new FileHandler("Baka.log", 1048576, 1, true);
        _bakaFormat = new BakaLogFormatter();
        _logger.addHandler(_fileTxt);
        _fileTxt.setFormatter(_bakaFormat);
    }

    public static void setup(boolean flag) {
        if (!flag) {
            return;
        }

        _logger = Logger.getLogger("");

        Handler[] handlers = _logger.getHandlers();
        for (Handler handler : handlers) {
            _logger.removeHandler(handler);
        }

        try {
            _fileTxt = new FileHandler("%t/Baka.log", 1048576, 1, true);
        } catch (SecurityException | IOException ex1) {
            // do nothing.
        }
        _bakaFormat = new BakaLogFormatter();
        _logger.addHandler(_fileTxt);
        _fileTxt.setFormatter(_bakaFormat);
    }

    public static void teardown() {
        Handler[] handlers = _logger.getHandlers();
        for (Handler handler : handlers) {
            handler.close();
            ;
        }
    }
}
