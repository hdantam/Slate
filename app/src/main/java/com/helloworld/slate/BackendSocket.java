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
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    Source source = Source.SERVER;

    private String TAG = "Slate";

    private DocumentReference docRef = db.collection("whiteboards").document("wb-0001");

    public static String encodeToBase64(Bitmap image){
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap decodeFromBase64(String byteString) {
        byte[] decodedByteArray = Base64.decode(byteString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public static void updateWhiteboard (Bitmap currentWhiteboard){
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

                       String bitmapString = snapshot.get("bitmap").toString();
                       //System.out.println(bitMap);
                       Bitmap workingBitmap = decodeFromBase64(bitmapString);
                       Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
                       MainActivity.setBitmap(mutableBitmap);
                       System.out.println("Reach set point");

                   //Log.d(TAG, source + " data: " + snapshot.get("bitmap"));
               } else {
                   System.out.println("DIDNT WORK");
               }
           }
       });
   }

}
