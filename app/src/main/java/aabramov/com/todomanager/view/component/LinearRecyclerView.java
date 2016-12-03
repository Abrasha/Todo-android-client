package aabramov.com.todomanager.view.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Andrii Abramov on 12/1/16.
 */
public class LinearRecyclerView extends RecyclerView {

    public LinearRecyclerView(Context context) {
        this(context, null, 0);
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
    }

}
