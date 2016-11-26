package aabramov.com.todomanager.persistence.db.cursor;

import android.database.Cursor;

/**
 * Created by Andrii Abramov on 9/15/16.
 */
public abstract class CursorWrapper<T> {

    private final Cursor cursor;

    public CursorWrapper(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public abstract T parseModel();

}