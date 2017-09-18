package com.emax.map_lib.event;

import android.app.FragmentManager;
import android.view.View;

import com.emax.map_lib.until.MapBuilder;

/**
 * Created by 90835 on 2017/9/5.
 */

public interface Builder {


    MapBuilder setFragmentManage(FragmentManager manager);

    MapBuilder setMapView(View view);


    /*
    * 缩放控制
    * */
    MapBuilder setZoomGesturesEnabled(Boolean isZoomGestures);

    /*    /*
    * 缩放控制
    * */
    MapBuilder setTiltGesturesEnabled(Boolean isTiltGestures);

    /*
    * 滑动
    * */
    MapBuilder setScrollGesturesEnabled(Boolean isScrollGestures);

    /*
    * 旋转
    * */
    MapBuilder setRotateGesturesEnabled(Boolean isRotateGestures);

    /*
    * 所有手势
    * */
    MapBuilder setAllGesturesEnabled(Boolean isAllGestures);

    /*
    * 显示定位蓝点
    * */

    MapBuilder setShowMyLocation(Boolean showMyLocation);

    /*
    * 显示定位蓝点的图标
    *
    * */
    MapBuilder myLocationIcon(int icon);

    /*
    * 层级选择器
    *
    * */
    MapBuilder setIndoorLevelPickerEnabled(Boolean isIndoor);

    /*
    * 地图工具   *
    * */
    MapBuilder setMapToolbarEnabled(Boolean isMapToolBar);

    /*
    * 罗盘显示
    * */
    MapBuilder setCompassEnabled(Boolean isCompassEnabled);

    /*
    * 地图类型
    *
    * Normal：典型道路地图。 显示道路、人类建造的一些特征以及河流等重要的自然特征。 此外，还会显示道路 和景观标签。
    * Hybrid：添加了道路地图的卫星照片数据。 此外，还会显示道路 和景观标签。
    * Satellite：卫星照片数据。 不显示道路和景观标签。
    * Terrain：地形数据。 地图包含颜色、轮廓线和标签以及 透视阴影。 此外，还会显示一些道路和标签。
    * None：无图块。 地图将渲染为空网格，不加载任何图块。（）
    * */
    MapBuilder setMapType(int mapType);

    /*
    * 动画摄像移动
    * */
    MapBuilder setMoveAnimateCamera(Boolean isMoveAnimateCamera);

    /*
    * 缩放等级
    * */
    MapBuilder setZoomLevel(float level);

    /*
    * 缩放控件
    * */
    MapBuilder setZoomControlsEnabled(Boolean isZonmControls);

    /*
    * 是否连续定位:true:不断的定位，将镜头始终以我的位置为中心，false：初始化时移动到我的位置，可以随意移动镜头
    * */
    MapBuilder setContinuousLocation(Boolean isContinuousLocation);

    /*
    * 移动时监听
    * */
    MapBuilder setMoveListener(MoveMapEvent listener);
}
