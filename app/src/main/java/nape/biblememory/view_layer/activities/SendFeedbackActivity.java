package nape.biblememory.view_layer.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nape.biblememory.R;
import nape.biblememory.utils.SendEmail;
import nape.biblememory.utils.UserPreferences;

public class SendFeedbackActivity extends AppCompatActivity {

    @BindView(R.id.feedback_edittext)EditText userInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        ButterKnife.bind(this);
        setTitle("Feedback");
    }

    @OnClick(R.id.feedback_send_bt)
    public void onClick(){
        if(userInput.getText().length() > 0) {
            new Send().execute(userInput.getText().toString());
            Toast.makeText(getApplicationContext(), "Feedback sent!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class Send extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            UserPreferences mPrefs = new UserPreferences();
            SendEmail sendEmail = new SendEmail();
            sendEmail.sendEmail("MemorizeIt Bible User Feedback", params[0] + "\n" + "UID: " +
                    mPrefs.getUserId(getApplicationContext()) + "\n" + mPrefs.getUserEmail(getApplicationContext()), getApplicationContext());
            return "Executed";
        }
    }
}
