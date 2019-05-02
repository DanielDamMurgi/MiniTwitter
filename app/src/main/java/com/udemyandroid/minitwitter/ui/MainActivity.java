package com.udemyandroid.minitwitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udemyandroid.minitwitter.R;
import com.udemyandroid.minitwitter.common.Constantes;
import com.udemyandroid.minitwitter.common.SharedPreferencesManager;
import com.udemyandroid.minitwitter.retrofit.MiniTwitterClient;
import com.udemyandroid.minitwitter.retrofit.MiniTwitterService;
import com.udemyandroid.minitwitter.retrofit.request.RequestLogin;
import com.udemyandroid.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvGoSignUp;
    EditText editTextEmail, editTextPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();

    }

    private void retrofitInit(){
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = MiniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnLogin = findViewById(R.id.buttonLogin);
        tvGoSignUp = findViewById(R.id.textViewGoSingUp);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    private void events(){
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSingUp:
                goToSignUp();
                break;
        }
    }

    private void goToLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("El email es requerido");
        }else if(password.isEmpty()){
            editTextPassword.setError("La contraseña es requerida");
        }else{
            RequestLogin requestLogin = new RequestLogin(email, password);

            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()){
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        SharedPreferencesManager.setSomeStringValue("PREF_TOKEN", response.body().getToken());

                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);

                        //Destruimos este activity para que no se pueda volver
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Algo fue mal revise sus datos de acceso.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexión. Intentalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToSignUp() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}
