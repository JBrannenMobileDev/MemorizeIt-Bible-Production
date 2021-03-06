package nape.biblememory.view_layer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @BindView(R.id.privacy_policy_webview)WebView webView;
    @BindView(R.id.privacy_progress_bar)ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);
        setTitle("Privacy policy");
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Privacy policy", null);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                loadingBar.setVisibility(View.GONE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://termsfeed.com/privacy-policy/17cbea6f727c4cf2998b29f884876d8e");
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
