package vn.vnpt.ansv.csrsandroid.common.injection.component;

import dagger.Component;
import vn.vnpt.ansv.csrsandroid.common.app.CSRSApplication;
import vn.vnpt.ansv.csrsandroid.common.injection.module.CSRSModule;

/**
 * Created by ANSV on 11/3/2017.
 */

@Component(modules = {CSRSModule.class})
public interface CSRSComponent {
    void inject (CSRSApplication o);
}
