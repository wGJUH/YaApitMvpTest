
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

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

    public List<EmbeddedTranslate> getTr() {
        return tr;
    }

    public void setTr(List<EmbeddedTranslate> tr) {
        this.tr = tr;
    }

}
class EmbeddedTranslate {

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