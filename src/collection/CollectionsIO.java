package collection;

import helpers.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class CollectionsIO {
    public static void saveCollectionsList(Pair<String, String>[] collections) {
        JSONArray jsonArray = new JSONArray();
        for (Pair<String, String> s : collections) {
            JSONArray tempJsonArray = new JSONArray();
            tempJsonArray.put(s.first());
            tempJsonArray.put(s.second());
            jsonArray.put(tempJsonArray);
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

    public static Pair<String, String>[] loadCollectionsList() {
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
            Pair<String, String>[] collections = new Pair[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray tempJsonArray = jsonArray.getJSONArray(i);
                collections[i] = new Pair<>(tempJsonArray.getString(0), tempJsonArray.getString(1));
            }

            return collections;

        } catch (IOException | JSONException e) {
            Pair<String, String>[] empty = new Pair[0];
            saveCollectionsList(empty);
            return empty;
        }
    }

    private static Pair<String, String>[] getExtendedCollectionsList() {
        Pair<String, String>[] collections = loadCollectionsList();
        Pair<String,String>[] newCollections = new Pair[collections.length + 1];

        System.arraycopy(collections, 0, newCollections, 0, collections.length);
        return newCollections;
    }

    private static void saveCollection(Collection collection) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", collection.getName());
        jsonObject.put("paths", collection.getJSONPaths());

        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(String.format("data/%s_collection.json", collection.getName()));
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(jsonObject.toString(4));
        writer.close();
    }

    public static void saveOneWayCollection(Collection collection) throws IOException {
        Pair<String, String>[] newCollections = getExtendedCollectionsList();
        newCollections[newCollections.length - 1] = new Pair<>(collection.getName(), "one-way");
        saveCollectionsList(newCollections);
        saveCollection(collection);
    }

    public static OneWayCollection loadOneWayCollection(String name) throws IOException {
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
                JSONArray curPath = jsonArray.getJSONArray(i);
                imagePaths[i] = curPath.getString(0);
            }
            return new OneWayCollection(name, imagePaths);
        } catch (IOException e) {
            throw e;
        }
    }
}
