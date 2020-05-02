import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class FPC {
    private static final DB db = new DB();

    public static double[] getFPC(String type, Update update) throws SQLException {
        switch (type) {
            case ("STANDART"):
                return FPCConstants.STANDART;
            case ("HIGH"):
                return FPCConstants.HIGH;
            case ("LOW"):
                return FPCConstants.LOW;
            case ("DRY"):
                return FPCConstants.DRY;
            case ("CUSTOM"):
                ResultSet resultSet = db.select("select f, p, c from custom_options where chat_id = " + update.getMessage().getChatId());
                resultSet.next();
                return new double[]{resultSet.getDouble("f"), resultSet.getDouble("p"), resultSet.getDouble("c")};
            default:
                return null;
        }
    }

    private static double getActivityIndex(int activity){
        switch (activity){
            case (0):
                return FPCConstants.a0;
            case (1):
                return FPCConstants.a1;
            case (2):
                return FPCConstants.a2;
            case (3):
                return FPCConstants.a3;
            case (4):
                return FPCConstants.a4;
            case (5):
                return FPCConstants.a5;
            case (6):
                return FPCConstants.a6;
            default:
                return 1;
        }

    }

    private static int BMR(Persona persona){
        switch (persona.getSex()) {
            case ("shemale"):
                return (int) (6.25 * persona.getHeight() + 10 * persona.getWeightWish() - 4.92 * persona.getAge() - 161);
            case ("male"):
                return (int) (6.25 * persona.getHeight() + 10 * persona.getWeightWish() - 4.92 * persona.getAge() + 5);
            default:
                return 2000;
        }
    }

    private static int[] calcFPC(double[] fpc, int bmr, double activity){
        int amr = (int) (bmr * activity);
        return new int[]{amr, (int) (fpc[0] * amr / 9), (int) (fpc[1] * amr / 4), (int) (fpc[2] * amr / 4)};
    }

    public static int[][] resultSetFPC(Persona persona) throws SQLException {
        int[][] result = new int[8][];
        double[] fpc = getFPC(persona.getType(), null);
        int bmr = BMR(persona);
        result[0] = new int[1];
        result[0][0] = bmr;
        for (int i = 1; i < 8; ++i){
            result[i] = calcFPC(fpc, bmr, getActivityIndex(persona.getActivity()[i - 1]));
        }
        return result;
    }

    public static int[][] resultSetFPC(Persona persona, ResultSet resultSet) throws SQLException {
        resultSet.next();
        int dif = resultSet.getInt("kal");
        int[][] result = new int[8][];
        double[] fpc = {resultSet.getDouble("f"), resultSet.getDouble("p"), resultSet.getDouble("c")};
        int bmr = BMR(persona) + dif;
        result[0] = new int[1];
        result[0][0] = bmr;
        for (int i = 1; i < 8; ++i){
            result[i] = calcFPC(fpc, bmr, getActivityIndex(persona.getActivity()[i - 1]));
        }
        return result;
    }

    public static String resultSetToString(int[][] resultSet){
        StringBuilder stringBuilder = new StringBuilder();
        String[] days = {"ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА", "ВОСКРЕСЕНЬЕ"};
        stringBuilder.append("Базовый обмен веществ: _" + resultSet[0][0] + "кКал_" + System.lineSeparator());
        for (int i = 0; i < 7; ++i) {
            stringBuilder.append("*" + days[i] + "*" + System.lineSeparator())
                    .append("Активный обмен веществ: _" + resultSet[i + 1][0] + "кКал_" + System.lineSeparator())
                    .append("Жиры: _" + resultSet[i + 1][1] + "_" + System.lineSeparator())
                    .append("Белки: _" + resultSet[i + 1][2] + "_" + System.lineSeparator())
                    .append("Углеводы: _" + resultSet[i + 1][3] + "_" + System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws ParseException, SQLException {
        String json = "{uid: \"aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaaaaaa\"," +
                " activity: [2, 0, 2, 0, 2, 0, 1],  " +
                "name: \"Ivan Ivanov\", " +
                "chat_id: 1111, " +
                "sex: \"male\"," +
                " height: 189, " +
                "weightCurrent: 82," +
                " weightWish: 82," +
                " born: \"04-04-1991\"," +
                " type: \"STANDART\"}";
        JSONObject a = new JSONObject(json);
        Persona p = new Persona(a);
        p.info();

        System.out.println(resultSetToString(resultSetFPC(p)));


    }

}
