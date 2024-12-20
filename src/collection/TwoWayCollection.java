package collection;

import helpers.Pair;

public class TwoWayCollection extends AbstractCollection {
    public TwoWayCollection(String name, Pair<String, String>[] paths) {
        super(name);
        TwoWayCollectionItem[] items = new TwoWayCollectionItem[paths.length];
        for (int i = 0; i < paths.length; i++) {
            items[i] = new TwoWayCollectionItem(paths[i].first(), paths[i].second());
        }
        this.items = items;
    }
}
