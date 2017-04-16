
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import rx.Observable;

public class Def {

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
}
