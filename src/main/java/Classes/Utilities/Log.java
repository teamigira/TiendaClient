package Classes.Utilities;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Application log: {@code <data folder>/logs/tienda-N.log}, five files of
 * 1 MB each, newest first. Ask a customer for these files when they report a
 * problem. Every {@code java.util.logging} logger in the app writes here.
 *
 * @author Nkanabo
 */
public final class Log {

    private static boolean started;

    private Log() {
    }

    /** Send all logging to the log files. Call once at startup. */
    public static synchronized void start() {
        if (started) {
            return;
        }
        started = true;
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tF %1$tT %4$s %3$s: %5$s%6$s%n");
            File dir = com.nkanabo.Tienda.AppPaths.logsDir();
            FileHandler fh = new FileHandler(new File(dir, "tienda-%g.log").getAbsolutePath(),
                    1_000_000, 5, true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.INFO);
            Logger root = Logger.getLogger("");
            root.addHandler(fh);
            Logger.getLogger(Log.class.getName()).info(
                    com.nkanabo.Tienda.AppInfo.NAME + " " + com.nkanabo.Tienda.AppInfo.VERSION + " starting");
        } catch (Exception e) {
            // Logging must never stop the till from opening.
            System.err.println("Could not open log file: " + e);
        }
    }

    /** Record an unexpected error with its stack trace. */
    public static void error(Throwable t) {
        Logger.getLogger(caller()).log(Level.SEVERE, t.getMessage(), t);
    }

    private static String caller() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        return st.length > 3 ? st[3].getClassName() : "Tienda";
    }
}
