package com.internetplus.yxy.intelligentspace;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Y.X.Y on 2017/5/5 0005.
 */
public class TableView extends TextView {

    TypedArray typedArray;

    boolean isfirst = false;

    int l, t, r, b;

    Paint paint = new Paint();

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        if (typedArray != null) {
            isfirst = typedArray.getBoolean(R.styleable.TableView_isfirst, false);
            typedArray.recycle();
        }
    }

    @Override
    public void onLayout(boolean change ,int l, int t, int r, int b) {
        super.onLayout(change, l, t, r, b);
        this.l = 0;
        this.t = 0;
        this.r = r-l;
        this.b = b-t;
        Log.d("l", l + "");
        Log.d("t", t + "");
        Log.d("r", r + "");
        Log.d("b", b + "");
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        if (isfirst) canvas.drawLine(l, t, r, t, paint);
        canvas.drawLine(l, t, l, b, paint);
        canvas.drawLine(l-1, b-1, r-1, b-1, paint);
        canvas.drawLine(r-1, t-1, r-1, b-1, paint);
    }
}
