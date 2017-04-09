package test.ya.translater.wgjuh.yaapitmvptest.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ModelImpl.getEventBus().onNext(new Event<String>(Event.EventType.ON_LANGUAGE_CHNAGED,"en-ru"));
    }
}
