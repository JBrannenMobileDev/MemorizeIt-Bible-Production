package nape.biblememory.view_layer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 10/1/2016.
 */
public class RecyclerViewSingleton {
    private static RecyclerViewSingleton ourInstance = new RecyclerViewSingleton();

    public static RecyclerViewSingleton getInstance() {
        return ourInstance;
    }

    private RecyclerViewSingleton() {
    }

    public List<String> listOfSelectedBooks = new ArrayList<>();
}
