import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Quiz {
    private static final Quiz quiz = new Quiz();
    private static final DB db = new DB();

    private Quiz(){};

    protected static SendMessage fpc(Update update) throws SQLException {
        db.update("update dialogs set status = 1 where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите ваш пол");

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("М");
        row1.add("Ж");
        row2.add("Меню");
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage sex(Update update) throws SQLException {
        String request = update.getMessage().getText();
        String sex = null;
        if (request.equals("М"))
            sex = "'male'";
        else if (request.equals("Ж"))
            sex = "'shemale'";
        else {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Попробуйте еще раз");
            return sendMessage;
        }
        db.update("update dialogs set status = 2 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set sex = " + sex + " where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите год рождения");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage born(Update update) throws SQLException {
        String request = update.getMessage().getText();
        Pattern pattern = Pattern.compile("^[0-9]{4}$");
        Matcher matcher = pattern.matcher(request);
        if (!matcher.find()) {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Нужно указать 4 цифры");
            return sendMessage;
        }
        int diff = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(request);
        if (diff < 18 || diff > 50) {
            return BotProcessing.menu(update, "Извините, но наши расчеты будут не корректны для вашего возраста. Всего доброго!");
        }
        db.update("update dialogs set status = 3 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set born = " + request + " where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите рост в см");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage height(Update update) throws SQLException {
        String request = update.getMessage().getText();
        Pattern pattern = Pattern.compile("^[0-9]{3}$");
        Matcher matcher = pattern.matcher(request);
        if (!matcher.find()) {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Не может быть!");
            return sendMessage;
        }
        db.update("update dialogs set status = 4 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set height = " + request + " where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите ваш вес в кг");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage weight_current(Update update) throws SQLException {
        String request = update.getMessage().getText();
        Pattern pattern = Pattern.compile("^[0-9]{2,3}$");
        Matcher matcher = pattern.matcher(request);
        if (!matcher.find()) {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Не может быть!");
            return sendMessage;
        }
        db.update("update dialogs set status = 5 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set weight_current = " + request + " where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите ваш желаемый вес");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage weight_wish(Update update) throws SQLException {
        String request = update.getMessage().getText();
        Pattern pattern = Pattern.compile("^[0-9]{2,3}$");
        Matcher matcher = pattern.matcher(request);
        if (!matcher.find()) {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Не может быть!");
            return sendMessage;
        }
        ResultSet resultSet = db.select("select weight_current from users where chat_id = " + update.getMessage().getChatId());
        resultSet.next();
        int weight_current = resultSet.getInt("weight_current");
        if (Math.abs(weight_current - Integer.parseInt(request)) > 5) {
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Давайте будем реалистами, разница с вашим текущим весом должна быть не более 5 кг");
            return sendMessage;
        }
        db.update("update dialogs set status = 6 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set weight_wish = " + request + " where chat_id = " + update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Поддержание веса");
        row2.add("Набор массы");
        row3.add("Снижение веса");
        row4.add("Сушка");
        row5.add("Меню");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("От ваших целей будет зависеть распределение ЖБУ, в дальнейшем вы сможете корректировать соотношение нутриентов, учитывая ваш личный опыт.");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage type(Update update) throws SQLException {
        String request = update.getMessage().getText();
        String type = null;
        if (request.equals("Поддержание веса"))
            type = "'STANDART'";
        if (request.equals("Набор массы"))
            type = "'HIGH'";
        if (request.equals("Снижение веса"))
            type = "'LOW'";
        if (request.equals("Сушка"))
            type = "'DRY'";
        db.update("update dialogs set status = 7 where chat_id = " + update.getMessage().getChatId());
        db.update("update users set type = " + type + " where chat_id = " + update.getMessage().getChatId());
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Укажите вашу активность в течении недели.\nПОНЕДЕЛЬНИК:");
        sendMessage.setReplyMarkup(Tools.getButtons());
        return sendMessage;
    }

    protected static SendMessage activity(Update update, int status) throws SQLException {
        db.update("update dialogs set status = " + (status + 1) + " where chat_id = " + update.getMessage().getChatId());
        String[] days = {"ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТРВЕРГ", "ПЯТНИЦА", "СУББОТА", "ВОСКРЕСЕНЬЕ"};
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText(days[status - 6]);
        sendMessage.setReplyMarkup(Tools.getButtons());
        String[] activity = {"Минимальные нагрузки (сидячая работа)",
                "Немного дневной активности или легкие упражнения",
                "Работа средней тяжести или тренировка",
                "Интенсиная тренировка",
                "Работа средней тяжести и интенсивная тренировка",
                "Тяжелая физическая работа",
                "Тяжелая физическая работа и тренировка"};
        String messageData = update.getMessage().getText();
        for (int i = 0; i < 7; ++i) {
            if (activity[i].equals(messageData)) {
                db.update("update users set activity[" + (status - 7) + "] = " + i + " where chat_id = " + update.getMessage().getChatId());
            }
        }

        return sendMessage;
    }

    protected static SendMessage finalQuiz(Update update) throws SQLException {
        db.update("update dialogs set status = " + 0 + " where chat_id = " + update.getMessage().getChatId());
        String[] activity = {"Минимальные нагрузки (сидячая работа)",
                "Немного дневной активности или легкие упражнения",
                "Работа средней тяжести или тренировка",
                "Интенсиная тренировка",
                "Работа средней тяжести и интенсивная тренировка",
                "Тяжелая физическая работа",
                "Тяжелая физическая работа и тренировка"};
        String messageData = update.getMessage().getText();
        for (int i = 0; i < 7; ++i) {
            if (activity[i].equals(messageData)) {
                db.update("update users set activity[" + (6) + "] = " + i + " where chat_id = " + update.getMessage().getChatId());
            }
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Меню");
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Спасибо за терпение! Ваши параметры сохранены. С результатами расчетов в можете ознакомиться в меню. Так же вы можете скорректировать количесвтво калорий, а также соотношение ЖБУ согласто вашему персональному опыту. Нужно понимать, что данные расчеты это базовые цифры, которые вы будете изменять, наблюдая за своим весом и объемами.");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        db.update("update menu set level = 1 where chat_id = " + update.getMessage().getChatId());
        return sendMessage;
    }

}
