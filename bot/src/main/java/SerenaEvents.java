import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.File;
import java.sql.ResultSet;

public class SerenaEvents extends Thread {
    MqManager mqManager = MqManager.getMqManager();
    Bot bot;
    DB db;

    public SerenaEvents(Bot bot) {
        mqManager.createQuIn("event");
        this.bot = bot;
        db = new DB();
    }

    @Override
    public void run() {
        while (true) {
            try {
                JSONObject json = new JSONObject(mqManager.consumer());
                ResultSet resultSet = db.select("select * from process_info where work_id = '" + json.getString("work_id") + "';");
                while (resultSet.next()){
                    json.put("chat_id", resultSet.getLong("chat_id"));
                    json.put("file_name", resultSet.getString("file_name"));
                    json.put("event", resultSet.getString("event"));
                    json.put("path", resultSet.getString("result"));
                }
                long chat_id = json.getLong("chat_id");
                String event = json.getString("event");
                String fileName = json.getString("file_name");
                if (event.equals("Done")) {
                    String path = json.getString("path");
                    SendMessage sendMessage = new SendMessage().setChatId(chat_id).setText("Расшифровка готова." +
                            " Мы будем рады обратной связи. Особенно, если вышло плохо! Мы очень хотим стать лучше.");
                    SendDocument sendDocument = new SendDocument().setChatId(chat_id).setDocument(new File(path));
                    bot.execute(sendDocument);
                    bot.execute(sendMessage);
                } else {
                    SendMessage sendMessage = new SendMessage().setChatId(chat_id).setText(fileName + " : " + event);
                    bot.execute(sendMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
