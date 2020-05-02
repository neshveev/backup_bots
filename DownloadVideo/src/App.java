import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws IOException {
        MqManager mqManager = new MqManager();
        DB db = new DB();
        mqManager.createQuIn("botOut");
        mqManager.createQuOut("downloadOut");
        mqManager.createQuBot("event");
        while (true) {
            String mqMessage = mqManager.consumer();
            JSONObject message = new JSONObject(mqMessage);
            System.out.println(message);
            try {
                ResultSet resultSet = db.select("select * from process_info where work_id = '" + message.getString("work_id") + "';");
                while (resultSet.next()){
                    message.put("url", resultSet.getString("url"));
                    message.put("fileName", resultSet.getString("file_name"));
                }
                db.update("update process_info set event = 'Скачиваем файл' where work_id = '" + message.get("work_id") + "'");
                mqManager.botProducer(message.toString());
                DownloadFile downloadFile = new DownloadFile(LinkMaster.getLink(message), LinkMaster.localFile(message));
                downloadFile.run();
                downloadFile.close();
                db.update("update process_info set event = 'Файл успешно скачан' where work_id = '" + message.get("work_id") + "'");
                mqManager.botProducer(message.toString());
                mqManager.producer(message.toString());
            } catch (JSONException | IOException | SQLException e) {
                try {
                    db.update("update process_info set event = 'Не удалось скачать файл' where work_id = '" + message.get("work_id") + "'");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                mqManager.botProducer(message.toString());
                e.printStackTrace();
            }
        }
    }
}
