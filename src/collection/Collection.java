package collection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.json.*;
import java.util.Random;

public class Collection {
    private String[] images;
    private String name;
    Random indexer = new Random();

    public Collection(String name, String[] paths) throws IOException {
        init(name, paths);
    }

    public Collection(JSONObject config) throws IOException {
        String name = config.get("name").toString();
        String[] paths = config.get("paths").toString().split(",");
        init(name, paths);
    }

    private void init(String name, String[] paths) {
        images = paths;
        this.name = name;
    }

    public BufferedImage getImage() throws IOException {
        return ImageIO.read(new File(getImagePath()));
    }

    public String getImagePath() {
        return images[indexer.nextInt(images.length)];
    }

    public String getName() {
        return name;
    }
}
