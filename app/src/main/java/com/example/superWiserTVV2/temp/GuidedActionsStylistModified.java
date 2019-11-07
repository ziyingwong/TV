package com.example.superWiserTVV2.temp;

import android.support.v17.leanback.widget.GuidedActionsStylist;

import com.example.superWiserTVV2.R;

public class GuidedActionsStylistModified extends GuidedActionsStylist {
    @Override
    public int onProvideItemLayoutId() {
        return R.layout.lb_guidedactions_item_modified;
    }
}
