import java.io.IOException;
import java.util.Calendar;

public class Bucket {
    private Runtime runtime;
    private Calendar calendar;

    public Bucket(){
        runtime = Runtime.getRuntime();
        calendar = Calendar.getInstance();
    }

    public void uploadFile(String path, String work_id) throws IOException {
        String command = new StringBuilder().append("s3cmd put ").append(path).append(" s3://serena.audio/")
                .append(work_id).append(".pcm").toString();
        Process process = runtime.exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("[%1$tF %1$tT] [COMMAND: %2$s] [STATUS: %3$d]\n", calendar.getTime(), command, process.exitValue());
    }

    public void deleteFile(String work_id) throws IOException {
        String command = new StringBuilder().append("s3cmd del s3://serena.audio/").append(work_id).append(".pcm")
                .toString();
        Process process = runtime.exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("[%1$tF %1$tT] [COMMAND: %2$s] [STATUS: %3$d]\n", calendar.getTime(), command, process.exitValue());
    }
}
