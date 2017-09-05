package com.emax.map_lib;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 90835 on 2017/8/9.
 */

public class LocationGoogleMapManage implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, IMapManager {

    private Context context;
    private FrameLayout mapParent;
    private GoogleMap mGoogleMap;
    private MarkerOptions options;
    private Marker marker;
    private String city;
    private double lat, lng;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private Address myAddress;
    private Address moveAddress;
    private LatLngData latLngData;
    private MapBuilder builder;

    public LocationGoogleMapManage(MapBuilder builder, MapFactory.OnMapMoveListener listener) {
        this.context = builder.getContext();
        this.builder = builder;
        mapParent = (FrameLayout) builder.getMapRootView().findViewById(R.id.map_container);
        LayoutInflater.from(context).inflate(R.layout.fragment_googlemap, mapParent);
        MapFragment mapFragment = (MapFragment) builder.getManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        initMap();
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 100)// 10秒，以毫秒为单位
                .setFastestInterval(10000); // 1秒，以毫秒为单位
        googleApiClient.connect();
    }

    @SuppressWarnings("MissingPermission")
    private void initMap() {
        mGoogleMap.setMapType(builder.getMapType());
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setMyLocationEnabled(builder.getShowMyLocation());
        mGoogleMap.setOnMyLocationButtonClickListener(() -> {
            moveMyLocation();
            return false;
        });
        UiSettings mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(builder.getAllGestures());
        mUiSettings.setIndoorLevelPickerEnabled(builder.getIndoor());
        mUiSettings.setMyLocationButtonEnabled(builder.getShowMyLocation());
        mUiSettings.setTiltGesturesEnabled(builder.getTiltGestures());
        mUiSettings.setMapToolbarEnabled(builder.getMapToolBar());
        mUiSettings.setRotateGesturesEnabled(builder.getRotateGestures());
        mUiSettings.setCompassEnabled(builder.getCompassEnabled());
        mUiSettings.setZoomControlsEnabled(builder.getZoomControls());
        mUiSettings.setZoomGesturesEnabled(builder.getZoomGestures());
        mUiSettings.setScrollGesturesEnabled(builder.getScrollGestures());
    }

    private void moveMyLocation() {
        myAddress = getAddressDetail(mLocation.getLatitude(), mLocation.getLongitude());
        if (TextUtils.isEmpty(myAddress.getLocality())) {
            myAddress.setLocality(myAddress.getAdminArea());
        }
        this.city = myAddress.getLocality();
        this.lat = myAddress.getLatitude();
        this.lng = myAddress.getLongitude();
        show();
    }


    private void show() {
        if (TextUtils.isEmpty(city)) return;
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0 && mGoogleMap != null) {
            LatLng latLng = new LatLng(lat, lng);
            if (builder.getMoveAnimateCamera()) {
                mGoogleMap.setOnMapLoadedCallback(() -> {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, builder.getLevel()), 2000, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                });
            } else {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, builder.getLevel()));
            }
        }
    }

    /*谷歌服务连接*/
    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        if (mLocation != null) {
            moveMyLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    /*
    * google服务连接失败
    * */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("连接错误：", connectionResult.getErrorMessage());
        Toast.makeText(context, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    /*
    *
    * 实时定位的位置变化
    * */
    @Override
    public void onLocationChanged(Location location) {
        this.mLocation = location;
        myAddress = getAddressDetail(mLocation.getLatitude(), mLocation.getLongitude());
    }

    /*
    * 地图移动中，不现实marker
    *
    * */
    @Override
    public void onCameraMove() {
        if (marker != null) {
            marker.remove();
        }
    }

    /*
    * 地图移动结束
    * */
    @Override
    public void onCameraIdle() {
        Double lat = mGoogleMap.getCameraPosition().target.latitude;
        Double lng = mGoogleMap.getCameraPosition().target.longitude;
        latLngData = new LatLngData(lat, lng);
    }

    /*
    *
    * 根据坐标解析地址
    * */
    public Address getAddressDetail(Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            final List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    *
    * 根据ID解析地址
    * */
    public void getPlaceInfo(Object data) {
        AutocompletePrediction prediction = (AutocompletePrediction) data;
        Places.GeoDataApi.getPlaceById(googleApiClient, prediction.getPlaceId())
                .setResultCallback(places -> {
                    if (places.getStatus().isSuccess() && places.getCount() > 0) {
                        final Place myPlace = places.get(0);
                        this.lat = myPlace.getLatLng().latitude;
                        this.lng = myPlace.getLatLng().longitude;
                        city = myPlace.getName().toString();
                        show();
                    } else {
                    }
                    places.release();
                });
    }

    /*
    * 生命周期
    * */
    @Override
    public void onResume() {
        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
}
