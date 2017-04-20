package test.ya.translater.wgjuh.yaapitmvptest;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class LeakCanaryApp extends Application {
    RefWatcher refWatcher;
    // TODO: 10.04.2017 ОТсюда запрашиваем статичный контекст для модели
    private static Context context;
    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return LeakCanaryApp.context;
    }

    public static RefWatcher getRefWatcher(Context context){
        LeakCanaryApp leakCanaryApp = (LeakCanaryApp)context.getApplicationContext();
        return leakCanaryApp.refWatcher;
    }
}
