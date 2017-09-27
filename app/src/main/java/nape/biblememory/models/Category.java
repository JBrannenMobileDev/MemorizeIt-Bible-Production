package nape.biblememory.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by jbrannen on 9/27/17.
 */

public class Category extends ExpandableGroup<MyVerse> {
    public Category(String title, List<MyVerse> items) {
        super(title, items);
    }
}
