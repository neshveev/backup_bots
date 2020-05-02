import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class BotAPI {
    private Bot bot;
    private TelegramBotsApi botsApi;


    BotAPI() throws TelegramApiRequestException {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(BotConstants.LOGIN, BotConstants.PASSWORD.toCharArray());
            }
        });
        ApiContextInitializer.init();
        botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyHost(BotConstants.HOST);
        botOptions.setProxyPort(BotConstants.PORT);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        bot = new Bot(BotConstants.TOKEN, BotConstants.NAME, botOptions);
    }

    public void registerBot() {
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws TelegramApiRequestException {
        System.out.println(BotConstants.HOST);
        BotAPI botAPI = new BotAPI();
        botAPI.registerBot();
    }
}
