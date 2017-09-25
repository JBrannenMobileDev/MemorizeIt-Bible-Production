package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.view_layer.adapters.RecyclerViewAdapterFriendDetails;
import nape.biblememory.models.ScriptureData;
import nape.biblememory.R;
import nape.biblememory.utils.UserPreferences;
import nape.biblememory.data_layer.DataStore;

public class FriendDetailsActivity extends AppCompatActivity {


    @BindView(R.id.friend_details_recycler_view)RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterFriendDetails adapter;
    private BaseCallback<Integer> copyVerseCallback;
    private BaseCallback<List<ScriptureData>> allVersesCallback;
    private BaseCallback<List<ScriptureData>> allMyVersesCallback;
    private List<ScriptureData> allFriendVerses;
    private List<ScriptureData> allMyVerses;
    private UserPreferences mPrefs;
    private String name;
    private String uid;
    private boolean waitingForResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        waitingForResponse = false;
        setContentView(R.layout.activity_friend_details);
        ButterKnife.bind(this);
        mPrefs = new UserPreferences();
        uid = getIntent().getStringExtra("uid");
        initCallbacks(uid);
        name = getIntent().getStringExtra("name");
        setTitle(name);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume(){
        super.onResume();
        DataStore.getInstance().getAllVerses(mPrefs.getUserId(getApplicationContext()), allMyVersesCallback);
    }

    private void initCallbacks(final String uid) {
        allMyVersesCallback = new BaseCallback<List<ScriptureData>>() {
            @Override
            public void onResponse(List<ScriptureData> response) {
                allMyVerses = response;
                allVersesCallback = new BaseCallback<List<ScriptureData>>() {
                    @Override
                    public void onResponse(List<ScriptureData> response) {
                        allFriendVerses = response;
                        initializeRecyclerView(response, allMyVerses);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                DataStore.getInstance().getAllVerses(uid, allVersesCallback);
            }

            @Override
            public void onFailure(Exception e) {
                allVersesCallback = new BaseCallback<List<ScriptureData>>() {
                    @Override
                    public void onResponse(List<ScriptureData> response) {
                        allFriendVerses = response;
                        initializeRecyclerView(response, allMyVerses);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                DataStore.getInstance().getAllVerses(uid, allVersesCallback);
            }
        };

        copyVerseCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                boolean alreadyHaveVerse = false;
                if(!waitingForResponse) {
                    for (ScriptureData verse : allMyVerses) {
                        if (verse.getVerseLocation().equalsIgnoreCase(allFriendVerses.get(response).getVerseLocation())) {
                            alreadyHaveVerse = true;
                        }
                    }
                    if (alreadyHaveVerse) {
                        Toast.makeText(getApplicationContext(), "You already have this verse.", Toast.LENGTH_SHORT).show();
                    } else {
                        ScriptureData verseToCopy = allFriendVerses.get(response);
                        verseToCopy.setMemoryStage(0);
                        verseToCopy.setMemorySubStage(0);
                        DataStore.getInstance().saveQuizVerse(verseToCopy, getApplicationContext());
                    }
                    BaseCallback<List<ScriptureData>> allMyVersesCb = new BaseCallback<List<ScriptureData>>() {
                        @Override
                        public void onResponse(List<ScriptureData> response) {
                            allMyVerses = response;
                            initializeRecyclerView(allFriendVerses, response);
                            waitingForResponse = false;
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    };
                    DataStore.getInstance().getAllVerses(mPrefs.getUserId(getApplicationContext()), allMyVersesCb);
                    waitingForResponse = true;
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    private void initializeRecyclerView(List<ScriptureData> response, List<ScriptureData> allMyVerses) {
        adapter = new RecyclerViewAdapterFriendDetails(response, copyVerseCallback, allMyVerses);
        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_unfollow:
                DataStore.getInstance().unFollowFriend(uid, getApplicationContext());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("uid", uid);
                setResult(1,returnIntent);
                finish();
                break;
            case R.id.action_bless:
                DataStore.getInstance().sendABlessing(uid, getApplicationContext());
                Toast.makeText(this, "Blessing sent to " + name, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_copy_all:
                copyAllVerses();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyAllVerses() {
        if(!waitingForResponse) {
            for (ScriptureData friendVerse : allFriendVerses) {
                boolean alreadyHaveVerse = false;
                for (ScriptureData verse : allMyVerses) {
                    if (verse.getVerseLocation().equalsIgnoreCase(friendVerse.getVerseLocation())) {
                        alreadyHaveVerse = true;
                    }
                }
                if (!alreadyHaveVerse) {
                    ScriptureData verseToCopy = friendVerse;
                    verseToCopy.setMemoryStage(0);
                    verseToCopy.setMemorySubStage(0);
                    DataStore.getInstance().saveQuizVerse(verseToCopy, getApplicationContext());
                }
            }
            BaseCallback<List<ScriptureData>> allMyVersesCb = new BaseCallback<List<ScriptureData>>() {
                @Override
                public void onResponse(List<ScriptureData> response) {
                    initializeRecyclerView(allFriendVerses, response);
                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            DataStore.getInstance().getAllVerses(mPrefs.getUserId(getApplicationContext()), allMyVersesCb);
            waitingForResponse = true;
        }
    }
}
