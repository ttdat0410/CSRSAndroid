package vn.vnpt.ansv.csrsandroid.common.app;

import android.app.Application;

import vn.vnpt.ansv.csrsandroid.BuildConfig;
import vn.vnpt.ansv.csrsandroid.common.injection.component.CSRSComponent;
import vn.vnpt.ansv.csrsandroid.common.injection.component.DaggerCSRSComponent;
import vn.vnpt.ansv.csrsandroid.common.injection.module.CSRSModule;

/**
 * Created by ANSV on 11/3/2017.
 */

public class CSRSApplication extends Application {

    private CSRSComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component().inject(this);
    }

    public CSRSComponent component() {
        if (component == null) {
            component = DaggerCSRSComponent
                    .builder()
                    .cSRSModule(new CSRSModule(this, BuildConfig.TCP_IP, BuildConfig.TCP_PORT))
                    .build();
        }
        return component;
    }
}
