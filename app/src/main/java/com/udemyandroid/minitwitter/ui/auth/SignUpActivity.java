package com.udemyandroid.minitwitter.ui.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udemyandroid.minitwitter.R;
import com.udemyandroid.minitwitter.retrofit.MiniTwitterClient;
import com.udemyandroid.minitwitter.retrofit.MiniTwitterService;
import com.udemyandroid.minitwitter.retrofit.request.RequestSignup;
import com.udemyandroid.minitwitter.retrofit.response.ResponseAuth;
import com.udemyandroid.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    TextView tvGoLogin;
    EditText etUsername, etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        retrofitInit();
        findViews();
        events();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = MiniTwitterClient.getMiniTwitterService();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                goToSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void goToSignUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty()){
            etUsername.setError("El nombre de usuario es requerido");
        }else if (email.isEmpty()){
            etEmail.setError("El email es requerido");
        }else if (password.isEmpty() || password.length() < 4){
            etPassword.setError("La contraseña es requerida y debe tener al menos 4 caracteres");
        }else {
            String code = "UDEMYANDROID";
            RequestSignup requestSignup = new RequestSignup(username, email, password, code);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignup);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()){
                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Algo fue mal revise sus datos de acceso.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error en la conexión. Inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToLogin() {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    private void events(){
        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    private void findViews(){
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }
}
