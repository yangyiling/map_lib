package com.emax.map_lib;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.amap.api.maps.model.MyLocationStyle;
import com.google.android.gms.common.api.GoogleApiClient;

public class TestActivity extends AppCompatActivity implements MapFactory.OnMapMoveListener {

    private FrameLayout mapContainer;
    private MapFactory mapFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mapContainer = (FrameLayout) findViewById(R.id.map_container);
        mapFactory = new MapBuilder(this)
                .setMapView(mapContainer)
                .setMapType(MapBuilder.MAP_NOMARL)
                .setFragmentManage(getFragmentManager())
                .setZoomGesturesEnabled(true)
                .setAllGesturesEnabled(true)
                .setCompassEnabled(true)
                .setShowMyLocation(true)
                .setMyLocationStyle(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER)
                .setRotateGesturesEnabled(true)
                .setScrollGesturesEnabled(true)
                .setTiltGesturesEnabled(true)
                .setMoveAnimateCamera(false)
                .setIndoorLevelPickerEnabled(true)
                .setMapToolbarEnabled(true)
                .setZoomControlsEnabled(true)
                .build();

    }

    @Override
    public void move() {

    }

    @Override
    public void step(LatLngData latLngData) {

    }

    @Override
    public void setMoveAddress(Address moveAddress) {

    }

    @Override
    public void initGoogleApi(GoogleApiClient googleApiClient) {

    }

    @Override
    public void mapManager(IMapManager mapManager, boolean isInChina) {

    }
}
