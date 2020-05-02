import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class Food {
    private final static Food food = new Food();
    private final static DB db = new DB();

    protected static SendMessage choose(Update update){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Начать");
        KeyboardRow row0 = new KeyboardRow();
        row0.add("Меню");
        keyboard.add(row1);
        keyboard.add(row0);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("У каждого человека свои предпочтения в еде. " +
                "Они могут быть обоснованы личным вкусом, местом проживания, достатком, медицинскими показаниями, настроением, в конце концов. " +
                "Пока у нас в меню будут только блюда на каждый день. 5 приемов пищи: 3 полноценных + 2 перекуса. Выберите, что вам по душе. " +
                "Далее бот будет сам формировать ваш рацион и составлять список покупок. Поехали)");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    protected static SendMessage foodMenu(Update update){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        String[] buttons = {"Мясо/Птица/Рыба/Морепродукты", "Гарниры", "Салаты", "Десерты", "Перекусы"};
        for (String b: buttons){
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(b);
            inlineKeyboardButton.setCallbackData(b);
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Меню")
                .setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
