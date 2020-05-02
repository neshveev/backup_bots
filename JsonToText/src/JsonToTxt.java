import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JsonToTxt {
    private FileWriter fileWriter = null;
    private int slot;

    public JsonToTxt(String fileName) {
        try {
            fileWriter = new FileWriter("/home/ejommy/Documents/serena1.0/text/" + fileName + ".txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        slot = 0;
    }

    public void start(String path) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            if ((boolean) object.get("done")) {
                JSONObject response = (JSONObject) object.get("response");
                JSONArray chunks = (JSONArray) response.get("chunks");
                chunks.forEach(chunk -> writeToFile((JSONObject) chunk));
                fileWriter.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getTimeCode(String start) {
        StringBuilder stringBuilder = new StringBuilder();
        int t = 0;
        for (int i = 0; i < start.length(); ++i) {
            if (Character.isDigit(start.charAt(i))) {
                stringBuilder.append(start.charAt(i));
            } else {
                break;
            }
        }
        t = Integer.parseInt(stringBuilder.toString());
        stringBuilder.delete(0, stringBuilder.length() - 1);
        if (slot == 0) {
            slot = t;
        } else if (t - slot < 30){
            return null;
        }
        slot = t;
        int seconds = t;
        int hh = seconds / 3600;
        int mm = (seconds - hh * 3600) / 60;
        int ss = seconds - hh * 3600 - mm * 60;
        String timeCode = String.format("\n[%02d:%02d:%02d] ", hh, mm, ss);
        return timeCode;
    }



    private void writeToFile(JSONObject chunk) {
        JSONArray alternatives = (JSONArray) chunk.get("alternatives");
        JSONObject elem = (JSONObject) alternatives.get(0);
        JSONArray words = (JSONArray) elem.get("words");

        for (int iter = 0; iter < words.size(); ++iter) {
            JSONObject word = (JSONObject) words.get(iter);
            String w = (String) word.get("word");
            String timeCode;
            if ((timeCode = getTimeCode((String) word.get("startTime"))) != null) {
                try {
                    fileWriter.append(timeCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (iter == 0)
                if (Character.isLetter(w.charAt(0)))
                    w = w.replaceFirst(".", "" + Character.toUpperCase(w.charAt(0)));
            try {
                fileWriter.append(w);
                if (iter == words.size() - 1)
                    fileWriter.append(". ");
                else
                    fileWriter.append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
