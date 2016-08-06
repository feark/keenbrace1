package com.keenbrace.activity;

import com.amap.api.maps2d.AMapUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.util.DateUitl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentMapGoogle extends Fragment implements
        OnClickListener, LocationSource, SensorEventListener,
        OnItemClickListener, OnMarkerClickListener, LocationListener {
    private OnLocationChangedListener mListener;
    private GoogleMap aMap;
    private MapView mapView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Marker startMarker, endMarker;
    TextView tv_factors, tv_sumtimes, tv_steprate, tv_stride, tv_step,
            tv_speed, tv_distance, tv_calories;
    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frame_map_google, null);
        mSensorManager = (SensorManager) this.getActivity().getSystemService(
                Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mapView = (MapView) view.findViewById(R.id.map);


        mapView.onCreate(savedInstanceState);//
        aMap = mapView.getMap();

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tv_factors = (TextView) view.findViewById(R.id.tv_factors);
        tv_sumtimes = (TextView) view.findViewById(R.id.tv_sumtimes);
        tv_steprate = (TextView) view.findViewById(R.id.tv_steprate);
        tv_stride = (TextView) view.findViewById(R.id.tv_stride);
        tv_step = (TextView) view.findViewById(R.id.tv_step);
        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_calories = (TextView) view.findViewById(R.id.tv_calories);
//        init();
//        aMap.setOnMarkerClickListener(this);//
        return view;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    public void updateTime(long mins) {
        tv_sumtimes.setText(DateUitl.getDateFormat4(mins) + "");
    }

    public void updateSpeed(int speed) {
        tv_speed.setText("" + DateUitl.formatToM(speed / 100000) + "km/h");
    }

    public void updateView(int factors, int steprate, int stride, int step,
                           long mins, float distance) {

        tv_factors.setText("" + factors);
        tv_steprate.setText(steprate + "/min");
        String stride_str = "Great";
        if (stride < 70)
            stride_str = "Great";
        else if (stride > 70 && stride < 90)
            stride_str = "Good";
        else if (stride >= 90 && stride < 110)
            stride_str = "Average";
        else if (stride >= 110 && stride < 130)
            stride_str = "Bad";
        else if (stride > 130)
            stride_str = "Risk";
        tv_stride.setText(stride_str);

        tv_step.setText("" + step);
        if (step != 0 && distance == 0) {
            distance = (int) (step * 74);// ����㷨��δʵ�� **

        }
        String ss = "m";
        String kk = "cal";
        if (distance > 100000) {
            distance = distance / 100000.0f;
            ss = "km";
            kk = "kcal";
        } else
            distance = distance / 100.0f;

        float calories = UtilConstants.Weight * distance * 1.306f;

        tv_distance.setText("" + DateUitl.formatToM(distance) + ss);
        tv_calories.setText("" + DateUitl.formatToM(calories / 1000.0f) + kk);
    }

    /**
     *
     */
    private void setUpMap() {


        aMap.setLocationSource(this);//
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16.1f));
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        aMap.setMyLocationEnabled(true);

        // mGPSMarker = aMap.addMarker(new MarkerOptions().icon(
        //
        // BitmapDescriptorFactory.fromBitmap(BitmapFactory
        // .decodeResource(getResources(),
        // R.mipmap.location_marker))).anchor(
        // (float) 0.5, (float) 0.5));

        endMarker = aMap.addMarker(new MarkerOptions().icon(

                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.end_map)))
                .anchor((float) 0.5, (float) 0.5));

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /*
    public void startRun(RunWaring rw) {
        startMarker = aMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.start_map))
                .anchor((float) 0.5, (float) 1));
        startMarker.setPosition(new LatLng(latitude, longitude));
        rw.setLatitude(latitude);
        rw.setLongitude(longitude);
    }
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location aLocation) {

        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// ��ʾϵͳС����
            // mGPSMarker.setPosition(new LatLng(aLocation.getLatitude(),
            // aLocation.getLongitude()));
            double x = aLocation.getLatitude();
            double y = aLocation.getLongitude();
            if (x != latitude || longitude != y) {
                if (latitude != 0 && longitude != 0) {

                    distance = AMapUtils.calculateLineDistance(new com.amap.api.maps2d.model.LatLng(latitude,
                            longitude), new com.amap.api.maps2d.model.LatLng(x, y));
                    sumDistance += distance;
                    // txt_data5.setText(DateUitl.formatToM(sumlc / 1000.0f));
                    // txt_data4
                    // .setText(""
                    // + DateUitl
                    // .getDateFormat4((long) (1000 / (sumlc / mins))));
                    aMap.addPolyline((new PolylineOptions()).add(
                            new LatLng(latitude, longitude), new LatLng(x, y))
                            .width(20.0f));
                    sb.append(x + "," + y + ";");

                }

            }
            latitude = x;
            longitude = y;

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    StringBuffer sb = new StringBuffer();
    double latitude = 0, longitude = 0;
    float sumDistance = 0.0f;
    float distance = 0.0f;

    // ���
    public float getDistance() {
        return distance * 100;
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationManager == null)
            locationManager = (LocationManager) this.getActivity().
                    getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPS) {


            if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    20.0f, (android.location.LocationListener) mListener);


        }


    }

    @Override
    public void deactivate() {
        mListener = null;

        unRegisterSensorListener();
    }

    public void registerSensorListener() {
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterSensorListener() {
        mSensorManager.unregisterListener(this, mSensor);
    }

    private long lastTime = 0;
    private final int TIME_SENSOR = 100;
    private float mAngle;

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                float x = event.values[0];

                // x += getScreenRotationOnPhone(this.getActivity());
                x %= 360.0F;
                if (x > 180.0F)
                    x -= 360.0F;
                else if (x < -180.0F)
                    x += 360.0F;
                if (Math.abs(mAngle - 90 + x) < 3.0f) {
                    break;
                }
                mAngle = x;

                lastTime = System.currentTimeMillis();
            }
        }

    }

    public String getMaps() {
        return sb.toString();
    }


    public static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;

            case Surface.ROTATION_90:
                return 90;

            case Surface.ROTATION_180:
                return 180;

            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        registerSensorListener();
    }
}
