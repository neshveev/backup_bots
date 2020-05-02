import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configure {
    public static String PROXY_HOST;
    public static int PROXY_PORT;
    public static String PROXY_USER;
    public static String PROXY_PASSWORD;
    public static String TOKEN;
    public static String BOT_NAME;
    public static String PASSWORD;
    public static String SUPPORT;
    public static String LOG_BOT;
    private final static Configure configure = new Configure();

    Configure() {
        TOKEN = "893434092:AAFje2hcSse2uRxiJXh2xkd0eN8E38M-5qk";
        BOT_NAME = "serena1_0_bot";
        String file = null;
        try {
            file = new String(
                    Files.readAllBytes(Paths.get("/home/ejommy/Documents/serena1.0/config.json")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        JSONObject json = new JSONObject(file);
        JSONObject core = json.getJSONObject("core");
        PASSWORD = core.getString("pw");
        SUPPORT = core.getString("support");
        JSONObject proxy = core.getJSONObject("proxy");
        PROXY_HOST = proxy.getString("host");
        PROXY_PORT = proxy.getInt("port");
        PROXY_USER = proxy.getString("user");
        PROXY_PASSWORD = proxy.getString("password");
        JSONObject logger = json.getJSONObject("logger");
        LOG_BOT = logger.getString("bot");
        printVars();
    }

    public static void printVars(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PROXY_HOST: " + PROXY_HOST)
                .append("\n")
                .append("PROXY_PORT: " + PROXY_PORT)
                .append("\n")
                .append("PROXY_USER: " + PROXY_USER)
                .append("\n")
                .append("PROXY_PASSWORD: " + PROXY_PASSWORD)
                .append("\n")
                .append("PASSWORD: " + PASSWORD);
        System.out.println(stringBuilder.toString());
    }
}
