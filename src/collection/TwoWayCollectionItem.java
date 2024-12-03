package collection;

import helpers.Pair;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TwoWayCollectionItem implements CollectionItem {
    public String frontPath;
    public String backPath;

    public TwoWayCollectionItem(String frontPath, String backPath) {
        this.frontPath = frontPath;
        this.backPath = backPath;
    }

    @Override
    public Object getContent() throws IOException {
        return new Pair<>(frontPath, backPath);
    }

    @Override
    public JSONArray getJSONPaths() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(frontPath);
        jsonArray.put(backPath);
        return jsonArray;
    }
}
