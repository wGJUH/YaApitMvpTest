package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.leakcanary.LeakTraceElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.LeakCanaryApp;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefRecyclerItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DefTranslateItem;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Mean;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.Synonyme;

import static test.ya.translater.wgjuh.yaapitmvptest.DATA.TAG;

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
            for (DefRecyclerItem defRecyclerItem : defRecyclerItems) {
                for (int i = 0; i < defRecyclerItem.getDefTranslateItems().size(); i++) {
                    View view = LayoutInflater.from(LeakCanaryApp.getAppContext()).inflate(R.layout.synonimes,null);
                    if(i == 0) {
                        ((TextView) view.findViewById(R.id.def_text)).setText(defRecyclerItem.getText());
                        ((TextView) view.findViewById(R.id.pos)).setText(defRecyclerItem.getPos());
                    }else {
                        view.findViewById(R.id.def_text).setVisibility(View.GONE);
                        view.findViewById(R.id.pos).setVisibility(View.GONE);
                    }
                        DefTranslateItem defTranslateItem = defRecyclerItem.getDefTranslateItems().get(i);
                        ((TextView)view.findViewById(R.id.row_number)).setText(Integer.toString(i+1));
                        ((TextView)view.findViewById(R.id.synonyms)).setText(TextUtils.join(", ",defTranslateItem.getTextAndSyn()));
                        Log.d(TAG,"synonyms adapter: " + TextUtils.join(", ",defTranslateItem.getTextAndSyn()));
                        if(defTranslateItem.getMeans().size() != 0) {

                            ((TextView) view.findViewById(R.id.means)).setText("(" + TextUtils.join(", ", defTranslateItem.getMeans()) + ")");
                        }else {
                            view.findViewById(R.id.means).setVisibility(View.GONE);
                        }
                    if(defTranslateItem.getExamples().size() != 0) {
                        ((TextView) view.findViewById(R.id.examples)).setText(TextUtils.join("\n", defTranslateItem.getExamples()));
                    }else {
                        view.findViewById(R.id.examples).setVisibility(View.GONE);
                    }
                        //view.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    holder.linearLayout.addView(view);
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
