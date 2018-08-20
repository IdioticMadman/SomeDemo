package com.robert.pullrefreshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final float TOUCH_DOWN_MAX_Y = 600;
    private float mTouchDownY;
    private PullRefreshView mRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRefreshView = (PullRefreshView) findViewById(R.id.refreshView);

        findViewById(R.id.container).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mTouchDownY = event.getY();
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                if (event.getY() > mTouchDownY) {
                                    float moveY = event.getY() - mTouchDownY;
                                    float progress = moveY - TOUCH_DOWN_MAX_Y >= 0 ?
                                            1 : moveY / TOUCH_DOWN_MAX_Y;
                                    mRefreshView.setProgress(progress);
                                    return true;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                mRefreshView.release();
                                break;

                        }
                        return false;
                    }
                }
        );
    }
}
