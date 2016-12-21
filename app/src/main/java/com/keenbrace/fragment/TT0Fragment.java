package com.keenbrace.fragment;



import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.keenbrace.R;


import java.util.Calendar;
import java.util.Locale;


public class TT0Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View view;
    private RadioGroup rg_gender;
    private RadioButton rb_female;
    private RadioButton rb_male;
    public Fragment fragment=null;

    private EditText et_weight;   //体重输入框
    private EditText et_height;   //身高输入框
    private InputMethodManager m;
    private View activityRootView; //最外层视图
    private View rl; //点击就隐藏的视图
    private View tv;
    private int weightBtn=0;  //weight设定状态
    private int heightBtn=0;

    //日期参数
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button btn;           //年龄按钮

    public TT0Fragment() {
        // Required empty public constructor
    }

    TT3Fragment tt3Fragment=new TT3Fragment();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_tt0, container, false);

        //监听性别选项
        rg_gender=(RadioGroup)view.findViewById(R.id.rg_gender);
        rb_female=(RadioButton)view.findViewById(R.id.rb_gender_female);
        rb_male=(RadioButton)view.findViewById(R.id.rb_gender_male);

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_female.getId() == checkedId) {
                    fragment=tt3Fragment.newInstance("female");
                } else if (rb_male.getId() == checkedId) {
                    fragment=tt3Fragment.newInstance("male");
                }
            }
        });

        //初始化日历对象
        Calendar c = Calendar.getInstance(Locale.CHINA);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //设置点击事件
        btn = (Button) view.findViewById(R.id.btn_date);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisplay();
                DatePickerDialog dpd=new DatePickerDialog(view.getContext(),mDateSetListener,mYear,mMonth,mDay);
                dpd.show();
            }
        });

        //体重、身高输入框
        et_weight=(EditText) view.findViewById(R.id.editText1);
        et_height=(EditText) view.findViewById(R.id.editText2);
        //该Activity的最外层Layout
        activityRootView = view.findViewById(R.id.root);
        //弹出键盘时隐藏上层视图
        rl=view.findViewById(R.id.rl1);
        tv=view.findViewById(R.id.rl_as_name);

        //获得焦点，弹出键盘   weightBtn,heightBtn设定为两个输入框的状态：0失去焦点，1得到焦点，2弹出键盘
        et_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    et_weight.setText("");
                    weightBtn=1;
                    rl.setVisibility(View.GONE);     //隐藏方法调用
                    tv.setVisibility(View.GONE);
                }else {
                    weightBtn=0;
                }
            }
        });

        et_height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    et_height.setText("");
                    heightBtn=1;
                    rl.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }else {
                    heightBtn=0;
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        m=(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //添加layout大小发生改变监听器
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(rect);
                // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                int rootInvisibleHeight = activityRootView.getRootView()
                        .getHeight() - rect.bottom;
                // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                if (rootInvisibleHeight > 100&&(weightBtn==1)&&(heightBtn==0)) {
                    //软键盘弹出来的时候
                    weightBtn=2;
                } else if (rootInvisibleHeight > 100&&(heightBtn==1)&&(weightBtn==0)){
                    //软键盘弹出来的时候
                    heightBtn=2;
                } else if (rootInvisibleHeight<100&&(weightBtn==2)&&(heightBtn==0)){
                    // 软键盘没有弹出来的时候
                    et_weight.clearFocus();
                    rl.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                } else if (rootInvisibleHeight<100&&(heightBtn==2)&&(weightBtn==0)) {
                    // 软键盘没有弹出来的时候
                    et_height.clearFocus();
                    rl.setVisibility(View.VISIBLE);     //显示上层视图
                    tv.setVisibility(View.VISIBLE);
                }
            }
                });
    }

    //设置显示日期
    private void updateDisplay() {
        btn.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    //监听日期设置事件
    private DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

}
