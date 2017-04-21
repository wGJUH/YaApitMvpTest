package test.ya.translater.wgjuh.yaapitmvptest.model.dict;

import java.util.ArrayList;
import java.util.List;

public class DefTranslateItem {
    private List<String> textAndSyn = new ArrayList<>();
    private List<String> means = new ArrayList<>();
    private List<String> examples = new ArrayList<>();

    public List<String> getTextAndSyn() {
        return textAndSyn;
    }

    public List<String> getMeans() {
        return means;
    }

    public List<String> getExamples() {
        return examples;
    }
}
