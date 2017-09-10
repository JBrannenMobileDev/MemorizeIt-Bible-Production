package nape.biblememory.view_layer.fragments.interfaces;

import java.util.List;

import io.realm.RealmResults;
import nape.biblememory.models.MyVerse;

/**
 * Created by jbrannen on 9/3/17.
 */

public interface MyVersesPresenterInterface {
    void fetchData();
    void onDatasetChanged(List<MyVerse> response);
    void onStop();
}
