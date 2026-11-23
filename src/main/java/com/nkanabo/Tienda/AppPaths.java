package com.nkanabo.Tienda;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Where Tienda keeps its data. One place, so the database, backups and
 * generated files always agree.
 *
 * <ul>
 *   <li>Windows: {@code %LOCALAPPDATA%\Tienda}</li>
 *   <li>macOS: {@code ~/Library/Application Support/Tienda}</li>
 *   <li>Linux: {@code $XDG_DATA_HOME/tienda} or {@code ~/.local/share/tienda}</li>
 * </ul>
 * Set the system property {@code tienda.data} or the environment variable
 * {@code TIENDA_DATA} to use another folder (e.g. a second till's test copy).
 *
 * <p>Layout: {@code db/Tienda.mv.db}, {@code backups/}, {@code resources/}
 * (barcodes and other generated files), {@code logs/}.
 *
 * @author Nkanabo
 */
public final class AppPaths {

    public static final String DB_NAME = "Tienda";
    private static File dataDir;

    private AppPaths() {
    }

    /** The data folder (created if missing). */
    public static synchronized File dataDir() {
        if (dataDir == null) {
            dataDir = resolveDataDir();
            dataDir.mkdirs();
        }
        return dataDir;
    }

    private static File resolveDataDir() {
        String override = firstNonBlank(System.getProperty("tienda.data"), System.getenv("TIENDA_DATA"));
        if (override != null) {
            return new File(override);
        }
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String local = firstNonBlank(System.getenv("LOCALAPPDATA"), home + "\\AppData\\Local");
            return new File(local, "Tienda");
        }
        if (os.contains("mac")) {
            return new File(home, "Library/Application Support/Tienda");
        }
        String xdg = firstNonBlank(System.getenv("XDG_DATA_HOME"), home + "/.local/share");
        return new File(xdg, "tienda");
    }

    public static File dbDir() {
        return mk(new File(dataDir(), "db"));
    }

    /** Database path without the {@code .mv.db} extension, as H2 expects. */
    public static String dbBase() {
        return new File(dbDir(), DB_NAME).getAbsolutePath();
    }

    public static String jdbcUrl() {
        return "jdbc:h2:" + dbBase() + ";DATABASE_TO_UPPER=false";
    }

    public static File backupsDir() {
        return mk(new File(dataDir(), "backups"));
    }

    public static File logsDir() {
        return mk(new File(dataDir(), "logs"));
    }

    /**
     * Copy data from the places older versions used ({@code ~/Tienda.mv.db},
     * {@code ~/TiendaBackups}, {@code ~/Tienda/resources}) on first start.
     * The old files are left where they are, so nothing is lost if anything
     * goes wrong; they can be deleted once the new version is working.
     */
    public static void migrateLegacyData() {
        String home = System.getProperty("user.home");
        File newDb = new File(dbDir(), DB_NAME + ".mv.db");
        if (!newDb.exists()) {
            File old = new File(home, "Tienda.mv.db");
            if (!old.isFile()) {
                old = new File(new File(home, "Tienda"), "Tienda.mv.db");
            }
            if (old.isFile()) {
                copy(old.toPath(), newDb.toPath());
                log("Copied database from " + old + " to " + newDb);
            }
        }
        copyTree(new File(home, "TiendaBackups").toPath(), backupsDir().toPath());
        copyTree(new File(new File(home, "Tienda"), "resources").toPath(),
                new File(dataDir(), "resources").toPath());
    }

    private static void copyTree(Path from, Path to) {
        if (!Files.isDirectory(from)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(from)) {
            walk.forEach(p -> {
                Path target = to.resolve(from.relativize(p).toString());
                if (Files.isDirectory(p)) {
                    target.toFile().mkdirs();
                } else if (!Files.exists(target)) {
                    copy(p, target);
                }
            });
        } catch (IOException e) {
            Logger.getLogger(AppPaths.class.getName()).log(Level.WARNING, "Could not copy " + from, e);
        }
    }

    private static void copy(Path from, Path to) {
        try {
            to.getParent().toFile().mkdirs();
            Files.copy(from, to, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            Logger.getLogger(AppPaths.class.getName()).log(Level.WARNING, "Could not copy " + from, e);
        }
    }

    private static File mk(File f) {
        f.mkdirs();
        return f;
    }

    private static void log(String msg) {
        Logger.getLogger(AppPaths.class.getName()).info(msg);
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }
}
