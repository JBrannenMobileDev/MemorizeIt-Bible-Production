package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MyVerse;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPresenterInterface;

/**
 * Created by jbrannen on 9/3/17.
 */

public class MyVersesPresenter implements MyVersesPresenterInterface {

    private MyVersesFragmentInterface fragment;
    private Context context;
    private RealmResults<MyVerse> myVersesRealm;
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
        List<MyVerse> reOrderedVerses = new ArrayList<>();
        for(int i = 0; i < myVersesRealm.size(); i++) {
            if (myVersesRealm.get(i).getGoldStar() == 1) {
                reOrderedVerses.add(myVersesRealm.get(i));
            }
        }
        for(int i = 0; i < myVersesRealm.size(); i++) {
            if (myVersesRealm.get(i).getGoldStar() == 0) {
                reOrderedVerses.add(myVersesRealm.get(i));
            }
        }
        fragment.onReceivedRecyclerData(reOrderedVerses);
    }

    @Override
    public void onDatasetChanged(List<MyVerse> response) {
        listToUpdate = response;
        if(listToUpdate != null){
            for(int i = 0; i < listToUpdate.size(); i++){
                listToUpdate.get(i).setListPosition(i);
            }
            updateRealm();
        }
    }

    @Override
    public void onStop() {
        if(listToUpdate != null) {
            for (int i = 0; i < listToUpdate.size(); i++) {
                DataStore.getInstance().updateQuizVerse(myVersesRealm.get(i), listToUpdate.get(i), context.getApplicationContext());
            }
        }
    }

    private void updateRealm() {
        if(listToUpdate != null) {
            if (listToUpdate.size() != myVersesRealm.size()) {
                for (final MyVerse verseToRemove : myVersesRealm) {
                    boolean deletVerse = true;
                    for (MyVerse checkVerses : listToUpdate) {
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
                    for(MyVerse verseToRemove : myVersesRealm) {
                        MyVerse realmVerse = realm.where(MyVerse.class).equalTo("verseLocation", verseToRemove.getVerseLocation()).findFirst();
                        realmVerse.deleteFromRealm();
                    }
                }
            });
        }
        DataStore.getInstance().updateQuizVerseToRealm(listToUpdate);
    }
}

