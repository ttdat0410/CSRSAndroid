package vn.vnpt.ansv.csrsandroid.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import vn.vnpt.ansv.csrsandroid.R;
import vn.vnpt.ansv.csrsandroid.common.ui.CSRSActivity;
import vn.vnpt.ansv.csrsandroid.scan.ScannerCode;

public class MainActivity extends CSRSActivity {

    private static final int REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final int TIMER = 2000;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        component().inject(this);
        onRequestPermissionsResultWithLocation();
        initToolbar();
        tryToShowSplashImageView(TIMER);
    }

    /**
     * initToolbar
     *
     * Sets up the toolbar, adds margin to top of toolbar; this is needed for devices running
     * Lollipop or greater. If the device is running Kitkat or below, getStatusBarHeight will
     * return 0.
     *
     */
    private void initToolbar() {
        toolbar.setBackgroundColor(this.getResourceColor(R.color.transparent));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
    }

    private void tryToShowScannerActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), ScannerCode.class));
            }
        }, TIMER);
    }

    /**
     * tryToShowSplashImageView
     *
     * Hàm hiên thị splash khi mới mở app
     * @param timer số giây để bắt đầu ẩn splash và bắt đầu quét mã
     * */
    private void tryToShowSplashImageView(int timer) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), ScannerCode.class));
            }
        }, timer);
    }

    /**
     * onRequestPermissionsResultWithLocation
     *
     * Hàm yêu cầu cho phép truy cập vị trí
     * */
    private void onRequestPermissionsResultWithLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Coarse location access needed to detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_ACCESS_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
    }
}
