package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.data_layer.firebase_db.FirebaseDb;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedPresenterInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MyVersesPresenterInterface;

/**
 * Created by jbrannen on 9/3/17.
 */

public class MemorizedPresenter implements MemorizedPresenterInterface {

    private MemorizedFragmentInterface fragment;
    private RealmResults<MemorizedVerse> myVersesRealm;
    private Realm realm;

    public MemorizedPresenter(MemorizedFragmentInterface fragment) {
        this.fragment = fragment;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void fetchData() {
        myVersesRealm = realm.where(MemorizedVerse.class).findAll().sort("listPosition", Sort.ASCENDING);
        fragment.onReceivedRecyclerData(myVersesRealm);
    }

    @Override
    public void onStop() {
        realm.close();
    }
}

