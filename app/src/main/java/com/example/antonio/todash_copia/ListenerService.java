package com.example.antonio.todash_copia;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Date;

import unisa.it.pc1.todash.R;

/**
 * Created by PC1 on 08/03/2018.
 */

public class ListenerService extends Service {

    private Handler handler;
    private Runnable runnable;

    private Animation animHead;

    private WindowManager mWindowManager;
    private View mChatHeadView;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clipText = clipboard.getPrimaryClip();
                ClipData.Item clipItem = clipText.getItemAt(0);
                final String text = clipItem.getText().toString();

                if(handler != null) {
                    stopTimerHead();
                    if (mChatHeadView != null) {
                        mWindowManager.removeView(mChatHeadView);
                        mChatHeadView = null;
                    }
                }

                final Date data = new Date();

                if(mChatHeadView == null) {
                    final WindowManager.LayoutParams params =  createHead();

                    mChatHeadView.setOnTouchListener(new View.OnTouchListener() {
                        private int lastAction;
                        private int initialY;
                        private float initialTouchY;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            //prova.setText(event.getAction());
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    stopTimerHead();
                                    //remember the initial position.
                                    initialY = params.y;

                                    //get the touch location
                                    initialTouchY = event.getRawY();

                                    lastAction = event.getAction();
                                    return true;
                                case MotionEvent.ACTION_UP:

                                    //As we implemented on touch listener with ACTION_MOVE,
                                    //we have to check if the previous action was ACTION_DOWN
                                    //to identify if the user clicked the view or not.
                                    if (lastAction == MotionEvent.ACTION_DOWN) {
                                        Intent intent = new Intent(ListenerService.this, DialogActivity.class);
                                        intent.putExtra("link",text);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);


                                        if (mChatHeadView != null) {
                                            mWindowManager.removeView(mChatHeadView);
                                            mChatHeadView = null;
                                        }
                                    }
                                    startTimerHead();
                                    lastAction = event.getAction();
                                    return true;
                                case MotionEvent.ACTION_MOVE:
                                    stopTimerHead();
                                    //Calculate the X and Y coordinates of the view.
                                    //params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                    params.y = initialY + (int) (event.getRawY() - initialTouchY);

                                    //Update the layout with new X & Y coordinate
                                    mWindowManager.updateViewLayout(mChatHeadView, params);
                                    int differenza = 0;
                                    if(params.y > initialY) {
                                        differenza = params.y - initialY;
                                    } else {
                                        differenza = initialY - params.y;
                                    }

                                    if(differenza > 0.3)
                                        lastAction = event.getAction();

                                    return true;
                            }
                            return false;
                        }
                    });
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimerHead(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mChatHeadView != null) {
                    mWindowManager.removeView(mChatHeadView);
                    mChatHeadView = null;
                }

            }
        };
        handler.postDelayed(runnable,4000);

    }

    private void stopTimerHead(){
        handler.removeCallbacks(runnable);
    }

    private WindowManager.LayoutParams createHead() {
        mChatHeadView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_head, null);
        ImageView headImg = mChatHeadView.findViewById(R.id.head_img);

        animHead = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.head_anim);

        headImg.startAnimation(animHead);

        //Add the view to the window.
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        //Initially view will be added to top-left corner
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);
        startTimerHead();

        return params;
    }
}
