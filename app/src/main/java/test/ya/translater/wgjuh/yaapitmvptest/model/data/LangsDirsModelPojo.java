
package test.ya.translater.wgjuh.yaapitmvptest.model.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LangsDirsModelPojo {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private LangsPojo langs;

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public LangsPojo getLangs() {
        return langs;
    }

    public void setLangs(LangsPojo langs) {
        this.langs = langs;
    }

}
