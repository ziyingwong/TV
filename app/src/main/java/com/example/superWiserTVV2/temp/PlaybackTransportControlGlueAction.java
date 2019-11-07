package com.example.superWiserTVV2.temp;

import android.content.Context;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import android.support.v17.leanback.media.PlayerAdapter;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PlaybackControlsRow.SkipNextAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.SkipPreviousAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.PlayPauseAction;
import android.support.v17.leanback.widget.PlaybackControlsRow.MoreActions;
import android.widget.Button;


public class PlaybackTransportControlGlueAction<T extends PlayerAdapter> extends PlaybackTransportControlGlue {
    SkipPreviousAction mSkipPreviousAction;
    SkipNextAction mSkipNextAction;
    PlayPauseAction mPlayPauseAction;
    MoreActions mMoreActions;

    public PlaybackTransportControlGlueAction(Context context, PlayerAdapter impl) {
        super(context, impl);
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter primaryActionsAdapter) {
        Button button = new Button(getContext());
        button.setText("try");
        primaryActionsAdapter.add(button);
        primaryActionsAdapter.add(this.mMoreActions = new MoreActions((this.getContext())));
        primaryActionsAdapter.add(this.mSkipPreviousAction = new SkipPreviousAction(this.getContext()));
        primaryActionsAdapter.add(this.mPlayPauseAction = new PlayPauseAction(this.getContext()));
        primaryActionsAdapter.add(this.mSkipNextAction = new SkipNextAction(this.getContext()));
    }


}
