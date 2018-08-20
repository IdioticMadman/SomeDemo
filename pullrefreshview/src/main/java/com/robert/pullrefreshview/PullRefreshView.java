package com.robert.pullrefreshview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by robert on 2017/8/19.
 */

public class PullRefreshView extends View {

    private static final String TAG = "PullRefreshView";
    //圆的画笔
    private Paint mPaint;
    //圆的重心x,y坐标
    private float mCircleX;
    private float mCircleY;
    //圆的半径
    private float mCircleRadius = 50;
    //下滑进度
    private float mProgress;
    //下滑最大高度
    private int mDragMaxHeight = 400;
    //最终Y轴的宽度
    private int mTargetWidth = 400;
    //贝塞尔曲线的画笔以及路径
    private Paint mPathPaint;
    private Path mPath = new Path();
    //圆的重心点的最终高度，会决定控制点的Y坐标
    private int mTargetGravityHeight = 10;
    //贝塞尔曲线终点相对于圆重心的切线角度
    private int mTangentAngle = 105;
    //释放动画
    private ValueAnimator mValueAnimator;
    private Interpolator mProgressInterpolator = new DecelerateInterpolator();
    private Interpolator mTangentAngleInterpolator;
    private Drawable mContent;
    private int mContentMargin;

    public PullRefreshView(Context context) {
        super(context);
        init(null);
    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PullRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PullRefreshView);
        int color = array.getColor(R.styleable.PullRefreshView_pColor, 0xff000000);

        mCircleRadius = array.getDimension(R.styleable.PullRefreshView_pRadius, 50);

        mDragMaxHeight = array.getDimensionPixelOffset(R.styleable.PullRefreshView_pDragHeight, mDragMaxHeight);
        mTangentAngle = array.getInteger(R.styleable.PullRefreshView_pTangentAngle, mTangentAngle);
        mTargetWidth = array.getDimensionPixelOffset(R.styleable.PullRefreshView_pTargetWidth, mTargetWidth);
        mTargetGravityHeight = array.getDimensionPixelOffset(R.styleable.PullRefreshView_pTargetGravityHeight, mTargetGravityHeight);
        mContent = array.getDrawable(R.styleable.PullRefreshView_pContentDrawBle);
        mContentMargin = array.getDimensionPixelOffset(R.styleable.PullRefreshView_pConTentDrawableMargin, 0);
        array.recycle();

        initPaint(color);

        mTangentAngleInterpolator = PathInterpolatorCompat.create(
                (2.0f * mCircleRadius) / mDragMaxHeight,
                90.0f / mTangentAngle
        );

    }

    private void initPaint(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        p.setAntiAlias(true);
        //设置防抖动
        p.setDither(true);
        //设置填充方式
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        mPaint = p;

        //初始化路径画笔
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        p.setAntiAlias(true);
        //设置防抖动
        p.setDither(true);
        //设置填充方式
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        mPathPaint = p;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int count = canvas.save();
        float v = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2;
        canvas.translate(v, 0);

        //画贝塞尔曲线
        canvas.drawPath(mPath, mPathPaint);

        //画圆
        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaint);

        Drawable drawable = mContent;
        if (drawable != null) {
            //剪切举行区域
            Log.i("Tag", "----=--lai");
            canvas.save();
            canvas.clipRect(drawable.getBounds());
            //绘制Drawble
            drawable.draw(canvas);
            canvas.restore();
        }
        canvas.restoreToCount(count);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int minWidthSize = (int) (mCircleRadius * 2 + getPaddingLeft() + getPaddingRight());

        int measureWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measureWidth = Math.min(minWidthSize, widthSize);
        } else {
            measureWidth = minWidthSize;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int minHeightSize = (int) ((mDragMaxHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom());
        int measureHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measureHeight = Math.min(minHeightSize, heightSize);
        } else {
            measureHeight = minHeightSize;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    /**
     * 当大小改变触发
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updatePathLayout();
    }

    private void updatePathLayout() {

        final float progress = mProgressInterpolator.getInterpolation(mProgress);
        final float endControlY = mTargetGravityHeight;

        //起始坐标
        final float width = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float height = getValueByLine(0, mDragMaxHeight, mProgress);

        final float cPointX = width / 2;
        final float cRadius = mCircleRadius;
        final float cPointY = height - cRadius;

        mCircleX = cPointX;
        mCircleY = cPointY;
        Log.e(TAG, "圆的中心坐标: x = " + mCircleX + " ,y = " + mCircleY);
        final Path path = mPath;
        path.reset();
        path.moveTo(0, 0);

        float lEndPointX, lEndPointY;
        float lControlPointX, lControlPointY;

        //获取当前切线弧度
        float angle = mTangentAngle * mTangentAngleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle);

        //贝塞尔曲线终点坐标
        lEndPointX = (float) (mCircleX - mCircleRadius * Math.sin(radian));
        lEndPointY = (float) (mCircleY + mCircleRadius * Math.cos(radian));

        //贝塞尔曲线控制点
        lControlPointX = (float) (lEndPointX - lEndPointY / Math.tan(radian));
        lControlPointY = getValueByLine(0, endControlY, progress);

        path.quadTo(lControlPointX, lControlPointY, lEndPointX, lEndPointY);
        Log.e(TAG, "updatePathLayout: lControlPointX: " + lControlPointX + " ,lControlPointY: " + lControlPointY);
        Log.e(TAG, "updatePathLayout: lEndPointX: " + lEndPointX + " ,lEndPointY: " + lEndPointY);

        //右侧
        float rEndPointX, rEndPointY;
        float rControlPointX, rControlPointY;
        rEndPointX = cPointX + cPointX - lEndPointX;
        rEndPointY = lEndPointY;
        rControlPointX = cPointX + cPointX - lControlPointX;
        rControlPointY = lControlPointY;

        path.lineTo(rEndPointX, rEndPointY);
        path.quadTo(rControlPointX, rControlPointY, width, 0);

        updateContentLayout(cPointX, cPointY, cRadius);

    }

    private void updateContentLayout(float cPointX, float cPointY, float cRadius) {
        Drawable drawable = mContent;
        if (drawable != null) {
            float margin = mContentMargin;
            int l = (int) (cPointX - cRadius + margin);
            int r = (int) (cPointX + cRadius - margin);
            int t = (int) (cPointY - cRadius + margin);
            int b = (int) (cPointY + cRadius - margin);
            drawable.setBounds(l, t, r, b);
        }
    }

    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    /**
     * 设置下滑进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.mProgress = progress;
        requestLayout();
        Log.e(TAG, "setProgress: " + progress);
    }


    public void release() {
        if (mValueAnimator == null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mProgress, 0);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedValue() instanceof Float) {
                        setProgress((Float) animation.getAnimatedValue());
                    }
                }
            });
            mValueAnimator = valueAnimator;
        } else {
            mValueAnimator.cancel();
            mValueAnimator.setFloatValues(mProgress, 0f);
        }
        mValueAnimator.start();
    }
}
