
package test.ya.translater.wgjuh.yaapitmvptest.model.translate;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranslateDTO {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("text")
    @Expose
    private List<String> text = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        if(this.text==null)
            return null;
        return text.get(0);
    }

    public void setText(String text) {
        if(this.text==null)
            this.text = new ArrayList<>();
        this.text.add(text);
    }

}
