import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadFile {
    private static BufferedInputStream bufferedInputStream = null;
    private static FileOutputStream fileOutputStream = null;

    public DownloadFile(String url, String file) throws IOException {
        if (url == null) throw new NullPointerException("Download URL is null");
        bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
        fileOutputStream = new FileOutputStream(file);
        Logger.add("DownloadFile constructor", "Successful");
    }

    public void run() throws IOException {
        final byte data[] = new byte[4096];
        Logger.add("DownloadFile run", "Start download");
        int count;
        while ((count = bufferedInputStream.read(data, 0, 4096)) != -1) {
            fileOutputStream.write(data, 0, count);
            fileOutputStream.flush();
            System.out.println(count);
        }
        Logger.add("DownloadFile run", "Finish download");
    }

    public void close() throws IOException {
        if (bufferedInputStream != null)
            bufferedInputStream.close();
        if (fileOutputStream != null)
            fileOutputStream.close();
        Logger.add("DownloadFile close", "Streams closed");
    }
}
