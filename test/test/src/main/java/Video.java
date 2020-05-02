import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video {
    public static final Video video = new Video();
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static HashMap<Long, JSONObject> mqMessage = new HashMap<Long, JSONObject>();


    public static SendMessage buildMqMessage(Message message, long chat_id, HashMap<Long, Integer> video){
        if (!mqMessage.containsKey(chat_id) || mqMessage.get(chat_id) == null)
            mqMessage.put(chat_id, new JSONObject());
        if (message.getText().equals("/exit")){
            video.put(chat_id, -1);
            mqMessage.put(chat_id, null);
            return new SendMessage().setChatId(chat_id).setText("Ну как знаешь! Напиши в поддержку, если какие проблемы");
        } else if (message.getText().equals("/run")){
            System.out.println(mqMessage.get(chat_id).toString());
            mqMessage.put(chat_id, null);
            video.put(chat_id, -1);

            return new SendMessage().setChatId(chat_id).setText("Поехали (пока никуда не поехали)");
        }
        switch (video.get(chat_id)){
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

    private static SendMessage addNameFile(Message message, long chat_id, HashMap<Long, Integer> video){
        mqMessage.get(chat_id)
                .put("fileName", message.getText());

        StringBuilder stringBuilder = new StringBuilder()
                .append(time.format(Calendar.getInstance().getTime()))
                .append("\t")
                .append(message.getFrom().getId())
                .append("\t")
                .append(message.getFrom().getUserName())
                .append("\t")
                .append(message.getText());
        System.out.println(stringBuilder.toString());
        video.put(chat_id, 3);
        return new SendMessage().setChatId(chat_id).setText("Все готово! Вот твой запрос:\n\n" + mqMessage.get(chat_id).toString() +"\n\n" +
                "Подтверди /run или отмени /exit");
    }


    private static SendMessage addLink(Message message, long chat_id, HashMap<Long, Integer> video){
        String url = message.getText().trim();
        Pattern pattern = Pattern.compile("yadi\\.sk\\/d\\/[A-Z|a-z|0-9]{14}");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()){
            video.put(chat_id, 2);
            mqMessage.get(chat_id)
                    .put("url", url);

            StringBuilder stringBuilder = new StringBuilder()
                    .append(time.format(Calendar.getInstance().getTime()))
                    .append("\t")
                    .append(message.getFrom().getId())
                    .append("\t")
                    .append(message.getFrom().getUserName())
                    .append("\t")
                    .append(url);
            System.out.println(stringBuilder.toString());
            return new SendMessage().setChatId(chat_id).setText("Окей! Теперь имя файла ИМЯ.ФОРМАТ или жми /exit");
        } else {
            return new SendMessage().setChatId(chat_id).setText("Не похоже это на то, что надо(( еще раз или /exit");
        }

    }

    private static SendMessage checkPassword(Message message, long chat_id, HashMap<Long, Integer> video){
        if (message.getText().equals("рыбынет")){
            video.put(chat_id, 1);
            mqMessage.get(chat_id)
                    .put("userName", message.getFrom().getUserName())
                    .put("chatId", chat_id);
            return new SendMessage().setChatId(chat_id).setText("Пока все ок! Пиши ссылку на яндекс.диск" +
                    " в формате yadi.sk/d/XXXXXXXXXXXXXX или нажмите /exit");
        } else{
            return new SendMessage().setChatId(chat_id).setText("Не-а! Попробуй еще раз или нажми /exit");
        }
    }
}
