package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.POST;
import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.TranslatePojo;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface YandexTranslateApiInterface {
    @POST("/api/v1.5/tr.json/getStoredLangs")
    Observable<LangsDirsModelPojo> getLangs(@Query("key") String apiKey, @Query("ui") String lang);

    @GET("/api/v1.5/tr.json/translate")
    Observable<TranslatePojo> translateForLanguage(@Query("key") String apiKey, @Query("text") String text, @Query("lang") String lang);

}