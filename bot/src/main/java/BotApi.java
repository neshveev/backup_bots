import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class BotApi {
    private Bot bot;
    private TelegramBotsApi botsApi;
    private SerenaEvents serenaEvents;


    BotApi() throws TelegramApiRequestException {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Configure.PROXY_USER, Configure.PROXY_PASSWORD.toCharArray());
            }
        });
        ApiContextInitializer.init();
        botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyHost(Configure.PROXY_HOST);
        botOptions.setProxyPort(Configure.PROXY_PORT);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        bot = new Bot(Configure.TOKEN, Configure.BOT_NAME, botOptions);
        serenaEvents = new SerenaEvents(bot);
    }

    public void registerBot() {
        try {
            botsApi.registerBot(bot);
            serenaEvents.run();

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
