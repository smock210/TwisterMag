package com.mobile.smock.twistermag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));

        //setContentView(R.layout.activity_main);
    }


    class DrawView extends SurfaceView implements SurfaceHolder.Callback {


        private DrawThread drawThread;
        Paint p;


        public DrawView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }


        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }


        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }


        class DrawThread extends Thread {


            private boolean running = false;
            Paint p;

            private SurfaceHolder surfaceHolder;


            public DrawThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
            }


            public void setRunning(boolean running) {
                this.running = running;
            }


            @Override


            public void run() {
                Canvas canvas;
                p = new Paint();
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated
                int r = 0;

                if (width >= height){
                    r = height/2;
                } else { r = width/2; }



                while (running) {
                    canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas == null)
                            continue;
                        canvas.drawColor(Color.GREEN);
                        canvas.drawCircle(width/2, height/2, r, p);
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

        }

        /*protected void onDraw(Canvas canvas)
        {
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(bmp, 10, 10, null);
        }*/
    }


        
}
