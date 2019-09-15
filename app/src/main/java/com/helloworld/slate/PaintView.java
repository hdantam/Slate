package com.helloworld.slate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class PaintView extends View {
    public LayoutParams params;
    private Path path = new Path();
    private static Paint brush = new Paint();
    private static Paint pBg = new Paint();
    private static Bitmap b;
    private static Canvas c;
    private static int width = 0;
    private static int height = 0;


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
                MainActivity.updateWhiteboard(getBitmap());
                break;

            default:
                return false;

        }
        postInvalidate();
        return false;
    }

    public static void setBitmap(Bitmap b){
        c = new Canvas();
        //Bitmap Transparent = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //c.setBitmap(Transparent);
        //c.drawBitmap(b, 0, 0, brush);
        Canvas tempCanvas = new Canvas(b);
        tempCanvas.drawBitmap(b, 0, 0, pBg);
        tempCanvas.drawLine(0, 0,0,0, pBg);
        c = new Canvas(b);
        //c.drawBitmap(b,0,0,pBg);
        //c = new Canvas(b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
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

    public static Bitmap getBitmap() {
        return b;
    }
}
