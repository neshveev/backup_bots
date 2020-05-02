import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Commands {
    private static HashMap<Long, Boolean> support;
    private static HashMap<Long, Integer> video;
    private static final Commands commands = new Commands();
    private static SimpleDateFormat time;

    private Commands() {
        support = new HashMap<Long, Boolean>();
        video = new HashMap<Long, Integer>();
        time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SendMessage response(Message message){
        User user = message.getFrom();
        long chat_id = message.getChatId();
        if (video.containsKey(chat_id) && video.get(chat_id) >= 0){
            return Video.buildMqMessage(message, chat_id, video);
        }
        switch (message.getText()){
            case ("/start"):
                return Commands.start(user, chat_id);
            case ("/help"):
                return Commands.help(chat_id);
            case ("/support"):
                return Commands.support(chat_id);
            case ("/video"):
                return Commands.video(chat_id);
            case ("/about"):
                return Commands.about(chat_id);
            default:
                return Commands.another(message, chat_id);
        }
    }

    private static SendMessage about(long chat_id){
        SendMessage message = new SendMessage().setChatId(chat_id)
                .setText("Мы пока тестимся! " +
                        "Нажав команду /video, у вас начнется диалог с ботом. " +
                        "Серена попросит у вас публичную ссылку на яндекс.диск, где лежит ваше видео, " +
                        "а так же имя видео файла. За раз она принимает одно видео. " +
                        "Потом она будет делать магию и спустя время пришлет вам <имя видео>.txt файл " +
                        "с результатом распознавания. Так как мы тестимся, то денег не берем. " +
                        "Пользоваться могут все, кто знает пароль " +
                        "(пароль может быть изменен в любой момент). " +
                        "Мы не храним ни видео, ни аудио, ни текст. Это ваша собственность, все дела.");
        return message;
    }

    private static SendMessage support(long chat_id){
        SendMessage message = new SendMessage().setChatId(chat_id);
        message.setText("Напишите ваше обращениие:");
        support.put(chat_id, true);
        return message;
    }

    private static SendMessage video(long chat_id){
        SendMessage message = new SendMessage().setChatId(chat_id);
        message.setText("Пароль:");
        video.put(chat_id, 0);
        return message;
    }

    public static SendMessage start(User user, long chat_id){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Привет, ");
        if (user.getFirstName() != null)
            stringBuilder.append(user.getFirstName());
        else if (user.getUserName() != null)
            stringBuilder.append((user.getUserName()));
        else
            stringBuilder.append("Тайный Агент без имени");
        stringBuilder.append("! Этот бот занимается расшифровкой видео. ");
        stringBuilder.append("Он понимает только команды. ");
        stringBuilder.append("Чтобы узнать, что к чему жми /help.");
        stringBuilder.append("Пока ничего не работает, я напишу, как все будет ок!");
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(stringBuilder.toString());
        return message;
    }

    public static SendMessage another(Message input, long chat_id){
        if (support.containsKey(chat_id) && support.get(chat_id)){
            SendMessage message = new SendMessage().setChatId(chat_id)
                .setText("Почитаем!");
            StringBuilder stringBuilder = new StringBuilder()
                    .append(time.format(Calendar.getInstance().getTime()))
                    .append("\t")
                    .append(input.getFrom().getId())
                    .append("\t")
                    .append(input.getFrom().getUserName())
                    .append("\t")
                    .append(input.getText());
            System.out.println(stringBuilder.toString());
            support.put(chat_id, false);
            return message;
        }
        SendMessage message = new SendMessage().setChatId(chat_id)
                .setText("Учись команды юзать, жми /help");
        return message;
    }

    public static SendMessage help(long chat_id){
        StringBuilder stringBuilder = new StringBuilder()
                .append("/help\n")
                .append("- это команда, чтобы узнать другие команды. Что логично.\n")
                .append("/about\n")
                .append("- как это все работает\n")
                .append("/support\n")
                .append("- тут ты можешь написать нам что-то хорошее или плохое.\n")
                .append("/video\n")
                .append("- собственно, ради чего и собрались.\n");
        SendMessage sendMessage = new SendMessage().setChatId(chat_id)
                .setText(stringBuilder.toString());
        return sendMessage;
    }
}
