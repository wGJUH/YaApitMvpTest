package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class DefRecyclerItem {
    String text;
    String pos;
    String ts;
    List<DefTranslateItem> defTranslateItems = new ArrayList<>();

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

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public List<DefTranslateItem> getDefTranslateItems() {
        return defTranslateItems;
    }

    public void setDefTranslateItems(List<DefTranslateItem> defTranslateItems) {
        this.defTranslateItems = defTranslateItems;
    }
    private void SetExamplesForTranslate(Translate translate, DefTranslateItem defTranslateItem) {
        if(translate.getExample() != null && translate.getExample().size() != 0) {
            translate.getExampleObservable().subscribe(example -> {
                defTranslateItem.getExamples().add(example.toString());
            });
        }
        defTranslateItems.add(defTranslateItem);
    }
}
