package Util;

import java.io.IOException;
import java.util.logging.*;

public class CustomLogger {

    static public void setup(String path) {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        if (handlers.length > 0){
            if (handlers[0] instanceof ConsoleHandler) {
                rootLogger.removeHandler(handlers[0]);
            }
        }

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(path, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileHandler.setFormatter(new MyCustomFormatter());
        logger.addHandler(fileHandler);
        logger.setFilter(record -> true);
    }

    private static class MyCustomFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            sb.append("["+record.getLevel()+"]");
            sb.append("["+record.getSourceClassName()+"]");
            sb.append("["+record.getSourceMethodName()+"]\n");

            String message = record.getMessage();
            for (String part: message.split("\n")) {
                sb.append("\t" + part + "\n");
            }

            sb.append("\n");
            return sb.toString();
        }

    }
}
