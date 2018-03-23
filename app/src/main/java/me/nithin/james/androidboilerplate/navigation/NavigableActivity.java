package me.nithin.james.androidboilerplate.navigation;

import android.content.Intent;
import android.view.View;

import me.nithin.james.androidboilerplate.core.BaseFragment;
import me.nithin.james.androidboilerplate.core.DefaultActivity;

/**
 * Defines an Activity that uses a NavigationHandler to navigate between
 * different fragments which form part of its UI.
 * <p>
 * Facilitates the sharing of fragments amongst Activities that are reliant on the Activity's
 * NavigationHandler to navigate between screens, without locking them to a particular
 * Activity.
 * <p>
 * Created by HyBr!DT3cH!3 on 05/03/2018
 */
public abstract class NavigableActivity extends DefaultActivity {

    protected View mRootView;

    /**
     * Navigates to a new fragment or refreshes the existing based on passed values. It affects
     * the navigation stack used to keep track of the screens (fragments) progressed through.
     *
     * @param navType  Navigation options
     * @param fragment Fragment to navigate to, if applicable.
     */
    public void navigate(NavigationHandler.NavigationType navType, BaseFragment fragment) {
        getNavigationHandler().navigate(navType, NavigationHandler.DisplayVariant.FULL, fragment);
    }

    /**
     * Returns a NavigationHandler specific to the Activity using it.
     *
     * @return NavigationHandler
     */
    public abstract NavigationHandler getNavigationHandler();

    /**
     * Cancels the activity, navigates back to the MainDrawerActivity and displays the
     * appropriate Fragment for the request (as defined in MainDrawerActivity).
     *
     * @param mainDrawerRequest Fragment to display
     */
    public void cancelAndNavigateBackToSettings(int mainDrawerRequest) {
        getNavigationHandler().resetStack();
        getNavigationHandler().saveStack();
        Intent intent = new Intent();
        intent.putExtra("1", mainDrawerRequest);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * Clears the navigation call stack, before finishing the activity with a Cancel result.
     */
    @Override
    public void onCancel() {
        getNavigationHandler().resetStack();
        getNavigationHandler().saveStack();

        super.onCancel();
    }

    /**
     * Clears the navigation call stack, before finishing the activity with a OK result.
     */
    @Override
    public void onFinish() {
        getNavigationHandler().resetStack();
        getNavigationHandler().saveStack();

        super.onFinish();
    }

    /**
     * Saves the navigation call stack before destroying the activity and all of its fragments.
     */
    @Override
    public void onDestroy() {
        getNavigationHandler().saveStack();

        super.onDestroy();
    }

    /**
     * Hides the root view.
     */
    public void hideRootView() {
        mRootView.setVisibility(View.GONE);
    }

    /**
     * Shows the root view.
     */
    public void showRootView() {
        mRootView.setVisibility(View.VISIBLE);
    }
}

