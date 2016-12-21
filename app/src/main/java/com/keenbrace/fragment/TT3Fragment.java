package com.keenbrace.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.keenbrace.R;


public class TT3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public TT3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tt3, container, false);
        Fragment_male male=new Fragment_male();
        Fragment_female female=new Fragment_female();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (this.getArguments()!=null){
            if (this.getArguments().getString("sex").equals("female")){
                //更新性别视图
                transaction.replace(R.id.viewFragment, female);
                transaction.commit();
            }else if (this.getArguments().getString("sex").equals("male")){
                transaction.replace(R.id.viewFragment, male);
                transaction.commit();
            }
        }else{
            //初始化加载female视图
            transaction.replace(R.id.viewFragment, female);
            transaction.commit();
            }
        return view;

    }

    //向TT3Fragment传递性别，新建一个TT3Fragment实例，然后将参数通过SetArguments设置到其中
    public static TT3Fragment newInstance(String sex){
        TT3Fragment tt3Fragment=new TT3Fragment();
        Bundle args=new Bundle();
        args.putString("sex",sex);
        tt3Fragment.setArguments(args);
        return tt3Fragment;
    }

}
