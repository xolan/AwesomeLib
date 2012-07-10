package no.xolan.awesomelib.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Localization {

    private List<ResourceBundle> properties = new ArrayList<>();
    private static Localization INSTANCE = new Localization();

    private Localization() {        
        //init(Locale.forLanguageTag("nb"));
        init(Locale.ENGLISH);
        
        for (Handler h : Logger.getLogger(get("name")).getParent().getHandlers()) {
            Logger.getLogger(get("name")).getParent().removeHandler(h);
        }
        
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SingleLineFormatter());
        Logger.getLogger(get("name")).getParent().addHandler(consoleHandler);
    }

    public static Localization getInstance() {
        return INSTANCE;
    }

    private void init(Locale locale) {
        this.properties.clear();
        this.properties.add(ResourceBundle.getBundle("res.strings.Cracker", locale));
        this.properties.add(ResourceBundle.getBundle("res.strings.Logger", locale));
        this.properties.add(ResourceBundle.getBundle("res.strings.Scripts", locale));
        Logger.getLogger("name").info(this.get("locale") + locale.getDisplayName() + ".");
    }
    
    public String get(String key) {
        
        for(ResourceBundle rb : this.properties) {
            if(rb.containsKey(key)) {
                return rb.getString(key);
            }
        }
        
        return this.get("key1") + key + this.get("key2");
         
    }

    public class SingleLineFormatter extends Formatter {

        Date dat = new Date();
        private final static String format = "{0,date} {0,time}";
        private MessageFormat formatter;
        private Object args[] = new Object[1];

        // Line separator string. This is the value of the line.separator
        // property at the moment that the SimpleFormatter was created.
        // private String lineSeparator = (String)
        // java.security.AccessController.doPrivileged(
        // new sun.security.action.GetPropertyAction("line.separator"));
        private String lineSeparator = "\n";

        /**
         * Format the given LogRecord.
         * 
         * @param record
         *            the log record to be formatted.
         * @return a formatted log record
         */
        public synchronized String format(LogRecord record) {

            StringBuilder sb = new StringBuilder();

            // Minimize memory allocations here.
            dat.setTime(record.getMillis());
            args[0] = dat;

            // Date and time
            StringBuffer text = new StringBuffer();
            if (formatter == null) {
                formatter = new MessageFormat(format);
            }
            formatter.format(args, text, null);
            sb.append(text);
            sb.append(" ");

            // Class name
            if (record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName());
            } else {
                sb.append(record.getLoggerName());
            }

            // Method name
            if (record.getSourceMethodName() != null) {
                sb.append(" ");
                sb.append(record.getSourceMethodName());
            }
            sb.append(" - "); // lineSeparator

            String message = formatMessage(record);

            // Level
            sb.append(record.getLevel().getLocalizedName());
            sb.append(": ");

            // Indent - the more serious, the more indented.
            // sb.append( String.format("% ""s") );
            int iOffset = (1000 - record.getLevel().intValue()) / 100;
            for (int i = 0; i < iOffset; i++) {
                sb.append(" ");
            }

            sb.append(message);
            sb.append(lineSeparator);
            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                }
            }
            return sb.toString();
        }
    }

}