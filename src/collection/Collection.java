package collection;

import org.json.JSONArray;

import java.io.IOException;

public interface Collection {
    public CollectionItem getItem();
    public String getName() throws IOException;
    public JSONArray getJSONPaths();
}
