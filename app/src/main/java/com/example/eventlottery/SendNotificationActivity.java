package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SendNotificationActivity extends AppCompatActivity {

    private Button send;
    private EditText title;
    private EditText message;

    String passing_title;
    String passing_message;
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_MESSAGE = "MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        send = findViewById(R.id.send_id);
        title = findViewById(R.id.title_id);
        message = findViewById(R.id.message_id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passing_title = title.getText().toString();
                passing_message = message.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(KEY_TITLE,passing_title);
                intent.putExtra(KEY_MESSAGE,passing_message);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }
}