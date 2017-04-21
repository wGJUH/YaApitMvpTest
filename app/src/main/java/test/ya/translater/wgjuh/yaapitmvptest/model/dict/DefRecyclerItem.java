package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.ArrayList;
import java.util.List;

public class DefRecyclerItem {
    private String text;
    private String pos;
    private List<DefTranslateItem> defTranslateItems = new ArrayList<>();

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

    public List<DefTranslateItem> getDefTranslateItems() {
        return defTranslateItems;
    }
}
