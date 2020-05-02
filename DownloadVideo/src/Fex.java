import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fex {
    private static final String API = "https://api.fex.net/api/v1/";
    private static final String ANONYMOUS = "config/anonymous";
    private static final String LIST = "https://api.fex.net/api/v1/file/share/";
    private static final Fex fex = new Fex();

    private Fex() {
    }

    public static String getDownloadUrl(String url, String fileName) throws IOException {
        String token = getToken();
        if (token == null) return null;
        System.out.println("getUrl");

        String folder;
        Pattern pattern = Pattern.compile("fex.net\\/.+s\\/(.+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            folder = matcher.group(1);
        } else return null;
        Logger.add("Fex getDownloadUrl", folder);
        HttpURLConnection connection = (HttpURLConnection) new URL(LIST + folder).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode = connection.getResponseCode();
        Logger.add("Fex getDownloadUrl", "Response code: " + responseCode);
        if (responseCode > 299) return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) stringBuilder.append(line);
        JSONObject response = new JSONObject(stringBuilder.toString());
        JSONObject tree = (JSONObject) response.get("tree");
        JSONArray files = (JSONArray) tree.get("children");
        for (int i = 0; i < files.length(); ++i) {
            JSONObject file = (JSONObject) files.get(i);
            if (file.getString("name").equals(fileName)) {
                Logger.add("Fex getDownloadUrl", file.getString("download_url"));
                System.out.println(file.getString("download_url"));
                return file.getString("download_url");
            }
        }
        Logger.add("Fex getDownloadUrl", "!!!!DownloadUrl is null!!!!");
        return null;
    }

    private static String getToken() throws IOException {
        Logger.add("Fex get token", "Start");
        HttpURLConnection connection = (HttpURLConnection) new URL(API + ANONYMOUS).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        Logger.add("Fex get token", "Response code: " + responseCode);
        if (responseCode > 299) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        JSONObject response = new JSONObject(stringBuilder.toString());
        JSONObject anonymous = (JSONObject) response.get("anonymous");
        String token = anonymous.getString("anonym_token");
        Logger.add("Fex get token", token);
        return token;
    }
}
