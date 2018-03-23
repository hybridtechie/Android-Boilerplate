package me.nithin.james.androidboilerplate.core;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

import me.nithin.james.androidboilerplate.navigation.NavigationHandler;

public abstract class BaseFragment extends Fragment {

    public View mRootView;
    public DefaultActivity mActivity;
    public boolean mInstanceResumed;
    public NavigationHandler.DisplayVariant mDisplayVariant;
    public boolean mRootViewInitialised;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mActivity = (DefaultActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final View rootView = getView();

        /*
        * Attach an event listener to the root view, so as to notify the fragment
        * when the view has finished measuring its layout.
        * This is useful when layouts need to be changed programmatically, but depend
        * on the given size of the root view for measurement.
        *
        * Example: a listView does not know that it covers the whole screen, until
        * it knows the height of the root view.
        */
        if (rootView != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                @SuppressLint("NewApi")
                @SuppressWarnings("deprecation")
                public void onGlobalLayout() {

                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    onRootViewLayout();
                }
            });
        }
    }

    /**
     * Called when the root view has finished determining its dimensions.
     */
    public void onRootViewLayout() {

        mRootViewInitialised = true;
    }
}
