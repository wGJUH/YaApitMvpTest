
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.util.Log;
import android.view.MotionEvent;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

public class Translate implements Serializable {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("gen")
    @Expose
    private String gen;
    @SerializedName("syn")
    @Expose
    private List<Synonyme> synonyme = null;
    @SerializedName("mean")
    @Expose
    private List<Mean> mean = null;
    @SerializedName("ex")
    @Expose
    private List<Example> example = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public String getGen() {
        return gen;
    }

    public List<Synonyme> getSynonyme() {
        return synonyme;
    }

    public List<Mean> getMean() {
        return mean;
    }

    public Observable<Mean> getMeanObservable() {
        return Observable.from(mean);
    }
    public Observable<Synonyme> getSynonymeObservable() {
        return Observable.from(synonyme);
    }

    public Observable<Example> getExampleObservable() {
        return Observable.from(example);
    }
    public List<Example> getExample(){
        return example;
    }


    @Override
    public String toString() {
        return "translate: " +
                "\ntext: " + getText()+
                "\npos: " + getPos()+
                "\ngen: " + getGen() /*+
                "\nsynonyme " + getSynonyme().toString()+
                "\nmean " + getMean().toString()+
                "\nexample " + getExampleObservable().toString()*/;
    }
}
