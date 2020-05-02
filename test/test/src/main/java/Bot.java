import com.google.inject.internal.cglib.core.$Customizer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message;
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                execute(Commands.response(update.getMessage()));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

        @Override
        public String getBotUsername() {
            return "serena1_0_bot";
        }

        @Override
        public String getBotToken() {
            return "893434092:AAFje2hcSse2uRxiJXh2xkd0eN8E38M-5qk";
        }
    }
