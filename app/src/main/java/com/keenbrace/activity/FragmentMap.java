package com.keenbrace.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.util.DateUitl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
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

public class FragmentMap extends Fragment implements OnClickListener,
		LocationSource, AMapLocationListener, SensorEventListener,
		OnItemClickListener, OnMarkerClickListener {
	private LocationManagerProxy mAMapLocationManager;
	private OnLocationChangedListener mListener;
	private AMap aMap;
	private MapView mapView;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private Marker startMarker, endMarker;
	TextView tv_factors, tv_sumtimes, tv_steprate, tv_stride, tv_step,
			tv_speed, tv_distance, tv_calories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frame_map, null);
		mSensorManager = (SensorManager) this.getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		tv_factors = (TextView) view.findViewById(R.id.tv_factors);
		tv_sumtimes = (TextView) view.findViewById(R.id.tv_sumtimes);
		tv_steprate = (TextView) view.findViewById(R.id.tv_steprate);
		tv_stride = (TextView) view.findViewById(R.id.tv_stride);
		tv_step = (TextView) view.findViewById(R.id.tv_step);
		tv_speed = (TextView) view.findViewById(R.id.tv_speed);
		tv_distance = (TextView) view.findViewById(R.id.tv_distance);
		tv_calories = (TextView) view.findViewById(R.id.tv_calories);
		init();
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
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
		tv_speed.setText("" + DateUitl.formatToM(speed  / 100000)
				+ "km/h");
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
			distance = (int) (step * 74);// 里程算法还未实现 **

		}
		String ss = "m";
		String kk="cal";
		if (distance > 100000) {
			distance = distance / 100000.0f;
			ss = "km";
			kk="kcal";
		} else
			distance = distance / 100.0f;

		float calories = UtilConstants.Weight * distance * 1.306f;

		tv_distance.setText("" + DateUitl.formatToM(distance) + ss);
		tv_calories.setText("" + DateUitl.formatToM(calories / 1000.0f)+kk);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// myLocationStyle.myLocationIcon(BitmapDescriptorFactory
		// .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.moveCamera(CameraUpdateFactory.zoomTo(16.1f));
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

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
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

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
	float sumlc = 0.0f;
	float lc = 0.0f;

	// 里程
	public float getDistance() {
		return lc * 100;
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			// mGPSMarker.setPosition(new LatLng(aLocation.getLatitude(),
			// aLocation.getLongitude()));
			double x = aLocation.getLatitude();
			double y = aLocation.getLongitude();
			if (x != latitude || longitude != y) {
				if (latitude != 0 && longitude != 0) {
					lc = AMapUtils.calculateLineDistance(new LatLng(latitude,
							longitude), new LatLng(x, y));
					sumlc += lc;
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
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this
					.getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */

			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 2000, 0.02f, this);

		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
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

	public String getMap() {
		return sb.toString();
	}

	/**
	 * 获取当前屏幕旋转角度
	 * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
	 */
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
