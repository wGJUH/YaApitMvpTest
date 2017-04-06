package test.ya.translater.wgjuh.yaapitmvptest;

import android.support.v4.util.SimpleArrayMap;

import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;

/**
 * Created by wGJUH on 06.04.2017.
 */

public class PresenterCache {
    private static PresenterCache instance = null;

    private PresenterCache(){}

    private SimpleArrayMap<String,Presenter> presenters;

    public static PresenterCache getInstance(){
        if (instance == null) instance = new PresenterCache();
        return instance;
    }

    protected final <T extends Presenter> T getpresenter(String tag, PresenterFactory<T> presenterFactory){

        if (presenters == null){
            presenters = new SimpleArrayMap<>();
        }
        T t = null;

        try {
            t = (T) presenters.get(tag);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        if (t == null){
            t = presenterFactory.createPresenter();
            presenters.put(tag,t);
        }
        return t;
    }


}
