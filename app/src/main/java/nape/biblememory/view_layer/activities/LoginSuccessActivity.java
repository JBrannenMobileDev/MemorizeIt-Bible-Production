package nape.biblememory.view_layer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nape.biblememory.data_layer.DataStore;
import nape.biblememory.managers.NetworkManager;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NetworkManager.getInstance().isInternet(getApplicationContext())) {
            DataStore.getInstance().updateRealm(getApplicationContext());
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            try {
                getApplicationContext().startActivity(intent);
            }catch(Exception e){

            }
        }
        finish();
    }
}
