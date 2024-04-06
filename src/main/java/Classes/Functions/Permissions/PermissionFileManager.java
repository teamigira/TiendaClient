package Classes.Functions.Permissions;

import Classes.Utilities.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PermissionFileManager {

    private static final String JSON_FILE_PATH = "resources/application.properties.json";
    private static final JsonParser JSON_PARSER = new JsonParser();

    public static List<String> loadPermissions() {
        try {
            // Load the file from the resources
            Resources rs = new Resources();
            File is = rs.getFileFromResource(JSON_FILE_PATH);

            // Get the absolute file path
            String filepath = is.getAbsolutePath();

            // Use the file path as needed
            // For example:
            System.out.println("File path: " + filepath);

            // Read JSON data from file
            JsonObject jsonObject = readJsonDataFromFile(filepath);

            // Parse JSON into Permissions object
            Gson gson = new Gson();
            Permissions permissionsObject = gson.fromJson(jsonObject, Permissions.class);
            return permissionsObject.getPermissions();
        } catch (URISyntaxException e) {
            // Handle any exceptions related to URI syntax
            e.printStackTrace();
        } catch (IOException e) {
            // Handle any IO exceptions
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonParseException e) {
            // Handle JSON parsing exceptions
            System.err.println("Error parsing JSON data: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static JsonObject readJsonDataFromFile(String filepath) throws IOException {
        try ( BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            return JSON_PARSER.parse(jsonData.toString()).getAsJsonObject();
        }
    }

    public static void main(String[] args) {
        // Load permissions from JSON file
        List<String> permissions = PermissionFileManager.loadPermissions();

        // Check if permissions are loaded successfully
        if (permissions != null) {
            System.out.println("List of permissions:");
            for (String permission : permissions) {
                System.out.println(permission);
            }
        } else {
            System.out.println("Failed to load permissions.");
        }
    }
}
