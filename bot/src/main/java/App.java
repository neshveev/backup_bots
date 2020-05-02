import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {
    public static void main(String[] args) throws TelegramApiRequestException {
//        Configure.printVars();
        BotApi botApi = new BotApi();
        botApi.registerBot();
    }
}
