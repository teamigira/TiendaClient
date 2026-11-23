package com.nkanabo.Tienda;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * Checks once per start whether a newer version is available.
 *
 * <p>The address comes from {@code update.url} in
 * {@code resources/application.properties}; blank turns the check off. It must
 * be an https URL returning JSON like:
 * <pre>
 *   {"version": "2.2.0",
 *    "url": "https://example.com/downloads/TiendaPOS-2.2.0.msi",
 *    "notes": "Faster reports, Kiswahili fixes"}
 * </pre>
 * Nothing about the shop or its sales is sent; only the app version is in the
 * User-Agent header.
 *
 * @author Nkanabo
 */
public final class UpdateChecker {

    /** A newer release. */
    public static final class Release {
        public final String version;
        public final String url;
        public final String notes;

        Release(String version, String url, String notes) {
            this.version = version;
            this.url = url;
            this.notes = notes;
        }
    }

    private UpdateChecker() {
    }

    /** Check in the background; {@code onNewer} runs (off the UI thread) only if a newer version exists. */
    public static void checkInBackground(Consumer<Release> onNewer) {
        Thread t = new Thread(() -> {
            Release r = check();
            if (r != null) {
                onNewer.accept(r);
            }
        }, "update-check");
        t.setDaemon(true);
        t.start();
    }

    /** @return the newer release, or {@code null} (none, disabled, or offline) */
    public static Release check() {
        String url = configuredUrl();
        if (url.isEmpty() || !url.startsWith("https://")) {
            return null;
        }
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL).build();
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(8))
                    .header("User-Agent", "TiendaPOS/" + AppInfo.VERSION).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                return null;
            }
            JSONObject j = new JSONObject(res.body());
            String latest = j.optString("version", "");
            String link = j.optString("url", "");
            if (latest.isEmpty() || !link.startsWith("https://")
                    || AppInfo.compareVersions(latest, AppInfo.VERSION) <= 0) {
                return null;
            }
            return new Release(latest, link, j.optString("notes", ""));
        } catch (Exception e) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.FINE, "Update check failed", e);
            return null;
        }
    }

    private static String configuredUrl() {
        try (InputStream in = UpdateChecker.class.getClassLoader()
                .getResourceAsStream("resources/application.properties")) {
            if (in == null) {
                return "";
            }
            Properties p = new Properties();
            p.load(in);
            return p.getProperty("update.url", "").replace("\"", "").trim();
        } catch (Exception e) {
            return "";
        }
    }
}
