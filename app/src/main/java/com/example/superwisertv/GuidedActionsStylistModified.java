package com.example.superwisertv;

import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionsStylist;

public class GuidedActionsStylistModified extends GuidedActionsStylist {
    @Override
    public int onProvideItemLayoutId() {
        return R.layout.lb_guidedactions_item_modified;
    }
}
