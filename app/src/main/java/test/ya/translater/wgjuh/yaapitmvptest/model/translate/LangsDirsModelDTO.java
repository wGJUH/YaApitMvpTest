
package test.ya.translater.wgjuh.yaapitmvptest.model.translate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LangsDirsModelDTO {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private Map<String, String> langs = new LinkedHashMap<>();

    public List<String> getDirs() {
        return dirs;
    }

    public Map<String, String> getLangs() {
        return langs;
    }

    public void addLang(String code, String name){
        langs.put(code, name);

    }
}
