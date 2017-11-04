package vn.vnpt.ansv.csrsandroid.scan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
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

//    @Bind(R.id.scanner_toolbar)
//    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_code);
        ButterKnife.bind(this);
        component().inject(this);
        if (presenter == null) {
            throw new IllegalStateException("Presenter has to be injected");
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


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowTitleEnabled(false);
        }

        changeStatusBarColor(getResourceColor(R.color.primary_color));
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null){
                barcodeResult = restoredBarcode;
                Log.i(TAG, restoredBarcode.rawValue);
            }
        }
        startScan();
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