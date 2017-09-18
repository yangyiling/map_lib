package com.emax.map_lib.until;

import android.Manifest;
import android.app.Activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.emax.map_lib.event.IMapManager;
import com.emax.map_lib.event.MoveMapEvent;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Created by 90835 on 2017/8/25.
 */

public class MapFactory implements AMapLocationListener {
    /*高德定位相关*/
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient;
    private IMapManager mapManager;
    private MoveMapEvent onMapMoveListener;
    private boolean isInChina = false;

    private MapBuilder mapBuilder;

    public MapFactory(MapBuilder mapBuilder) {
        this.mapBuilder = mapBuilder;
        RxPermissions rxPermissions = new RxPermissions((Activity) mapBuilder.getContext());
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                    } else {
                        initMap();
                    }
                });

    /*    this.isAllGestures = mapBuilder.getAllGestures();
        this.isZoomControls = mapBuilder.getZoomControls();
        this.isScrollGestures = mapBuilder.getScrollGestures();
        this.isRotateGestures = mapBuilder.getRotateGestures();
        this.icon = mapBuilder.getIcon();
        this.showMyLocation = mapBuilder.getShowMyLocation();*/
    }

    private void initMap() {
        mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(mapBuilder.getContext());
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    public IMapManager getMapManager() {
        isInChina=false;
        if (!isInChina) {
            mapManager = new LocationGoogleMapManage(mapBuilder);
        } else {
            mapManager = new LocationAmapManage(mapBuilder);
        }
        return mapManager;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        isInChina = CoordinateConverter.isAMapDataAvailable(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        getMapManager();

    }


}
