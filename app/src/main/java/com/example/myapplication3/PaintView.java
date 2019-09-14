package com.example.myapplication3;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.graphics.*;

public class PaintView extends View {

    public LayoutParams params;
    private Path path = new Path();
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
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX,pointY);
                break;

            case MotionEvent.ACTION_UP:
                path.lineTo(pointX, pointY);
                c.drawPath(path, brush);
                path = new Path();
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

    public Bitmap getBitmap(Bitmap b) {
        return b;
    }
}
