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
public class CircularProgressBarColors extends View {

    private int mDuration = 30;
    private int mProgress_red = 30;
    private int mProgress_yellow = 30;
    private int mProgress_blue = 30;
    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    private int mBackgroundColor = Color.LTGRAY;
    private int mPrimaryColor = Color.parseColor("#6DCAEC");
    private int color_yellow = Color.rgb(252, 248, 0);
    private int color_red = Color.rgb(255, 128, 48);
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

    public CircularProgressBarColors(Context context) {
        super(context);
    }

    public CircularProgressBarColors(Context context, AttributeSet attrs) {
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
    public void setProgress(int progress_red, int progress_yellow, int progress_blue) {
        this.mProgress_red = progress_red;
        this.mProgress_yellow = progress_yellow;
        this.mProgress_blue = progress_blue;

        invalidate();
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
        mPaint.setStrokeWidth(mStrokeWidth * 3);
        mPaint.setStyle(Paint.Style.STROKE);    //����ͼ��Ϊ����

        // ������
        canvas.drawCircle(halfWidth, halfHeight, radius - halfStrokeWidth * 3, mPaint);

        // ����ǰ��ȵ�Բ��
        mPaint.setColor(mPrimaryColor);    // �ı仭����ɫ
        mPaint.setStrokeWidth(mStrokeWidth);
        mRectF.top = halfHeight - radius + halfStrokeWidth * 5;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth * 5;
        mRectF.left = halfWidth - radius + halfStrokeWidth * 5;
        mRectF.right = halfWidth + radius - halfStrokeWidth * 5;
        canvas.drawArc(mRectF, 90, getRateOfProgress_blue() * 360, false, mPaint);
//		


//		
        mPaint.setColor(color_red);        // �ı仭����ɫ
        mPaint.setStrokeWidth(mStrokeWidth);
        mRectF.top = halfHeight - radius + halfStrokeWidth;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth;
        mRectF.left = halfWidth - radius + halfStrokeWidth;
        mRectF.right = halfWidth + radius - halfStrokeWidth;
        canvas.drawArc(mRectF, 90, getRateOfProgress_red() * 360, false, mPaint);


        mPaint.setColor(color_yellow);    // �ı仭����ɫ
        mPaint.setStrokeWidth(mStrokeWidth);
        mRectF.top = halfHeight - radius + halfStrokeWidth * 3;
        mRectF.bottom = halfHeight + radius - halfStrokeWidth * 3;
        mRectF.left = halfWidth - radius + halfStrokeWidth * 3;
        mRectF.right = halfWidth + radius - halfStrokeWidth * 3;
        canvas.drawArc(mRectF, 90, getRateOfProgress_yellow() * 360, false, mPaint);
//		
////		
//		mPaint.setColor(Color.rgb(252, 248, 0));	// �ı仭����ɫ
//		mPaint.setStrokeWidth(mStrokeWidth);
////		
////		int halfWidth = getWidth() / 2;
////		int halfHeight = getHeight() /2;
////		int radius = halfWidth < halfHeight ? halfWidth : halfHeight;
//		mRectF.top = halfHeight - radius + halfStrokeWidth*3;
//		mRectF.bottom = halfHeight + radius - halfStrokeWidth*3;
//		mRectF.left = halfWidth - radius + halfStrokeWidth*3;
//		mRectF.right = halfWidth + radius - halfStrokeWidth*3;
//    	canvas.drawArc(mRectF, 90, getRateOfProgress() * 360, false, mPaint);

//		
//		mPaint.setColor(Color.GREEN);	// �ı仭����ɫ
//		mPaint.setStrokeWidth(halfStrokeWidth/3);
//		mRectF.top = halfHeight - radius + halfStrokeWidth;
//		mRectF.bottom = halfHeight + radius - halfStrokeWidth;
//		mRectF.left = halfWidth - radius + halfStrokeWidth;
//		mRectF.right = halfWidth + radius - halfStrokeWidth;
//		canvas.drawArc(mRectF, -90, getRateOfProgress() * 360, false, mPaint);
        canvas.save();
    }

    /**
     * �õ���ǰ�Ľ�ȵı���
     * <p> �ý������ǰ��ֵ �� ����������ֵ���� </p>
     *
     * @return
     */
    private float getRateOfProgress_red() {
        return (float) mProgress_red / mDuration;
    }

    private float getRateOfProgress_yellow() {
        return (float) mProgress_yellow / mDuration;
    }

    private float getRateOfProgress_blue() {
        return (float) mProgress_blue / mDuration;
    }
}
