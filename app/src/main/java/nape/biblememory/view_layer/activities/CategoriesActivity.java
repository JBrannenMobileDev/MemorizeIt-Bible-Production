package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.Category;
import nape.biblememory.models.MemorizedVerse;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.expandable_recyclerview.CategoryAdapter;

public class CategoriesActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private BaseCallback<MyVerse> addVerseSelected;
    private BaseCallback<MyVerse> verseSelected;
    private List<Category> categories;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categories");
        categories = getCategories();
        recyclerView = (RecyclerView) findViewById(R.id.categories_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        addVerseSelected = new BaseCallback<MyVerse>() {
            @Override
            public void onResponse(MyVerse response) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
                RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();
                boolean alreadyHas = false;
                for(MyVerse verseLocal : myVerses){
                    if(response.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                        alreadyHas = true;
                    }
                }

                for(MemorizedVerse verseLocal : memorizedVerses){
                    if(response.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                        alreadyHas = true;
                    }
                }
                if(!alreadyHas){
                    DataStore.getInstance().saveQuizVerse(response.toScriptureData(), getApplicationContext());
                    Toast.makeText(CategoriesActivity.this, "Verse added!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                realm.close();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        verseSelected = new BaseCallback<MyVerse>() {
            @Override
            public void onResponse(MyVerse response) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<MyVerse> myVerses = realm.where(MyVerse.class).findAll();
                RealmResults<MemorizedVerse> memorizedVerses = realm.where(MemorizedVerse.class).findAll();
                boolean alreadyHas = false;
                for(MyVerse verseLocal : myVerses){
                    if(response.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                        alreadyHas = true;
                    }
                }

                for(MemorizedVerse verseLocal : memorizedVerses){
                    if(response.getVerseLocation().equalsIgnoreCase(verseLocal.getVerseLocation())){
                        alreadyHas = true;
                    }
                }
                if(alreadyHas){
                    Intent intent = new Intent(getApplicationContext(), VerseDetailsActivity.class);
                    intent.putExtra("verseLocation", response.getVerseLocation());
                    startActivity(intent);
                }
                realm.close();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        initAdapter();
    }

    private void initAdapter(){
        //instantiate your adapter with the list of genres
        adapter = new CategoryAdapter(categories);
        adapter.setCallback(addVerseSelected, verseSelected);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public List<Category> getCategories() {
        List<MyVerse> cat1MyVerseList = new ArrayList<>();
        List<String> category1References = Arrays.asList(getResources().getStringArray(R.array.category_peace_reference_esv));
        List<String> category1Verse = Arrays.asList(getResources().getStringArray(R.array.category_peace_text_esv));

        List<MyVerse> cat2MyVerseList = new ArrayList<>();
        List<String> category2References = Arrays.asList(getResources().getStringArray(R.array.category_encouragement_reference_esv));
        List<String> category2Verse = Arrays.asList(getResources().getStringArray(R.array.category_encouragement_text_esv));

        for(int i = 0; i < category1References.size(); i++){
            MyVerse temp = new MyVerse();
            temp.setVerseLocation(category1References.get(i));
            temp.setVerse(category1Verse.get(i));
            temp.setMemoryStage(0);
            temp.setMemorySubStage(0);
            cat1MyVerseList.add(temp);
        }

        for(int i = 0; i < category2References.size(); i++){
            MyVerse temp = new MyVerse();
            temp.setVerseLocation(category2References.get(i));
            temp.setVerse(category2Verse.get(i));
            temp.setMemoryStage(0);
            temp.setMemorySubStage(0);
            cat2MyVerseList.add(temp);
        }
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Peace", cat1MyVerseList));
        categories.add(new Category("Encouragement", cat2MyVerseList));
        return categories;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
