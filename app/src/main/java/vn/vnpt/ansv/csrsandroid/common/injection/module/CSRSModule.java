package vn.vnpt.ansv.csrsandroid.common.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vn.vnpt.ansv.csrsandroid.common.injection.qualifier.ForApplication;

/**
 * Created by ANSV on 11/3/2017.
 */
@Module
public class CSRSModule {

    private Context context;
    private String ip;
    private String port;

    public CSRSModule(Context context, String ip, String port) {
        this.context = context;
        this.ip = ip;
        this.port = port;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    @ForApplication
    String provideIp() {
        return ip;
    }

    @Provides
    @Singleton
    @ForApplication
    String providePort() {
        return port;
    }
}
