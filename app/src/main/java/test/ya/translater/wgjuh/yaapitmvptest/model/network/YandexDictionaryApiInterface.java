package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.LangsDirsModelPojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.data.TranslatePojo;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

/**
 * Created by wGJUH on 08.04.2017.
 */

public interface YandexDictionaryApiInterface {

/*    @GET("/api/v1.5/tr/getLangs")
    Observable<LangsDirsModelPojo> getLangs(@Query("key") String apiKey, @Query("ui") String ui);*/

    @GET("/api/v1/dicservice.json/lookup")
    Observable<DictDTO> translateForLanguage(@Query("key") String apiKey, @Query("lang") String lang, @Query("text") String text);


}
