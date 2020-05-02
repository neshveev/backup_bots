import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public final class BotProcessing {
    private static final BotProcessing botProcessing = new BotProcessing();
    private static final DB db = new DB();

    private BotProcessing() {
    }

    private static ReplyKeyboardMarkup getMenuKeyboard(long chat_id) throws SQLException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Калории и БЖУ");
        keyboard.add(row1);
        ResultSet resultSet = db.select("select level from menu where chat_id = " + chat_id);
        resultSet.next();
        int level = resultSet.getInt("level");
        if (level > 0) {
            KeyboardRow row2 = new KeyboardRow();
//            KeyboardRow row3 = new KeyboardRow();
            row2.add("Выбрать блюда");
//            row3.add("Скорректировать калории и БЖУ");
            keyboard.add(row2);
//            keyboard.add(row3);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static SendMessage branch1(Update update) throws SQLException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Рассчитать калории и БЖУ");
        KeyboardRow row0 = new KeyboardRow();
        row0.add("Меню");
        keyboard.add(row1);

        ResultSet resultSet = db.select("select level from menu where chat_id = " + update.getMessage().getChatId());
        resultSet.next();
        int level = resultSet.getInt("level");
        if (level > 0) {
            KeyboardRow row2 = new KeyboardRow();
            KeyboardRow row3 = new KeyboardRow();
            row2.add("Показать калории и БЖУ");
            row3.add("Скорректировать калории и БЖУ");
            keyboard.add(row2);
            keyboard.add(row3);
        }
        keyboard.add(row0);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Калории и БЖУ");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }


    private static SendMessage menu(Update update) throws SQLException {
        db.update("update dialogs set status = 0 where chat_id = " + update.getMessage().getChatId());
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Меню");
        sendMessage.setReplyMarkup(getMenuKeyboard(update.getMessage().getChatId()));
        return sendMessage;
    }

    protected static SendMessage menu(Update update, String text) throws SQLException {
        SendMessage sendMessage = menu(update);
        sendMessage.setText(text);
        return sendMessage;
    }

    private static SendMessage menu(Update update, int status) throws SQLException {
        if (status == 1) {
            ResultSet resultSet = db.select("select * from users where chat_id = " + update.getMessage().getChatId());
            if (resultSet.next()) {
                SendMessage sendMessage = menu(update);
                sendMessage.setText("С возвращением, " + update.getMessage().getFrom().getUserName());
                return sendMessage;
            } else {
                db.insert("insert into users (chat_id, username) values(" + update.getMessage().getChatId() + ", '" + update.getMessage().getFrom().getUserName() + "')");
                db.insert("insert into dialogs values(" + update.getMessage().getChatId() + ", 0)");
                db.insert("insert into menu values(" + update.getMessage().getChatId() + ", 0)");
                db.insert("insert into custom_error values(" + update.getMessage().getChatId() + ", '{0,0,0,0,0}')");
                db.insert("insert into custom_options values(" + update.getMessage().getChatId() + ", 0, 0, 0, 0)");
                SendMessage sendMessage = menu(update);
                sendMessage.setText("Добро пожаловать, " + update.getMessage().getFrom().getUserName());
                return sendMessage;
            }
        }
        return null;
    }

    private static SendMessage test(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("test");


        return sendMessage;
    }

    public static SendMessage answer(Update update) throws SQLException, ParseException {
        if (update.getMessage().getText().equals("Меню"))
            return menu(update);
        if (update.getMessage().getText().equals("/test"))
            return test(update);
        ResultSet resultSet = db.select("select status from dialogs where chat_id = " + update.getMessage().getChatId());
        int status = 0;
        while (resultSet.next()) {
            status = resultSet.getInt("status");
        }
        switch (status) {
            case (1):
                return Quiz.sex(update);
            case (2):
                return Quiz.born(update);
            case (3):
                return Quiz.height(update);
            case (4):
                return Quiz.weight_current(update);
            case (5):
                return Quiz.weight_wish(update);
            case (6):
                return Quiz.type(update);
            case (7):
            case (8):
            case (9):
            case (10):
            case (11):
            case (12):
                return Quiz.activity(update, status);
            case (13):
                return Quiz.finalQuiz(update);
            default:
                String request = update.getMessage().getText();
                switch (request) {
                    case ("Калории и БЖУ"):
                        return branch1(update);
                    case ("Рассчитать калории и БЖУ"):
                        return Quiz.fpc(update);
                    case ("Показать калории и БЖУ"):
                        return Tools.printFpc(update);
                    case ("Скорректировать калории и БЖУ"):
                        return CustomFPC.fixFpc(update);
                    case ("/start"):
                        return menu(update, 1);
                    case ("Меню"):
                    case ("Отмена"):
                        return menu(update);
                    case ("Выбрать блюда"):
                        return Food.choose(update);
                    case ("Начать"):
                        return Food.foodMenu(update);
                    default:
                        return menu(update, "Не понимаю о чем вы.");
                }
        }
    }
}
