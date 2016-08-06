package com.keenbrace.widget;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CurveChartSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {
    SurfaceHolder holder = null;
    private int gridPaddingLeft = 3;
    private int gridPaddingRight = 3;
    private int gridPaddingTop = 3;
    private int gridPaddingBottom = 3;
    private int pointGapDef = 2;
    private Context context = null;

    private boolean isShowGrid;

    private boolean isStatic;

    private boolean isShowScale;

    private float minValue = 0;
    private float currentMaxValue;
    private float maxValue = 0;

    private int ceilValue = 10;
    private PointF lastPoint = null;
    private int maxCeil = 0;
    private boolean setScope = false;
    private int minCeil = 0;
    private int gridStart = gridPaddingLeft;
    int gridColor = Color.GRAY;
    int textColor = Color.BLACK;
    private int selectLineColor = 0xaaaaaaaa;
    private int popupColor = 0xaa33ff33;
    private float[] pointX = null;
    private int[] curveColor = null;
    /**
     * �Ƿ���ʾѡ��ʱ�����
     */
    private boolean showTips = true;
    private int selectPoint = -1;
    private float pointGap = pointGapDef;

    public CurveChartSurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        this.setFocusable(true);
    }

    public CurveChartSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        this.setFocusable(true);
    }

    public CurveChartSurfaceView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        holder = getHolder();
        holder.addCallback(this);
        this.setFocusable(true);
    }

    private void init(Context context) {
        this.context = context;
        gridPaddingLeft = toPixel(context, gridPaddingLeft);
        gridPaddingTop = toPixel(context, gridPaddingTop);
        gridPaddingRight = toPixel(context, gridPaddingRight);
        gridPaddingBottom = toPixel(context, gridPaddingBottom);
        pointGapDef = toPixel(context, pointGapDef);
    }

    /**
     * ��Сˮƽ�ߵ���Ŀ
     */
    private int minHarizonLineCount = 3;
    private boolean sizeHaveSet = false;
    private int maxHarizonLineCount = 10;
    private float[][] staticData = null;

    /**
     * �����������<br>
     * ������ֻ������Ϊ��̬ʱ��Ч<br>
     * �������ʱ��Ӧ�������õ�һ�����ߵ���ݡ�����һ�����ߵ����û�����ã���ֱ�����ú����������ݣ����������Զ�������Ϊ��һ�����ߵ���ݡ�
     *
     * @param index �ڼ������ߣ���0��ʼ
     * @param data
     */
    public void setData(int index, float[] data) {
        if (data == null || isStatic == false)
            return;
        if (staticData == null || staticData.length <= index)
            return;
        if (staticData[0] == null)
            index = 0;
        for (int i = 0; i < data.length; i++) {
            if (maxValue < data[i]) {
                maxValue = data[i];
            }
            if (minValue > data[i]) {
                minValue = data[i];
            }
        }
        staticData[index] = data;
        if (setScope == false) {
            maxCeil = (int) Math.ceil(maxValue);
            minCeil = (int) Math.floor(minValue);
        }
        currentMaxValue = maxValue - minValue;
        pointX = null;
    }

    /**
     * ׷��һ����̬���<br>
     * ������ֻ�ڶ�̬����ʱ��Ч
     *
     * @param data
     */
    public void appendData(float[] data) {
        if (data == null || isStatic || dataList == null
                || dataList.length != data.length) {
            return;
        }
        if (sizeHaveSet == false) {
            int s = (getWidth() - getPaddingLeft() - getPaddingRight())
                    / pointGapDef;
            if (s > 1) {
                for (int i = 0; i < dataList.length; i++) {
                    if (dataList[i] == null)
                        dataList[i] = new DataQueue();
                    dataList[i].setSize(s);
                }
                sizeHaveSet = true;
                pointGap = pointGapDef;
            }
        }
        for (int i = 0; i < data.length; i++) {
            if (maxValue < data[i])
                maxValue = data[i];
            if (minValue > data[i])
                minValue = data[i];
            if (dataList[i] == null)
                dataList[i] = new DataQueue();
            dataList[i].add(new Float(data[i]));
        }
        if (setScope == false) {
            maxCeil = (int) Math.ceil(maxValue);
            minCeil = (int) Math.floor(minValue);
        }
        currentMaxValue = maxValue - minValue;
        invalidate();
    }

    public void draw() {
        Canvas canvas = holder.lockCanvas();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int height = getHeight() - paddingTop - paddingBottom;
        int width = getWidth() - paddingLeft - paddingRight;
        canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        Paint p = new Paint();
        float vsp = 1;
        int ceilValue = Math.abs(maxCeil - minCeil);
        if (ceilValue == 0)
            ceilValue = minHarizonLineCount;
        if (ceilValue > maxHarizonLineCount) {
            vsp = (ceilValue * 1.0f) / maxHarizonLineCount;
            ceilValue = maxHarizonLineCount;
        }
        float textw = 0;
        if (isShowScale) {
            Rect rect = new Rect();
            String txt = String.valueOf(maxCeil) + "88";
            p.getTextBounds(txt, 0, txt.length(), rect);
            textw = rect.width();
            txt = String.valueOf(minCeil) + "88";
            p.getTextBounds(txt, 0, txt.length(), rect);
            if (textw < rect.width())
                textw = rect.width();
            textw += 5;
        }
        int vheight = height - gridPaddingTop - gridPaddingBottom;
        int vwidth = (int) (width - gridPaddingLeft - gridPaddingRight - textw);
        /** ˮƽ���߾��� */
        float verticalSpace = (vheight * 1.0f) / (ceilValue * 1.0f);
        gridStart = gridPaddingLeft;
        // ����
        Paint paint = new Paint();
        paint.setColor(textColor);
        FontMetrics fm = paint.getFontMetrics();
        paint.setTextAlign(Paint.Align.RIGHT);
        gridStart += textw;

        for (int i = 0; i <= ceilValue; i++) {
            if (isShowGrid) {
                p.setColor(gridColor);
                canvas.drawLine(gridStart, gridPaddingTop + i * verticalSpace,
                        gridStart + vwidth, gridPaddingTop + i * verticalSpace,
                        p);
            }
            if (isShowScale) {
                if (i > 0 && i < ceilValue) {
                    p.setColor(textColor);
                    String cs = String.format("%.1f", maxCeil - i * vsp);
                    if (cs.endsWith("0"))
                        cs = cs.substring(0, cs.length() - 2);
                    canvas.drawText(cs, gridStart, gridPaddingTop + i
                                    * verticalSpace - fm.ascent - toPixel(context, 5),
                            paint);
                }
            }
        }
        if (isShowGrid) {
            p.setColor(gridColor);
            canvas.drawLine(gridStart, gridPaddingTop, gridStart,
                    gridPaddingTop + vheight, p);
            canvas.drawLine(gridStart + vwidth, gridPaddingTop, gridStart
                    + vwidth, gridPaddingTop + vheight, p);
        }
        if (isShowScale) {
            canvas.drawText(String.valueOf(maxCeil), gridStart, gridPaddingTop
                    - fm.ascent - toPixel(context, 5), paint);
            canvas.drawText(String.valueOf(minCeil), gridStart, gridPaddingTop
                    + vheight + toPixel(context, 5), paint);
        }
        canvas.restore();
        if (isStatic)
            drawStaticCurve(canvas, vwidth, vheight, verticalSpace, vsp);
        else {
            drawDynamicCurve(canvas, vwidth, vheight, verticalSpace, vsp);
        }
    }

    private void drawStaticCurve(Canvas canvas, int width, int height,
                                 float hsp, float hv) {
        Paint p = new Paint();
        canvas.save();
        canvas.translate(getPaddingLeft() + gridStart, getPaddingTop()
                + gridPaddingTop);
        canvas.clipRect(0, 0, width, height + 1);
        if (staticData != null && staticData[0] != null) {
            boolean po = false;
            int s = staticData[0].length;
            if (pointX == null) {
                pointX = new float[s];
                po = true;
            }

            for (int k = 0; k < staticData.length; k++) {
                p.setColor(curveColor[k]);
                boolean flag = false;

                float px = 0;
                float py = 0;
                if (staticData[k] == null)
                    continue;
                for (int i = 0; i < s; i++) {
                    float v = staticData[k][i];
                    float x = (i * pointGap);
                    float y = height - ((minCeil - v) * (-1.0f) * hsp) / hv;
                    if (po)
                        pointX[i] = x;

                    if (flag) {
                        canvas.drawLine(px, py, x, y, p);
                    } else {
                        flag = true;
                    }
                    px = x;
                    py = y;
                }
            }
        }

        if (showTips && selectPoint != -1) {
            p.setColor(selectLineColor);
            canvas.drawLine(pointX[selectPoint], 0, pointX[selectPoint],
                    height, p);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setColor(popupColor);
            FontMetrics fm = paint.getFontMetrics();
            int bd = 2;
            float fms = fm.descent - fm.ascent;
            float x = width / 2 - 20;
            float y = gridPaddingTop + 10;
            Rect bounds = new Rect();
            for (int i = 0; i < staticData.length; i++) {
                if (staticData[i] == null)
                    continue;
                String txt = String.valueOf(staticData[i][selectPoint]);
                paint.getTextBounds(txt, 0, txt.length(), bounds);
                RectF rf = new RectF(x - bd, y - bd, x + bounds.width() + bd, y
                        + fms + bd);
                // canvas.drawRoundRect(rf, 3, 3, paint);
                p.setColor(curveColor[i]);
                canvas.drawText(txt, x, y - fm.ascent, p);
                y += fms + bd + 3;
            }
            selectPoint = -1;
        }
        canvas.restore();
    }

    private DataQueue[] dataList = null;

    private void drawDynamicCurve(Canvas canvas, int width, int height,
                                  float hsp, float hv) {

        if (dataList != null && dataList[0] != null) {
            canvas.save();
            canvas.translate(getPaddingLeft() + gridStart, getPaddingTop()
                    + gridPaddingTop);
            canvas.clipRect(0, 0, width, height + 1);
            Paint p = new Paint();
            int s = dataList[0].getListSize();
            float mx = minCeil;
            float mi = maxCeil;
            for (int i = 0; i < dataList.length; i++) {
                if (dataList[i] == null)
                    continue;
                p.setColor(curveColor[i]);
                boolean flag = false;
                float px = 0;
                float py = 0;
                float tmp = width - s * pointGap;
                if (tmp > 0) {
                    for (int j = 0; j < s; j++) {
                        float v = dataList[i].getData(j);
                        if (mx < v)
                            mx = v;
                        if (mi > v)
                            mi = v;
                        float x = width - j * pointGap;
                        float y = height - ((minCeil - v) * (-1.0f) * hsp) / hv;

                        if (flag) {
                            canvas.drawLine(px, py, x, y, p);
                        } else {
                            flag = true;
                        }
                        px = x;
                        py = y;
                    }
                } else {
                    int k = 0;
                    for (int j = s - 1; j >= 0; j--) {
                        float v = dataList[i].getData(j);
                        if (mx < v)
                            mx = v;
                        if (mi > v)
                            mi = v;
                        float x = k * pointGap;
                        k++;
                        if (x > width)
                            break;
                        float y = height - ((minCeil - v) * (-1.0f) * hsp) / hv;

                        if (flag) {
                            canvas.drawLine(px, py, x, y, p);
                        } else {
                            flag = true;
                        }
                        px = x;
                        py = y;
                    }
                }
            }
            if (maxValue > mx)
                maxValue = mx;
            if (minValue < mi)
                minValue = mi;
            canvas.restore();
            FontMetrics fm = p.getFontMetrics();
            float tw = 0;
            float ty = gridPaddingTop + height + toPixel(context, 15)
                    - fm.ascent;
            float tx = gridPaddingLeft + toPixel(context, 30);

            for (int i = 0; i < dataList.length; i++) {
                if (dataList[i] == null)
                    continue;
                String tmp = String.valueOf(dataList[i].getData(s - 1));
                Rect r = new Rect();
                p.setColor(curveColor[i]);
                p.getTextBounds(tmp, 0, tmp.length(), r);
                tw += r.width();
                canvas.drawText(tmp, tx, ty, p);
                tx += tw + toPixel(context, 10);
            }
        }
    }

    private boolean isDown = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            reDraw();
        }
    };

    private void reDraw() {
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (showTips == false || isStatic == false)
            return false;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isDown) {
                    handler.removeMessages(100);
                    return true;
                } else {
                    isDown = true;
                }
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                selectPoint = getPointData(x, y);
                invalidate();
                isDown = false;
                return true;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(100, 5000);
                isDown = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                isDown = false;
                break;
        }
        return false;
    }

    private int getPointData(float x, float y) {
        if (pointX != null && pointX.length > 2) {
            float sp = pointX[1] - pointX[0];
            if (lastPoint != null) {
                if (lastPoint.x > x) {
                    for (int i = (int) lastPoint.y; i >= 0; i--) {
                        if (Math.abs(x - pointX[i]) <= sp) {
                            lastPoint.x = x;
                            lastPoint.y = i;
                            return i;
                        }
                    }
                } else if (lastPoint.x < x) {
                    for (int i = (int) lastPoint.y; i < pointX.length; i++) {
                        if (Math.abs(x - pointX[i]) <= sp) {
                            lastPoint.x = x;
                            lastPoint.y = i;
                            return i;
                        }
                    }
                } else {
                    return (int) lastPoint.y;
                }
            } else {
                for (int i = 0; i < pointX.length; i++) {
                    if (Math.abs(x - pointX[i]) <= sp) {
                        lastPoint = new PointF();
                        lastPoint.x = x;
                        lastPoint.y = i;
                        return i;
                    }
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    private int toPixel(Context context, int dip) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dip,
                res.getDisplayMetrics());
        return (int) px;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init(context);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private class DataQueue {
        private LinkedList<Float> list = new LinkedList<Float>();
        private int size = 1;

        public DataQueue() {
        }

        public DataQueue(int s) {
            size = s;
        }

        public void setSize(int s) {
            if (s <= 1)
                return;
            if (size > s && list.size() > s) {
                for (int i = 0; i < list.size() - s; i++) {
                    list.poll();
                }
            }
            size = s;
        }

        public void add(Float f) {
            if (list.size() >= size) {
                list.poll();
            }
            list.add(f);
        }

        public Float getData(int index) {
            return list.get(index);
        }

        public int getListSize() {
            return list.size();
        }

        public void removeAll() {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.remove();
                }
            }
        }
    }

    public void BuildDynamicECGWavePath(Path path, PointF lastPoint,
                                        Rect rCurveRect, short[] buffer, int offset, int count) {
        if ((offset < 0) || (offset >= buffer.length)) {
            return;
        }
        int m_ZeroVal = 0;
        int end = offset + count;
        if (end > buffer.length)
            end = buffer.length - offset;

        float horzSolution = 0.5f;
        float vertSolution = 0.5f;

        float zeroLine = rCurveRect.top + (rCurveRect.height() >> 1);

        if (lastPoint.x < 0.0f) {
            lastPoint.x = 0.0f;
            lastPoint.y = (-buffer[offset] - m_ZeroVal) * vertSolution
                    + zeroLine;

            ++offset;
        }

        float offsetX = lastPoint.x + rCurveRect.left;
        path.moveTo(offsetX, lastPoint.y);

        boolean bMoveTo = false;
        int nowX = (int) (offsetX + horzSolution);
        if (nowX >= rCurveRect.right) {
            bMoveTo = true;
            nowX = rCurveRect.left;
            offsetX = nowX;
        }

        int beginVal = -buffer[offset];
        int endVal = beginVal;
        int maxVal = beginVal;
        int minVal = beginVal;

        for (int i = offset + 1; i < end; ++i) {
            int tmp = (int) (offsetX + (i - offset + 1) * horzSolution);
            if (tmp == nowX) {
                endVal = -buffer[i];
                if (minVal > endVal)
                    minVal = endVal;
                if (maxVal < endVal)
                    maxVal = endVal;
            } else {
                if (bMoveTo) {
                    path.moveTo(nowX, (beginVal - m_ZeroVal) * vertSolution
                            + zeroLine);
                    bMoveTo = false;
                } else {
                    path.lineTo(nowX, (beginVal - m_ZeroVal) * vertSolution
                            + zeroLine);
                }
                path.lineTo(nowX, (minVal - m_ZeroVal) * vertSolution
                        + zeroLine);
                path.lineTo(nowX, (maxVal - m_ZeroVal) * vertSolution
                        + zeroLine);
                path.lineTo(nowX, (endVal - m_ZeroVal) * vertSolution
                        + zeroLine);

                nowX = tmp;
                if (nowX >= rCurveRect.right) {
                    bMoveTo = true;
                    nowX = rCurveRect.left;
                    offsetX = nowX;
                }

                beginVal = -buffer[i];
                endVal = beginVal;
                maxVal = beginVal;
                minVal = beginVal;
            }
        }

        lastPoint.x = nowX - rCurveRect.left;
        lastPoint.y = (endVal - m_ZeroVal) * vertSolution + zeroLine;

        if (bMoveTo) {
            path.moveTo(nowX, (beginVal - m_ZeroVal) * vertSolution + zeroLine);
        } else {
            path.lineTo(nowX, (beginVal - m_ZeroVal) * vertSolution + zeroLine);
        }
        path.lineTo(nowX, (minVal - m_ZeroVal) * vertSolution + zeroLine);
        path.lineTo(nowX, (maxVal - m_ZeroVal) * vertSolution + zeroLine);
        path.lineTo(nowX, lastPoint.y);
    }
}
