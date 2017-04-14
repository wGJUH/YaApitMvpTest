package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;

public class MyhistoryfavoriteitemRecyclerViewAdapter extends RecyclerView.Adapter<MyhistoryfavoriteitemRecyclerViewAdapter.ViewHolder> {

    private final List<DictDTO> mValues;

    public MyhistoryfavoriteitemRecyclerViewAdapter(List<DictDTO> items) {
        mValues = items;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_historyfavoriteitem, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues.size() != 0) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTarget());
            holder.mContentView.setText(mValues.get(position).getCommonTranslate());
            holder.checkBox.setChecked(!mValues.get(position).getFavorite().equals("-1"));
            holder.langs.setText(mValues.get(position).getLangs());
        }
/*
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
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
            checkBox = (CheckBox)view.findViewById(R.id.isfavorite);
            langs = (TextView)view.findViewById(R.id.textview_langs);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
