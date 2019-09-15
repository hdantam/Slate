package com.helloworld.slate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

public class PaintView extends View {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "Slate";
    private DocumentReference docRef = db.collection("whiteboards").document("wb-0001");

    public LayoutParams params;
    private Path path = new Path();
    private Segment currentSegment;

    private Paint brush = new Paint();
    private Paint pBg = new Paint();

    private Bitmap b;
    private Canvas c;

    public PaintView(Context context) {
        super(context);

        brush.setAntiAlias(true);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float pointX = event.getX();
        float pointY = event.getY();

        switch(event.getAction()){
            //When user begins new segment
            case MotionEvent.ACTION_DOWN:
                currentSegment = new Segment();
                currentSegment.points.add(new Point(pointX, pointY));
                path.moveTo(pointX, pointY);
                return true;
            //User continues drawing segment
            case MotionEvent.ACTION_MOVE:
                currentSegment.points.add(new Point(pointX, pointY));
                path.lineTo(pointX,pointY);
                break;
             //User finishes drawing segment
            case MotionEvent.ACTION_UP:
                path.lineTo(pointX, pointY);
                c.drawPath(path, brush);
                updateWhiteboard();
                break;
            default:
                return false;
        }
        postInvalidate();
        return false;
    }

    public void syncPoints(ArrayList<HashMap<String, Double>> points) {
        try {
            float x = ((Double) (points.get(0).get("x"))).floatValue();
            float y = ((Double) (points.get(0).get("y"))).floatValue();
            path.moveTo(x, y);
            for (int i = 1; i < points.size(); i++) {
                x = ((Double) points.get(i).get("x")).floatValue();
                y = ((Double) points.get(i).get("y")).floatValue();
                path.lineTo(x, y);
            }
        } catch (Exception e) {
            System.out.println("Failed to sync for reason: " + e);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(b, 0, 0, pBg);
        canvas.drawPath(path, brush);
    }

    public void updateWhiteboard (){
        db.collection("whiteboards").document("wb-0001").update("segment", currentSegment.points);
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
                        ArrayList<HashMap<String, Double>> points = (ArrayList<HashMap<String, Double>>) snapshot.get("segment");
                        syncPoints(points);
                        postInvalidate();
                    } catch (Exception ee){
                        System.out.println(ee);
                    }
                }
            }
        });
    }
}
