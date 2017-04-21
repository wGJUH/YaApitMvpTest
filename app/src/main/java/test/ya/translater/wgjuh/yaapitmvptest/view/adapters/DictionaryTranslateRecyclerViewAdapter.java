package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.App;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;

public class DictionaryTranslateRecyclerViewAdapter extends RecyclerView.Adapter<DictionaryTranslateRecyclerViewAdapter.ViewHolder> {

    private final List<DefRecyclerItem> defRecyclerItems;
    private ViewHolder viewHolder;

    public DictionaryTranslateRecyclerViewAdapter(List<DefRecyclerItem> defRecyclerItems) {
        this.defRecyclerItems = defRecyclerItems;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_translate_recycler_item_container, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (defRecyclerItems.size() != 0) {
            for (DefRecyclerItem defRecyclerItem : defRecyclerItems) {
                for (int i = 0; i < defRecyclerItem.getDefTranslateItems().size(); i++) {
                    View view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.fragment_translate_recycler_item, null);
                    if (i == 0) {
                        ((TextView) view.findViewById(R.id.def_text)).setText(defRecyclerItem.getText());
                        ((TextView) view.findViewById(R.id.pos)).setText(defRecyclerItem.getPos());
                    } else {
                        view.findViewById(R.id.def_text).setVisibility(View.GONE);
                        view.findViewById(R.id.pos).setVisibility(View.GONE);
                    }
                    DefTranslateItem defTranslateItem = defRecyclerItem.getDefTranslateItems().get(i);
                    ((TextView) view.findViewById(R.id.row_number)).setText(String.format(Locale.getDefault(), "%d", i + 1));
                    ((TextView) view.findViewById(R.id.synonyms)).setText(TextUtils.join(", ", defTranslateItem.getTextAndSyn()));
                    if (defTranslateItem.getMeans().size() != 0) {

                        ((TextView) view.findViewById(R.id.means)).setText("(" + TextUtils.join(", ", defTranslateItem.getMeans()) + ")");
                    } else {
                        view.findViewById(R.id.means).setVisibility(View.GONE);
                    }
                    if (defTranslateItem.getExamples().size() != 0) {
                        ((TextView) view.findViewById(R.id.examples)).setText(TextUtils.join("\n", defTranslateItem.getExamples()));
                    } else {
                        view.findViewById(R.id.examples).setVisibility(View.GONE);
                    }
                    holder.linearLayout.addView(view);
                }

            }
        }
    }

    public void removeAllViews() {
        viewHolder.linearLayout.removeAllViews();
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView;
        }

    }
}
