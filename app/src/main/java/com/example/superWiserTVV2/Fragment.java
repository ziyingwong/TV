//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.superWiserTVV2;

import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.R.color;
import android.support.v17.leanback.R.dimen;
import android.support.v17.leanback.app.BrandedFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/** @deprecated */
@Deprecated
public class Fragment extends BrandedFragment {
        private ViewGroup mErrorFrame;
        private ImageView mImageView;
        private TextView mTextView;
        private TextView mTextViewSmall;
        private Button mButton;
        private Drawable mDrawable;
        private CharSequence mMessage;
        private CharSequence mMessageSmall;
        private String mButtonText;
        private OnClickListener mButtonClickListener;
        private Drawable mBackgroundDrawable;
        private boolean mIsBackgroundTranslucent = true;

        public Fragment() {
        }

        public void setDefaultBackground(boolean translucent) {
                this.mBackgroundDrawable = null;
                this.mIsBackgroundTranslucent = translucent;
                this.updateBackground();
                this.updateMessage();
        }

        public boolean isBackgroundTranslucent() {
                return this.mIsBackgroundTranslucent;
        }

        public void setBackgroundDrawable(Drawable drawable) {
                this.mBackgroundDrawable = drawable;
                if (drawable != null) {
                        int opacity = drawable.getOpacity();
                        this.mIsBackgroundTranslucent = opacity == -3 || opacity == -2;
                }

                this.updateBackground();
                this.updateMessage();
        }

        public Drawable getBackgroundDrawable() {
                return this.mBackgroundDrawable;
        }

        public void setImageDrawable(Drawable drawable) {
                this.mDrawable = drawable;
                this.updateImageDrawable();
        }

        public Drawable getImageDrawable() {
                return this.mDrawable;
        }

        public void setMessage(CharSequence message) {
                this.mMessage = message;
                this.updateMessage();
        }

        public CharSequence getMessage() {
                return this.mMessage;
        }

        public void setMessageSmall(CharSequence messageSmall) {
                this.mMessageSmall = messageSmall;
                this.updateMessageSmall();
        }

        public CharSequence getMessageSmall() { return this.mMessageSmall; }

        public void setButtonText(String text) {
                this.mButtonText = text;
                this.updateButton();
        }

        public String getButtonText() {
                return this.mButtonText;
        }

        public void setButtonClickListener(OnClickListener clickListener) {
                this.mButtonClickListener = clickListener;
                this.updateButton();
        }

        public OnClickListener getButtonClickListener() {
                return this.mButtonClickListener;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View root = inflater.inflate(R.layout.error_fragment, container, false);
                this.mErrorFrame = (ViewGroup)root.findViewById(R.id.error_frame);
                this.updateBackground();
                this.installTitleView(inflater, this.mErrorFrame, savedInstanceState);
                this.mImageView = (ImageView)root.findViewById(R.id.image);
                this.updateImageDrawable();
                this.mTextView = (TextView)root.findViewById(R.id.message);
                this.updateMessage();
                this.mTextViewSmall = (TextView)root.findViewById(R.id.message_small);
                this.mButton = (Button)root.findViewById(R.id.button);
                this.updateButton();
                FontMetricsInt metrics = getFontMetricsInt(this.mTextView);
                int underImageBaselineMargin = container.getResources().getDimensionPixelSize(dimen.lb_error_under_image_baseline_margin);
                setTopMargin(this.mTextView, underImageBaselineMargin + metrics.ascent);
                int underMessageBaselineMargin = container.getResources().getDimensionPixelSize(dimen.lb_error_under_message_baseline_margin);
                setTopMargin(this.mButton, underMessageBaselineMargin - metrics.descent);
                return root;
        }

        private void updateBackground() {
                if (this.mErrorFrame != null) {
                        if (this.mBackgroundDrawable != null) {
                                this.mErrorFrame.setBackground(this.mBackgroundDrawable);
                        } else {
                                this.mErrorFrame.setBackgroundColor(this.mErrorFrame.getResources().getColor(this.mIsBackgroundTranslucent ? color.lb_error_background_color_translucent : color.lb_error_background_color_opaque));
                        }
                }

        }

        private void updateMessage() {
                if (this.mTextView != null) {
                        this.mTextView.setText(this.mMessage);
                        this.mTextView.setVisibility(TextUtils.isEmpty(this.mMessage) ? View.INVISIBLE : View.VISIBLE);
                }

        }

        private void updateMessageSmall() {
                if (this.mTextViewSmall != null) {
                        this.mTextViewSmall.setText(this.mMessageSmall);
                        this.mTextViewSmall.setVisibility(TextUtils.isEmpty(this.mMessageSmall) ? View.INVISIBLE : View.VISIBLE);
                }

        }

        private void updateImageDrawable() {
                if (this.mImageView != null) {
                        this.mImageView.setImageDrawable(this.mDrawable);
                        this.mImageView.setVisibility(this.mDrawable == null ? View.INVISIBLE : View.VISIBLE);

                }

        }

        private void updateButton() {
                if (this.mButton != null) {
                        this.mButton.setText(this.mButtonText);
                        this.mButton.setOnClickListener(this.mButtonClickListener);
                        this.mButton.setVisibility(TextUtils.isEmpty(this.mButtonText) ? View.INVISIBLE : View.VISIBLE);
                        this.mButton.requestFocus();
                }

        }

        public void onStart() {
                super.onStart();
                this.mErrorFrame.requestFocus();
        }

        private static FontMetricsInt getFontMetricsInt(TextView textView) {
                Paint paint = new Paint(1);
                paint.setTextSize(textView.getTextSize());
                paint.setTypeface(textView.getTypeface());
                return paint.getFontMetricsInt();
        }

        private static void setTopMargin(TextView textView, int topMargin) {
                MarginLayoutParams lp = (MarginLayoutParams)textView.getLayoutParams();
                lp.topMargin = topMargin;
                textView.setLayoutParams(lp);
        }


}
