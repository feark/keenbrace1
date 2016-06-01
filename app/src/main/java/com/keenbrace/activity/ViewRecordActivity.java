package com.keenbrace.activity;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;

import com.keenbrace.R;
import com.keenbrace.adapter.WarnsListAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.RunWaring;
import com.keenbrace.util.DateUitl;
import com.keenbrace.widget.SwipeListView;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import butterknife.Bind;

public class ViewRecordActivity extends BaseActivity implements OnMapLoadedListener {
    private MapView mapView;
    private AMap aMap;

    @Bind(R.id.lv_data)
    SwipeListView lvdata;


    WarnsListAdapter adapter;

    KeenBrace keenBrace;
    TextView tx_mileage, tx_warings, tx_times, tx_calories;
    List<RunWaring> runWaringList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_record;
    }
    @Override
    protected boolean hasActionBar() {
        return true;
    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    public void initView() {
        this.setActionBarTitle("Running");
        mapView = (MapView) findViewById(R.id.map);

        keenBrace = (KeenBrace) this.getIntent().getSerializableExtra("bleData");

        runWaringList = KeenbraceDBHelper.getInstance(this).queryRunWaringByRunId(keenBrace.getId());
        adapter = new WarnsListAdapter(this);
        adapter.addRunWarings(runWaringList);
        lvdata.setAdapter(adapter);
        init();
        lvdata.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                adapter.setSelectId(arg2);
//				Intent intent = new Intent();
//				intent.setAction(SHOW_MODEL);
//				intent.putExtra("selectId", adapter.getDevice(arg2).getIndex()
//						+ "");
//				this.sendBroadcast(intent);

                Intent intent = new Intent();
                intent.putExtra("modelIndex", runWaringList.get(arg2).getIndex());
                intent.setClass(ViewRecordActivity.this, ModelAcitvity.class);
                startActivity(intent);
                return false;
            }

        });
        tx_mileage = (TextView) this.findViewById(R.id.tx_mileage);
        tx_warings = (TextView) this.findViewById(R.id.tx_warings);
        tx_times = (TextView) this.findViewById(R.id.tx_times);
        tx_calories = (TextView) this.findViewById(R.id.tx_calories);
        tx_times.setText(DateUitl.getDateFormat4(keenBrace.getTimelength()));
        tx_warings.setText("" + keenBrace.getSumwarings());
    }

    @Override
    public void initData() {


        float distance = keenBrace.getMileage()!=null?keenBrace.getMileage():0.0f;
        String ss = "m";
        String kk = "cal";
        if (distance > 100000) {
            distance = distance / 100000.0f;
            ss = "km";
            kk = "kcal";
        } else
            distance = distance / 100.0f;

        float calories = UtilConstants.Weight * distance * 1.306f;

        tx_mileage.setText("" + DateUitl.formatToM(distance) + ss);
        tx_calories.setText("" + DateUitl.formatToM(calories / 1000.0f) + kk);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_record, menu);
        return true;
    }

    /**
     *
            */
    private void init() {


        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(keenBrace.getLatitude(), keenBrace.getLongitude())).build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        String latlngs = keenBrace.getLatLngs();
        double bakx = keenBrace.getLatitude();
        double baky = keenBrace.getLongitude();
        Marker startMarker = aMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .fromResource(R.mipmap.start_map)).anchor(
                (float) 0.5, (float) 1));
        startMarker.setPosition(new LatLng(bakx, baky));
        Marker endtMarker = aMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .fromResource(R.mipmap.end_map)).anchor(
                (float) 0.5, (float) 1));
        endtMarker.setPosition(new LatLng(keenBrace.getEndlatitude(), keenBrace.getEndlatitude()));
        if (latlngs != null && !"".equals(latlngs)) {
            String[] sss = latlngs.split(";");
            if (sss != null && sss.length > 0) {

                for (String s : sss) {
                    String[] vs = s.split(",");
                    aMap.addPolyline((new PolylineOptions())
                            .add(new LatLng(bakx, baky),
                                    new LatLng(Double.parseDouble(vs[0]), Double.parseDouble(vs[1])))
                            .width(20.0f));
                    bakx = Double.parseDouble(vs[0]);
                    baky = Double.parseDouble(vs[1]);
                }
            }
        }

    }

}
