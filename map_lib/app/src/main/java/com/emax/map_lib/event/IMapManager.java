package com.emax.map_lib.event;

import android.os.Bundle;

public interface IMapManager<T> {

    void onResume();

    void onPause();

    void onDestroy();
}
