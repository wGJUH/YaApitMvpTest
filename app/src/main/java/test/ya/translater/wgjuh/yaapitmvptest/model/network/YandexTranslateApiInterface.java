package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import retrofit2.http.POST;
import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslateDTO;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface YandexTranslateApiInterface {
    @POST("/api/v1.5/tr.json/getLangs")
    Observable<LangsDirsModelDTO> getLangs(@Query("key") String apiKey, @Query("ui") String lang);

    @GET("/api/v1.5/tr.json/translate")
    Observable<TranslateDTO> translateForLanguage(@Query("key") String apiKey, @Query("text") String text, @Query("lang") String lang);

}