package com.example.edugate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class Teacherdashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

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

        // Dashboard Click Listeners
        MaterialCardView cardTotalStudents = findViewById(R.id.cardTotalStudents);
        Button btnPostAnnouncement = findViewById(R.id.btnPostAnnouncement);
        Button btnViewSubmissions = findViewById(R.id.btnViewSubmissions);
        Button btnGradeBook = findViewById(R.id.btnGradeBook);

        cardTotalStudents.setOnClickListener(v -> {
            startActivity(new Intent(Teacherdashboard.this, TeacherStudentsActivity.class));
        });

        btnPostAnnouncement.setOnClickListener(v -> {
            startActivity(new Intent(Teacherdashboard.this, PostAnnouncementActivity.class));
        });

        btnViewSubmissions.setOnClickListener(v -> {
            startActivity(new Intent(Teacherdashboard.this, ViewSubmissionsActivity.class));
        });

        btnGradeBook.setOnClickListener(v -> {
            startActivity(new Intent(Teacherdashboard.this, GradebookActivity.class));
        });
    }
}