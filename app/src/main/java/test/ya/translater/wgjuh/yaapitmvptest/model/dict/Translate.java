
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Observable;

public class Translate implements Parcelable {

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

    private Translate(Parcel in) {
        text = in.readString();
        pos = in.readString();
        gen = in.readString();
        synonyme = in.createTypedArrayList(Synonyme.CREATOR);
        mean = in.createTypedArrayList(Mean.CREATOR);
        example = in.createTypedArrayList(Example.CREATOR);
    }
    public Translate(){

    }

    public static final Creator<Translate> CREATOR = new Creator<Translate>() {
        @Override
        public Translate createFromParcel(Parcel in) {
            return new Translate(in);
        }

        @Override
        public Translate[] newArray(int size) {
            return new Translate[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String getPos() {
        return pos;
    }

    private String getGen() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(pos);
        parcel.writeString(gen);
        parcel.writeTypedList(synonyme);
        parcel.writeTypedList(mean);
        parcel.writeTypedList(example);
    }
}
