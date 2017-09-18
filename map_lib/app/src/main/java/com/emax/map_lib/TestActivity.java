package com.emax.map_lib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.emax.map_lib.bean.AddressData;
import com.emax.map_lib.event.MoveMapEvent;
import com.emax.map_lib.until.LogUtils;
import com.emax.map_lib.until.MapBuilder;
import com.emax.map_lib.until.MapFactory;

public class TestActivity extends AppCompatActivity implements MoveMapEvent {

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
                .setRotateGesturesEnabled(true)
                .setScrollGesturesEnabled(true)
                .setTiltGesturesEnabled(true)
                .setMoveAnimateCamera(true)
                .setIndoorLevelPickerEnabled(true)
                .setMapToolbarEnabled(true)
                .setZoomControlsEnabled(true)
                .setContinuousLocation(false)
                .setMoveListener(this)
                .build();

    }

    @Override
    public void move() {

    }

    @Override
    public void stop(AddressData addressData) {
        LogUtils.e("是急急急急急急急急急急急急急急急急急急:", addressData);
    }
}
