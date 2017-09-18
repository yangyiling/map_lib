package com.emax.map_lib.until;

import android.content.Context;
import android.location.Address;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.emax.map_lib.R;
import com.emax.map_lib.bean.AddressData;
import com.emax.map_lib.event.IMapManager;
import com.emax.map_lib.event.MoveMapEvent;

/**
 * Created by 90835 on 2017/8/25.
 */

public class LocationAmapManage implements IMapManager, AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
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
    private MoveMapEvent listener;
    private Address moveAddress;
    private AMapLocation aMapLocation;
    private AddressData addressData;
    private MapBuilder builder;
    private Boolean isContinuousLocation = true;
    private MyLocationStyle myLocationStyle;
    private GeocodeSearch geocodeSearch;
    private RegeocodeQuery query;


    public LocationAmapManage(MapBuilder builder) {
        this.context = builder.getContext();
        this.listener = builder.getListener();
        this.builder = builder;
        mapParent = (FrameLayout) builder.getMapRootView().findViewById(R.id.map_container);
        LayoutInflater.from(context).inflate(R.layout.fragment_gmap, mapParent);
        mapFragment = (MapFragment) builder.getManager().findFragmentById(R.id.amap_map);
        if (aMap == null) {
            aMap = mapFragment.getMap();
        }
        initMap();
    }

    private void initMap() {
        initSetting();
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
        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    private void initSetting() {
        aMap.setMyLocationEnabled(builder.getShowMyLocation());
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setOnCameraChangeListener(this);
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(builder.getAllGestures());
        mUiSettings.setIndoorSwitchEnabled(builder.getIndoor());
        mUiSettings.setMyLocationButtonEnabled(builder.getShowMyLocation());
        mUiSettings.setTiltGesturesEnabled(builder.getTiltGestures());
//        mUiSettings.setMapToolbarEnabled(builder.getMapToolBar());
        mUiSettings.setRotateGesturesEnabled(builder.getRotateGestures());
        mUiSettings.setCompassEnabled(builder.getCompassEnabled());
        mUiSettings.setZoomControlsEnabled(builder.getZoomControls());
        mUiSettings.setZoomGesturesEnabled(builder.getZoomGestures());
        mUiSettings.setScrollGesturesEnabled(builder.getScrollGestures());


    }

    private void show() {
        if (aMap != null) {
            final LatLng latLng = new LatLng(lat, lng);
            if (builder.getMoveAnimateCamera()) {
                //设置希望展示的地图缩放级别
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, new AMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        } else {
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
            if (isContinuousLocation) {
                moveMyLocation();
                isContinuousLocation = builder.getContinuousLocation();
            }
        }
    }

    private void moveMyLocation() {
        this.city = aMapLocation.getCity();
        this.lat = aMapLocation.getLatitude();
        this.lng = aMapLocation.getLongitude();
        show();
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
//        latLngData = new LatLngData(lat, lng);
//        listener.stop(latLngData);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    /*逆地址解析*/
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtils.a("解析-++++++++++++++：", regeocodeResult.getRegeocodeQuery());
        addressData = new AddressData();
        addressData.setAddressDetail(regeocodeResult.getRegeocodeAddress().getFormatAddress());
        addressData.setCity(regeocodeResult.getRegeocodeAddress().getCity());
        addressData.setArea(regeocodeResult.getRegeocodeAddress().getDistrict());
        addressData.setProvincial(regeocodeResult.getRegeocodeAddress().getProvince());
        addressData.setTown(regeocodeResult.getRegeocodeAddress().getTownship());
        addressData.setPostcode(regeocodeResult.getRegeocodeAddress().getCityCode());
        addressData.setLatitude(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude());
        addressData.setLongitude(regeocodeResult.getRegeocodeQuery().getPoint().getLongitude());
        listener.stop(addressData);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtils.a("解析-++++++++++++++：", geocodeResult.getGeocodeQuery());
    }
}
