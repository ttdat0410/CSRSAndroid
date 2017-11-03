package vn.vnpt.ansv.csrsandroid.common.injection.component;

import dagger.Component;
import vn.vnpt.ansv.csrsandroid.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.csrsandroid.main.MainActivity;
import vn.vnpt.ansv.csrsandroid.scan.ScannerCode;

/**
 * Created by ANSV on 11/3/2017.
 */

@ActivityScope
@Component(dependencies = CSRSComponent.class)
public interface ActivityComponent {
    void inject(MainActivity o);
    void inject(ScannerCode o);
}
