package vn.vnpt.ansv.csrsandroid.scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.vision.barcode.Barcode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.vnpt.ansv.csrsandroid.R;
import vn.vnpt.ansv.csrsandroid.common.ui.CSRSActivity;

/**
 * Created by ANSV on 11/3/2017.
 */

public class ScannerCode extends CSRSActivity implements ScannerCodeListener {

    private static final String TAG = ScannerCode.class.getSimpleName();
    public static final String BARCODE_KEY = "BARCODE";

    private Barcode barcodeResult;

    @Inject
    ScannerCodePresenter presenter;

    @Bind(R.id.materialViewPager)
    MaterialViewPager materialViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_code);
        setTitle("");
        ButterKnife.bind(this);
        component().inject(this);
        if (presenter == null) {
            throw new IllegalStateException("Presenter has to be injected");
        }
        final Toolbar toolbar = materialViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
//        setSupportActionBar(toolbar);
//        toolbar.setBackgroundColor(getResourceColor(R.color.sl_terbium_green));
//        toolbar.setTitle(getString(R.string.app_name));
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
//        params.height += getStatusBarHeight();
//
//        toolbar.setLayoutParams(params);
//        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);


//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            //actionBar.setDisplayShowTitleEnabled(false);
//        }

        RecyclerViewFragment.newInstance();
        changeStatusBarColor(getResourceColor(R.color.primary_color));
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null){
                barcodeResult = restoredBarcode;
                Log.i(TAG, restoredBarcode.rawValue);
            }
        }
        startScan();
        //
        materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    //case 0:
                    //    return RecyclerViewFragment.newInstance();
                    //case 1:
                    //    return RecyclerViewFragment.newInstance();
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        return RecyclerViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
//                switch (position % 4) {
//                    case 0:
//                        return "Selection";
//                    case 1:
//                        return "Actualités";
//                    case 2:
//                        return "Professionnel";
//                    case 3:
//                        return "Divertissement";
//                }
                return "";
            }
        });

        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        materialViewPager.getViewPager().setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());

        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void startScan() {

        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(ScannerCode.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        Log.i(TAG, barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    @Override
    public void onData() {

    }
}