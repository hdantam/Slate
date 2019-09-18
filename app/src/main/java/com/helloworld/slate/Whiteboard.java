package com.helloworld.slate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles all whiteboard actions such as:
 * - Drawing locally
 * - Sending updates to the cloud
 * - Syncing screen to match the cloud
 */

public class Whiteboard extends View {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "Slate";
    private String docId;
    private CollectionReference collRef = db.collection("whiteboards");
    private DocumentReference docRef;
    private Segment currentSegment;
    private ArrayList<Path> currentMoveList ;
    private ArrayList<Path> moveList  ;
    private Path path = new Path();
    private Paint brush = new Paint();
    private Paint pBg = new Paint();
    private Bitmap b;
    private Canvas c;

    HashMap<HashMap<String, Float>, HashMap<String, String>> mockValue = new HashMap<>();

    public Whiteboard(Context context) {
        super(context);
        this.moveList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        setPreferences();
    }

    public Whiteboard(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.moveList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        setPreferences();
    }

    public Whiteboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.moveList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        setPreferences();
    }

    public void setPreferences(){
        brush.setAntiAlias(true);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(14f);
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
                pushChangesToCloud();
                break;
            default:
                return false;
        }
        postInvalidate();
        return false;
    }

    /**
     * Sends latest segment changes to the cloud
     */
    public void pushChangesToCloud(){
        docRef.update("segment", currentSegment.points);
    }

    /**
     * Clears canvas of any markings
     * NOTE: This is only a local clear, other screens
     * will still show the drawings
     */
    public void resetCanvas() {
        path.reset();
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path path : currentMoveList) {
            canvas.drawPath(path, brush);
        }
        for (Path path : moveList) {
            canvas.drawPath(path, brush);
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(b, 0, 0, pBg);
        canvas.drawPath(path, brush);
    }

    /**
     * Takes changes from the cloud and adds them to the whiteboard
     */
    public void syncToCloud(ArrayList<HashMap<String, Double>> points) {
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

    public String getCode(){
        return docId;
    }

    public void addEventListener(){
        docId = MainActivity.getCode();
        //Try to use entered code, if fails, create new random whiteboard with new code
        try {
            docRef = collRef.document(docId);
        } catch(Exception e) {
            docId = String.format("%04d", (int) (Math.random() * 1000));
        }
        collRef.document(docId).set(mockValue);
        docRef = collRef.document(docId);
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
                    ArrayList<HashMap<String, Double>> points = (ArrayList<HashMap<String, Double>>) snapshot.get("segment");
                    syncToCloud(points);
                    postInvalidate();
                }
            }
        });
    }
}