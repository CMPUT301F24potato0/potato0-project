package com.example.eventlottery.Admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminImagesFragment extends Fragment {
    public AdminImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.admin_image_gallery_grid, container, false);
        GridView gridView = rootView.findViewById(R.id.image_gallery_list);
        ArrayList<ImageItem> items = new ArrayList<ImageItem>();
        ImageItemAdapter adapter = new ImageItemAdapter(getContext(), items);
        gridView.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (gridView.getAdapter().getItem(position) != null) {
                ImageItem item = (ImageItem) gridView.getAdapter().getItem(position);
                if (item.isPhoto) {
                    db.collection("users").document(item.id).get().addOnCompleteListener( t -> {
                        UserModel user = t.getResult().toObject(UserModel.class);
                        new AdminUserDetailsFragment(user).show(getParentFragmentManager(), "Admin user view");
                    });
                } else {
                    db.collection("events").document(item.id).get().addOnCompleteListener( t -> {
                        EventModel event = t.getResult().toObject(EventModel.class);
                        Intent i = new Intent(getActivity(), AdminEventDetailsActivity.class);
                        i.putExtra("item", event);
                        startActivity(i);
                    });
                }
            }
        });
        ArrayList<ImageItem> photos = new ArrayList<ImageItem>();
        ArrayList<ImageItem> posters = new ArrayList<ImageItem>();
        CollectionReference photosRef = db.collection("photos");
        CollectionReference postersRef = db.collection("posters");
        photosRef.addSnapshotListener((@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) -> {
            photos.clear();
            for (QueryDocumentSnapshot doc : value) {
                byte[] bytes = doc.getBlob("Blob").toBytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                photos.add(new ImageItem(true, bitmap, doc.getId()));
            }
            adapter.clear();
            items.clear();
            for (ImageItem image : photos) if (!image.id.equals("default")) items.add(image);
            for (ImageItem image : posters) items.add(image);
            adapter.notifyDataSetChanged();
        });
        postersRef.addSnapshotListener((@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) -> {
            posters.clear();
            for (QueryDocumentSnapshot doc : value) {
                byte[] bytes = doc.getBlob("Blob").toBytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                posters.add(new ImageItem(false, bitmap, doc.getId()));
            }
            adapter.clear();
            items.clear();
            for (ImageItem image : photos) if (!image.id.equals("default")) items.add(image);
            for (ImageItem image : posters) items.add(image);
            adapter.notifyDataSetChanged();
        });
        return rootView;
    }

}

class ImageItem {
    public boolean isPhoto;
    public Bitmap bitmap = null;
    public String id;

    public ImageItem(boolean isPhoto, Bitmap bitmap, String id) {
        this.isPhoto = isPhoto;
        this.bitmap = bitmap;
        this.id = id;
    }
}

class ImageItemAdapter extends ArrayAdapter<ImageItem> {
    public ImageItemAdapter(@NonNull Context context, ArrayList<ImageItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.admin_image_gallery_gridview, null);
        } else {
            view = convertView;
        }
        ImageItem item = getItem(position);
        ImageView img = view.findViewById(R.id.imageView);
//        if (item == null) return view;
        Bitmap bitmap = item.bitmap;
        if (bitmap != null) img.setImageBitmap(bitmap);
        return view;
    }
}