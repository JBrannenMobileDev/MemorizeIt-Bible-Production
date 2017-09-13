package nape.biblememory.view_layer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.activities.BaseCallback;
import nape.biblememory.view_layer.activities.MemorizedVerseDetailsActivity;
import nape.biblememory.view_layer.activities.VerseDetailsActivity;
import nape.biblememory.view_layer.adapters.RecyclerViewAdapterMemorized;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedFragmentInterface;
import nape.biblememory.view_layer.fragments.interfaces.MemorizedPresenterInterface;
import nape.biblememory.view_layer.fragments.presenters.MemorizedPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemorizedFragment extends Fragment implements MemorizedFragmentInterface {
    private MemorizedPresenterInterface presenter;
    private View view;
    private RecyclerViewAdapterMemorized adapter;
    private BaseCallback<MemorizedVerse> itemSelectedCallback;
    private BaseCallback<MemorizedVerse> shareSelectedCallback;
    private BaseCallback<MemorizedVerse> reviewNowSelectedCallback;
    @BindView(R.id.memorized_recycler_view)RecyclerView recyclerView;

    public MemorizedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_memorized, container, false);
        ButterKnife.bind(this, v);
        this.presenter = new MemorizedPresenter(this);
        this.view = v;
        initCallbacks();
        return v;
    }

    private void initCallbacks() {
        itemSelectedCallback = new BaseCallback<MemorizedVerse>() {
            @Override
            public void onResponse(MemorizedVerse verse) {
                Intent intent = new Intent(getActivity(), MemorizedVerseDetailsActivity.class);
                if(verse.isForgotten()){
                    intent.putExtra("reviewNow", true);
                }
                intent.putExtra("verseLocation", verse.getVerseLocation());
                startActivityForResult(intent, 3);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        shareSelectedCallback = new BaseCallback<MemorizedVerse>() {
            @Override
            public void onResponse(MemorizedVerse verse) {
                sendShareIntent(verse);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        reviewNowSelectedCallback = new BaseCallback<MemorizedVerse>() {
            @Override
            public void onResponse(MemorizedVerse verse) {
                Intent intent = new Intent(getActivity(), MemorizedVerseDetailsActivity.class);
                intent.putExtra("verseLocation", verse.getVerseLocation());
                intent.putExtra("reviewNow", true);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    private void sendShareIntent(MemorizedVerse verse){
        try {
            UserPreferences mPrefs = new UserPreferences();
            mPrefs.setComingFromMemorizedDetails(true, getActivity().getApplicationContext());
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, verse.getVerseLocation() + "  " + "Memorize It - Bible");
            String sAux = "\n" +verse.getVerseLocation() + "  " + verse.getVerse() + "\n\n";
            sAux = sAux + "Hey! I just memorized " + verse.getVerseLocation() +  " using this app.  It gives me a quiz every time I open my phone.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.fetchData();
    }

    @Override
    public void onStop(){
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onReceivedRecyclerData(List<MemorizedVerse> myVerses) {
        adapter = new RecyclerViewAdapterMemorized(myVerses, itemSelectedCallback, shareSelectedCallback, reviewNowSelectedCallback);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
