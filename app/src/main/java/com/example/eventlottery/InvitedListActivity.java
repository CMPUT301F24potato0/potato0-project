package com.example.eventlottery;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class InvitedListActivity extends AppCompatActivity {

    private ArrayList<UsersList> userInvitedList;
    private ListView invitedList;
    private ArrayAdapter<UsersList> invitedAdapter;
    private EventModel event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_invited_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userInvitedList = new ArrayList<UsersList>();

        Bundle e = getIntent().getExtras();
        if (e != null) {
            event = (EventModel) e.getSerializable("eventModel");
        }

        userInvitedList = event.getInvitedList();

        invitedList = findViewById(R.id.invited_list);
        invitedAdapter = new UserListviewAdapter(this, 0, userInvitedList, "invite");
        invitedList.setAdapter(invitedAdapter);
    }
}