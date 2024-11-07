package com.example.eventlottery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is the WaitlistEventAdapter
 */
public class WaitlistEventAdapter extends ArrayAdapter<UsersList> {
    private ArrayList<UsersList> waitList;
    private ArrayList<UsersList> cancelList;
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
     * @param waitList The list
     */
    public WaitlistEventAdapter(@NonNull Context context, int resource, ArrayList<UsersList> waitList, ArrayList<UsersList> cancelList, EventModel event, FirebaseFirestore db) {
        super(context, resource, waitList);
        this.waitList = waitList;
        this.cancelList = cancelList;
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
                //event.unqueueWaitingList(user);
                removeFromList(user, waitList, cancelList);

//                Task<DocumentSnapshot> t = db.collection("users")
//                        .document(user.getiD())
//                        .get();
//                t.addOnSuccessListener
//                        (new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//                                    temp = documentSnapshot.toObject(CurrentUser.class);
//                                }
//                            }
//                        });
//                t.onSuccessTask(t1 -> {
                    db.collection("events").document(event.getEventID()).set(event);
//                    temp.removeTopics(event.getEventID() + "_" + temp.getiD());
//                    db.collection("users").document(temp.getiD()).set(temp);
//                    return null;
//                });
//                return null;
            }
        });
        return view;
    }

    /**
     * removeFromList override
     * @param user The user
     * @param waitList The waitList
     */
    private void removeFromList(UsersList user, ArrayList<UsersList> waitList, ArrayList<UsersList> cancelList) {
        for (int i = 0; i < waitList.size(); i++) {
            if (waitList.get(i).getName().equals(user.getName())) {
                cancelList.add(waitList.get(i));
                waitList.remove(i);
                notifyDataSetChanged();

            }
        }
    }
}
