package test.ya.translater.wgjuh.yaapitmvptest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Расширяем EditText для возможности использовать кнопку Готово вместе с multiLine режимом ввода
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        if(outAttrs.imeOptions != EditorInfo.IME_ACTION_DONE){
            outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        }
        return connection;
    }
}
