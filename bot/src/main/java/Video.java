import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video {
    private static MqManager mqManager = MqManager.getMqManager();
    public static final Video video = new Video();
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static HashMap<Long, JSONObject> mqMessage = new HashMap<Long, JSONObject>();
//    private static String password;

//    private static String getConfige() throws IOException {
//        return new String(Files.readAllBytes(Paths.get("/home/ejommy/Documents/serena1.0/config.json")));
//    }
//
//    private String getPassword() {
//        JSONObject config = null;
//        try {
//            config = new JSONObject(getConfige());
//            JSONObject core = (JSONObject) config.get("core");
//            Logger.add("Video", "PW set");
//            return (String) core.get("pw");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("!!!!PW didn't get!!!!");
//        return null;
//    }

    private Video() {
        mqManager.createQuOut("VideoDownloadIn");
//        password = getPassword();
    }

    public static SendMessage buildMqMessage(Message message, long chat_id, HashMap<Long, Integer> video) {
        if (!mqMessage.containsKey(chat_id) || mqMessage.get(chat_id) == null)
            mqMessage.put(chat_id, new JSONObject());
        if (message.getText().equals("/exit")) {
            video.put(chat_id, -1);
            mqMessage.put(chat_id, null);
            return new SendMessage().setChatId(chat_id).setText("Ну как знаешь! Напиши в поддержку, если какие проблемы");
        } else if (message.getText().equals("/run")) {
            mqMessage.get(chat_id).put("work-id", UUID.randomUUID().toString())
                    .put("userName", message.getFrom().getUserName())
                    .put("chatId", chat_id);
            System.out.println(mqMessage.get(chat_id).toString());
            Logger.add("Video", mqMessage.get(chat_id).toString());
            mqManager.producer(mqMessage.get(chat_id).toString());
            mqMessage.put(chat_id, null);
            video.put(chat_id, -1);

            return new SendMessage().setChatId(chat_id).setText("Поехали!");
        }
        switch (video.get(chat_id)) {
            case (0):
                return checkPassword(message, chat_id, video);
            case (1):
                return addLink(message, chat_id, video);
            case (2):
                return addNameFile(message, chat_id, video);
            case (3):
                return new SendMessage().setChatId(chat_id).setText("/run или /exit");
            default:
                return new SendMessage().setChatId(chat_id).setText("Пока все норм");
        }
    }

    private static SendMessage addNameFile(Message message, long chat_id, HashMap<Long, Integer> video) {
        mqMessage.get(chat_id)
                .put("fileName", message.getText());
        video.put(chat_id, 3);
        return new SendMessage().setChatId(chat_id).setText("Все готово! Вот твой запрос:\n\n" + mqMessage.get(chat_id).toString() + "\n\n" +
                "Подтверди /run или отмени /exit");
    }

    private static boolean checkLink(String url) {
        Pattern yandex = Pattern.compile("https:\\/\\/yadi\\.sk\\/[d|i]\\/.{14}");
        Pattern fex = Pattern.compile("https:\\/\\/fex\\.net\\/s\\/.{7}");
        Matcher matcher = yandex.matcher(url);
        if (matcher.find()) return true;
        else {
            matcher = fex.matcher(url);
            if (matcher.find()) return true;
        }
        return false;
    }

    private static SendMessage addLink(Message message, long chat_id, HashMap<Long, Integer> video) {
        String url = message.getText().trim();
        if (checkLink(url)) {
            mqMessage.get(chat_id).put("url", url);
            video.put(chat_id, 2);
            return new SendMessage().setChatId(chat_id).setText("Окей! Теперь имя файла ИМЯ.ФОРМАТ или жми /exit");
        } else {
            return new SendMessage().setChatId(chat_id).setText("Не похоже это на то, что надо(( еще раз или /exit");
        }
    }

    private static SendMessage checkPassword(Message message, long chat_id, HashMap<Long, Integer> video) {
        if (message.getText().equals(Configure.PASSWORD)) {
            video.put(chat_id, 1);
            return new SendMessage().setChatId(chat_id).setText("Пока все ок! Пиши ссылку файловое хранилище или нажмите /exit");
        } else {
            return new SendMessage().setChatId(chat_id).setText("Не-а! Попробуй еще раз или нажми /exit");
        }
    }
}
