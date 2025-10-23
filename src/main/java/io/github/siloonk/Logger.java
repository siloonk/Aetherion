package io.github.siloonk;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Logger {

    public static final String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));;


    public static void info(String message) {
        System.out.printf("%s [INFO] %s%n", new Date(), message);
        writeMessage("[INFO]", message);
    }

    public static void error(String message) {
        System.err.printf("%s [ERROR] %s%n", new Date(), message);
        writeMessage("[ERROR]", message);
    }

    private static void writeMessage(String prefix, String message) {
        File file = new File("logs/%s.logs".formatted(currentDate));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try(FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.write(String.format("%s %s %s%n", new Date(), prefix, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
