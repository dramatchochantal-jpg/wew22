package com.example.edugate;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class Studentdashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        // Bottom Navigation Logic
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_schedule) {
                startActivity(new Intent(this, ScheduleActivity.class));
                return true;
            } else if (itemId == R.id.nav_messages) {
                startActivity(new Intent(this, MessagesActivity.class));
                return true;
            } else if (itemId == R.id.nav_grades) {
                startActivity(new Intent(this, GradesActivity.class));
                return true;
            } else if (itemId == R.id.nav_more) {
                startActivity(new Intent(this, AnnouncementsActivity.class));
                return true;
            }
            return false;
        });

        // Dashboard Card Click Listeners
        MaterialCardView cardEnrolledSubjects = findViewById(R.id.cardEnrolledSubjects);
        MaterialCardView cardPendingTasks = findViewById(R.id.cardPendingTasks);
        MaterialCardView cardAvgGrade = findViewById(R.id.cardAvgGrade);

        cardEnrolledSubjects.setOnClickListener(v -> {
            startActivity(new Intent(Studentdashboard.this, SubjectsActivity.class));
        });

        cardPendingTasks.setOnClickListener(v -> {
            startActivity(new Intent(Studentdashboard.this, TasksActivity.class));
        });

        cardAvgGrade.setOnClickListener(v -> {
            // As per request, redirect to Schedule page
            startActivity(new Intent(Studentdashboard.this, ScheduleActivity.class));
        });
    }
}