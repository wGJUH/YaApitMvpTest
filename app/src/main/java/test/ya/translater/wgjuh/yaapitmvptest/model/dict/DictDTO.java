
package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DictDTO {

    @SerializedName("def")
    @Expose
    private List<Def> def = null;
    private String commonTranslate;
    private String langs;
    private String target;
    private String id;

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }

    public void setCommonTranslate(String commonTranslate) {
        this.commonTranslate = commonTranslate;
    }

    public String getCommonTranslate() {
        return commonTranslate;
    }

    @Override
    public String toString() {
        return getDef().get(0).toString();
    }

    public void setLangs(String langs) {
        this.langs = langs;
    }

    public String getLangs() {
        return langs;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
