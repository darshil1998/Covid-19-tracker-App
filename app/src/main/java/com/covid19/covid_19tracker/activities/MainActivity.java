package com.covid19.covid_19tracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.covid19.covid_19tracker.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtCases, txtRecoverd, txtCritical, txtActive, txtTodayCases, txtTotalDeaths, txtTodayDeaths, txtAffectedCountries;
    ScrollView scrollView;
    Button btnTrack;
    PieChart pieChart;
    SimpleArcLoader loader;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCases = findViewById(R.id.txtCases);
        txtRecoverd = findViewById(R.id.txtRecoverd);
        txtCritical = findViewById(R.id.txtCritical);
        txtActive = findViewById(R.id.txtActive);
        txtTodayCases = findViewById(R.id.txtTodayCases);
        txtTotalDeaths = findViewById(R.id.txtTotalDeaths);
        txtTodayDeaths = findViewById(R.id.txtTodayDeaths);
        txtAffectedCountries = findViewById(R.id.txtAffectedCountries);
        adView = findViewById(R.id.adView);
        loader = findViewById(R.id.arcLoader);
        scrollView = findViewById(R.id.scrollState);
        btnTrack = findViewById(R.id.btnTrack);
        pieChart = findViewById(R.id.piechart);
        btnTrack.setOnClickListener(this);

        MobileAds.initialize(this, "ca-app-pub-3596102289377036~3753165417");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        fetchData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.contact) {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        loader.start();
        String url = "https://corona.lmao.ninja/v2/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    txtCases.setText(jsonObject.getString("cases"));
                    txtRecoverd.setText(jsonObject.getString("recovered"));
                    txtCritical.setText(jsonObject.getString("critical"));
                    txtActive.setText(jsonObject.getString("active"));
                    txtTodayCases.setText(jsonObject.getString("todayCases"));
                    txtTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                    txtTotalDeaths.setText(jsonObject.getString("deaths"));
                    txtAffectedCountries.setText(jsonObject.getString("affectedCountries"));

                    pieChart.addPieSlice(new PieModel("cases", Integer.parseInt(txtCases.getText().toString()), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("recovered", Integer.parseInt(txtRecoverd.getText().toString()), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("deaths", Integer.parseInt(txtTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("active", Integer.parseInt(txtActive.getText().toString()), Color.parseColor("#29B6F6")));

                    loader.stop();
                    loader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Require internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTrack) {
            Intent intent = new Intent(this, AffectedCountriesActivity.class);
            startActivity(intent);
        }
    }

}
