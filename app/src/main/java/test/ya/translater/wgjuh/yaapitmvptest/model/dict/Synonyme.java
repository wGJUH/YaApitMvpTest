
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Synonyme {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("gen")
    @Expose
    private String gen;

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

}
