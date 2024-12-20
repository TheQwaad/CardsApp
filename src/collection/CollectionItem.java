package collection;

import org.json.JSONArray;

import java.io.IOException;

public interface CollectionItem {
    public Object getContent() throws IOException;
    public JSONArray getJSONPaths();
}
