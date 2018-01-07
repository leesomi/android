package com.clbee.mypaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import com.clbee.mypaint.PosInfo;

/**
 * Created by somi on 2018. 1. 7..
 */

public class Custompaintview extends View {

    public Paint paint;
    public float x, y, r;

    ArrayList<PosInfo> points = new ArrayList<PosInfo>();

    class Point {
        float x;
        float y;
        boolean isDraw;
        public Point(float x, float y, boolean isDraw) {
            this.x = x;
            this.y = y;
            this.isDraw = isDraw;
        }
    }

    public Custompaintview(Context context) {
        super(context);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        this.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch( event.getAction() ) {
//                    case MotionEvent.ACTION_MOVE:
//                        points.add(new Point(event.getX(), event.getY(), true));
//                        invalidate();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_DOWN:
//                        points.add(new Point(event.getX(), event.getY(), false));
//                }
//                return true;
//            }
//        });
    }
    @Override
    protected void onDraw(Canvas canvas) {

        for(int i=1; i<points.size(); i++){
            canvas.drawLine(points.get(i-1).getX(), points.get(i-1).getY(), points.get(i).getX(), points.get(i).getY(), points.get(i).getPaint());
        }
        super.onDraw(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 터치한 위치 값 추출
        x = event.getX();
        y = event.getY();
        //Log.d("onTouchEvent", "onTouchEvent:"+ x+" : "+y);

        // Pos 클래스를 생성해 데이터 저장 후
        // List에 추가
        PosInfo posInfo = new PosInfo(x, y);

        // 각각에 점에 paint와 r 설정
        posInfo.setPaint(paint);
        posInfo.setR(r);

        points.add(posInfo);

        // 화면을 강제로 그리기 위해 호출하는 메소드
        invalidate();

        return true;
    }

    // 색이나 두께가 변할 때마다 독립적으로 paint, thick 설정
    public void setPaintInfo(int color, float thick) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(thick);
    }
}
