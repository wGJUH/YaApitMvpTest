
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Synonyme implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("gen")
    @Expose
    private String gen;

    protected Synonyme(Parcel in) {
        text = in.readString();
        pos = in.readString();
        gen = in.readString();
    }

    public static final Creator<Synonyme> CREATOR = new Creator<Synonyme>() {
        @Override
        public Synonyme createFromParcel(Parcel in) {
            return new Synonyme(in);
        }

        @Override
        public Synonyme[] newArray(int size) {
            return new Synonyme[size];
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

    public String getGen() {
        return gen;
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
    }
}
