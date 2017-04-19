package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.IHistoryFavoritePresenter;

public class HistoryFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<HistoryFavoriteRecyclerViewAdapter.ViewHolder> {

    private final List<DictDTO> mValues;
    private boolean isHistory;
    private IHistoryFavoritePresenter historyFavoritePresenter;

    public HistoryFavoriteRecyclerViewAdapter(List<DictDTO> items, boolean isHistory, IHistoryFavoritePresenter historyFavoritePresenter) {
        mValues = items;
        this.isHistory = isHistory;
        this.historyFavoritePresenter = historyFavoritePresenter;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_historyfavoriteitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
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
            return historyFavoritePresenter
                    .deleteItem(mValues.get(position)) > 0;
        });
        holder.itemView.setOnClickListener(view1 -> {
            int position = holder.getAdapterPosition();
            historyFavoritePresenter
                    .showTranslate(mValues.get(position));
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues.size() != position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTarget());
            holder.mContentView.setText(mValues.get(position).getCommonTranslate());
            holder.checkBox.setChecked(!mValues.get(position).getFavorite().equals("-1"));
            holder.langs.setText(mValues.get(position).getLangs().toUpperCase(Locale.getDefault()));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final CheckBox checkBox;
        public final TextView langs;
        public DictDTO mItem;

        public ViewHolder(View view) {
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
