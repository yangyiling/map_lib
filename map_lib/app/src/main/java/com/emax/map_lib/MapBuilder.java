package com.emax.map_lib;

import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

import com.amap.api.maps.model.MyLocationStyle;

/**
 * Created by 90835 on 2017/9/5.
 */

public class MapBuilder implements Builder {

    public static final int MAP_NOMARL = 1;
    public static final int MAP_SATELLITE = 2;
    private Boolean isZoomControls = false;
    private Boolean isZoomGestures = false;
    private Boolean isScrollGestures = true;
    private Boolean isRotateGestures = false;
    private Boolean isAllGestures = true;
    private Boolean showMyLocation = true;
    private Boolean isTiltGestures = false;
    private Boolean isCompassEnabled = false;
    private Boolean isMapToolBar = false;
    private Boolean isIndoor = false;
    private MyLocationStyle myLocationStyle;
    private int icon;
    private View mapRootView;
    private Context context;
    private FragmentManager manager;
    private int mapType=MAP_NOMARL;
    private Boolean isMoveAnimateCamera=false;
    private float level=16;

    public MapBuilder(Context context) {
        this.context = context;
    }


    @Override
    public MapBuilder setFragmentManage(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    public MapBuilder setMapView(View view) {
        this.mapRootView = view;
        return this;
    }

    public FragmentManager getManager() {
        return manager;
    }

    @Override
    public MapBuilder setZoomGesturesEnabled(Boolean isZoomGestures) {
        this.isZoomGestures = isZoomGestures;
        return this;
    }

    public Boolean getZoomGestures() {
        return isZoomGestures;
    }

    @Override
    public MapBuilder setTiltGesturesEnabled(Boolean isTiltGestures) {
        this.isTiltGestures = isTiltGestures;
        return this;
    }

    @Override
    public MapBuilder setScrollGesturesEnabled(Boolean isScrollGestures) {
        this.isScrollGestures = isScrollGestures;
        return this;
    }

    @Override
    public MapBuilder setRotateGesturesEnabled(Boolean isRotateGestures) {
        this.isRotateGestures = isRotateGestures;
        return this;
    }

    @Override
    public MapBuilder setAllGesturesEnabled(Boolean isAllGestures) {
        this.isAllGestures = isAllGestures;
        return this;
    }


    public Boolean getTiltGestures() {
        return isTiltGestures;
    }

    @Override
    public MapBuilder setShowMyLocation(Boolean showMyLocation) {
        this.showMyLocation = showMyLocation;
        return this;
    }

    @Override
    public MapBuilder myLocationIcon(int icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public MapBuilder setIndoorLevelPickerEnabled(Boolean isIndoor) {
        this.isIndoor = isIndoor;
        return this;
    }

    @Override
    public MapBuilder setMapToolbarEnabled(Boolean isMapToolBar) {
        this.isMapToolBar = isMapToolBar;
        return this;
    }

    @Override
    public MapBuilder setMyLocationStyle(int type) {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(type);
        return this;
    }

    public MyLocationStyle getMyLocationStyle() {
        return myLocationStyle;
    }

    @Override
    public MapBuilder setCompassEnabled(Boolean isCompassEnabled) {
        this.isCompassEnabled = isCompassEnabled;
        return this;
    }

    @Override
    public MapBuilder setMapType(int mapType) {
        this.mapType=mapType;
        return this;
    }

    @Override
    public MapBuilder setMoveAnimateCamera(Boolean isMoveAnimateCamera) {
        this.isMoveAnimateCamera=isMoveAnimateCamera;
        return this;
    }

    @Override
    public MapBuilder setZoomLevel(float level) {
        this.level=level;
        return this;
    }

    @Override
    public MapBuilder setZoomControlsEnabled(Boolean isZoomControls) {
        this.isZoomControls=isZoomControls;
        return this;
    }

    public float getLevel() {
        return level;
    }

    public Boolean getMapToolBar() {
        return isMapToolBar;
    }

    public Boolean getIndoor() {
        return isIndoor;
    }

    public Boolean getMoveAnimateCamera() {
        return isMoveAnimateCamera;
    }

    public int getMapType() {
        return mapType;
    }

    public Boolean getCompassEnabled() {
        return isCompassEnabled;
    }

    public Boolean getZoomControls() {
        return isZoomControls;
    }

    public Boolean getScrollGestures() {
        return isScrollGestures;
    }

    public Boolean getRotateGestures() {
        return isRotateGestures;
    }

    public Boolean getAllGestures() {
        return isAllGestures;
    }

    public Boolean getShowMyLocation() {
        return showMyLocation;
    }

    public int getIcon() {
        return icon;
    }

    public View getMapRootView() {
        return mapRootView;
    }

    public Context getContext() {
        return context;
    }

    public MapFactory build() {
        return new MapFactory(this);
    }

}
