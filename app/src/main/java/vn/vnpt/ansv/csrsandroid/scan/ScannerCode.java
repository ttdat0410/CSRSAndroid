package vn.vnpt.ansv.csrsandroid.scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import vn.vnpt.ansv.csrsandroid.common.utils.ParsableAsType;

/**
 * Created by ANSV on 11/3/2017.
 */

public class ScannerCode extends CSRSActivity implements ScannerCodeListener {

    private static final String TAG = ScannerCode.class.getSimpleName();
    public static final String BARCODE_KEY = "BARCODE";

    private Barcode barcodeResult;

    @Inject
    ScannerCodePresenter scannerCodePresenter;

    @Bind(R.id.materialViewPager)
    MaterialViewPager materialViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_code);
        setTitle("");
        ButterKnife.bind(this);
        component().inject(this);
        if (scannerCodePresenter == null) {
            throw new IllegalStateException("Presenter has to be injected");
        }
        final Toolbar toolbar = materialViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        RecyclerViewFragment.newInstance();
        changeStatusBarColor(getResourceColor(R.color.primary_color));
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null){
                barcodeResult = restoredBarcode;
                Log.i(TAG, restoredBarcode.rawValue);
                // TODO found barcode, send request to server to get staff infomation (1)

            }
        }
        startScan();
        //
        materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return scannerCodePresenter.getItemViewPage(position);
            }

            @Override
            public int getCount() {
                return scannerCodePresenter.getCountViewPage();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return scannerCodePresenter.getPageTitleViewPage(position);
            }
        });

        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndUrl(
                        R.color.green,
                        "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
            }
        });

        materialViewPager.getViewPager().setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerCodePresenter.setViewListener(this);
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
                        // TODO found barcode, send request to server to get staff infomation (2) if success
                        ParsableAsType.isParsableAsLong(barcode.rawValue, new ParsableAsType.ParsableCallback() {
                            @Override
                            public void callback(boolean isMatches, String ex) {
                                if (isMatches) {
                                    Log.i(TAG, "MATCHES");
                                } else {
                                    Log.i(TAG, "DONT MATCHES");
                                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                    alertDialog.setTitle("Thông báo");
                                    alertDialog.setMessage("Mã không hợp lệ.");
                                    alertDialog.setButton("Xác nhận", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        });
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
        Log.i(TAG, "received data from presenter");
        ParsableAsType.isParsableAsLong("68L", new ParsableAsType.ParsableCallback() {
            @Override
            public void callback(boolean isMatches, String ex) {
                if (isMatches) {
                    // MATCH....
                } else {
                    Log.i(TAG, ex);
                    // error with ex
                }
            }
        });

    }
}