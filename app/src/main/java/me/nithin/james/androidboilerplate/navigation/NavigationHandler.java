package me.nithin.james.androidboilerplate.navigation;

import android.support.v4.app.FragmentTransaction;

import java.util.Stack;

import me.nithin.james.androidboilerplate.R;
import me.nithin.james.androidboilerplate.application.DefaultApplication;
import me.nithin.james.androidboilerplate.core.BaseFragment;
import me.nithin.james.androidboilerplate.core.DefaultActivity;

public abstract class NavigationHandler {

    public Stack<BaseFragment> mStack;
    protected DefaultActivity mActivity;
    protected String mStackId;

    public NavigationHandler(DefaultActivity a, String stackId) {

        mActivity = a;
        mStackId = stackId;
        restoreStack();
    }

    public void restoreStack() {

        try {
            final DefaultApplication app = (DefaultApplication) mActivity.getApplicationContext();
            Stack<BaseFragment> data = (Stack<BaseFragment>) app.restoreData(mStackId);
            if (data == null)
                throw new NullPointerException();
            mStack = data;
        } catch (Exception e) {
            // No analytics logging required here.
            // If fails or empty, create a new stack.
            resetStack();
        }
    }

    public void resetStack() {

        mStack = new Stack<>();
    }

    public abstract void navigateBack();

    public void saveStack() {

        final DefaultApplication app = (DefaultApplication) mActivity.getApplicationContext();
        app.saveData(mStackId, mStack);
    }

    /**
     * Navigates to a new fragment or refreshes the existing based on passed values. It affects
     * the navigation stack used to keep track of the screens (fragments) progressed through.
     * <p>
     * Navigation outcomes, based on passed navType and variant.
     * ---------------------------------------------------------
     *
     * @param navType        - navigation options
     * @param variant        - type of display, function only cares if it is DETAILS or something else.
     * @param insertFragment - Fragment to navigate to, if applicable.
     * @todo finish documenting the method, need to understand more about DisplayVariant.DETAILS
     * <p>
     * REFRESH - reload the current fragment
     * REPLACE - replaces the currently displayed fragment with the new one
     * PUSH - Adds the given fragment to the top of the navigation stack and displays it
     * PUSH_DETAILS - ?
     * POP - Removes the current fragment from the stack and navigates back to the previous
     * fragment.
     * POP_AND_PUSH - ?
     * POP_TO_START - navigates to fragment at the bottom of the stack, removing all other elements
     * POP_TO_START_AND_PUSH - removes all fragments from the stack except for the bottom one, adds
     * the new one to the stack and navigates to it.
     */
    public void navigate(NavigationType navType, DisplayVariant variant, BaseFragment insertFragment) {

        final DefaultApplication app = (DefaultApplication) mActivity.getApplicationContext();

        if (insertFragment != null) {

            insertFragment.mInstanceResumed = true;

            if (variant != null)
                insertFragment.mDisplayVariant = variant;
        }

        BaseFragment currentFragment = null;

        switch (navType) {

            case REFRESH:
                currentFragment = mStack.peek();
                break;

            case REPLACE:
                mStack.pop();
                mStack.push(insertFragment);
                currentFragment = insertFragment;
                break;

            case PUSH:
                mStack.push(insertFragment);
                currentFragment = insertFragment;
                break;

            case PUSH_DETAILS:
                currentFragment = mStack.peek();
                if (currentFragment.mDisplayVariant == DisplayVariant.DETAILS)
                    mStack.pop();

                mStack.push(insertFragment);
                currentFragment = insertFragment;
                break;

            case POP:
                mStack.pop();
                if (mStack.peek().mDisplayVariant == DisplayVariant.DETAILS)
                    mStack.pop();
                currentFragment = mStack.peek();
                break;

            case POP_AND_PUSH:
                mStack.pop();
                if (mStack.peek().mDisplayVariant == DisplayVariant.DETAILS)
                    mStack.pop();
                mStack.push(insertFragment);
                currentFragment = insertFragment;
                break;

            case POP_TO_START:
                while (mStack.size() > 1) {
                    mStack.pop();
                }
                currentFragment = mStack.peek();
                break;

            case POP_TO_START_AND_PUSH:
                while (mStack.size() > 1) {
                    mStack.pop();
                }
                mStack.push(insertFragment);
                currentFragment = insertFragment;
                break;
        }

        replaceFragmentContent(navType, currentFragment);
    }

    protected void replaceFragmentContent(NavigationType navType, BaseFragment insertFragment) {

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.port_content, insertFragment);
        ft.commit();
    }

    public boolean hasFragments() {

        return (mStack.size() > 0);
    }

    public boolean hasMultipleFragments() {

        return (mStack.size() > 1);
    }

    public BaseFragment getCurrentFragment() {

        return mStack.peek();
    }

    public BaseFragment getPreviousFragment() {

        if (mStack.size() > 1) {
            return mStack.get(mStack.size() - 2);
        } else {
            return null;
        }
    }

    public enum NavigationType {
        REFRESH,
        PUSH,
        PUSH_DETAILS,
        POP,
        POP_AND_PUSH,
        POP_TO_START,
        POP_TO_START_AND_PUSH,
        REPLACE
    }

    /**
     * Type of screen being naviagted to.
     * <p>
     * LIST -
     * DETAILS -
     * FULL -
     */
    public enum DisplayVariant {
        LIST,
        DETAILS,
        FULL
    }
}
