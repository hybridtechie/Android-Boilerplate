package me.nithin.james.androidboilerplate.core;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.nithin.james.androidboilerplate.R;
import me.nithin.james.androidboilerplate.application.DefaultApplication;
import me.nithin.james.androidboilerplate.navigation.MainNavigator;
import me.nithin.james.androidboilerplate.navigation.NavigationHandler;

public class DefaultActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    protected OnActivityGotPermissionsMessage mPermissionsCallback;
    protected int mCurrentPermissionsCode;
    protected MainNavigator mNavigationHandler;
    private DefaultApplication app;

    /**
     * Set Permissions Callback Listener
     */
    public void registerPermissionsMessageCallback(int codeToHandle, OnActivityGotPermissionsMessage listener) {

        // Set the code that we're listening for
        mCurrentPermissionsCode = codeToHandle;

        // Set the listener that needs to handle the response for the code
        mPermissionsCallback = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (DefaultApplication) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handleFabButtonClick();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationHandler = new MainNavigator(this);
        Log.d("Data", "hey");
    }

    private void handleFabButtonClick() {

    }

    public void navigate(NavigationHandler.NavigationType navType, BaseFragment fragment) {

        mNavigationHandler.navigate(navType, NavigationHandler.DisplayVariant.FULL, fragment);
    }

    public void onCancel() {
    }

    public void onFinish() {
        finish();
    }

    public void openBrowser(final Uri uri) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        DefaultApplication app = (DefaultApplication) getApplicationContext();
        Resources res = getResources();

        builder.setMessage(res.getString(R.string.open_web_browser))
                .setTitle("") //alertTitle
                .setPositiveButton(res.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        try {
                            Intent browse = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(browse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mNavigationHandler.navigateBack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        app.setCurrentActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback handler for permissions request
     */
    public interface OnActivityGotPermissionsMessage {
        void onRequestApproved();

        void onRequestDenied();
    }
}
