package com.example.jpmart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpmart.R;
import com.example.jpmart.database.DatabaseHelper;
import com.example.jpmart.models.User;
import com.example.jpmart.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        // Nếu đã đăng nhập, chuyển thẳng vào home
        if (session.isLoggedIn()) {
            navigateToHome();
            return;
        }

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);
        btnLogin = findViewById(R.id.btn_login);

        // Auto-fill if remembered
        if (session.isRememberPassword()) {
            etUsername.setText(session.getSavedUsername());
            etPassword.setText(session.getSavedPassword());
            cbRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void navigateToHome() {
        Intent intent = session.isManager()
                ? new Intent(this, HomeManagerActivity.class)
                : new Intent(this, HomeStaffActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.login(username, password);
        if (user != null) {
            session.createSession(user.getId(), user.getUsername(), user.getFullname(), user.getRole());

            if (cbRemember.isChecked()) {
                session.saveCredentials(username, password);
            } else {
                session.clearCredentials();
            }

            navigateToHome();
        } else {
            Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
}
