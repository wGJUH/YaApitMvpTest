package test.ya.translater.wgjuh.yaapitmvptest;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by wGJUH on 07.04.2017.
 */

public class LeakCanaryApp extends Application {
    RefWatcher refWatcher;
    @Override public void onCreate() {
        super.onCreate();
/*        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }*/
        refWatcher = LeakCanary.install(this);
        // Normal app init code...
    }

    public static RefWatcher getRefWatcher(Context context){
        LeakCanaryApp leakCanaryApp = (LeakCanaryApp)context.getApplicationContext();
        return leakCanaryApp.refWatcher;
    }
}
