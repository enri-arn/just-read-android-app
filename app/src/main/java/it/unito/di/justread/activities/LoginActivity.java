package it.unito.di.justread.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import it.unito.di.justread.asynctasks.AuthenticationTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.R;

public class LoginActivity extends AppCompatActivity implements TaskCompletionListener<Boolean>, View.OnClickListener {

    private static final String LOGINURL = "http://192.168.0.1:8080/JustRead/api/login";

    private Button loginButton;
    private TextView registerLink;
    private EditText emailInput;
    private EditText passwordInput;
    private AuthenticationTask authenticationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        emailInput = findViewById(R.id.login_input_field);
        passwordInput = findViewById(R.id.password_input_field);
        authenticationTask = new AuthenticationTask(this, LOGINURL);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onComplete(Boolean result) {
        if (result) {
            Intent intent = new Intent(this, MainActivity.class);
            Snackbar.make(getCurrentFocus(), "Welcome on JustRead", Snackbar.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(getCurrentFocus(), "Authentication failed", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        String email = emailInput.getText().toString();
        String token = passwordInput.getText().toString();
        if (isValidEmail(email)) {
            if (isValidPassword(token)) {
                authenticationTask.execute(email, token);
            } else {
                Snackbar.make(getCurrentFocus(), "password not correct", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(getCurrentFocus(), "email not valid", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Validation of email with regex roule.
     *
     * @param email: email to validate.
     * @return true if validation was succesful, false otherwise.
     */
    private boolean isValidEmail(String email) {
        System.out.println(email.matches("\\S+@\\S+\\.\\S+"));
        return email.matches("\\S+@\\S+\\.\\S+");
    }

    /**
     * Validation of password with regex roule. Password must contain:
     * - min 8 characters;
     * - at least one uppercase letter;
     * - at least one lowercase letter;
     * - at least one number;
     * - at least one special characters;
     *
     * @param password: password to validate.
     * @return true if validation was succesful, false otherwise.
     */
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$");
    }
}
