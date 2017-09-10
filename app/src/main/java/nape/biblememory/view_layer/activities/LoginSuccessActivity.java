package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nape.biblememory.data_layer.DataStore;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataStore.getInstance().updateRealm(getApplicationContext());
        finish();
    }
}
