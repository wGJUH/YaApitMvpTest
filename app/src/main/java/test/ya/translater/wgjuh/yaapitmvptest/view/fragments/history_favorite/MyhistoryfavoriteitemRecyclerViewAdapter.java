package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.dict.DictDTO;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.HistoryFavoritesFragment.OnListFragmentInteractionListener;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.history_favorite.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
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
        public DictDTO mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
