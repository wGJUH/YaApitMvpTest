
package test.ya.translater.wgjuh.yaapitmvptest.model.translate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LangsDirsModelPojo {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private Map<String, String> langs;

    public List<String> getDirs() {
        return dirs;
    }

    public Map<String, String> getLangs() {
        return langs;
    }
}
