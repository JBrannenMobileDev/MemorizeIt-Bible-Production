package nape.biblememory.view_layer.activities.presenters;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.models.Category;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.activities.interfaces.CategoriesActivityInterface;
import nape.biblememory.view_layer.activities.interfaces.CategoriesPresenterInterface;

/**
 * Created by jbrannen on 9/27/17.
 */

public class CategoriesPresenter implements CategoriesPresenterInterface {

    private CategoriesActivityInterface view;

    public CategoriesPresenter(CategoriesActivityInterface view){
        this.view = view;
    }

    @Override
    public void buildCategoriesForAdapter(List<List<String>> categoryReferences, List<List<String>> categoryVerses, List<String> categoryNames) {
        List<Category> categories = new ArrayList<>();
        for(int i = 0; i < categoryNames.size(); i++) {
            List<MyVerse> resultCategory = new ArrayList<>();
            for (int j = 0; j < categoryReferences.get(i).size(); j++) {
                MyVerse temp = new MyVerse();
                temp.setVerseLocation(categoryReferences.get(i).get(j));
                temp.setVerse(categoryVerses.get(i).get(j));
                temp.setMemoryStage(0);
                temp.setMemorySubStage(0);
                resultCategory.add(temp);
            }
            categories.add(new Category(categoryNames.get(i), resultCategory));
        }
        view.initAdapter(categories);
    }

    @Override
    public void onVerseSelected(MyVerse verse) {
        if(null != verse) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
            RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();
            boolean alreadyHas = false;
            for (MyVerse verseLocal : myVerses) {
                if (verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())) {
                    alreadyHas = true;
                }
            }
            for (MemorizedVerse verseLocal : memorizedVerses) {
                if (verse.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())) {
                    alreadyHas = true;
                }
            }
            if (alreadyHas) {
                view.launchVerseDetailsActivity(verse.getVerseLocation());
            }
            realm.close();
        }else{
            view.showVerseDetailsErrorToast();
        }
    }
}
