package helpers;

import org.json.JSONObject;

import java.io.*;

public class JSONHelper {
    public static JSONObject getJSONData(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(String.format("data/%s.json", name)));
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }
        return new JSONObject(jsonContent.toString());
    }

    public static void writeJSONData(String name, JSONObject data) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(String.format("data/%s.json", name));
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(data.toString(4));
        writer.close();
    }
}
