package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import org.json.JSONObject;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 08.04.2017.
 */

public interface YandexDictionaryApiInterface {

    @GET("/api/v1/dicservice/getStoredLangs")
    Observable<LangsDirsModelPojo> getLangs(@Query("key") String apiKey);

    @GET("/api/v1/dicservice.json/lookup")
    Observable<DictDTO> translateForLanguage(@Query("key") String apiKey, @Query("lang") String lang, @Query("text") String text);

}
