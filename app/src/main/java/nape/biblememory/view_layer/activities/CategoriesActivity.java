package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nape.biblememory.R;
import nape.biblememory.models.Category;
import nape.biblememory.models.MyVerse;
import nape.biblememory.view_layer.expandable_recyclerview.CategoryAdapter;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categories");
        List<Category> categories = getCategories();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.categories_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //instantiate your adapter with the list of genres
        CategoryAdapter adapter = new CategoryAdapter(categories);
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
        categories.add(new Category("Encouragement", cat1MyVerseList));
        return categories;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
