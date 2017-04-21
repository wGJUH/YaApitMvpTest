package test.ya.translater.wgjuh.yaapitmvptest.model.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YandexTranslateApiModule {


    public static YandexTranslateApiInterface getYandexTranslateApiInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//Конвертер, необходимый для преобразования JSON'а в объекты
                .client(new OkHttpClient()).build();
        return retrofit.create(YandexTranslateApiInterface.class);
    }


}
