import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CustomFPC {
    private final static CustomFPC customFPC = new CustomFPC();
    private final static DB db = new DB();

    private static List<InlineKeyboardButton> getTitle(String s) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(s);
        inlineKeyboardButton.setCallbackData(" ");
        keyboardButtonsRow.add(inlineKeyboardButton);
        return keyboardButtonsRow;
    }

    private static List<InlineKeyboardButton> getContent(String s, double[] fpc, Update update) throws SQLException {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("-");
        inlineKeyboardButton1.setCallbackData(s + "-");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        switch (s) {
            case ("Жиры"):
                inlineKeyboardButton2.setText(((int) (fpc[0] * 100)) + "%");
                break;
            case ("Белки"):
                inlineKeyboardButton2.setText(((int) (fpc[1] * 100)) + "%");
                break;
            case ("Углеводы"):
                inlineKeyboardButton2.setText(((int) (fpc[2] * 100)) + "%");
                break;
            case ("Калории"):
                ResultSet resultSet = db.select("select kal from custom_options where chat_id = " + update.getMessage().getChatId());
                resultSet.next();
                inlineKeyboardButton2.setText(String.valueOf(resultSet.getInt("kal")));
                break;
        }
        inlineKeyboardButton2.setCallbackData(" ");
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("+");
        inlineKeyboardButton3.setCallbackData(s + "+");
        keyboardButtonsRow.add(inlineKeyboardButton1);
        keyboardButtonsRow.add(inlineKeyboardButton2);
        keyboardButtonsRow.add(inlineKeyboardButton3);
        return keyboardButtonsRow;
    }

    private static List<InlineKeyboardButton> addFinalButton() {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Применить");
        inlineKeyboardButton.setCallbackData("Применить");
        keyboardButtonsRow.add(inlineKeyboardButton);
        return keyboardButtonsRow;
    }

    protected static SendMessage fixFpc(Update update) throws SQLException {
        ResultSet resultSet = db.select("select type from users where chat_id = " + update.getMessage().getChatId());
        resultSet.next();
        String type = resultSet.getString("type");
        double[] fpc = FPC.getFPC(type, update);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        String[] rows = {"Калории", "Жиры", "Белки", "Углеводы"};
        for (String s : rows) {
            rowList.add(getTitle(s));
            rowList.add(getContent(s, fpc, update));
        }
        rowList.add(addFinalButton());

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Подходите к корректировке очень ответственно! " +
                "В разделе меню \"Статьи\" вы найдете полезную информацию. Так же бот не даст вам установить экстремальные параметры.")
                .setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    private static void changeRowList(List<List<InlineKeyboardButton>> rowList, String request, long chat_id) throws SQLException {
        ResultSet resultSet = db.select("select error from custom_error where chat_id = " + chat_id);
        resultSet.next();
        Array array = resultSet.getArray("error");
        Integer[] error = (Integer[]) array.getArray();
        if (request.startsWith("Калории")){
            List<InlineKeyboardButton> keyboardButtonsRow = rowList.get(1);
            InlineKeyboardButton inlineKeyboardButton = keyboardButtonsRow.get(1);
            int value = Integer.parseInt(inlineKeyboardButton.getText());
            value = request.endsWith("-") ? value - 50 : value + 50;
            error[0] = value < -500 || value > 500 ? 1 : 0;
            inlineKeyboardButton.setText(value + "");
        } else if (request.startsWith("Жиры")){
            List<InlineKeyboardButton> keyboardButtonsRow = rowList.get(3);
            InlineKeyboardButton inlineKeyboardButton = keyboardButtonsRow.get(1);
            String f = inlineKeyboardButton.getText();
            f = f.substring(0, f.length() - 1);
            int value = Integer.parseInt(f);
            value = request.endsWith("-") ? value - 5 : value + 5;
            error[1] = value < 10 || value > 50 ? 1 : 0;
            inlineKeyboardButton.setText(value + "%");
        } else if (request.startsWith("Белки")){
            List<InlineKeyboardButton> keyboardButtonsRow = rowList.get(5);
            InlineKeyboardButton inlineKeyboardButton = keyboardButtonsRow.get(1);
            String f = inlineKeyboardButton.getText();
            f = f.substring(0, f.length() - 1);
            int value = Integer.parseInt(f);
            value = request.endsWith("-") ? value - 5 : value + 5;
            error[2] = value < 10 || value > 50 ? 1 : 0;
            inlineKeyboardButton.setText(value + "%");
        } else if (request.startsWith("Углеводы")){
            List<InlineKeyboardButton> keyboardButtonsRow = rowList.get(7);
            InlineKeyboardButton inlineKeyboardButton = keyboardButtonsRow.get(1);
            String f = inlineKeyboardButton.getText();
            f = f.substring(0, f.length() - 1);
            int value = Integer.parseInt(f);
            value = request.endsWith("-") ? value - 5 : value + 5;
            error[3] = value < 10 || value > 50 ? 1 : 0;
            inlineKeyboardButton.setText(value + "%");
        }

        String f = rowList.get(3).get(1).getText();
        f = f.substring(0, f.length() - 1);
        String p = rowList.get(5).get(1).getText();
        p = p.substring(0, p.length() - 1);
        String c = rowList.get(7).get(1).getText();
        c = c.substring(0, c.length() - 1);
        error[4] = (Integer.parseInt(p) + Integer.parseInt(c) + Integer.parseInt(f)) != 100 ? 1 : 0;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; ++i){
            if (i == 0)
                stringBuilder.append("'{");
            stringBuilder.append(error[i]);
            if (i != 4)
                stringBuilder.append(",");
            else
                stringBuilder.append("}'");
        }
        db.update("update custom_error set error = " + stringBuilder.toString() + " where chat_id = " + chat_id);

        List<InlineKeyboardButton> keyboardButtonsRow = rowList.get(8);
        InlineKeyboardButton inlineKeyboardButton = keyboardButtonsRow.get(0);
        inlineKeyboardButton.setText("Применить");
        inlineKeyboardButton.setCallbackData("Применить");
        if (error[0] == 1){
            inlineKeyboardButton.setText("ОШИБКА: Разница калорий больше 500");
            inlineKeyboardButton.setCallbackData(" ");
        } if (error[4] == 1) {
            inlineKeyboardButton.setText("ОШИБКА: Сумма ЖБУ должна быть 100%");
            inlineKeyboardButton.setCallbackData(" ");
        } if (error[1] == 1 || error[2] == 1 || error[3] == 1){
            inlineKeyboardButton.setText("ОШИБКА: Нутриент меньше 10% или больше 50%");
            inlineKeyboardButton.setCallbackData(" ");
        }
    }

    private static void applyChange(Update update) throws SQLException {
        db.update("update users set type = 'CUSTOM' where chat_id = " + update.getCallbackQuery().getMessage().getChatId());
        InlineKeyboardMarkup inlineKeyboardMarkup = update.getCallbackQuery().getMessage().getReplyMarkup();
        List<List<InlineKeyboardButton>> rowList = inlineKeyboardMarkup.getKeyboard();
        String k = rowList.get(1).get(1).getText();
        String f = rowList.get(3).get(1).getText();
        f = f.substring(0, f.length() - 1);
        String p = rowList.get(5).get(1).getText();
        p = p.substring(0, p.length() - 1);
        String c = rowList.get(7).get(1).getText();
        c = c.substring(0, c.length() - 1);
//        System.out.println("update custom_options set " +
//                "kal = " + k + ", f = " + (Double.parseDouble(f) / 100) + ", p = " +
//                (Double.parseDouble(p) / 100) + ", c = " + (Double.parseDouble(c) / 100) + " where chat_id = " + update.getCallbackQuery().getMessage().getChatId());
        db.update("update custom_options set " +
                "kal = " + k + ", f = " + (Double.parseDouble(f) / 100) + ", p = " +
                (Double.parseDouble(p) / 100) + ", c = " + (Double.parseDouble(c) / 100) + " where chat_id = " + update.getCallbackQuery().getMessage().getChatId());
    }

    public static EditMessageReplyMarkup editMessageReplyMarkup(Update update) throws SQLException {
        String request = update.getCallbackQuery().getData();
        if (request.equals("Применить")){
            applyChange(update);
            EditMessageReplyMarkup message = new EditMessageReplyMarkup().setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
            return message;
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = update.getCallbackQuery().getMessage().getReplyMarkup();
        List<List<InlineKeyboardButton>> rowList = inlineKeyboardMarkup.getKeyboard();
        changeRowList(rowList, request, update.getCallbackQuery().getMessage().getChatId());
        inlineKeyboardMarkup.setKeyboard(rowList);
        EditMessageReplyMarkup message = new EditMessageReplyMarkup().setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setInlineMessageId(update.getCallbackQuery().getInlineMessageId())
                .setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

}
