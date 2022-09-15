package com.mim.apollo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mim.apollo.databinding.ActivityMainBinding;
import com.mim.apollo.dto.PacienteDTO;
import com.mim.apollo.dto.UsuarioDTO;
import com.mim.apollo.service.AlertaAPI;
import com.mim.apollo.util.FileDB;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean bandera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);


        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        dataSetUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Invalid password");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Invalid email");
            focusView = mEmailView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            AlertaAPI.Factory.getInstance().executeLogin(email, password).enqueue(new Callback<PacienteDTO>() {
                @Override
                public void onResponse(Call<PacienteDTO> call, Response<PacienteDTO> response) {
                    if (response != null) {
                        if (response.body() != null) {

                            writeToUserFile(response.body());
                            changeActivity(response.body());
                        } else {
                            showProgress(false);
                            Toast.makeText(LoginActivity.this, "credenciales erroneas", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PacienteDTO> call, Throwable t) {
                    if (t.getCause() != null) {
                        Toast.makeText(LoginActivity.this, t.getCause().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "hubo algun error", Toast.LENGTH_LONG).show();
                    }
                    showProgress(false);
                }
            });
        }
    }

    private void writeToUserFile(PacienteDTO body) {
        Gson gson = new Gson();
        String json = gson.toJson(body);


        File file = new File(this.getFilesDir(),
                "cerveceria.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            FileUtils.writeStringToFile(file, json, "UTF-8");
            //Toast.makeText(LoginActivity.this, json, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void changeActivity(PacienteDTO user) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("parce", user);
        //intent.putExtra("planta",plantaInfo);
        //Bundle bundle=new Bundle();
        //bundle.putParcelable("planta",plantaInfo);
        //intent.putExtra("planta", settings);
        //intent.putExtra("bl",bundle);
        startActivity(intent);
        //finish();
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }



    private void dataSetUp() {

        File file = new File(this.getFilesDir(),
                "cerveceria.txt");


        readList(file);

    }

    private void readList(File file) {
        String content;
        if (file.exists()) {

            try {
                //FileUtils.writeStringToFile(file, "porque?", "UTF-8");
                content = FileUtils.readFileToString(file, "UTF-8");
                if (content != null) {
                    if (content.length() > 0) {
                        Gson gson = new Gson();


                        Type listType = new TypeToken<PacienteDTO>() {
                        }.getType();
                        if (bandera) {
                            //FileDB.writeToSettingsFile(content, getFilesDir().getAbsolutePath());
                            changeActivity((PacienteDTO) gson.fromJson(content, listType));
                        }


                    }else{
                        FileDB.writeToSettingsFile("", getFilesDir().getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

