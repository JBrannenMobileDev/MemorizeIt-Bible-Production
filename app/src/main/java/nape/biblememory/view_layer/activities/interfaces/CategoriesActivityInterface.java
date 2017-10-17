package nape.biblememory.view_layer.activities.interfaces;

import java.util.List;

import nape.biblememory.models.Category;

/**
 * Created by jbrannen on 9/27/17.
 */

public interface CategoriesActivityInterface {
    void initAdapter(List<Category> categories);
    void showVerseAddedToast();
    void launchVerseDetailsActivity(String verseLocation);
    void showVerseDetailsErrorToast();
}
