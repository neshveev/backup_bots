import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processing {
    private static ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private static DB db = new DB();
    private static SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
    private static Processing processing = new Processing();


    public static SendMessage sendMessage(Update update) throws SQLException {
        db.logUpdate(update);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        sendMessage.setText(getAnswer(update.getMessage()));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    private static String getAnswer(Message message) throws SQLException {
        String question = message.getText();

        if (question.equals("/start"))
            return registration(message);
        if (question.equals("Меню"))
            return menu();
        else if (question.equals("О нас"))
            return about();
        else if (question.equals("Инструкция"))
            return manual();
        else if (question.equals("Поддержка"))
            return support(message);
        else if (question.equals("Расшифровать видео"))
            return video(message, 0);
        else if (question.equals("Отмена"))
            return cancel(message);

        ResultSet resultSet = db.select("select * from processing_status where chat_id = " + message.getChatId());
        while (resultSet.next()) {
            if (resultSet.getBoolean("is_support")){
                db.support(message);
                return appeal(message);
            }
            if (resultSet.getBoolean("is_able"))
                return video(message, resultSet.getInt("process_state"));
        }
        return "Не понимаю";
    }

    private static String video(Message message, int process_state) throws SQLException {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();

        if (process_state == 0) {
            row1.add("Отмена");
            keyboard.add(row1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            long chat_id = message.getChatId();
            String work_id = UUID.randomUUID().toString();
            ResultSet resultSet = db.select("select chat_id from processing_status");
            while (resultSet.next()) {
                if (chat_id == resultSet.getLong("chat_id")) {
                    db.update("update processing_status set last_update = current_timestamp(1)," +
                            " is_able = true, process_state = 1," +
                            "work_id = '" + work_id + "' where chat_id = " + chat_id);
                    return "Введите пароль. Для того, чтобы прервать оформление заявки, нажмите \"Отмена\".";
                }
            }
            db.insert("insert into processing_status values (" + chat_id + ", true, false, 1, '" + work_id + "', current_timestamp(1))");
            return "Введите пароль. Для того, чтобы прервать оформление заявки, нажмите \"Отмена\".";
        }

        if (process_state == 1) {
            row1.add("Отмена");
            keyboard.add(row1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            long chat_id = message.getChatId();
            if (Configure.PASSWORD.equals(message.getText())) {
                db.update("update processing_status set last_update = current_timestamp(1)," +
                        " process_state = 2 where chat_id = " + chat_id);
                return "Введите ссылку на видео. Для того, чтобы прервать оформление заявки, нажмите \"Отмена\".";
            } else {
                return "Пароль не верный( Попробуйте еще раз";
            }

        } else if (process_state == 2) {
            row1.add("Отмена");
            keyboard.add(row1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            long chat_id = message.getChatId();
            ResultSet resultSet = db.select("select work_id from processing_status where chat_id = " + chat_id);
            resultSet.next();
            String work_id = resultSet.getString("work_id");
            String url = message.getText();
            Pattern fex = Pattern.compile("https:\\/\\/fex\\.net\\/.+s\\/.{7}");
            Matcher matcher = fex.matcher(url);
            if (matcher.find()) {
                db.update("update processing_status set last_update = current_timestamp(1), process_state = 3 where chat_id = " + chat_id);
                db.insert("insert into process_info values ('" + work_id + "', " + chat_id + ", '" + url +
                        "', null,  null, 'Application-url', null, current_timestamp(1))");
                return "Введите имя файла [имя_файла.формат - например, film.avi].";
            } else {
                return "Не похоже на то что надо( Попробуйте еще раз";
            }
        } else if (process_state == 3) {
            row1.add("Отмена");
            KeyboardRow row2 = new KeyboardRow();
            row2.add("Подтвердить");
            keyboard.add(row2);
            keyboard.add(row1);
            replyKeyboardMarkup.setKeyboard(keyboard);
            long chat_id = message.getChatId();
            ResultSet resultSet = db.select("select work_id from processing_status where chat_id = " + chat_id);
            resultSet.next();
            String work_id = resultSet.getString("work_id");
            String file_name = message.getText();
            db.update("update processing_status set last_update = current_timestamp(1), process_state = 4 where chat_id = " + chat_id);
            db.update("update process_info set file_name = '" + file_name + "', " +
                    "status = 'Application-file_name', last_update = current_timestamp(1)" +
                    " where work_id = '" + work_id + "'");
            resultSet = db.select("select url from process_info where work_id = '" + work_id + "'");
            resultSet.next();
            String url = resultSet.getString("url");

            return "Ваша заявка:\n\n" + url + "\n" + file_name;
        } else if (process_state == 4 && "Подтвердить".equals(message.getText())) {
            KeyboardRow row2 = new KeyboardRow();
            KeyboardRow row3 = new KeyboardRow();
            keyboard.clear();
            row1.add("О нас");
            row1.add("Инструкция");
            row2.add("Расшифровать видео");
            row3.add("Поддержка");
            keyboard.add(row1);
            keyboard.add(row2);
            keyboard.add(row3);
            replyKeyboardMarkup.setKeyboard(keyboard);
            long chat_id = message.getChatId();
            ResultSet resultSet = db.select("select work_id from processing_status where chat_id = " + chat_id);
            resultSet.next();
            String work_id = resultSet.getString("work_id");
            db.update("update processing_status set last_update = current_timestamp(1), is_able = false, process_state = 0 where chat_id = " + chat_id);
            db.update("update process_info set status = 'Application-done', last_update = current_timestamp(1) where work_id = '" + work_id + "'");
            ResultSet resultSet1 = db.select("select * from process_info where work_id = '" + work_id + "'");
            resultSet.next();
            MqManager mq = MqManager.getMqManager();
            mq.producer("{work_id: \"" + work_id + "\"}");
            return "Ваша заявка принята. В скором времени вам придет расшифровка.";
        }
        return "Что-то не то вы делаете(";
    }

    private static String appeal(Message message) throws SQLException {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("О нас");
        row1.add("Инструкция");
        row2.add("Расшифровать видео");
        row3.add("Поддержка");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        long chat_id = message.getChatId();
        db.update("update processing_status set last_update = current_timestamp(1)," +
                " is_support = false, is_able = false, process_state = 0 where chat_id = " + chat_id);
        System.out.println("[" + LocalDateTime.now().toString() + "] [" + chat_id + "] [" + message.getText() + "]");
        try {
            FileWriter fileWriter = new FileWriter(Configure.SUPPORT + data.format(Calendar.getInstance().getTime()) + ".txt", true);
            fileWriter.append("[" +LocalDateTime.now().toString() + "] [");
            fileWriter.append(message.getFrom().toString() + "] [");
            fileWriter.append(message.getText() + "]\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Ваше обращение принято. Оно ценно для нас, при необходимости мы вам ответим.";
    }

    private static String cancel(Message message) throws SQLException {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("О нас");
        row1.add("Инструкция");
        row2.add("Расшифровать видео");
        row3.add("Поддержка");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        long chat_id = message.getChatId();
        db.update("update processing_status set last_update = current_timestamp(1)," +
                " is_support = false, is_able = false, process_state = 0 where chat_id = " + chat_id);
        return "Меню";
    }


    private static String support(Message message) throws SQLException {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("Отмена");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);

        long chat_id = message.getChatId();
        ResultSet resultSet = db.select("select chat_id from processing_status");
        while (resultSet.next()) {
            if (chat_id == resultSet.getLong("chat_id")) {
                db.update("update processing_status set last_update = current_timestamp(1), is_support = true where chat_id = " + chat_id);
                return "Напишите свое обращение";
            }
        }
        db.insert("insert into processing_status values (" + chat_id + ", false, true, 0, null, current_timestamp(1))");
        return "Напишите свое обращение";
    }


    private static String manual() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return "МЫ РАБОТАЕМ В ТЕСТОВОМ РЕЖИМЕ\n" +
                "ПОЭТОМУ ПОКА БЕСПЛАТНО) НО НУЖНО ЗНАТЬ ПАРОЛЬ\n" +
                "Где его узнать? Нигде\n" +
                "А если вы его все же знаете, то...\n" +
                "1. Нам нужно получить от вас видео. Пока мы будем использовать для этих целей этот ресурс https://fex.net\n" +
                "Зарегестрируйтесь, загурзите свое видео. Нам нужна будет публичная ссылка на ваше хранилище\n" +
                "2. Пока за раз мы обрабатываем ОДНО видео. Нам нужно будет название файла с форматом видео (даже если в хранилище лежит 1 файл)\n" +
                "3. Расшифровка занимает время, зависит от объема файла, длительности записи. К примеру, расшифровка 3Gb/10min займет приблизительно 3 минуты\n" +
                "Вы можете не дожижаться ответа, перед тем как отправить на распознование следующие видео. " +
                "Ответ придет вам в чат в виде txt файла, который будет назван так же как видео.\n";
    }

    private static String about() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.add("Меню");
        row2.add("Инструкция");
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return "Однажды вечером мы долго ждали товарища с работы, пока он закончит расшифровывать отснятые за неделю интервью. " +
                "И пока ждали, родилась идея помочь ему в этом скучном занятии. Так появилась Serena1.0. " +
                "Скажем так, она пока не идеальна, но уже может значительно облегчить ваш труд. " +
                "Надеемся вам она будет полезна!\n" +
                "P.S. будьте умницой, прочтите инструкцию)";
    }


    private static String registration(Message message) throws SQLException {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("О нас");
        row1.add("Инструкция");
        row2.add("Расшифровать видео");
        row3.add("Поддержка");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        long chat_id = message.getChatId();
        ResultSet resultSet = db.select("select chat_id, user_name from users");
        while (resultSet.next()) {
            if (chat_id == resultSet.getLong("chat_id")) {
                return "С возвращением, " + resultSet.getString("user_name") + "!";
            }
        }
        db.insert("insert into users values (" + chat_id + ", '" + message.getFrom().getUserName() +
                "', '" + message.getFrom().getFirstName() + " " + message.getFrom().getLastName() +
                "', null, 0, false, 'client', 0, 0, current_timestamp(1))");
        return "Добро пожаловать, " + message.getFrom().getUserName();
    }

    private static String menu() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.clear();
        row1.clear();
        row1.add("О нас");
        row1.add("Инструкция");
        row2.add("Расшифровать видео");
        row3.add("Поддержка");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return "Меню";
    }
}
