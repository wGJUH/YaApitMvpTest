
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Observable;

public class Def implements Parcelable{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("ts")
    @Expose
    private String transcription;
    @SerializedName("tr")
    @Expose
    private List<Translate> translate = null;

    protected Def(Parcel in) {
        text = in.readString();
        pos = in.readString();
        transcription = in.readString();
        translate = in.createTypedArrayList(Translate.CREATOR);
    }

    public static final Creator<Def> CREATOR = new Creator<Def>() {
        @Override
        public Def createFromParcel(Parcel in) {
            return new Def(in);
        }

        @Override
        public Def[] newArray(int size) {
            return new Def[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public String getTranscription() {
        return transcription;
    }

    public Observable<Translate> getTranslateObservable() {
        return Observable.from(translate);
    }

    public int getTranslate(){
        return translate.size();
    }

    public void setTranslate(List<Translate> translate) {
        this.translate = translate;
    }

    @Override
    public String toString() {
        return "DEF: " +
                "\ntranscription: " + getTranscription() + " " + getTranslateObservable().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(pos);
        parcel.writeString(transcription);
        parcel.writeTypedList(translate);
    }
}
