
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example implements Serializable{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
    private List<EmbeddedTranslate> tr = null;

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
}
class EmbeddedTranslate implements Serializable{

    @SerializedName("text")
    @Expose
    private String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}