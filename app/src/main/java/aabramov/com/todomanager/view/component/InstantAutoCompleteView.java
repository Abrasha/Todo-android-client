package aabramov.com.todomanager.view.component;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * @author Andrii Abramov on 11/27/16.
 */
public class InstantAutoCompleteView extends AutoCompleteTextView {

    public InstantAutoCompleteView(Context context) {
        super(context);
    }

    public InstantAutoCompleteView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoCompleteView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (focused && getAdapter() != null) {
//            performFiltering(getText(), 0);
//            showDropDown();
//        }
    }

}
