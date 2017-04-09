package test.ya.translater.wgjuh.yaapitmvptest.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import test.ya.translater.wgjuh.yaapitmvptest.DATA;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSharedPreferences(DATA.APP_PREF,MODE_PRIVATE).edit().putString(DATA.LANG,"en-ru").apply();
        EventBus.getInstance().getEventBus().onNext(new Event<String>(Event.EventType.ON_LANGUAGE_CHANGED,"en-ru"));
    }
}
