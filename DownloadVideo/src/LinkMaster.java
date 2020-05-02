import org.json.JSONObject;

import java.io.IOException;

public class LinkMaster {
    private static final LinkMaster linkMaster = new LinkMaster();

    private LinkMaster() {
    }

    public static String getLink(JSONObject mqMessage) throws IOException {
        String url = mqMessage.getString("url");
        String fileName = mqMessage.getString("fileName");
        if (url.startsWith("https://yadi.sk")){
            Logger.add("LinkMaster getLink", "Request yandex' link");
            return Yandex.getDownloadUrl(url, fileName);
        }
        else if (url.startsWith("https://fex.net")) {
            Logger.add("LinkMaster getLink", "Request fex' link");
            return Fex.getDownloadUrl(url, fileName);
        }
        Logger.add("LinkMaster getLink", "!!!!Link is null!!!!");
        return null;
    }

    public static String localFile(JSONObject mqMessage) {
        String work_id = mqMessage.getString("work_id");
        String fileName = mqMessage.getString("fileName");
        String format = fileName.substring(fileName.indexOf("."));
        String file = "/home/ejommy/Documents/serena1.0/video/" + work_id + format;
        Logger.add("LinkMaster local file", file);
        mqMessage.put("path", file);
        return file;
    }
}
