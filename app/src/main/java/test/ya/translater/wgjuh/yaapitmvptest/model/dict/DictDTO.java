
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// TODO: 16.04.2017 поменять на parcelable
public class DictDTO implements Serializable{

    @SerializedName("def")
    @Expose
    private List<Def> def = null;
    private String commonTranslate;
    private String langs;
    private String target;
    private String id;
    private String favorite;

    public Observable<Def> getDef() {
        if(def != null) {
            return Observable.from(def).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io());
        }return Observable.never();
    }

    public void setCommonTranslate(String commonTranslate) {
        this.commonTranslate = commonTranslate;
    }

    public String getCommonTranslate() {
        return commonTranslate;
    }

    public void setLangs(String langs) {
        this.langs = langs;
    }

    public String getLangs() {
        return langs;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object obj) {
        DictDTO dictDTO = (DictDTO)obj;
        if(dictDTO.target.equals(target) && dictDTO.langs.equals(langs)) {
            return true;
        }else {
            return false;
        }
    }
}
