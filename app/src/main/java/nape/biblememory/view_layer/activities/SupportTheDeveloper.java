package nape.biblememory.view_layer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import nape.biblememory.R;

public class SupportTheDeveloper extends AppCompatActivity {

    @BindView(R.id.support_the_dev_share_button)Button shareBt;
    @BindView(R.id.support_the_dev_view_ad_bt)Button viewAdBt;
    @BindView(R.id.support_the_dev_donate_bt)Button donateBt;
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private IInAppBillingService billingService;
    private ServiceConnection billingServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_the_developer);
        ButterKnife.bind(this);
        setTitle("Support the developer!");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        mFirebaseAnalytics.setCurrentScreen(this, "Support the developer activity", null);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                viewAdBt.setBackgroundColor(getResources().getColor(R.color.greyBgDark));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                viewAdBt.setEnabled(true);
                viewAdBt.setBackgroundColor(getResources().getColor(R.color.colorProgressBg));
            }
        });

        viewAdBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("view_ad_selected", null);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("share_selected", null);
                sendShareIntent();
            }
        });

        donateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAnalytics.logEvent("donate_selected", null);
                Toast.makeText(getApplicationContext(), "This feature is in progress. The production app version will include this.", Toast.LENGTH_LONG).show();
            }
        });


        billingServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                billingService = IInAppBillingService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                billingService = null;
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, billingServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (billingService != null) {
            unbindService(billingServiceConnection);
        }
    }


    private void sendShareIntent(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "MemorizeIt Bible");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=nape.biblememory&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {

        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }
}
