package com.robert.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by robert on 2017/8/26.
 */

public class PathMeasureDemo extends View {

    private static final String TAG = "PathMeasureDemo";

    Paint mPaint;
    //内圈
    private Path mInnerPath;
    //外圈
    private Path mOuterPath;
    //正三角
    private Path mTriangle1;
    //倒三角
    private Path mTriangle2;
    //path测量类
    private PathMeasure mPathMeasure;
    //中心点
    private int mCenterX;
    private int mCenterY;
    //值动画
    private ValueAnimator mAnimator;
    //更新动画阶段Handler
    private Handler mHandler;
    private Path mDrawPath;

    //当前动画的三个状态
    private interface State {
        //画圆状态
        String CIRCLE_STATE = "circle_state";
        //画虚线的三角路径状态
        String TRIANGLE_STATE = "triangle_state";
        //最后六角形状态
        String FINISH_STATE = "finish_state";
    }

    //当前状态
    private String mCurrentState;
    //当前阶段动画执行百分比
    private float mPercent;

    public PathMeasureDemo(Context context) {
        this(context, null);
    }

    public PathMeasureDemo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化操作
    private void init() {
        initPaint();
        initPath();
        initAnimation();
        initHandle();
        mCurrentState = State.CIRCLE_STATE;
        mAnimator.start();
    }

    private void initHandle() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };
    }

    //初始化值动画以及相关监听
    private void initAnimation() {

        mAnimator = ValueAnimator.ofFloat(0, 1).setDuration(3000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();//获取值动画进度来更新
                invalidate();
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //当前动画结束，发消息开始下一个动画
                switch (mCurrentState) {
                    case State.CIRCLE_STATE:
                        mCurrentState = State.TRIANGLE_STATE;
                        mAnimator.start();
                        break;
                    case State.TRIANGLE_STATE:
                        mCurrentState = State.FINISH_STATE;
                        mAnimator.start();
                        break;
                }
            }
        });
    }

    //初始化两个圆以及两个三角形的PATH
    private void initPath() {
        Path innerPath = new Path();
        Path outerPath = new Path();
        Path triangle1Path = new Path();
        Path triangle2Path = new Path();
        mDrawPath = new Path();

        PathMeasure pathMeasure = new PathMeasure();

        RectF innerRect = new RectF(-230, -230, 230, 230);
        RectF outerRect = new RectF(-280, -280, 280, 280);
        //设置两个圆的PATH,两个角度是为了做动画起点不同
        innerPath.addArc(innerRect, 150, 359.9F);
        outerPath.addArc(outerRect, 60, 359.9F);

        pathMeasure.setPath(innerPath, false);
        float length = pathMeasure.getLength();

        //利用PathMeasure找到对应的三角形的三个点
        float[] pos = new float[2];
        pathMeasure.getPosTan(0, pos, null);
        triangle1Path.moveTo(pos[0], pos[1]);//第一个点
        Log.e(TAG, "first point: " + pos[0] + " : " + pos[1]);

        pathMeasure.getPosTan((1f / 3f) * length, pos, null);
        triangle1Path.lineTo(pos[0], pos[1]);//第二个点
        Log.e(TAG, "second point: " + pos[0] + " : " + pos[1]);

        pathMeasure.getPosTan((2f / 3f) * length, pos, null);
        triangle1Path.lineTo(pos[0], pos[1]);//第三个点
        Log.e(TAG, "third point: " + pos[0] + " : " + pos[1]);
        triangle1Path.close();//闭合当前Path形成三角形

        Matrix matrix = new Matrix();
        matrix.setRotate(-180);
        triangle1Path.transform(matrix, triangle2Path);//利用矩阵翻转得到第二个三角形的Path

        mInnerPath = innerPath;
        mOuterPath = outerPath;
        mTriangle1 = triangle1Path;
        mTriangle2 = triangle2Path;
        mPathMeasure = pathMeasure;
    }

    //初始化画笔
    private void initPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(12);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setShadowLayer(12, 0, 0, Color.WHITE);
        mPaint = paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path drawPath = mDrawPath;
        PathMeasure pathMeasure = mPathMeasure;
        Paint paint = mPaint;
        canvas.save();
        //将画布的中心点移至屏幕中间，默认是在（0,0）的位置
        canvas.translate(mCenterX, mCenterY);
        switch (mCurrentState) {
            case State.CIRCLE_STATE:
                drawPath.reset();//画内圈
                pathMeasure.setPath(mInnerPath, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * mPercent, drawPath, true);//获取内圈片段
                canvas.drawPath(drawPath, paint);
                drawPath.reset();//画外圈
                pathMeasure.setPath(mOuterPath, false);
                pathMeasure.getSegment(0, pathMeasure.getLength() * mPercent, drawPath, true);//获取外圈片段
                canvas.drawPath(drawPath, paint);
                break;
            case State.TRIANGLE_STATE:
                drawPath.reset();
                canvas.drawPath(mInnerPath, paint);//将两个圆画好
                canvas.drawPath(mOuterPath, paint);
                pathMeasure.setPath(mTriangle1, false);//正三角形
                float endD = pathMeasure.getLength() * mPercent;//计算终点
                /**
                 * 计算起点
                 * (0.5f - Math.abs(0.5f - mPercent))取值范围 0->0.5->0
                 * 所以这个线会看起来由窄变宽，宽变窄
                 */
                float startD = endD - (0.5f - Math.abs(0.5f - mPercent)) * 200;
                pathMeasure.getSegment(startD, endD, drawPath, true);//获取正三角形片段
                canvas.drawPath(drawPath, paint);
                drawPath.reset();
                pathMeasure.setPath(mTriangle2, false);//反三角形
                pathMeasure.getSegment(startD, endD, drawPath, true);//获取正三角形片段
                canvas.drawPath(drawPath, paint);
                break;
            case State.FINISH_STATE:
                drawPath.reset();
                canvas.drawPath(mInnerPath, paint);//将两个圆画好
                canvas.drawPath(mOuterPath, paint);
                pathMeasure.setPath(mTriangle1, false);//正三角形
                float length = pathMeasure.getLength() * mPercent;
                pathMeasure.getSegment(0, length, drawPath, true);//获取正三角形片段
                canvas.drawPath(drawPath, paint);
                drawPath.reset();
                pathMeasure.setPath(mTriangle2, false);//反三角形
                pathMeasure.getSegment(0, length, drawPath, true);//获取正三角形片段
                canvas.drawPath(drawPath, paint);
                break;
        }
        //恢复画布
        canvas.restore();
    }

    //获取屏幕的中心点
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = getWidth() >> 1;
        mCenterY = getHeight() >> 1;
    }
}
