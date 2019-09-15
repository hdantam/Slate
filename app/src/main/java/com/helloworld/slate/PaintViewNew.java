package com.helloworld.slate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

public class PaintViewNew extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view_new);
        System.out.println("WORKS__________________________");
        button = findViewById(R.id.button);
        final PaintV p = findViewById(R.id.paintV);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button", "Working");
                p.undo();
            }
        });

    }
}

class PaintV extends View {
    private ArrayList<Path> undoList;
    private ArrayList<Path> currentMoveList ;
    private ArrayList<Path> moveList  ;
    private Path path = new Path();
    private Paint brush = new Paint();
    private Paint pBg = new Paint();
    private Bitmap b;
    private Canvas c;

    public PaintV(Context context) {
        super(context);
        this.moveList = new ArrayList<>();
        this.undoList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        init();

    }

    public PaintV(Context context, @Nullable AttributeSet attrs){

        super(context, attrs);
        this.moveList = new ArrayList<>();
        this.undoList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        init();
    }

    public PaintV(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.moveList = new ArrayList<>();
        this.undoList = new ArrayList<>();
        this.currentMoveList = new ArrayList<>();
        init();
    }




    public void init(){
        brush.setAntiAlias(true);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(14f);

        //params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float pointX = event.getX();
        float pointY = event.getY();
        Log.i("Touch", "Works");

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX,pointY);
                currentMoveList.add(path);
                break;

            case MotionEvent.ACTION_UP:
                //path.lineTo(pointX, pointY);
                moveList.add(path);
                currentMoveList.clear();
                break;

            default:
                return true;

        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("Draw", "Works");
        //super.onDraw(canvas);

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

    public void undo() {
//        Log.i("Move list", ""+moveList.size());
//        if (moveList.size() > 0) {
//            undoList.add(moveList.remove(moveList.size() - 1));
//            invalidate();
//        }
        b.eraseColor(Color.TRANSPARENT);
        c.drawBitmap(b, 0, 0, pBg);
    }



    public Bitmap getBitmap(Bitmap b) {
        return b;
    }


}
