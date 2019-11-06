package com.example.superWiserTVV2.halfway;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;
import android.widget.Toast;
import com.example.superWiserTVV2.EmailSent;
import com.example.superWiserTVV2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class InvalidPassword extends Activity {

    private static final String TAG = InvalidPassword.class.getSimpleName();

    /* Action ID definition */
    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;
    static String email;
    static FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        email = getIntent().getStringExtra("email");
        if (null == savedInstanceState) {
            GuidedStepFragment.add(getFragmentManager(), new FirstStepFragment());
        }
    }

    private static void addAction(List actions, long id, String title, String desc) {
        actions.add(new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .build());
    }

    public static class FirstStepFragment extends GuidedStepFragment {
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = "Invalid ID or password";
            String breadcrumb = "";
            String description = "Forgot your password? Use 'Recover Password' and we will email you a link to create a new password";
            Drawable icon = getActivity().getDrawable(R.drawable.incorrect);

            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState) {
            addAction(actions, ACTION_BACK, "Try Again", "Retype e-mail and password");
            addAction(actions, ACTION_CONTINUE, "Recover Password", "Sent password recovery email");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {

            switch ((int) action.getId()) {
                case ACTION_CONTINUE:
                    FragmentManager fm = getFragmentManager();
                    GuidedStepFragment.add(fm, new SecondStepFragment());
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

    public static class SecondStepFragment extends GuidedStepFragment {
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = "Recovery email will be sent to ";
            String breadcrumb = "";
            String description = email;
            Drawable icon = getActivity().getDrawable(R.drawable.email2);

            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState) {
            addAction(actions, ACTION_BACK, "Cancel", "Change something here");
            addAction(actions, ACTION_CONTINUE, "Send", "Send reset password link to email");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {

            switch ((int) action.getId()) {
                case ACTION_CONTINUE:

                    auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(getActivity(), EmailSent.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("myTag", e.toString());
                            Toast.makeText(getActivity(), "Error : Please retype your email", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    });
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