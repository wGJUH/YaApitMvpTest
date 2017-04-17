
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example implements Parcelable{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
    private List<EmbeddedTranslate> tr = null;

    protected Example(Parcel in) {
        text = in.readString();
        tr = in.createTypedArrayList(EmbeddedTranslate.CREATOR);
    }

    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EmbeddedTranslate getTr() {
        return tr.get(0);
    }

    public void setTr(List<EmbeddedTranslate> tr) {
        this.tr = tr;
    }

    @Override
    public String toString() {
        return text + " — " + getTr().getText();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeTypedList(tr);
    }
}
class EmbeddedTranslate implements Parcelable{

    @SerializedName("text")
    @Expose
    private String text;

    protected EmbeddedTranslate(Parcel in) {
        text = in.readString();
    }

    public static final Creator<EmbeddedTranslate> CREATOR = new Creator<EmbeddedTranslate>() {
        @Override
        public EmbeddedTranslate createFromParcel(Parcel in) {
            return new EmbeddedTranslate(in);
        }

        @Override
        public EmbeddedTranslate[] newArray(int size) {
            return new EmbeddedTranslate[size];
        }
    };

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }
}