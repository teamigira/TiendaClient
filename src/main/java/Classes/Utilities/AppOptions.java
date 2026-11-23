package Classes.Utilities;

import java.util.prefs.Preferences;

/**
 * Small shared application options, backed by {@link Preferences} so they
 * persist across runs without a DB table. Written by the "Options" tab
 * ({@code Interface.Products.OptionsDialog}) and read wherever needed
 * (e.g. the low-stock warning threshold).
 *
 * @author Nkanabo
 */
public final class AppOptions {

    private static final Preferences PREFS =
            Preferences.userRoot().node("TiendaOptions");

    private static final String KEY_LOW_STOCK = "lowStockThreshold";
    private static final String KEY_CURRENCY = "currencySymbol";
    private static final String KEY_SEC_CURRENCY = "secondaryCurrency";
    private static final String KEY_SEC_RATE = "secondaryRate";

    public static final double DEFAULT_LOW_STOCK = 5;
    public static final String DEFAULT_CURRENCY = "TZS";

    private AppOptions() {
    }

    // --- Multi-currency (Premium) ------------------------------------

    /** Secondary currency code to also show on receipts, or "" when disabled. */
    public static String getSecondaryCurrency() {
        return PREFS.get(KEY_SEC_CURRENCY, "");
    }

    public static void setSecondaryCurrency(String v) {
        PREFS.put(KEY_SEC_CURRENCY, v == null ? "" : v.trim());
    }

    /** Units of secondary currency per 1 unit of the base currency. */
    public static double getSecondaryRate() {
        return PREFS.getDouble(KEY_SEC_RATE, 0);
    }

    public static void setSecondaryRate(double v) {
        PREFS.putDouble(KEY_SEC_RATE, v);
    }

    /** True when a secondary currency and a positive rate are configured. */
    public static boolean hasSecondaryCurrency() {
        return !getSecondaryCurrency().isEmpty() && getSecondaryRate() > 0;
    }

    public static double getLowStockThreshold() {
        return PREFS.getDouble(KEY_LOW_STOCK, DEFAULT_LOW_STOCK);
    }

    public static void setLowStockThreshold(double v) {
        PREFS.putDouble(KEY_LOW_STOCK, v);
    }

    /**
     * A second folder that each daily backup is also copied to: a USB drive,
     * or a folder synced to the cloud (OneDrive, Google Drive, Dropbox).
     * Blank = none.
     */
    public static String getExtraBackupDir() {
        return PREFS.get("extraBackupDir", "");
    }

    public static void setExtraBackupDir(String v) {
        PREFS.put("extraBackupDir", v == null ? "" : v.trim());
    }

    public static String getCurrencySymbol() {
        return PREFS.get(KEY_CURRENCY, DEFAULT_CURRENCY);
    }

    public static void setCurrencySymbol(String v) {
        PREFS.put(KEY_CURRENCY, v == null ? DEFAULT_CURRENCY : v);
    }
}
