import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {
    public static void main(String[] args) throws TelegramApiRequestException {
        BotAPI botAPI = new BotAPI();
        botAPI.registerBot();
    }
}
