package ku.cs.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileStorage {
    public static String saveFileToDirectory(File sourceFile, String destinationDirectory) throws IOException {
        Path destinatonPath = Path.of(destinationDirectory);

        // ถ้าไม่มีโฟล์เดอร์อยู่ให้สร้างใหม่
        if (!Files.exists(destinatonPath)) {
            Files.createDirectories(destinatonPath);
        }

        Path filePath = destinatonPath.resolve(sourceFile.getName());
        Files.copy(sourceFile.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING); // copy file ไปไว้ในโฟล์เดอร์ที่ต้องการุถ้ามีไฟล์อยู่แล้วให้วางซ้ำ
        return filePath.toString();
    }
}
