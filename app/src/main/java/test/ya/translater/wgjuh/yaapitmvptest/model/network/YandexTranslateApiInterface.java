package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface YandexTranslateApiInterface {
    @GET("/api/v1.5/tr/getLangs")
    Observable<LangsDirsModelPojo> getLangs(@Query("key") String apiKey, @Query("ui") String ui);

    @GET("/api/v1.5/tr.json/translate")
    Observable<TranslatePojo> translateForLanguage(@Query("key") String apiKey, @Query("text") String text, @Query("lang") String lang);

}