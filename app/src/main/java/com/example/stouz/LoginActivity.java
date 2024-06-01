package com.example.stouz;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private FirebaseAuth mAuth;
    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
        register = findViewById((R.id.register));

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SwitchToResetPasswordActivity();
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SwitchToRegistrationActivity();
            }
        });
    }

    private void LoginUser()
    {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            passwordEditText.setError("Password is required");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful() && Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        SwitchToMainActivity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void SwitchToRegistrationActivity()
    {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void SwitchToMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void SwitchToResetPasswordActivity()
    {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void ReloadPage()
    {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}