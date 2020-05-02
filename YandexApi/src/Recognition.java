import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Recognition {
    private String api_key = "AQVN2IDAvhRxKXmQdtBXiVnHqHHORnP_EVDe3rJq";

    public Recognition() {
    }

    public String post(String work_id) {
        String url = "https://transcribe.api.cloud.yandex.net/speech/stt/v2/longRunningRecognize";
        String body = "{\n" +
                "    \"config\": {\n" +
                "        \"specification\": {\n" +
                "            \"languageCode\": \"ru-RU\",\n" +
                "            \"audioEncoding\": \"LINEAR16_PCM\",\n" +
                "            \"sampleRateHertz\": 48000,\n" +
                "            \"audioChannelCount\": 1\n" +
                "        }\n" +
                "    },\n" +
                "    \"audio\": {\n" +
                "        \"uri\": \"https://storage.yandexcloud.net/serena.audio/" +  work_id +".pcm\"\n" +
                "    }\n" +
                "}";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Api-Key " + api_key);
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(body.getBytes());
            outputStream.flush();
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void get(String work_id, String id){
        String url = "https://operation.api.cloud.yandex.net/operations/" + id;
        while (true) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Api-Key " + api_key);
                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                FileWriter fileWriter = new FileWriter("/home/ejommy/Documents/serena1.0/json/" + work_id + ".json", false);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                System.out.println(jsonObject.toString());
                if (jsonObject.getBoolean("done")) {
                    fileWriter.write(stringBuilder.toString());
                    fileWriter.flush();
                    fileWriter.close();
                    break;
                } else {
                    TimeUnit.SECONDS.sleep(5);
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
