import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Persona {
    private String name;
    private long chat_id;
    private String sex;
    private int height;
    private int weightCurrent;
    private int weightWish;
    private int born;
    private String type;
    private int[] activity = new int[7];

    Persona(JSONObject data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        name = data.getString("name");
        chat_id = data.getLong("chat_id");
        sex = data.getString("sex");
        height = data.getInt("height");
        weightCurrent = data.getInt("weightCurrent");
        weightWish = data.getInt("weightWish");
        born = data.getInt("born");
        type = data.getString("type");

        JSONArray activityArray = data.getJSONArray("activity");
        for (int i = 0; i < 7; ++i){
            activity[i] = activityArray.getInt(i);
        }
    }

    Persona(ResultSet resultSet) throws ParseException, SQLException {
        resultSet.next();

        name = resultSet.getString("username");
        chat_id = resultSet.getLong("chat_id");
        sex = resultSet.getString("sex");
        height = resultSet.getInt("height");
        weightCurrent = resultSet.getInt("weight_current");
        weightWish = resultSet.getInt("weight_wish");
        born = resultSet.getInt("born");
        type = resultSet.getString("type");

        Array array = resultSet.getArray("activity");
        Integer[] a = (Integer[]) array.getArray();
        for (int i = 0; i < 7; ++i){
            activity[i] = a[i];
        }
    }


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NAME: " + name + System.lineSeparator())
                .append("BORN: " + born + System.lineSeparator())
                .append("AGE: " + getAge() + System.lineSeparator())
                .append("CHAT_ID: " + chat_id + System.lineSeparator())
                .append("SEX: " + sex + System.lineSeparator())
                .append("HEIGHT: " + height + System.lineSeparator())
                .append("WEIGHT_CURRENT: " + weightCurrent + System.lineSeparator())
                .append("WEIGHT_WISH: " + weightWish + System.lineSeparator())
                .append("TYPE: " + type + System.lineSeparator())
                .append("ACTIVITY: ");
        for (int i = 0; i < 7; ++i){
            stringBuilder.append(activity[i]);
            if (i != 6){
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public void info(){
        System.out.println(toString());
    }

    public int[] getActivity(){
        return activity;
    }

    public String getType(){
        return type;
    }

    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - born;
    }

    public String getName() {
        return name;
    }

    public long getChat_id() {
        return chat_id;
    }

    public String getSex() {
        return sex;
    }

    public int getHeight() {
        return height;
    }

    public int getWeightCurrent() {
        return weightCurrent;
    }

    public int getWeightWish() {
        return weightWish;
    }

    public void setWeightCurrent(int weightCurrent) {
        this.weightCurrent = weightCurrent;
    }

    public void setWeightWish(int weightWish) {
        this.weightWish = weightWish;
    }

    public static void main(String[] args) throws ParseException {
        String json = "{uid: \"aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaaaaaa\", name: \"Ivan Ivanov\", chat_id: 1111, sex: \"male\", height: 189, weightCurrent: 82, weightWish: 82, born: \"04-04-1991\"}";
        JSONObject a = new JSONObject(json);

        Persona p = new Persona(a);
        p.info();
    }
}
