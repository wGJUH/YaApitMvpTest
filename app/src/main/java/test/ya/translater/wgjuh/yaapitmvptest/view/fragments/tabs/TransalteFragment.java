package test.ya.translater.wgjuh.yaapitmvptest.view.fragments.tabs;

import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import test.ya.translater.wgjuh.yaapitmvptest.view.fragments.View;

/**
 * Created by wGJUH on 04.04.2017.
 */

public interface TransalteFragment extends View{
      FragmentManager getTranslateFragmentManager();
     FrameLayout getInputFrame();
     FrameLayout getTranslateFrame();
}
