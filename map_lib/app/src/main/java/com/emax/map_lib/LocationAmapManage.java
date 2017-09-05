package com.emax.map_lib;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 90835 on 2017/8/25.
 */

public class LocationAmapManage implements IMapManager, AMapLocationListener, AMap.OnCameraChangeListener {
    private Context context;
    private FrameLayout mapParent;
    private MapFragment mapFragment;
    private AMap aMap;
    private Marker marker;
    private MarkerOptions markerOption;
    private UiSettings mUiSettings;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationClientOption mLocationOption = null;
    private String city;
    private double lat, lng;
    private MapFactory.OnMapMoveListener listener;
    private Address myAddress;
    private Address moveAddress;
    private AMapLocation aMapLocation;
    private LatLngData latLngData;

    public LocationAmapManage(MapBuilder builder,
                              MapFactory.OnMapMoveListener onMapMoveListener) {
        this.context = builder.getContext();
        this.listener = onMapMoveListener;
        mapParent = (FrameLayout) builder.getMapRootView().findViewById(R.id.map_container);
        LayoutInflater.from(context).inflate(R.layout.fragment_gmap, mapParent);
        mapFragment = (MapFragment) builder.getManager().findFragmentById(R.id.amap_map);
        if (aMap == null) {
            aMap = mapFragment.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(builder.getZoomControls());
        mUiSettings.setTiltGesturesEnabled(builder.getTiltGestures());
        mUiSettings.setCompassEnabled(builder.getCompassEnabled());
        mUiSettings.setScrollGesturesEnabled(builder.getScrollGestures());
        mUiSettings.setAllGesturesEnabled(builder.getAllGestures());
        mUiSettings.setRotateGesturesEnabled(builder.getRotateGestures());
        aMap.setMyLocationEnabled(builder.getShowMyLocation());
        aMap.setMyLocationStyle(builder.getMyLocationStyle());


        aMap.setOnCameraChangeListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(context);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(this);
        //启动定位
        mLocationClient.startLocation();

    }


    public void getPlaceInfo(Object o) {
        PoiItem poiItem = (PoiItem) o;
        this.lat = poiItem.getLatLonPoint().getLatitude();
        this.lng = poiItem.getLatLonPoint().getLongitude();
        city = poiItem.getCityName();
        show();
    }


    private void show() {
//        ToastUtil.show("定位数据：city==" + myAddress.getLocality() + "坐标为：lat==" + lat + "\t\tlng==" + lng, context);
        if (aMap != null) {
            final LatLng latLng = new LatLng(lat, lng);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        } else {
//            ToastUtil.show("测试结果：aMap为空", context);
        }

    }

    @Override
    public void onResume() {
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        mLocationClient.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            this.aMapLocation = aMapLocation;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (marker != null) {
            marker.remove();
        }
        listener.move();
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Double lat = cameraPosition.target.latitude;
        Double lng = cameraPosition.target.longitude;
        latLngData = new LatLngData(lat, lng);
        listener.step(latLngData);
//        Log.e("移动状态监听：", "111111111111111111111111111");
    }

    private void getAddressDetail(Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (aMapLocation == null) {
            return;
        }
        try {
            final List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
//                if (isMyLocation) {
//                    this.myAddress = addresses.get(0);
//                    if (TextUtils.isEmpty(myAddress.getLocality())) {
//                        myAddress.setLocality(myAddress.getAdminArea());
//                    }
//                } else {
                moveAddress = addresses.get(0);
                if (TextUtils.isEmpty(moveAddress.getLocality())) {
                    moveAddress.setLocality(moveAddress.getAdminArea());
                }
                this.city = moveAddress.getLocality();
                this.lat = moveAddress.getLatitude();
                this.lng = moveAddress.getLongitude();
                listener.setMoveAddress(moveAddress);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
