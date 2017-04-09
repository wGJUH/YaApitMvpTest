package test.ya.translater.wgjuh.yaapitmvptest.model;

import android.content.Context;

import org.json.JSONObject;

import retrofit2.Call;
import rx.Observable;

import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface IModel {

    void getLangsDirsForLanguage(String language);

    Observable<TranslatePojo> getTranslateForLanguage(String target, String language);

    Observable<DictDTO> getDicTionaryTranslateForLanguage(String target, String language);

    DictDTO getHistoryTranslate(String target, String langs);

    void updateLanguages();

    void saveToDBAndNotify(DictDTO dictDTO);

    String getCurrentLang();
}
