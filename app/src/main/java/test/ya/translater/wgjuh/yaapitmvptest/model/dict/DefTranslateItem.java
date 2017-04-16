package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class DefTranslateItem {
    List<String> textAndSyn = new ArrayList<>();
    List<String> means = new ArrayList<>();
    List<String> examples = new ArrayList<>();

    public List<String> getTextAndSyn() {
        return textAndSyn;
    }

    public void setTextAndSyn(List<String> textAndSyn) {
        this.textAndSyn = textAndSyn;
    }

    public List<String> getMeans() {
        return means;
    }

    public void setMeans(List<String> means) {
        this.means = means;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }
}
