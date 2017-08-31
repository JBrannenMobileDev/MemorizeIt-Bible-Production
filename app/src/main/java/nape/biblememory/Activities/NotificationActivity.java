package nape.biblememory.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.Adapters.RecyclerViewAdapterNotifications;
import nape.biblememory.Models.User;
import nape.biblememory.R;
import nape.biblememory.data_store.DataStore;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.notifications_recycler_view)RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterNotifications adapter;
    private List<User> usersThatBlessed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        setTitle("Social Notifications");
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume(){
        super.onResume();
        BaseCallback<List<User>> usersThatGaveBlessingsCallback = new BaseCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                usersThatBlessed = response;
                initializeRecyclerView(response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        DataStore.getInstance().getUsersThatBlessedMe(usersThatGaveBlessingsCallback, getApplicationContext());
    }

    private void initializeRecyclerView(List<User> response) {
        BaseCallback<Integer> friendSelectedCallback = new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                Intent intent = new Intent(getApplicationContext(), FriendDetailsActivity.class);
                intent.putExtra("uid", usersThatBlessed.get(response).getUID());
                intent.putExtra("name", usersThatBlessed.get(response).getName());
                startActivity(intent);
                DataStore.getInstance().deleteFriendBlessing(usersThatBlessed.get(response).getUID(), getApplicationContext());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        adapter = new RecyclerViewAdapterNotifications(response, friendSelectedCallback);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notifications_menu, menu);
        menu.findItem(R.id.action_delete_all).getIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            for(User user : usersThatBlessed){
                DataStore.getInstance().deleteFriendBlessing(user.getUID(), getApplicationContext());
            }
            adapter = new RecyclerViewAdapterNotifications(new ArrayList<User>(), null);
            recyclerView.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
