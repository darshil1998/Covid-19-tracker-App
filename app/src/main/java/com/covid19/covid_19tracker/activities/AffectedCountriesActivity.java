package com.covid19.covid_19tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
import com.covid19.covid_19tracker.adapters.CountryCaseAdapter;
import com.covid19.covid_19tracker.models.CountryModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountriesActivity extends AppCompatActivity {
    EditText edtSearch;
    ListView listCountryView;
    SimpleArcLoader arcLoader;
    CountryCaseAdapter countryCaseAdapter;
    public static List<CountryModel> countryModelList = new ArrayList<>();
    CountryModel countryModel;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);
        edtSearch = findViewById(R.id.edtSearch);
        listCountryView = findViewById(R.id.listCountryView);
        arcLoader = findViewById(R.id.arcLoader);
        adView = findViewById(R.id.adView);
        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MobileAds.initialize(this, "ca-app-pub-3596102289377036~3753165417");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        fetchData();

        listCountryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AffectedCountriesActivity.this, DetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countryCaseAdapter.getFilter().filter(s);
                countryCaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        arcLoader.start();
        String url = "https://corona.lmao.ninja/v2/countries/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (countryModelList != null) {
                    countryModelList.clear();
                }
                try {
                    JSONArray jsonCountryArray = new JSONArray(response);
                    for (int i = 0; i < jsonCountryArray.length(); i++) {

                        JSONObject jsonCountryObject = jsonCountryArray.getJSONObject(i);
                        String countryName = jsonCountryObject.getString("country");
                        String cases = jsonCountryObject.getString("cases");
                        String todayCases = jsonCountryObject.getString("todayCases");
                        String deaths = jsonCountryObject.getString("deaths");
                        String todayDeaths = jsonCountryObject.getString("todayDeaths");
                        String recovered = jsonCountryObject.getString("recovered");
                        String active = jsonCountryObject.getString("active");
                        String critical = jsonCountryObject.getString("critical");
                        JSONObject flagObject = jsonCountryObject.getJSONObject("countryInfo");
                        String flagUrl = flagObject.getString("flag");

                        countryModel = new CountryModel(flagUrl, countryName, cases, todayCases, deaths, todayDeaths, recovered, active, critical);
                        countryModelList.add(countryModel);
                    }
                    countryCaseAdapter = new CountryCaseAdapter(AffectedCountriesActivity.this, countryModelList);
                    listCountryView.setAdapter(countryCaseAdapter);
                    arcLoader.stop();
                    arcLoader.setVisibility(View.GONE);

                } catch (JSONException e) {
                    arcLoader.stop();
                    arcLoader.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(AffectedCountriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arcLoader.stop();
                arcLoader.setVisibility(View.GONE);

                Toast.makeText(AffectedCountriesActivity.this,"Require internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
