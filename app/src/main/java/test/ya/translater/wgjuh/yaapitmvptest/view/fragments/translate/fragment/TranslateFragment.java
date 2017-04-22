package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ya.translater.wgjuh.yaapitmvptest.R;
import test.ya.translater.wgjuh.yaapitmvptest.model.EventBusImpl;
import test.ya.translater.wgjuh.yaapitmvptest.model.ModelImpl;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.TranslatePrsenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.Presenter;
import test.ya.translater.wgjuh.yaapitmvptest.presenter.impl.TranslatePresenterImpl;
import test.ya.translater.wgjuh.yaapitmvptest.view.adapters.DictionaryTranslateRecyclerViewAdapter;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.BaseFragment;
import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.translate.TranslateView;

public class TranslateFragment extends BaseFragment implements TranslateView {
    @BindView(R.id.textview_common_translate)
    TextView translate;
    @BindView(R.id.recycler_translate)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add_favorite)
    CheckBox btnFavorite;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.error_frame)
    RelativeLayout error_frame;
    @BindView(R.id.btn_retry)
    Button btn_retry;
    @BindView(R.id.error_text)
    TextView error_textView;
    @BindView(R.id.license_window)
    LinearLayout license;

    private DictionaryTranslateRecyclerViewAdapter viewAdapter;

    private TranslatePrsenter translatePresenterImpl;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        translatePresenterImpl = new TranslatePresenterImpl(ModelImpl.getInstance(), EventBusImpl.getInstance());
        translatePresenterImpl.onBindView(this);
        viewAdapter = new DictionaryTranslateRecyclerViewAdapter(translatePresenterImpl.getDictionarySate(), getActivity());
        translatePresenterImpl.restoreState();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(viewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        btnFavorite.setOnClickListener(btn -> {
            if (btnFavorite.isChecked()) {
                translatePresenterImpl.addFavorite();
            } else {
                translatePresenterImpl.deleteFavorite();
            }
        });
        btn_retry.setOnClickListener(btn_retry -> translatePresenterImpl.startRetry());
        translate.setOnClickListener(textView -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(((TextView) textView).getText(), ((TextView) textView).getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getResources().getText(R.string.copy_to_clipdoard), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate_recycler_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected Presenter getPresenter() {
        return translatePresenterImpl;
    }

    @Override
    public void showTranslate(String translate) {
        this.translate.setText(translate);
    }

    @Override
    public void showProgressBar(Boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.hide();
        }
    }

    @Override
    public void setBtnFavoriteSelected(Boolean selected) {
        btnFavorite.setChecked(selected);
    }

    @Override
    public void updateAdapterTale(int size) {
        viewAdapter.notifyItemInserted(size);
        viewAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    @Override
    public void clearAdapter(int oldSize) {
        viewAdapter.notifyDataSetChanged();
        viewAdapter.removeAllViews();
    }

    @Override
    public void hideError() {
        if (error_frame.getVisibility() == View.VISIBLE) {
            error_frame.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String error) {
        error_textView.setText(error);
        error_frame.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setBtnFavoriteEnabled(Boolean enabled) {
        this.translate.setEnabled(enabled);
        this.btnFavorite.setEnabled(enabled);
    }

    @Override
    public void showLicenseUnderCommonTranslate(Boolean show) {
        if (show) {
            license.setVisibility(View.VISIBLE);
            license.findViewById(R.id.textview_dictionary_license).setVisibility(View.GONE);
            ((TextView)license.findViewById(R.id.textview_translate_license)).setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            license.setVisibility(View.GONE);
        }

    }

}
