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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetButton;
    private TextView backToLogin;
    private FirebaseAuth mAuth;
    private boolean Sent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText = findViewById(R.id.email);
        resetButton = findViewById(R.id.reset_password);
        backToLogin = findViewById(R.id.back_to_login);

        mAuth = FirebaseAuth.getInstance();
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ResetPassword())
                {
                    SwitchToLoginActivity();
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SwitchToLoginActivity();
            }
        });
    }

    private boolean ResetPassword()
    {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            emailEditText.setError("Error in sending password reset email");
            return false;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Sent = true;
                            Toast.makeText(ResetPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return Sent;
    }

    private void SwitchToLoginActivity()
    {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}