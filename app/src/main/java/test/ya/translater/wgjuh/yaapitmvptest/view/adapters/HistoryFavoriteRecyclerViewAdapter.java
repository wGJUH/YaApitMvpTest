package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.HistoryFavoritePresenter;

public class HistoryFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<HistoryFavoriteRecyclerViewAdapter.ViewHolder> {

    private final List<DictDTO> mValues;
    private final HistoryFavoritePresenter historyFavoritePresenter;
    public static final int TYPE_FOOTER = 7899;

    public HistoryFavoriteRecyclerViewAdapter(List<DictDTO> items, HistoryFavoritePresenter historyFavoritePresenter) {
        mValues = items;
        this.historyFavoritePresenter = historyFavoritePresenter;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder holder;
        if(viewType != TYPE_FOOTER) {
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_historyfavorite_recycler_item, parent, false);
             holder = new ViewHolder(view);
            holder.checkBox.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (!holder.checkBox.isChecked()) {
                        historyFavoritePresenter.deleteFavorite(mValues.get(position));
                    } else {
                        historyFavoritePresenter.addFavorite(mValues.get(position));
                    }
                }
            });
            holder.itemView.setOnLongClickListener(view1 -> {
                int position = holder.getAdapterPosition();
                historyFavoritePresenter
                        .onLongItemClick(mValues.get(position));
                return true;
            });
            holder.itemView.setOnClickListener(view1 -> {
                int position = holder.getAdapterPosition();
                historyFavoritePresenter
                        .showTranslate(mValues.get(position));
            });
        }else{
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.license, parent, false);
            view.findViewById(R.id.textview_dictionary_license).setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.textview_translate_license)).setMovementMethod(LinkMovementMethod.getInstance());
             holder = new ViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mValues.size()) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position < mValues.size()) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTarget());
            holder.mContentView.setText(mValues.get(position).getCommonTranslate());
            holder.checkBox.setChecked(!mValues.get(position).getFavorite().equals("-1"));
            holder.langs.setText(mValues.get(position).getLangs().toUpperCase(Locale.getDefault()));
        }
    }

    @Override
    public int getItemCount() {
        if(mValues.size() == 0){
            return 0;
        }else {
            return mValues.size() + 1;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final CheckBox checkBox;
        public final TextView langs;
        DictDTO mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            checkBox = (CheckBox) view.findViewById(R.id.isfavorite);
            langs = (TextView) view.findViewById(R.id.textview_langs);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
