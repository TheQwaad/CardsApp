package collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class CollectionsIO {
    public static void saveCollections(String[] collection) {
        JSONArray jsonArray = new JSONArray();
        for (String s : collection) {
            jsonArray.put(s);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("collections", jsonArray);
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("data/config.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonObject.toString(4));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] loadCollections() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/config.json"));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("collections");
            String[] collections = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                collections[i] = jsonArray.getString(i);
            }

            return collections;

        } catch (IOException | JSONException e) {
            String[] empty = new String[0];
            saveCollections(empty);
            return empty;
        }
    }

    public static void saveCollection(String name, String[] paths) throws IOException {
        String[] collections = loadCollections();
        String[] newCollections = new String[collections.length + 1];

        for (int i = 0; i < collections.length; i++) {
            newCollections[i] = collections[i];
        }

        newCollections[collections.length] = name;

        saveCollections(newCollections);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        JSONArray jsonArray = new JSONArray();
        for (String s : paths) {
            jsonArray.put(s);
        }
        jsonObject.put("paths", jsonArray);

        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(String.format("data/%s_collection.json", name));
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(jsonObject.toString(4));
        writer.close();
    }

    public static Collection loadCollection(String name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.format("data/%s_collection.json", name)));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("paths");
            String[] imagePaths = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                imagePaths[i] = jsonArray.getString(i);
            }
            return new Collection(name, imagePaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
