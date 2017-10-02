package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.models.TopVerse;
import nape.biblememory.view_layer.adapters.RecyclerViewAdapterPopularVerses;

public class PopularVerses extends AppCompatActivity {

    @BindView(R.id.popular_verses_recycler)RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapterPopularVerses adapter;
    private List<TopVerse> topTenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_verses);
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        initRecyclerView();
    }

    private void initRecyclerView() {
        BaseCallback<List<ScriptureData>> allMemorizedVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                if(response != null && response.size() > 0) {
                    topTenList = generateTopTenList(response);
//                    adapter = new RecyclerViewAdapterPopularVerses(topTenList, )
                }else{
                    topTenList = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getAllMemorizedVerses(allMemorizedVersesCallback);
    }

    private List<TopVerse> generateTopTenList(List<ScriptureData> response) {
        List<TopVerse> topVerses = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        List<TopVerse> resultList = new ArrayList<>();
        for(ScriptureData verse : response){
            topVerses.add(verse.toTopVerseData());
        }
        Collections.sort(topVerses);

        int currentCount = 0;
        String currentVerseLocation;
        String previousVerseLocation = topVerses.get(0).getVerseLocation();
        for(int i = 1; i < topVerses.size(); i++){
            currentCount++;
            currentVerseLocation = topVerses.get(i).getVerseLocation();
            if(!currentVerseLocation.equalsIgnoreCase(previousVerseLocation)) {
                if (resultList.isEmpty()) {
                    resultList.add(topVerses.get(i));
                    countList.add(currentCount);
                } else {
                    for(int j = 0; j < countList.size(); j++){
                        if(currentCount >= countList.get(j)){
                            resultList.add(j, topVerses.get(i));
                            countList.add(j, currentCount);
                        }
                    }
                }
                currentCount = 0;
            }else{
                previousVerseLocation = currentVerseLocation;
            }
        }
        return resultList;
    }

}
