package vn.vnpt.ansv.csrsandroid.scan;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Created by ANSV on 11/3/2017.
 */

public class ScannerCodePresenter {

    private ScannerCodeListener scannerCodeListener;

    @Inject
    ScannerCodePresenter() {

    }

    public void setViewListener(ScannerCodeListener scannerCodeListener) {
        this.scannerCodeListener = scannerCodeListener;
        scannerCodeListener.onData();
    }

    protected Fragment getItemViewPage(int position) {
        if (position == 0) {
            return RecyclerViewFragment.newInstance();
        } else {
            return null;
        }
    }
    protected int getCountViewPage() {
        return 1;
    }

    protected String getPageTitleViewPage(int position) {
        if (position == 0) {
            return "";
        } else {
            return "csrs";
        }
    }
}
