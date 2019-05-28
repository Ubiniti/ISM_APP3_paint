package com.example.komputer.ism_app_grafika;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PowierzchniaRysunku extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public int COLOR_DEFAULT = Color.BLUE;
    public int STROKE_WIDTH_DEFAULT = 2;
    public int CIRCLE_RADIOUS_DEFAULT = 10;

    private SurfaceHolder mPojemnik;

    private Thread mWatekRysujacy;

    private boolean mWatekPracuje = false;

    private Object mBlokada = new Object();

    private Bitmap mBitmapa = null;

    private Canvas mKanwa = null;

    private Paint mFarba;

    private Paint mFarbaKolo;

    private Path mPath = null;

    public PowierzchniaRysunku(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPojemnik = getHolder();
        mPojemnik.addCallback(this);

        mFarba = new Paint();
        setStrokeWidth(STROKE_WIDTH_DEFAULT);

        mFarbaKolo = new Paint(mFarba);

        setColor(COLOR_DEFAULT);
        mFarba.setStyle(Paint.Style.STROKE);
        mFarbaKolo.setStyle(Paint.Style.FILL);
    }

    public void wznowRysowanie() {
        mWatekRysujacy = new Thread(this);
        mWatekPracuje = true;
        mWatekRysujacy.start();
    }

    public void pauzujRysowanie() {
        mWatekPracuje = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        float x = event.getX(),
              y = event.getY();
        synchronized (mBlokada) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mKanwa.drawCircle(event.getX(), event.getY(), CIRCLE_RADIOUS_DEFAULT, mFarbaKolo);
                    handleLineMovement(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    mKanwa.drawCircle(event.getX(), event.getY(), CIRCLE_RADIOUS_DEFAULT, mFarbaKolo);
                    handleLineMovement(event.getX(), event.getY());
                    mPath = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleLineMovement(event.getX(), event.getY());
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    void handleLineMovement(float x, float y) {
        if(mPath == null) {
            mPath = new Path();
            mPath.moveTo(x, y);
        } else {
            mPath.lineTo(x, y);
        }

        mKanwa.drawPath(mPath, mFarba);
    }

    public boolean performClick()
    {
        return super.performClick();
    }


    @Override
    public void run() {
        while(mWatekPracuje) {
            Canvas kanwa = null;
            try {
                synchronized (mPojemnik) {
                    if(!mPojemnik.getSurface().isValid()) continue;
                    kanwa = mPojemnik.lockCanvas(null);
                    synchronized (mBlokada) {
                        if (mWatekPracuje) {
                            kanwa.drawBitmap(mBitmapa, 0, 0, null);
                        }
                    }
                }
            } finally {
                if (kanwa != null) {
                    mPojemnik.unlockCanvasAndPost(kanwa);
                }
            }
            try {
                Thread.sleep(1000 / 25);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mBitmapa = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        mKanwa = new Canvas(mBitmapa);
        mKanwa.drawARGB(255, 255, 255, 255);
        wznowRysowanie();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mWatekPracuje = false;
    }

    public void setColor(int color) {
        mFarba.setColor(color);
        mFarbaKolo.setColor(color);
    }

    public void setStrokeWidth(int width) {
        mFarba.setStrokeWidth(width);
    }

    public void setStyleFill() {
        mFarba.setStyle(Paint.Style.FILL);
    }

    public void setStyleStroke() {
        mFarba.setStyle(Paint.Style.STROKE);
    }

    void clear() {
        mKanwa.drawARGB(255, 255, 255, 255);
    }
}
