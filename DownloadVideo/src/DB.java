//
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;

public class DB {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "%?Li0tr0pi@n";
    private static Connection connection = null;


    DB(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public void insert(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public void update(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

//    public void logUpdate(Update update) throws SQLException {
//        insert("insert into message_log values(" +
//                "current_timestamp, '" + update.getMessage().getFrom().getUserName() +
//                "', " + update.getMessage().getChatId() + ",'" + update.getMessage().getText() + "');");
//    }
//
//    public void support(Message message) throws SQLException {
//        insert("insert into support_log values(" +
//                "current_timestamp, '" + message.getFrom().getUserName() +
//                "', " + message.getChatId() + ",'" + message.getText() + "');");
//    }

    public static void main(String[] args) throws SQLException {
        int chat_id = 1239;
        DB db = new DB();
        ResultSet resultSet = db.select("select * from processing_status");
        while (resultSet.next()){
            System.out.println(resultSet.getInt("chat_id"));
            if (resultSet.getInt("chat_id") == chat_id){
                db.update("update processing_status set last_update = current_timestamp(1) where chat_id = " + chat_id);
                System.out.println("ura");
                return;
            }
        }
        db.insert("insert into processing_status values (" + chat_id + ", false, false, 0, current_timestamp(1))");



    }
}
