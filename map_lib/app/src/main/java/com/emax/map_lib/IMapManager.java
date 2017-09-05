package com.emax.map_lib;

import android.os.Bundle;

public interface IMapManager<T> {

    void onResume();

    void onPause();

    void onDestroy();
}
