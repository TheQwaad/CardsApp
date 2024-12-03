package collection;

public class OneWayCollection extends AbstractCollection {
    public OneWayCollection(String name, String[] paths) {
        super(name);
        OneWayCollectionItem[] items = new OneWayCollectionItem[paths.length];
        for (int i = 0; i < paths.length; i++) {
            items[i] = new OneWayCollectionItem(paths[i]);
        }
        this.items = items;
    }
}
