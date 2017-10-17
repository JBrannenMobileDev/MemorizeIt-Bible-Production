package nape.biblememory.view_layer.activities.interfaces;

import java.util.List;

import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/27/17.
 */

public interface CategoriesPresenterInterface {
    void buildCategoriesForAdapter(List<List<String>> categoryReferences, List<List<String>> categoryVerses, List<String> categoryNames);
    void onVerseSelected(MyVerse verse);
}
