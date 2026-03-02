package com.example.edugate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signinup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput;
    private Button btnAction;
    private TextView tvForgotPassword;
    private RadioGroup roleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_out);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnAction = findViewById(R.id.btnAction);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        roleGroup = findViewById(R.id.roleGroup);

        btnAction.setOnClickListener(v -> handleLogin());
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        int selectedId = roleGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String uiRole = selectedRadioButton.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verifyUserRole(uiRole);
                    } else {
                        Toast.makeText(Signinup.this, "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void verifyUserRole(String uiRole) {
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check "Users" collection
        db.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                checkRoleMatch(task.getResult(), uiRole);
            } else {
                // Fallback: Check lowercase "users" collection
                db.collection("users").document(userId).get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful() && task2.getResult().exists()) {
                        checkRoleMatch(task2.getResult(), uiRole);
                    } else {
                        String error = "No database entry for UID: " + userId + "\nMake sure collection is 'Users' and Document ID is the UID.";
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        Log.e("EduGateAuth", error);
                        mAuth.signOut();
                    }
                });
            }
        });
    }

    private void checkRoleMatch(DocumentSnapshot document, String uiRole) {
        String dbRole = document.getString("role");
        if (dbRole == null) dbRole = document.getString("Role"); // Try alternative casing

        if (dbRole != null && uiRole.equalsIgnoreCase(dbRole.trim())) {
            navigateToDashboard(dbRole.trim());
        } else {
            String msg = "Role Mismatch! You selected '" + uiRole + "' but DB says '" + dbRole + "'";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            mAuth.signOut();
        }
    }

    private void handleForgotPassword() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email first", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Reset email sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToDashboard(String role) {
        Intent intent;
        if (role.equalsIgnoreCase("Student")) {
            intent = new Intent(this, Studentdashboard.class);
        } else if (role.equalsIgnoreCase("Teacher")) {
            intent = new Intent(this, Teacherdashboard.class);
        } else if (role.equalsIgnoreCase("Admin")) {
            intent = new Intent(this, Admindashboard.class);
        } else {
            Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }
}
