package com.nkanabo.Tienda;

/**
 * Version and product name in one place. Keep {@link #VERSION} equal to the
 * {@code <version>} in pom.xml (the installer build reads the pom).
 *
 * @author Nkanabo
 */
public final class AppInfo {

    public static final String NAME = "Tienda POS";
    public static final String VERSION = "2.1.0";

    private AppInfo() {
    }

    /**
     * Compare dotted versions numerically ("2.10.0" &gt; "2.9.1").
     *
     * @return negative, zero or positive like {@link Comparable#compareTo}
     */
    public static int compareVersions(String a, String b) {
        String[] x = a.trim().split("\\.");
        String[] y = b.trim().split("\\.");
        for (int i = 0; i < Math.max(x.length, y.length); i++) {
            int p = i < x.length ? parse(x[i]) : 0;
            int q = i < y.length ? parse(y[i]) : 0;
            if (p != q) {
                return Integer.compare(p, q);
            }
        }
        return 0;
    }

    private static int parse(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9].*$", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
