package nape.biblememory.view_layer.fragments.presenters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        fragment.onReceivedRecyclerData(sortResponse(myVersesRealm));
    }

    private List<MemorizedVerse> sortResponse(RealmResults<MemorizedVerse> myVersesRealm) {
        List<MemorizedVerse> forgottenList = new ArrayList<>();
        List<MemorizedVerse> pastDueList = new ArrayList<>();
        List<MemorizedVerse> dueList = new ArrayList<>();
        List<MemorizedVerse> currentList = new ArrayList<>();
        List<MemorizedVerse> result = new ArrayList<>();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();


        for(MemorizedVerse verse : myVersesRealm) {
            Calendar reviewDatePlusOneMonth = Calendar.getInstance();
            Calendar reviewDatePlusOneWeek = Calendar.getInstance();
            Date lastReviewed = null;
            try {
                lastReviewed = formatter.parse(verse.getLastSeenDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (lastReviewed != null) {
                reviewDatePlusOneMonth.setTime(lastReviewed);
                reviewDatePlusOneMonth.add(Calendar.DATE, 30);
                reviewDatePlusOneWeek.setTime(lastReviewed);
                reviewDatePlusOneWeek.add(Calendar.DATE, 7);
            }
            if (verse.isForgotten()) {
               forgottenList.add(verse);
            } else if (lastReviewed != null && reviewDatePlusOneMonth.before(cal)) {
                pastDueList.add(verse);
            } else if (lastReviewed != null && reviewDatePlusOneWeek.before(cal)) {
                dueList.add(verse);
            } else {
                currentList.add(verse);
            }
        }

        result.addAll(forgottenList);
        result.addAll(pastDueList);
        result.addAll(dueList);
        result.addAll(currentList);
        return result;
    }

    @Override
    public void onStop() {
        realm.close();
    }
}

