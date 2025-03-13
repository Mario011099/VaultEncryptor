package com.datasec.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.logging.Logger;

public class FileUtils {

    static Logger logger = Logger.getLogger(FileUtils.class.getName());

    private FileUtils() {
    }

    public static void saveStringToFile(String content, String filePath) {
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            logger.log(Level.INFO,"Content saved in {0}", filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Failed to save file: {0}", e.getMessage());
        }
    }

    public static List<String> readStringFromFile(String filePath) {
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            return lines.toList();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Failed to read file: {0} ", e.getMessage());
            return Collections.emptyList();
        }
    }
}
