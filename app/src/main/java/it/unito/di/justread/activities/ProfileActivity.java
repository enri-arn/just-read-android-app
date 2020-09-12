package it.unito.di.justread.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.R;
import it.unito.di.justread.model.User;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, TaskCompletionListener<String> {

    private String PROFILE_URL = "http://192.168.0.1:8080/JustRead/api/user/info";

    private FloatingActionButton modifyProfile;
    private TextView mail;
    private TextView name;
    private TextView phoneNumber;
    private TextView address;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GetJsonTask profileTask = new GetJsonTask("GET", this);
        profileTask.execute(PROFILE_URL);

        modifyProfile = (FloatingActionButton)findViewById(R.id.fab_profile_modify);
        modifyProfile.setOnClickListener(this);

        mail = (TextView)findViewById(R.id.lbl_email);
        name = (TextView)findViewById(R.id.lbl_name_surname);
        phoneNumber = (TextView)findViewById(R.id.lbl_phone_number);
        address = (TextView)findViewById(R.id.lbl_address);
        profileImage = (ImageView)findViewById(R.id.imageView_profileActivity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
                mail.setText(bundle.getString("email"));
                name.setText(bundle.getString("name_surname"));
                phoneNumber.setText(bundle.getString("phone_number"));
                address.setText(bundle.getString("address"));
                Picasso.with(getApplicationContext()).load(new File(bundle.getString("image_path"))).fit().centerCrop().into(profileImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onComplete(String result) {
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(result, User.class);
                mail.setText(user.getEmail());
                name.setText(user.getName() + " " + user.getSurname());
                address.setText(user.getAddress() != null ? user.getAddress() : "address not present");
                Picasso.with(getApplicationContext()).load(new File(user.getProfileImage())).fit().centerCrop().into(profileImage);
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
