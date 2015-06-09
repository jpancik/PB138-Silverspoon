package cz.muni.pb138.silverspoon.visualizer.gui;

import java.io.*;
import java.util.Scanner;

/**
 * Utility class for working with files.
 *
 * @author juraj@pancik.com
 */
public final class FileUtils {
    private FileUtils() {
    }

    public static String loadFileIntoString(InputStream input) throws FileNotFoundException {
        StringBuilder builder = new StringBuilder("");
        try (Scanner scanner = new Scanner(input)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                builder.append(line).append("\n");
            }

            scanner.close();

        }
        return builder.toString();
    }

    public static void writeStringIntoFile(File file, String data) throws IOException {
        try(Writer writer = new PrintWriter(file)) {
            writer.write(data);
        }
    }
}
