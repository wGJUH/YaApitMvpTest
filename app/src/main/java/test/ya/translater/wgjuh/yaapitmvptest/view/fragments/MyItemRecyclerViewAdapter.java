package test.ya.translater.wgjuh.yaapitmvptest.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBus;
import test.ya.translater.wgjuh.yaapitmvptest.model.db.LangModel;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<String> codes;
    private final List<String> names;
    private final OnLanguageChossen onLanguageChossen;

    public MyItemRecyclerViewAdapter(LangModel langModel, OnLanguageChossen onLanguageChossen) {
        codes = langModel.code;
        names = langModel.lang;
        this.onLanguageChossen = onLanguageChossen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.code = codes.get(position);
        holder.mIdView.setText(names.get(position));
        holder.mView.setOnClickListener(v -> {
            onLanguageChossen.onLanguageChoosen();
            EventBus.getInstance().getEventBus().onNext(EventBus.getInstance().createEvent(Event.EventType.ON_LANGUAGE_CHANGED,holder.code));
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mView;
        public final TextView mIdView;
        public String code;

        public ViewHolder(View view) {
            super(view);
            mView = (LinearLayout)view.findViewById(R.id.item_container);
            mIdView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
interface OnLanguageChossen{
    void onLanguageChoosen();
}
