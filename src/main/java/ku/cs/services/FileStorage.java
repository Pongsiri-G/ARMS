package ku.cs.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileStorage {
    public static String saveFileToDirectory(File sourceFile, String destinationPath) throws IOException {
        Files.copy(sourceFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        return destinationPath; // Return the new file path
    }

    // Method to replace the file while updating the timestamp
    public static String replaceFileWithTimestamp(File newFile, String existingFilePath) throws IOException {
        // Extract the directory from the existing file path
        Path existingPath = Path.of(existingFilePath);
        Path parentDirectory = existingPath.getParent();

        // If the parent directory does not exist, create it
        if (!Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }

        // Extract the original file name without the old timestamp
        String originalFileName = extractOriginalFileName(existingPath.getFileName().toString());

        // Generate the new timestamp in yyyy-MM-dd format
        String newTimestamp = getCurrentTimestamp();

        // Build the new file name with the new timestamp (e.g., "original_file-yyyy-MM-dd.png")
        String newFileName = originalFileName + "_" + newTimestamp + ".png";  // Change this line for format 2

        // Construct the file path for the new file
        Path newFilePath = parentDirectory.resolve(newFileName);

        // Copy the new file to the destination, replacing the old file if necessary
        Files.copy(newFile.toPath(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilePath.toString();
    }

    // Helper method to get the current timestamp in the desired format (yyyy-MM-dd)
    private static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    // Helper method to extract the original file name before the timestamp
    private static String extractOriginalFileName(String fileName) {
        // Remove the file extension (e.g., ".png")
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));

        // Find the last dash before the old timestamp
        int lastDashIndex = nameWithoutExtension.lastIndexOf('-');

        // If a dash is found, assume the part after it is the old timestamp and remove it
        if (lastDashIndex != -1) {
            return nameWithoutExtension.substring(0, lastDashIndex);
        }

        // If no dash is found, return the name without the extension as is
        return nameWithoutExtension;
    }

}
