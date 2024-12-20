package collection;

import org.json.JSONArray;

import java.util.Random;

public class AbstractCollection implements Collection {
    protected CollectionItem[] items;
    private Random indexer = new Random();
    private String name;

    protected AbstractCollection(String name) {
        this.name = name;
    }


    @Override
    public CollectionItem getItem() {
        return items[getRandomItemIndex()];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONArray getJSONPaths() {
        JSONArray json = new JSONArray();
        for (CollectionItem item : items) {
            json.put(item.getJSONPaths());
        }
        return json;
    }

    protected int getRandomItemIndex() {
        return indexer.nextInt(items.length);
    }
}
