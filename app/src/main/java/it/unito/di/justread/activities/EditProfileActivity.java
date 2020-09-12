package it.unito.di.justread.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.asynctasks.ActionPerformedTask;
import it.unito.di.justread.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, TaskCompletionListener<Boolean> {

    private EditText mail;
    private EditText password;
    private EditText nameSurname;
    private EditText phone;
    private EditText address;
    private ImageButton changeImageProfile;

    private String mailUser = "default";
    private String nameUser = "default";
    private String phoneUser = "default";
    private String addressUser = "default";

    private String UPDATE_URL = "http://localhost:8080/ServletJustRead/Servlet?action=updateProfile&user=";

    int TAKE_PHOTO_CODE = 0;
    private static final int REQUEST_IMAGE = 100;
    public static int count = 0;
    File destination;
    String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mail = (EditText)findViewById(R.id.editText_email);
        password = (EditText)findViewById(R.id.editText_password);
        nameSurname = (EditText)findViewById(R.id.editText_username);
        phone = (EditText)findViewById(R.id.editText_phonenumber);
        address = (EditText)findViewById(R.id.editText_address);
        changeImageProfile = (ImageButton)findViewById(R.id.change_image_profile);

        mail.setHint("Insert mail here");
        password.setHint("Insert password here");
        nameSurname.setHint("Insert name and surname here");
        phone.setHint("Insert your phone number");
        address.setHint("Insert your address");


        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        String name =  dateToString(new Date(),"yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");


        changeImageProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK ){
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                imagePath = destination.getAbsolutePath();
                Log.i("EXCEPTION", "Image path: " + imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
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
        mailUser = mail.getText().toString();
        nameUser = nameSurname.getText().toString();
        phoneUser = phone.getText().toString();
        ActionPerformedTask actionPerformedTask = new ActionPerformedTask(this);
        actionPerformedTask.execute(UPDATE_URL);
    }

    @Override
    public void onComplete(Boolean result) {
        final Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        if (result){
            intent.putExtra("email", mailUser);
            intent.putExtra("name_surname", nameUser);
            intent.putExtra("phone_number", phoneUser);
            intent.putExtra("address", addressUser);
            if (imagePath != null){
                intent.putExtra("image_path", imagePath);
            }
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(getCurrentFocus(), "Update wasn't successfully, we invite you to try again or go back and do it later", Snackbar.LENGTH_LONG).setAction("GO BACK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                    finish();
                }
            }).show();
        }
    }
}
