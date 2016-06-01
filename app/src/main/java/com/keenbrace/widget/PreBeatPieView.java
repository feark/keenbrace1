package com.keenbrace.widget;


import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.keenbrace.core.utils.WDevice;
import com.keenbrace.core.utils.WLoger;


public class PreBeatPieView extends View {

    private int ScrHeight;
    private int ScrWidth;

    private Paint[] arrPaintArc;
    private Paint whitePaint;
    private Paint PaintText = null;

    private final int arrColorRgb[][] = {{0x45, 0x76, 0xb5},
                                         {0xb8, 0x46, 0x44},
                                         {0x90, 0xb2, 0x4f}};
    private float arrPer[] = new float[3];

    private String titles[] = new String[]{"房性早搏", "室性早搏", "其他"};

    public PreBeatPieView(Context context) {
        super(context);
        init(context);
    }

    public PreBeatPieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PreBeatPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context){
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        if (Build.VERSION.SDK_INT>=11){
            this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }

        //设置边缘特殊效果
        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(
                1, BlurMaskFilter.Blur.INNER);

        arrPaintArc = new Paint[3];
        //Resources res = this.getResources();
        for(int i=0;i<3;i++) {
            arrPaintArc[i] = new Paint();
            //arrPaintArc[i].setColor(res.getColor(colors[i] ));
            arrPaintArc[i].setARGB(255, arrColorRgb[i][0], arrColorRgb[i][1], arrColorRgb[i][2]);
            arrPaintArc[i].setStyle(Paint.Style.FILL);
            arrPaintArc[i].setStrokeWidth(4);
            arrPaintArc[i].setMaskFilter(PaintBGBlur);
        }
        whitePaint = new Paint();
        whitePaint.setARGB(255,0xe5,0xe5,0xe5);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeWidth(4);

        PaintText = new Paint();
        PaintText.setColor(Color.BLACK);
        PaintText.setTextSize(WDevice.dpToPixel(context,14));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        ScrHeight = MeasureSpec.getSize(widthMeasureSpec);
        ScrWidth = MeasureSpec.getSize(widthMeasureSpec);

        WLoger.debug("scrWidth:"+ScrWidth);
    }

    public void setPercent(float per1,float per2){
        WLoger.debug("per1:"+per1+",per2:"+per2);
        arrPer[0] = per1;
        arrPer[1] = per2;
        arrPer[2] = (100-per1-per2) ;

        invalidate();
    }



    public void onDraw(Canvas canvas){
        //画布背景
//        canvas.drawColor(Color.WHITE);

        float cirX = ScrWidth / 2;
        float cirY = ScrHeight / 2-30;
        float radius = ScrWidth /4 ;
        //先画个圆确定下显示位置
        //canvas.drawCircle(cirX,cirY,radius,PaintArcRed);
        float arcLeft = cirX - radius;
        float arcTop  = cirY - radius ;
        float arcRight = cirX + radius ;
        float arcBottom = cirY + radius ;
        RectF arcRF0 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);


        float CurrPer = -90f; //偏移角度
        float Percentage =  0f; //当前所占比例

        int scrOffsetW = ScrWidth/5;
        int scrOffsetH = (int)(ScrHeight/2+radius);
        int scrOffsetT = (ScrWidth-10)/3;

        //Resources res = this.getResources();
        int i= 0;
        for(i=0; i<3; i++)
        {
            Percentage = 360 * (arrPer[i]/ 100);
            Percentage = (float)(Math.round(Percentage *100))/100;

            canvas.drawArc(arcRF0, CurrPer, Percentage, true, arrPaintArc[i]);

            canvas.drawRect(5+ i * scrOffsetT, scrOffsetH,
                    5 + 60 + i * scrOffsetT, scrOffsetH + 30, arrPaintArc[i]);
            canvas.drawText(String.valueOf(titles[i]),5 + 70 + i * scrOffsetT, scrOffsetH+30, PaintText);

            CurrPer += Percentage;
        }

        canvas.drawText("房性早搏",scrOffsetW, arcTop-30, PaintText);
        canvas.drawText("占"+(arrPer[0])+"%",scrOffsetW, arcTop+25, PaintText);
        canvas.drawText("室性早搏", arcRight-120, arcTop-30, PaintText);
        canvas.drawText("占"+(arrPer[1])+"%", arcRight-65, arcTop+25, PaintText);
    }
}