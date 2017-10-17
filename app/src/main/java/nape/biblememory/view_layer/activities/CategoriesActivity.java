package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;
import nape.biblememory.data_layer.DataStore;
import nape.biblememory.models.Category;
import nape.biblememory.models.MyVerse;
import nape.biblememory.models.UserPreferencesModel;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.view_layer.activities.interfaces.CategoriesActivityInterface;
import nape.biblememory.view_layer.activities.interfaces.CategoriesPresenterInterface;
import nape.biblememory.view_layer.activities.presenters.CategoriesPresenter;
import nape.biblememory.view_layer.expandable_recyclerview.CategoryAdapter;

public class CategoriesActivity extends AppCompatActivity implements CategoriesActivityInterface {
    private static final String TAG = CategoriesActivity.class.getName();
    @BindView(R.id.categories_recycler_view) RecyclerView recyclerViewExpandable;
    private CategoryAdapter adapter;
    private BaseCallback<MyVerse> addVerseCallback;
    private BaseCallback<MyVerse> verseSelected;
    private LinearLayoutManager layoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CategoriesPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        setTitle(getString(R.string.categories_activity_title));
        presenter = new CategoriesPresenter(this);
        initFirebase();
        initCallbacks();
        initData();
    }

    private void initData() {
        List<List<String>> categoryReferences = new ArrayList<>();
        categoryReferences.add(Arrays.asList(getResources().getStringArray(R.array.category_peace_reference_esv)));
        categoryReferences.add(Arrays.asList(getResources().getStringArray(R.array.category_encouragement_reference_esv)));

        List<List<String>> categoryVerses = new ArrayList<>();
        categoryVerses.add(Arrays.asList(getResources().getStringArray(R.array.category_peace_text_esv)));
        categoryVerses.add(Arrays.asList(getResources().getStringArray(R.array.category_encouragement_text_esv)));

        List<String> categoryNames = Arrays.asList(getResources().getStringArray(R.array.category_names));

        presenter.buildCategoriesForAdapter(categoryReferences, categoryVerses, categoryNames);
    }

    private void initCallbacks() {
        addVerseCallback = new BaseCallback<MyVerse>() {
            @Override
            public void onResponse(MyVerse response) {
                updatefirebaseAnalytics(response);
                updateUserPrefSettings();
                DataStore.getInstance().saveQuizVerse(response.toScriptureData(), getApplicationContext());//Updates the local and Firebase DB with the added verse.
                showVerseAddedToast();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(CategoriesActivity.this, R.string.verse_added_error_toast_msg, Toast.LENGTH_SHORT).show();
            }
        };

        verseSelected = new BaseCallback<MyVerse>() {
            @Override
            public void onResponse(MyVerse response) {
                presenter.onVerseSelected(response);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, e.getMessage());
                showVerseDetailsErrorToast();
            }
        };
    }

    private void initFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(this, getString(R.string.categories_firebase_screen_name), null);
    }

    private void updatefirebaseAnalytics(MyVerse response){
        Bundle bundle = new Bundle();
        bundle.putString("verse_added_categories", response.getVerseLocation());
        mFirebaseAnalytics.logEvent("verse_added_categories", bundle);
    }

    private void updateUserPrefSettings(){
        UserPreferencesModel model = new UserPreferencesModel();
        UserPreferences mPrefs = new UserPreferences();
        mPrefs.setTourStep1Complete(true, getApplicationContext());
        model.initAllData(getApplicationContext(), mPrefs);
        DataStore.getInstance().saveUserPrefs(model, getApplicationContext());
    }

    @Override
    public void initAdapter(List<Category> categories){
        layoutManager = new LinearLayoutManager(this);
        adapter = new CategoryAdapter(categories);
        adapter.setCallback(addVerseCallback, verseSelected);
        recyclerViewExpandable.setLayoutManager(layoutManager);
        recyclerViewExpandable.setAdapter(adapter);
    }

    @Override
    public void showVerseAddedToast(){
        Toast.makeText(CategoriesActivity.this, R.string.verse_added_toast_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showVerseDetailsErrorToast() {
        Toast.makeText(CategoriesActivity.this, R.string.verse_selected_eeror_toast_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launchVerseDetailsActivity(String verseLocation){
        Intent intent = new Intent(getApplicationContext(), VerseDetailsActivity.class);
        intent.putExtra("verseLocation", verseLocation);
        startActivity(intent);
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
