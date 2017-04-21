package test.ya.translater.wgjuh.yaapitmvptest.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.Event;
import test.ya.translater.wgjuh.yaapitmvptest.model.translate.LangsDirsModelDTO;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.SettingsPresenterImpl;

public class LanguageSettingsRecyclerViewAdapter extends RecyclerView.Adapter<LanguageSettingsRecyclerViewAdapter.ViewHolder> {

    private final List<String> codes;
    private final List<String> names;
    private final SettingsPresenterImpl settingsPresenterImpl;
    private final Event.EventType eventType;

    public LanguageSettingsRecyclerViewAdapter(LangsDirsModelDTO langModel, SettingsPresenterImpl settingsPresenterImpl, Event.EventType eventType) {
        codes = new ArrayList<>(langModel.getLangs().keySet());
        names = new ArrayList<>(langModel.getLangs().values());
        this.settingsPresenterImpl = settingsPresenterImpl;
        this.eventType = eventType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_languagesettings_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mView.setOnClickListener(view1 -> {
                    settingsPresenterImpl.popBackStack();
                    settingsPresenterImpl.sentLanguageChangedEvent(eventType, viewHolder.code);
                }
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.code = codes.get(position);
        holder.mIdView.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout mView;
        final TextView mIdView;
        public String code;

        ViewHolder(View view) {
            super(view);
            mView = (LinearLayout) view.findViewById(R.id.item_container);
            mIdView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
