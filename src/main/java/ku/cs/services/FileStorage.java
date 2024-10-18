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
        return destinationPath; 
    }

    
    public static String replaceFileWithTimestamp(File newFile, String existingFilePath) throws IOException {
        
        Path existingPath = Path.of(existingFilePath);
        Path parentDirectory = existingPath.getParent();

        
        if (!Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }

        
        String originalFileName = extractOriginalFileName(existingPath.getFileName().toString());

        
        String newTimestamp = getCurrentTimestamp();

        
        String newFileName = originalFileName + "_" + newTimestamp + ".png";  

        
        Path newFilePath = parentDirectory.resolve(newFileName);

        
        Files.copy(newFile.toPath(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        return newFilePath.toString();
    }

    
    private static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    
    private static String extractOriginalFileName(String fileName) {
        
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));

        
        int lastDashIndex = nameWithoutExtension.lastIndexOf('-');

        
        if (lastDashIndex != -1) {
            return nameWithoutExtension.substring(0, lastDashIndex);
        }

        
        return nameWithoutExtension;
    }

}
