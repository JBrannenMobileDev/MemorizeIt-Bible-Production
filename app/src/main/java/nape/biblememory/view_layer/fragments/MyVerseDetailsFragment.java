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
import nape.biblememory.models.MyVerse;

public class MyVerseDetailsFragment extends Fragment {
    @BindView(R.id.verse_details_verse)TextView verseText;
    @BindView(R.id.verse_details_last_seen_tv)TextView lastSeen;
    @BindView(R.id.verse_details_progress_tv)TextView progressTv;
    private MyVerse verse;
    private String verseLocation;
    private Realm realm;

    public MyVerseDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_verse_details, container, false);
        ButterKnife.bind(this, v);
        verseLocation = getArguments().getString("verseLocation");
        realm = Realm.getDefaultInstance();
        verse = realm.where(MyVerse.class).equalTo("verseLocation", verseLocation).findFirst();
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
        getActivity().setTitle(verseLocation);
    }

    private void initView(MyVerse verse) {
        if(verse.isValid()) {
            this.verse = verse;
            verseText.setText(verse.getVerse());
            lastSeen.setText(verse.getLastSeenDate());
            progressTv.setText(String.valueOf(calculateProgress(verse.getMemoryStage(), verse.getMemorySubStage())) + "%");
        }
    }

    private int calculateProgress(int memoryStage, int memorySubStage) {
        double progress;
        switch (memoryStage) {
            case 0:
                progress = 0;
                break;
            case 1:
                progress = 1 + memorySubStage;
                break;
            case 2:
                progress = 4 + memorySubStage;
                break;
            case 3:
                progress = 7 + memorySubStage;
                break;
            case 4:
                progress = 10 + memorySubStage;
                break;
            case 5:
                progress = 13 + memorySubStage;
                break;
            case 6:
                progress = 16 + memorySubStage;
                break;
            case 7:
                progress = 19;
                break;
            default:
                progress = 0;
        }
        progress = (progress/20)*100;
        return (int)progress;
    }

    @Override
    public void onStop(){
        super.onStop();
        realm.close();
    }

}
