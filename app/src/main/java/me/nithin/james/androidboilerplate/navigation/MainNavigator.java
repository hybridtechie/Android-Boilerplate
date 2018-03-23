package me.nithin.james.androidboilerplate.navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import me.nithin.james.androidboilerplate.R;
import me.nithin.james.androidboilerplate.application.DefaultApplication;
import me.nithin.james.androidboilerplate.core.BaseFragment;
import me.nithin.james.androidboilerplate.core.DefaultActivity;

public class MainNavigator extends NavigationHandler implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String STACK_ID = "MainNavigatorFragmentStack";
    private static final int LIST_PANEL_WIDTH = 450;
    private static final int LIST_PANEL_ANIMATION_DURATION = 300; // 0.5 seconds
    private DefaultActivity mActivity;
    private DefaultApplication app;

    public MainNavigator(DefaultActivity activity) {

        super(activity, STACK_ID);
        mActivity = activity;
        NavigationView navigationView = mActivity.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        app = (DefaultApplication) mActivity.getApplicationContext();
        navigationView.setNavigationItemSelectedListener(this);

        if (hasFragments()) {
//   navigate(NavigationType.REFRESH, null, null);
        } else {
            //    navigate(NavigationType.PUSH, DisplayVariant.FULL, new HomeFragment());
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case 1:

                break;
        }

        DrawerLayout drawer = mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void navigateBack() {
        if (mStack.size() > 1) {
            navigate(NavigationType.POP, null, null);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
            alertDialogBuilder.setTitle("Leave App?").setMessage("Are you sure you want to leave the app?").setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.onFinish();
                }
            }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    @Override
    protected void replaceFragmentContent(NavigationType navType, BaseFragment insertFragment) {

        final DefaultApplication app = (DefaultApplication) mActivity.getApplicationContext();
        super.replaceFragmentContent(navType, insertFragment);
    }
}
