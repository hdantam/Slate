package com.helloworld.slate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class BackendSocket {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Source source = Source.SERVER;

    private String TAG = "Slate";

    private DocumentReference docRef = db.collection("whiteboards").document("wb-0001");

    public String encodeToBase64(Bitmap image){
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap decodeFromBase64(String byteString) {
        byte[] decodedByteArray = Base64.decode(byteString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void updateWhiteboard (Bitmap currentWhiteboard){
        String bitmapString = encodeToBase64(currentWhiteboard);
        db.collection("whiteboards").document("wb-0001").update("bitmap", bitmapString);
   }

   public void addEventListener(){
       docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot snapshot,
                   @Nullable FirebaseFirestoreException e) {
               if (e != null) {
                   Log.w(TAG,"Listen failed.", e);
                   return;
               }

               String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                       ? "Local" : "Server";

               if (snapshot != null && snapshot.exists()) {
                   try {
                       String bitMap = snapshot.get("bitmap").toString();
                       System.out.println(bitMap);
                       MainActivity.setBitmap(decodeFromBase64(bitMap));
                   } catch(Exception except){
                       System.out.println("NULL POINTER");

                   }
                   //Log.d(TAG, source + " data: " + snapshot.get("bitmap"));
               } else {
                   Log.d(TAG, source + " data: null");
               }
           }
       });
   }

}
