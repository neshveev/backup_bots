import org.json.JSONObject;

public class App {
    public static void main(String[] args) {
        MqManager mqManager = new MqManager();
        mqManager.createQuIn("YandexResponse");
        mqManager.createQuOut("Text");

        while (true) {
            String mqMessage = mqManager.consumer();
            System.out.println(mqMessage);
            JSONObject message = new JSONObject(mqMessage);

            String work_id = message.getString("work-id");
            String path = message.getString("path");
            String fileName = message.getString("fileName");

            System.out.println(work_id);
            System.out.println(path);

//        String path = "/home/ejommy/Documents/serena1.0/json/05ae8866-13fe-479d-9cf2-1e2d7e920379.json";
//        String work_id = "05ae8866-13fe-479d-9cf2-1e2d7e920379";

            JsonToTxt jsonToTxt = new JsonToTxt(fileName);
            jsonToTxt.start(path);
        }
    }
}
