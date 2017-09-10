package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MyVerse;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPresenterInterface;

/**
 * Created by jbrannen on 9/3/17.
 */

public class MyVersesPresenter implements MyVersesPresenterInterface {

    private MyVersesFragmentInterface fragment;
    private Context context;
    private List<MyVerse> myVersesRealm;
    private List<MyVerse> myVerses;
    private List<MyVerse> listToUpdate;
    private Realm realm;

    public MyVersesPresenter(MyVersesFragmentInterface fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void fetchData() {
        myVersesRealm = realm.where(MyVerse.class).findAll().sort("listPosition", Sort.ASCENDING);
        myVerses = new ArrayList<>(myVersesRealm);
        fragment.onReceivedRecyclerData(myVerses);
    }

    @Override
    public void onDatasetChanged(List<MyVerse> response) {
        listToUpdate = response;
        if(response != null) {
            if (response.size() != myVersesRealm.size()) {
                for (final MyVerse verseToRemove : myVersesRealm) {
                    boolean deletVerse = true;
                    for (MyVerse checkVerses : response) {
                        if (verseToRemove.getVerseLocation().equalsIgnoreCase(checkVerses.getVerseLocation())) {
                            deletVerse = false;
                        }
                    }
                    if (deletVerse) {
                        FirebaseDb.getInstance().deleteQuizVerse(verseToRemove, context);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verseToRemove.getVerseLocation()).findFirst();
                                realmVerse.deleteFromRealm();
                            }
                        });
                    }
                }
            }
        }else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for(MyVerse verseToRemove : myVerses) {
                        MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verseToRemove.getVerseLocation()).findFirst();
                        realmVerse.deleteFromRealm();
                    }
                }
            });
        }
        DataStore.getInstance().updateQuizVerseToRealm(listToUpdate);
    }

    @Override
    public void onStop() {
        if(listToUpdate != null) {
            for(int i = 0; i < listToUpdate.size(); i++){
                listToUpdate.get(i).setListPosition(i);
            }
            for (int i = 0; i < listToUpdate.size(); i++) {
                DataStore.getInstance().updateQuizVerse(myVersesRealm.get(i), listToUpdate.get(i), context.getApplicationContext());
            }
            realm.close();
        }
    }
}
