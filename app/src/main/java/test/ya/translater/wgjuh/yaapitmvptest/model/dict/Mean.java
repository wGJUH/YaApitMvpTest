
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mean implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;

    protected Mean(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Mean> CREATOR = new Creator<Mean>() {
        @Override
        public Mean createFromParcel(Parcel in) {
            return new Mean(in);
        }

        @Override
        public Mean[] newArray(int size) {
            return new Mean[size];
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
