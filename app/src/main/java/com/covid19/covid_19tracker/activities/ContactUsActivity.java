package com.covid19.covid_19tracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.covid19.covid_19tracker.R;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtPhoneCall, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        txtPhoneCall = findViewById(R.id.txtPhoneCall);
        txtEmail = findViewById(R.id.txtEmail);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtPhoneCall.setOnClickListener(this);
        txtEmail.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtPhoneCall)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+917069905083"));
            startActivity(callIntent);
        }
        else if (v.getId()==R.id.txtEmail)
        {
            Intent emailIntent = new Intent (Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"savaliyadarshil112@gmail.com"});
            startActivity(Intent.createChooser(emailIntent,"choose an email client"));
        }
    }
}
