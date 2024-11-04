package com.example.eventlottery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class EventOrganizerActivity extends AppCompatActivity {

    private Button waitlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_organizer);

        waitlist = (Button) findViewById(R.id.event_organizer_waitlist_button);
        waitlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_waitlist eventWaitlist = new event_waitlist();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_event_organizer_id,eventWaitlist);
                transaction.commit();
            }
        });

    }
}