package com.helloworld.slate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import androidx.annotation.Nullable;



public class PaintView extends View{
    private ArrayList<Path> undoList = null;
    private ArrayList<Path> currentMoveList = null;
    private ArrayList<Path> moveList = null;
    private Path path = new Path();
    private Paint brush = new Paint();
    private Paint pBg = new Paint();
    private Bitmap b;
    private Canvas c;

    public PaintView(Context context) {
        super(context);
        this.moveList = new ArrayList<Path>();
        this.undoList = new ArrayList<Path>();
        this.currentMoveList = new ArrayList<Path>();
        init();

    }

    public PaintView(Context context, @Nullable AttributeSet attrs){

        super(context, attrs);
        this.moveList = new ArrayList<Path>();
        this.undoList = new ArrayList<Path>();
        this.currentMoveList = new ArrayList<Path>();
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.moveList = new ArrayList<Path>();
        this.undoList = new ArrayList<Path>();
        this.currentMoveList = new ArrayList<Path>();
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
        moveList.add(path);

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX,pointY);
                currentMoveList.add(path);
                break;

            case MotionEvent.ACTION_UP:
                path.lineTo(pointX, pointY);
                c.drawPath(path, brush);
                currentMoveList.clear();

                break;

            default:
                return false;

        }
        postInvalidate();
        return false;


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

    public void undo() {
            if (moveList.size() > 0) {
                undoList.add(moveList.remove(moveList.size() - 1));
                invalidate();
            }
    }



    public Bitmap getBitmap(Bitmap b) {
        return b;
    }


        submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            p.undo();
        }
    });

}
