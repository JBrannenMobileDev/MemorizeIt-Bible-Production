package nape.biblememory.view_layer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import nape.biblememory.R;
import nape.biblememory.models.MemorizedVerse;

public class MemorizedVerseDetailsFragment extends Fragment {
    @BindView(R.id.verse_details_verse)TextView verseText;
    @BindView(R.id.verse_details_last_seen_tv)TextView lastSeen;
    @BindView(R.id.verse_details_progress_tv)TextView progressTv;
    private MemorizedVerse verse;
    private String verseLocation;
    private Realm realm;

    public MemorizedVerseDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_memorized_verse_details, container, false);
        ButterKnife.bind(this, v);
        verseLocation = getArguments().getString("verseLocation");
        realm = Realm.getDefaultInstance();
        verse = realm.where(MemorizedVerse.class).equalTo("verseLocation", verseLocation).findFirst();
        verse.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                initView(verse);
            }
        });
        initView(verse);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void initView(MemorizedVerse verse) {
        if(verse.isValid()) {
            this.verse = verse;
            verseText.setText(verse.getVerse());
            lastSeen.setText(verse.getLastSeenDate());
            if (verse.isForgotten()) {
                progressTv.setText("Forgotten");
            } else {
                progressTv.setText("100%");
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        realm.close();
    }

}
