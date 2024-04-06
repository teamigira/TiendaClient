import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AutoUpdater {
    public static void main(String[] args) {
        // URL of the latest executable file
        String updateUrl = "http://example.com/latest_version.exe";
        
        // Local path where the executable will be downloaded and replaced
        String localFilePath = "path/to/local/exe_file.exe";
        
        // Download the updated executable file
        downloadFile(updateUrl, localFilePath);
    }
    
    public static void downloadFile(String fileUrl, String localFilePath) {
        try {
            // Open a connection to the file URL
            URL url = new URL(fileUrl);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            
            // Create a temporary file to download the updated executable
            Path tempFile = Files.createTempFile("temp_exe_", ".exe");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile());
            
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            
            // Read from the input stream and write to the temporary file
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            
            // Close streams
            in.close();
            fileOutputStream.close();
            
            // Success message
            System.out.println("File downloaded successfully.");
            
            // Replace the existing local executable file with the downloaded one
            replaceExecutable(tempFile, localFilePath);
            
        } catch (IOException e) {
            // Error handling
            System.err.println("Error downloading file: " + e.getMessage());
        }
    }
    
    public static void replaceExecutable(Path tempFile, String localFilePath) {
        try {
            // Check if the current OS is Windows
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                // Replace the existing local executable file with the downloaded one
                Files.copy(tempFile, Path.of(localFilePath), StandardCopyOption.REPLACE_EXISTING);
                
                // Delete the temporary file
                Files.deleteIfExists(tempFile);
                
                System.out.println("File replaced successfully.");
            } else {
                System.err.println("Unsupported operating system for automatic update.");
            }
        } catch (IOException e) {
            // Error handling
            System.err.println("Error replacing file: " + e.getMessage());
        }
    }
}
  