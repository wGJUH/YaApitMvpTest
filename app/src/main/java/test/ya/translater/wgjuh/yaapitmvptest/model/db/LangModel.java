package test.ya.translater.wgjuh.yaapitmvptest.model.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wGJUH on 10.04.2017.
 */

public class LangModel {
    public List<String> code;
    public List<String> lang;
    public LangModel(int length) {
        this.code = new ArrayList<>(length);
        this.lang = new ArrayList<>(length);
    }
}
