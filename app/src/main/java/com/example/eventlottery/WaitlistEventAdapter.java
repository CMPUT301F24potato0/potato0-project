package com.example.eventlottery;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is the WaitlistEventAdapter
 */
public class WaitlistEventAdapter extends ArrayAdapter<UsersList> {
    private ArrayList<UsersList> list;
    private EventModel event;
    private FirebaseFirestore db;

    /**
     * This is the constructor for WaitlistEventAdapter
     * @param context The context
     * @param resource The resource
     */

    private CurrentUser temp;

    public WaitlistEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * This is the constructor for WaitlistEventAdapter
     * @param context The context
     * @param resource The resource
     * @param list The list
     */
    public WaitlistEventAdapter(@NonNull Context context, int resource, ArrayList<UsersList> list, EventModel event, FirebaseFirestore db) {
        super(context, resource, list);
        this.list = list;
        this.event = event;
        this.db = db;
    }

    /**
     * getView override
     * @param position The position
     * @param convertView The convertView
     * @param parent The parent
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.user_listview_content, null);
        } else {
            view = convertView;
        }
        Button remove = view.findViewById(R.id.listview_remove_button);
        Button sendInvite = view.findViewById(R.id.listview_send_invite_button);
        sendInvite.setVisibility(View.GONE);
        UsersList user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.unqueueWaitingList(user);
                } catch(Exception e) {}; // temporary workaround
                removeFromList(user, list);

                Task<DocumentSnapshot> t = db.collection("users")
                        .document(user.getiD())
                        .get();
                t.addOnSuccessListener
                        (new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    temp = documentSnapshot.toObject(CurrentUser.class);
                                }
                            }
                        });
                t.onSuccessTask(t1 -> {
                    db.collection("events").document(event.getEventID()).set(event);
                    temp.removeTopics(event.getEventID() + "_" + temp.getiD());
                    db.collection("users").document(temp.getiD()).set(temp);
                    return null;
                });
//                return null;
            }
        });
        return view;
    }

    /**
     * removeFromList override
     * @param user The user
     * @param list The list
     */
    private void removeFromList(UsersList user, ArrayList<UsersList> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(user.getName())) {
                list.remove(i);
                notifyDataSetChanged();

            }
        }
    }
}
