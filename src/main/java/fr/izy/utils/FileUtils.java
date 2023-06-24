package fr.izy.utils;

import java.io.*;

public class FileUtils {

    public static void createFile(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public static void save(File file, String text) {
        final FileWriter fw;
        try {
            createFile(file);
            fw = new FileWriter(file);
            fw.write(text);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println("File cannot be created.");
        }
    }

    public static String loadContent(File file) {
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
                reader.close();
                return text.toString();
            } catch (IOException e) {
                System.out.println("A file doesn't not exist.");
            }
        }
        return "";
    }
}
