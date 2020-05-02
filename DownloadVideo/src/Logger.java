import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static String path;
//    private static String pathSupport;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS] ");
    private static SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = new Logger();

    private static String getConfige() throws IOException {
        return new String(Files.readAllBytes(Paths.get("/home/ejommy/Documents/serena1.0/config.json")));
    }

    public static void selectPath(String type) throws IOException {
        JSONObject config = new JSONObject(getConfige());
        JSONObject logger = (JSONObject) config.get("logger");
//        pathSupport = logger.getString("support");
        JSONObject currentType = (JSONObject) logger.get(type);
        path = (String) currentType.get("path");
        System.out.println(simpleDateFormat.format(Calendar.getInstance().getTime()) + "LogPath: " + path);
    }

    public static void add(String location, String message) {
        try {
            FileWriter fileWriter = new FileWriter(path + data.format(Calendar.getInstance().getTime()) + ".txt", true);
            fileWriter.append(simpleDateFormat.format(Calendar.getInstance().getTime()));
            fileWriter.append("[" + location + "] ");
            fileWriter.append("[" + message + "]\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void support(String message) {
//        try {
//            FileWriter fileWriter = new FileWriter(pathSupport + data.format(Calendar.getInstance().getTime()) + ".txt", true);
//            fileWriter.append(simpleDateFormat.format(Calendar.getInstance().getTime()));
//            fileWriter.append(message + "\n");
//            fileWriter.flush();
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
