import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class Bot extends AbilityBot {
    private static SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");


    protected Bot(String botToken, String botUsername, DefaultBotOptions botOptions) {
        super(botToken, botUsername, botOptions);
    }

    @Override
    public int creatorId() {
        return 0;
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage sendMessage = Processing.sendMessage(update);
            execute(sendMessage);
        } catch (TelegramApiException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing() {

    }
}

