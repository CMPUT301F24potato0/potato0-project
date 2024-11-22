package com.example.eventlottery.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.Models.CurrentUser;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEventDetailsActivity extends AppCompatActivity {
    public FirebaseFirestore db;
    public AdminEventDetailsActivity() {
        db = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EventModel event = (EventModel) getIntent().getExtras().getSerializable("item");
        ((TextView)findViewById(R.id.admin_event_title)).setText(event.getEventTitle());
        ((TextView)findViewById(R.id.admin_event_organizer_event_description)).setText(event.getEventDescription());
        ((TextView)findViewById(R.id.admin_event_date)).setText(event.getJoinDeadline().toString());
        String id = event.getFacilityID();
        Task<DocumentSnapshot> task = db.collection("users").document(id).get();
        task.addOnCompleteListener((Task<DocumentSnapshot> posttask) -> {
            CurrentUser organizer = posttask.getResult().toObject(CurrentUser.class);
            ((TextView)findViewById(R.id.organizer_name)).setText(String.format("%s %s", organizer.getfName(), organizer.getlName()));
        });
        task = db.collection("facilities").document(id).get();
        task.addOnCompleteListener((Task<DocumentSnapshot> posttask) -> {
            FacilityModel facility = posttask.getResult().toObject(FacilityModel.class);
            ((TextView)findViewById(R.id.facility_name)).setText(facility.getName());
        });

        ((Button)findViewById(R.id.admin_delete_event_button)).setOnClickListener((View view) -> {
            db.collection("events").document(event.getEventID()).delete();
            finish();
        });
    }
}