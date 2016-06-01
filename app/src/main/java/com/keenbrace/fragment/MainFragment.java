package com.keenbrace.fragment;


import android.content.Intent;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.activity.MainMenuActivity;
import com.keenbrace.activity.RecordActivity;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.core.utils.SystemTool;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.util.StringUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zrq on 16/1/28.
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.tx_times)
    TextView tx_times;
    @Bind(R.id.tx_warings)
    TextView tx_warings;
    @Bind(R.id.tx_mileage)
    TextView tx_mileage;
    @OnClick(R.id.ll_record)
    void openRecord()
    {
        readyGo(RecordActivity.class);
    }
    @OnClick (R.id.iv_more)
    void onMore(){
        ((MainActivity)getActivity()).showMenu();
    }

@OnClick(R.id.iv_run)
void gotoRun()
{
    readyGo(MainMenuActivity.class);
}
    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_select;
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        List<KeenBrace> datas = KeenbraceDBHelper.getInstance(this.getActivity()).queryKeenBraces();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = KeenbraceDBHelper.getInstance(this.getActivity()).querySumBle();
        int sumlc = 0;

        float[] pl = new float[datas.size()];
        int i = 0;
        for (KeenBrace data : datas) {
            sumlc += data.getMileage()==null?0:data.getMileage();
            pl[i] = data.getSumscore()==null?0:data.getSumscore();
            i++;
        }

        tx_times.setText("" + datas.size());
//		if (sumMap.get("timelength") != null)
//			tx_times.setText( DateUitl.getDateFormat4(Integer.parseInt(sumMap.get("timelength"))));
//		else
//			tx_times.setText("0");
        if (sumMap.get("sumwarings") != null)
            tx_warings.setText("" + sumMap.get("sumwarings"));
        else
            tx_warings.setText("0");
        if (sumMap.get("mileage") != null)
            tx_mileage.setText("" + StringUtil.formatToLC(sumMap.get("mileage")));
        else
            tx_mileage.setText("0");
    }


    public void readyGo(Class c)

    {
        Intent intent1 = new Intent(this.getActivity(),c);
        startActivity(intent1);

    }

}
