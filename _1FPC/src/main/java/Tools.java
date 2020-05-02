import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public final class Tools {
    private final static Tools tools = new Tools();
    private final static DB db = new DB();

    protected static ReplyKeyboardMarkup getButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();
        KeyboardRow row6 = new KeyboardRow();
        KeyboardRow row7 = new KeyboardRow();
        KeyboardRow row8 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        row1.add("Минимальные нагрузки (сидячая работа)");
        row2.add("Немного дневной активности или легкие упражнения");
        row3.add("Работа средней тяжести или тренировка");
        row4.add("Интенсиная тренировка");
        row5.add("Работа средней тяжести и интенсивная тренировка");
        row6.add("Тяжелая физическая работа");
        row7.add("Тяжелая физическая работа и тренировка");
        row8.add("Меню");
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);
        keyboard.add(row8);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    protected static SendMessage printFpc(Update update) throws SQLException, ParseException {
        ResultSet resultSet = db.select("select * from users where chat_id = " + update.getMessage().getChatId());
        Persona persona = new Persona(resultSet);
        int[][] result;
        if (persona.getType().equals("CUSTOM"))
            result = FPC.resultSetFPC(persona, db.select("select * from custom_options where chat_id = " + update.getMessage().getChatId()));
        else
            result = FPC.resultSetFPC(persona);
        String resultString = FPC.resultSetToString(result);
        SendMessage sendMessage = new SendMessage().setText(resultString).setChatId(update.getMessage().getChatId()).setParseMode("Markdown");
        return sendMessage;
    }
}
