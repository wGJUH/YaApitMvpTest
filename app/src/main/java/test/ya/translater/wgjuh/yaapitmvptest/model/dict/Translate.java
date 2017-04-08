
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import android.util.Log;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

public class Translate {

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
    @SerializedName("asp")
    @Expose
    private String asp;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public List<Synonyme> getSynonyme() {
        return synonyme;
    }

    public void setSynonyme(List<Synonyme> synonyme) {
        this.synonyme = synonyme;
    }

    public List<Mean> getMean() {
        return mean;
    }

    public void setMean(List<Mean> mean) {
        this.mean = mean;
    }

    public List<Example> getExample() {
        return example;
    }

    public void setExample(List<Example> example) {
        this.example = example;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    @Override
    public String toString() {
        return "translate: " +
                "\ntext: " + getText()+
                "\npos: " + getPos()+
                "\ngen: " + getGen() /*+
                "\nsynonyme " + getSynonyme().toString()+
                "\nmean " + getMean().toString()+
                "\nexample " + getExample().toString()*/;
    }
}
