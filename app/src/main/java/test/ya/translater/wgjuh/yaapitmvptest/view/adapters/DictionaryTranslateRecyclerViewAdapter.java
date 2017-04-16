package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.leakcanary.LeakTraceElement;

import java.util.ArrayList;
import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Synonyme;

/**
 * Created by wGJUH on 16.04.2017.
 */

public class DictionaryTranslateRecyclerViewAdapter extends RecyclerView.Adapter<DictionaryTranslateRecyclerViewAdapter.ViewHolder> {

    List<DefRecyclerItem> defRecyclerItems;
    ViewHolder viewHolder;
    public DictionaryTranslateRecyclerViewAdapter(List<DefRecyclerItem> defRecyclerItems) {
        this.defRecyclerItems = defRecyclerItems;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dictionary_states, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(defRecyclerItems.size() != 0) {
            TextView textView;
            for (DefRecyclerItem defRecyclerItem :
                    defRecyclerItems) {


                for (DefTranslateItem defTranslateItem :
                        defRecyclerItem.getDefTranslateItems()) {

                        textView = new TextView(LeakCanaryApp.getAppContext());
                        textView.setTextColor(Color.BLACK);
                        StringBuilder stringBuilder = new StringBuilder();
                    for (String synonyme :
                            defTranslateItem.getTextAndSyn()) {
                        stringBuilder.append(synonyme+",");
                    }
                        textView.setText(stringBuilder.toString());
                        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        holder.linearLayout.addView(textView);

                }
            }
        }
    }

    public void removeAllViews(){
        viewHolder.linearLayout.removeAllViews();
    }
    @Override
    public int getItemCount() {
        return 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView;
        }

    }
}
