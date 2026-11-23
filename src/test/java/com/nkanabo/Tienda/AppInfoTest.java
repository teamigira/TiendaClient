package com.nkanabo.Tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class AppInfoTest {

    @Test
    void comparesVersionsNumerically() {
        assertTrue(AppInfo.compareVersions("2.10.0", "2.9.1") > 0);
        assertTrue(AppInfo.compareVersions("2.1", "2.1.1") < 0);
        assertEquals(0, AppInfo.compareVersions("2.1.0", "2.1"));
        assertTrue(AppInfo.compareVersions("3.0.0-beta", "2.9") > 0);
    }

    @Test
    void versionMatchesPom() throws Exception {
        String pom = new String(Files.readAllBytes(Paths.get("pom.xml")));
        assertTrue(pom.contains("<version>" + AppInfo.VERSION + "</version>"),
                "AppInfo.VERSION must equal the version in pom.xml");
    }
}
