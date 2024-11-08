package com.example.eventlottery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private String eventID;
    FirebaseFirestore db;
    ConstraintLayout mainView;
    ProgressBar progressBar;
    private EventModel event;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        confirm  = (Button) findViewById(R.id.confirm_signup);
        mainView = findViewById(R.id.accept_invite_buttons);
        progressBar = findViewById(R.id.signup_progressBar);
        db = FirebaseFirestore.getInstance();
        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            eventID = extra.getString("eventID");
        }

        Task<DocumentSnapshot> task = db.collection("events").document(eventID).get();

        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    event = documentSnapshot.toObject(EventModel.class);
                }
            }
        });
        // https://stackoverflow.com/questions/66698325/how-to-wait-for-firebase-task-to-complete-to-get-result-as-an-await-function
        task.onSuccessTask(task1 -> {
            mainView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            signup();
            return null;
        });


    }
    public void signup(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Signed up", Toast.LENGTH_SHORT).show();
            }
        });

    }
}