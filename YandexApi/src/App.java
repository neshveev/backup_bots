import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        MqManager mqManager = new MqManager();
        mqManager.createQuIn("AudioExtractOut");
        mqManager.createQuOut("YandexResponse");
        Bucket bucket = new Bucket();
        Recognition recognition = new Recognition();

//                        recognition.get("aaa", "e0374c0rrobu68hqmd16");



        while (true) {
            String mqMessage = mqManager.consumer();
            System.out.println(mqMessage);
            JSONObject message = new JSONObject(mqMessage);

            String work_id = message.getString("work-id");
            String path = message.getString("path");


            try {
                bucket.uploadFile(path, work_id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = recognition.post(work_id);
//String response = "{ \"done\": false, \"id\": \"e03cb8ma4vnjublmjo99\", \"createdAt\": \"2019-12-06T09:16:26Z\", \"createdBy\": \"ajer2jkti38a0q7o2anr\", \"modifiedAt\": \"2019-12-06T09:16:26Z\"}";
            System.out.println(response);

            String wait = message.getString("wait");
//        String work_id ="05ae8866-13fe-479d-9cf2-1e2d7e920379";
//        String wait = "66";
            try {
                TimeUnit.SECONDS.sleep(Integer.parseInt(wait));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("start");
            Pattern pattern = Pattern.compile("\"id\": \"(.+?)\",");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                System.out.println(matcher.group(1));
                recognition.get(work_id, matcher.group(1));
                message.put("path", "/home/ejommy/Documents/serena1.0/json/" + work_id + ".json");
                System.out.println(message.toString());
                mqManager.producer(message.toString());
            }

            try {
                bucket.deleteFile(work_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
