package com.example.deltadayonetask1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RectEdit extends View {


    private Paint rectangleFillPaint,rectangleStrokePaint;
    private Rect rectangle;
    private float rectCenterX, rectCenterY;
    private float touchRadius;
    private final int LEFT_SIDE =1;
    private final int RIGHT_SIDE =2;
    private final int TOP_SIDE =3;
    private final int BOTTOM_SIDE =4;
    private final int INSIDE =5;
    private final int OUTSIDE =0;
    private int selectedPartOfRect;


    public RectEdit(Context context) {
        super(context);
        init(null);
    }

    public RectEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RectEdit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RectEdit(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        rectangleFillPaint= new Paint();
        rectangleStrokePaint =new Paint();
        rectangleFillPaint.setColor(Color.BLUE);
        rectangleFillPaint.setStyle(Paint.Style.FILL);
        rectangleStrokePaint.setColor(Color.BLACK);
        rectangleStrokePaint.setStyle(Paint.Style.STROKE);
        rectangleStrokePaint.setStrokeWidth(10);
        rectangleStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        rectangle = new Rect();
        rectangle.bottom=0;                        //initial fake value, used "rectangle initialisation" in onDraw
        rectCenterX=0;
        rectCenterY=0;
        touchRadius=13;                            // for changing the size of area of touch, for the edges

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(rectangle.bottom==0){ // rectangle initialisation
            rectangle.left = getWidth() / 4;
            rectangle.top = getWidth() / 3;
            rectangle.bottom = rectangle.top + getWidth() / 3;
            rectangle.right = rectangle.left + getWidth() / 2;
        }

        canvas.drawColor(Color.WHITE);
        canvas.drawRect(rectangle,rectangleFillPaint);
        canvas.drawRect(rectangle,rectangleStrokePaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                if(x>rectangle.left-touchRadius && x<rectangle.left+ touchRadius && y>rectangle.top+touchRadius && y< rectangle.bottom-touchRadius){
                    selectedPartOfRect=LEFT_SIDE;

                }else if (x>rectangle.right-touchRadius && x<rectangle.right+ touchRadius && y>rectangle.top+touchRadius && y< rectangle.bottom-touchRadius){
                    selectedPartOfRect=RIGHT_SIDE;

                }else if (x>rectangle.left+touchRadius && x<rectangle.right-touchRadius && y>rectangle.top-touchRadius && y< rectangle.top+touchRadius){
                    selectedPartOfRect=TOP_SIDE;

                }else if(x>rectangle.left+touchRadius && x<rectangle.right-touchRadius && y>rectangle.bottom-touchRadius && y< rectangle.bottom+touchRadius){
                    selectedPartOfRect=BOTTOM_SIDE;
                }else if(y>rectangle.top+ touchRadius && y< rectangle.bottom-touchRadius && x>rectangle.left +touchRadius && x<rectangle.right -touchRadius) {
                    selectedPartOfRect = INSIDE;
                }else
                    selectedPartOfRect= OUTSIDE;
                Log.d("rect Part touched", "onTouchEvent:" + selectedPartOfRect);

                return true;
            case MotionEvent.ACTION_MOVE:
                switch (selectedPartOfRect){
                    case LEFT_SIDE:
                        rectangle.left= (int) x;
                        break;
                    case RIGHT_SIDE:
                        rectangle.right= (int) x;
                        break;
                    case TOP_SIDE:
                        rectangle.top =(int) y;
                        break;
                    case BOTTOM_SIDE:
                        rectangle.bottom= (int) y;
                        break;
                    case INSIDE:
                        float dx=rectangle.right-rectangle.left,dy= rectangle.bottom-rectangle.top;
                        rectangle.left= (int) (x- dx/2);
                        rectangle.right= (int) (x+ dx/2);
                        rectangle.top= (int) (y- dy/2);
                        rectangle.bottom= (int) (y+ dy/2);
                        break;

                }
                postInvalidate();

                return true;
            case MotionEvent.ACTION_UP:
                return true;

        }

        return super.onTouchEvent(event);
    }
}
