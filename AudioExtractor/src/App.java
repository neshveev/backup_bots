import org.json.JSONObject;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args) {
        MqManager mqManager = new MqManager();
        mqManager.createQuIn("AudioExtractIn");
        mqManager.createQuOut("AudioExtractOut");
        while (true) {
            String mqMessage = mqManager.consumer();
            JSONObject message = new JSONObject(mqMessage);
            try {
                String work_id = message.getString("work-id");
                String path = message.getString("path");
                Process process = runtime.exec(getCommand(path, work_id));
                process.waitFor();
                mqManager.producer(buildMessage(message, process.exitValue()));
            } catch (JSONException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getDuration(String path) {
        try {
            Process process = runtime.exec("ffmpeg -i " + path);
            process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("  Duration:")) {
                    Pattern pattern = Pattern.compile("Duration: (.+?),");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String[] time = matcher.group(1).split(":");
                        int hh = Integer.parseInt(time[0]);
                        int mm = Integer.parseInt(time[1]);
                        int ss = Integer.parseInt(time[2].split("\\.")[0]);
                        String duration = String.valueOf(hh * 3600 + mm * 60 + ss);
                        return duration;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCommand(String path, String work_id) {
        String command = new StringBuilder()
                .append("ffmpeg -i ")
                .append(path)
                .append(" -f s16le")
                .append(" -acodec pcm_s16le")
                .append(" -ac 1")
                .append(" -ar 48000")
                .append(" /home/ejommy/Documents/serena1.0/audio/")
                .append(work_id)
                .append(".pcm").toString();
        return command;
    }

    private static String getWaitTime(String time) {
        int seconds = Integer.parseInt(time);
        return String.valueOf(seconds / 5);
    }

    private static String buildMessage(JSONObject message, int state) {
        String time = getDuration(message.getString("path"));
        message.put("path", "/home/ejommy/Documents/serena1.0/audio/" + message.getString("work-id") + ".pcm");
        message.put("duration", time);
        message.put("wait", getWaitTime(time));
        message.put("stateAudio", String.valueOf(state));
        System.out.println(message.toString());
        return message.toString();
    }
}