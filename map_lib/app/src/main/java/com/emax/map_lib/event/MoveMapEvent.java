package com.emax.map_lib.event;

import com.emax.map_lib.bean.AddressData;

/**
 * Created by 90835 on 2017/9/18.
 */

public interface MoveMapEvent {

    void move();

    void stop(AddressData latLngData);

}
