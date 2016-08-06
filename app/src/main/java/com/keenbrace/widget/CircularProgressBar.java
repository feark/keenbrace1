package com.keenbrace.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * ���εĽ����
 *
 * @author lwz <lwz0316@gmail.com>
 */
public class CircularProgressBar extends View {

    private int mDuration = 100;
    private int mProgress = 0;

    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    private int mBackgroundColor = Color.LTGRAY;
    private int mPrimaryColor = Color.parseColor("#6DCAEC");
    private float mStrokeWidth = 10F;

    /**
     * ������ı����
     * <p/>
     * {@link #onChange(int duration, int progress, float rate)}
     */
    public interface OnProgressChangeListener {
        /**
         * ��ȸı��¼������������ȸı䣬�ͻ���ø÷���
         *
         * @param duration �ܽ��
         * @param progress ��ǰ���
         * @param rate     ��ǰ������ܽ�ȵ��� ����rate = (float)progress / duration
         */
        public void onChange(int duration, int progress, float rate);
    }

    private OnProgressChangeListener mOnChangeListener;

    /**
     * ���ý�����ı����
     *
     * @param l
     */
    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        mOnChangeListener = l;
    }

    public CircularProgressBar(Context context) {
        super(context);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * ���ý���������ֵ, ��ֵҪ ���� 0
     *
     * @param max
     */
    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        mDuration = max;
    }

    /**
     * �õ�����������ֵ
     *
     * @return
     */
    public int getMax() {
        return mDuration;
    }

    /**
     * ���ý�����ĵ�ǰ��ֵ
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress > mDuration) {
            progress = mDuration;
        }
        mProgress = progress;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange(mDuration, progress, getRateOfProgress());
        }
        invalidate();
    }

    /**
     * �õ��������ǰ��ֵ
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * ���ý������������ɫ
     */
    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
    }

    /**
     * ���ý������ȵ���ɫ
     */
    public void setPrimaryColor(int color) {
        mPrimaryColor = color;
    }

    /**
     * ���û��εĿ��
     *
     * @param width
     */
    public void setCircleWidth(float width) {
        mStrokeWidth = width;

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        int radius = halfWidth < halfHeight ? halfWidth : halfHeight;
        float halfStrokeWidth = mStrokeWidth / 2;

        // ���û���
        mPaint.setColor(mBackgroundColor);
        mPaint.setDither(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);    //����ͼ��Ϊ����

        // ������
        canvas.drawCircle(halfWidth, halfHeight, radius - halfStrokeWidth, mPaint);

        // ����ǰ��ȵ�Բ��
        mPaint.setColor(mPrimaryColor);    // �ı仭����ɫ
        mRectF.top = halfHeight - radius + halfStrokeWidth;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth;
        mRectF.left = halfWidth - radius + halfStrokeWidth;
        mRectF.right = halfWidth + radius - halfStrokeWidth;
        canvas.drawArc(mRectF, 90, getRateOfProgress() * 360, false, mPaint);

        canvas.save();
    }

    /**
     * �õ���ǰ�Ľ�ȵı���
     * <p> �ý������ǰ��ֵ �� ����������ֵ���� </p>
     *
     * @return
     */
    private float getRateOfProgress() {
        return (float) mProgress / mDuration;
    }

}
