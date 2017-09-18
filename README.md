＃集成谷歌地图和高德地图
根据地区自动选择加载地图，满足基本需求，使用方法：
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
                
             
