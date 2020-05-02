import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.text.ParseException;

public class Bot extends AbilityBot {
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
            if (update.hasMessage() && update.getMessage().hasText()) {
                try {
                    execute(BotProcessing.answer(update));
                } catch (SQLException | ParseException e) {
                    e.printStackTrace();
                    SendMessage sendMessage = new SendMessage().setText("Что-то пошло не так...").setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                }
            } else if (update.hasCallbackQuery()) {
                try {
                    if (!update.getCallbackQuery().getData().equals(" "))
                        execute(CustomFPC.editMessageReplyMarkup(update));
                } catch (SQLException e) {
                    e.printStackTrace();
                    SendMessage sendMessage = new SendMessage().setText("Что-то пошло не так...").setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing() {
    }
}

