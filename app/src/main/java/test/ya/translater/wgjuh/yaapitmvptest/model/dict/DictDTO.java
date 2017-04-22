
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DictDTO implements Parcelable {

    @SerializedName("def")
    @Expose
    private List<Def> def = null;
    private String commonTranslate;
    private String langs;
    private String target;
    private String id;
    private String favorite;

    protected DictDTO(Parcel in) {
        commonTranslate = in.readString();
        langs = in.readString();
        target = in.readString();
        id = in.readString();
        favorite = in.readString();
        def = in.createTypedArrayList(Def.CREATOR);
    }

    public DictDTO() {

    }

    public static final Creator<DictDTO> CREATOR = new Creator<DictDTO>() {
        @Override
        public DictDTO createFromParcel(Parcel in) {
            return new DictDTO(in);
        }

        @Override
        public DictDTO[] newArray(int size) {
            return new DictDTO[size];
        }
    };

    public Observable<Def> getDef() {
        if (def != null && def.size() != 0) {
            return Observable.from(def)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io());
        }
        return Observable.error(new Throwable("emptyDef"));
    }

    public void setCommonTranslate(String commonTranslate) {
        this.commonTranslate = commonTranslate;
    }

    public String getCommonTranslate() {
        return commonTranslate;
    }

    public void setDef(List<Def> def) {
        this.def = def;
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

    public DictDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public DictDTO setFavorite(String favorite) {
        this.favorite = favorite;
        return this;
    }

    public String getFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DictDTO
                && ((DictDTO) obj).target.equals(target)
                && ((DictDTO) obj).langs.equals(langs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(commonTranslate);
        parcel.writeString(langs);
        parcel.writeString(target);
        parcel.writeString(id);
        parcel.writeString(favorite);
        parcel.writeTypedList(def);
    }
}
