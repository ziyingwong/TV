package com.example.superwisertv;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionsStylist;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;


public class GuidedStepActivitySetInterval extends Activity {

    private static final String TAG = GuidedStepActivitySetInterval.class.getSimpleName();

    /* Action ID definition */
    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            GuidedStepFragment.add(getFragmentManager(), new FirstStepFragment());
        }
    }

    private static void addAction(List actions, long id, String title, String desc, Drawable icon) {
        actions.add(new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .icon(icon)
                .build());
    }


    public static class FirstStepFragment extends GuidedStepFragment {
        @NonNull


        GuidedActionsStylistModified mActionsStylist = this.onCreateActionsStylist();

        private GuidedActionsStylistModified mButtonActionsStylist = this.onCreateButtonActionsStylist();

        @Override
        public GuidedActionsStylistModified onCreateActionsStylist() {
            return new GuidedActionsStylistModified();
        }

        @Override
        public GuidedActionsStylistModified onCreateButtonActionsStylist() {
            GuidedActionsStylistModified stylist = new GuidedActionsStylistModified();
            stylist.setAsButtonActions();
            return stylist;
        }
        @Override
        public GuidedActionsStylistModified getGuidedActionsStylist() {
            return this.mActionsStylist;
        }

        public GuidedActionsStylistModified getGuidedButtonActionsStylist() {
            return this.mButtonActionsStylist;
        }



            @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState) {
//            String title = "Set Time Interval";
            addAction(actions, ACTION_CONTINUE, "Try Again", "Retype ID and password", getActivity().getDrawable(R.drawable.up_arrow));
            addAction(actions, ACTION_BACK, "Recover Password", "Create new password", getActivity().getDrawable(R.drawable.down_arrow));
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {

            switch ((int) action.getId()){
                case ACTION_CONTINUE:
                    // FragmentManager fm = getFragmentManager();
                    // GuidedStepFragment.add(fm, new SecondStepFragment());
                    break;
                case ACTION_BACK:
                    getActivity().finish();
                    break;
                default:
                    Log.w(TAG, "Action is not defined");
                    break;
            }
        }
    }
}