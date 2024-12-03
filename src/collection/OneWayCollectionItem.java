package collection;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OneWayCollectionItem implements CollectionItem {
    private final String filePath;
    OneWayCollectionItem(String filePath) {
        this.filePath = filePath;
    }

    public Object getContent() throws IOException {
        return filePath;
    }

    @Override
    public JSONArray getJSONPaths() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(filePath);
        return jsonArray;
    }

}
