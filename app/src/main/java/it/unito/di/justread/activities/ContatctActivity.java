package it.unito.di.justread.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.clans.fab.FloatingActionButton;

import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.R;
import it.unito.di.justread.model.Rule;

public class ContatctActivity extends AppCompatActivity implements TaskCompletionListener<String> {

    private String CONTACT = "http://192.168.0.1:8080/JustRead/api/contacts/rule";

    private TextView libraryStreet;
    private TextView libraryPhone;
    private TextView libraryMail;
    private TextView webSite;
    private FloatingActionButton phone;
    private FloatingActionButton mail;
    private FloatingActionButton web;

    String phoneNumber = "tel:";
    String mailLibrary = "mailto:?subject=";
    String webSiteLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GetJsonTask getJsonTask = new GetJsonTask("GET", this);
        getJsonTask.execute(CONTACT);

        libraryStreet = findViewById(R.id.library_street_contact);
        libraryPhone = findViewById(R.id.library_phone_contact);
        libraryMail = findViewById(R.id.library_mail_contact);
        webSite = findViewById(R.id.library_site_contact);
        phone = findViewById(R.id.fab_phone_indicator);
        mail = findViewById(R.id.fab_mail_indicator);
        web = findViewById(R.id.fab_site_indicator);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumber));
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse(mailLibrary);
                intent.setData(data);
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(webSiteLib));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onComplete(String result) {
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Rule rule = mapper.readValue(result, Rule.class);
                libraryStreet.setText(rule.getAddress());
                libraryMail.setText("justread@gmail.com");
                libraryPhone.setText(rule.getPhoneNumber());
                webSite.setText("www.JustRead.it");
                phoneNumber += rule.getPhoneNumber();
                mailLibrary += "justread@gmail.com";
                webSiteLib = "http://192.168.0.1:8080/JustRead/app/catalog";
            } catch (Exception e) {
                Log.i("EXCEPTION", "Error parsing jsonArray");
            }
        } else {
            Intent goToOption = new Intent(getApplicationContext(), ServerOfflineActivity.class);
            startActivity(goToOption);
            finish();
        }
    }
}
