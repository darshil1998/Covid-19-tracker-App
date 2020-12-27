package com.covid19.covid_19tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.covid19.covid_19tracker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {
    TextView txtCases, txtRecoverd, txtCritical, txtActive, txtTodayCases, txtTotalDeaths, txtTodayDeaths, txtCountryName;
    private Integer countryPosition;
    private InterstitialAd mInterstitialAd;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        countryPosition = intent.getIntExtra("position", 0);

        getSupportActionBar().setTitle("Details of " + AffectedCountriesActivity.countryModelList.get(countryPosition).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adView = findViewById(R.id.adView);
        txtCases = findViewById(R.id.txtCases);
        txtRecoverd = findViewById(R.id.txtRecoverd);
        txtCritical = findViewById(R.id.txtCritical);
        txtActive = findViewById(R.id.txtActive);
        txtTodayCases = findViewById(R.id.txtTodayCases);
        txtTotalDeaths = findViewById(R.id.txtTotalDeaths);
        txtTodayDeaths = findViewById(R.id.txtTodayDeaths);
        txtCountryName = findViewById(R.id.txtCountryName);

        MobileAds.initialize(this, "ca-app-pub-3596102289377036~3753165417");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        prepareAdd();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("Tag", "intersial add is not loaded");
                        }
                        prepareAdd();
                    }
                });
            }
        }, 0, 2, TimeUnit.MINUTES);

        countryDetails();
    }

    private void countryDetails() {
        txtCountryName.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getCountry());
        txtCases.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getCases());
        txtRecoverd.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getRecovered());
        txtCritical.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getCritical());
        txtActive.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getActive());
        txtTodayCases.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getTodayCases());
        txtTotalDeaths.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getDeaths());
        txtTodayDeaths.setText(AffectedCountriesActivity.countryModelList.get(countryPosition).getTodayDeaths());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void prepareAdd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3596102289377036/2670080235");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

}
