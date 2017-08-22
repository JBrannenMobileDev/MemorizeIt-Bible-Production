package nape.biblememory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import butterknife.BindView;
import nape.biblememory.R;

public class FriendsActivity extends AppCompatActivity {

    @BindView(R.id.friends_search_view)SearchView searchView;
    @BindView(R.id.friends_rv)RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setTitle("Friends");
    }
}
