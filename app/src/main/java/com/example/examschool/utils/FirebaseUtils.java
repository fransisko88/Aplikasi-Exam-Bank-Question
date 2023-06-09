package com.example.examschool.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class FirebaseUtils {
    private static FirebaseAuth fAuth;
    private static FirebaseFirestore fStore;

    public static FirebaseUser getFirebaseUser() {
        FirebaseAuth mAuth = getFirebaseAuth();
        if (mAuth != null) {
            return mAuth.getCurrentUser();
        }
        return null;
    }

    public static StorageReference getStorageReference() {
        StorageReference  storageReference = FirebaseStorage.getInstance().getReference();
        if (storageReference != null) {
            return storageReference;
        }
        return null;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (fAuth == null) {
            fAuth = FirebaseAuth.getInstance();
        }
        return fAuth;
    }

    public static FirebaseFirestore getFirestore() {
        if (fStore == null) {
            fStore = FirebaseFirestore.getInstance();
        }
        return fStore;
    }

    public static Bitmap getPictureHospital(ImageView picture, String hospitalId){

        final StorageReference profileRef = getStorageReference().child("hospital/"+hospitalId+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(picture);
            }
        });
        return null;
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
