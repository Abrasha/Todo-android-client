package aabramov.com.todomanager.persistence.data;

import aabramov.com.todomanager.model.ServerAddress;
import aabramov.com.todomanager.persistence.db.QueryTemplates;
import aabramov.com.todomanager.persistence.db.TodoDatabase;
import aabramov.com.todomanager.persistence.db.TodoDatabase.Tables.Servers;
import aabramov.com.todomanager.persistence.db.cursor.ServerAddressCursor;
import aabramov.com.todomanager.persistence.db.cv.ServerEntryContentValues;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrii Abramov on 11/26/16.
 */
public class ServersRepository {

    private static final String TAG = ServersRepository.class.getName();

    private static final String TABLE_NAME = Servers.NAME;

    private final TodoDatabase todoDatabase;


    public ServersRepository(TodoDatabase todoDatabase) {
        this.todoDatabase = todoDatabase;
    }

    public List<ServerAddress> findAll() {
        Cursor result = QueryTemplates.findAll(todoDatabase.getReadableDatabase(), TABLE_NAME);
        return parseCursor(result);
    }

    public long insert(ServerAddress serverAddress) {
        Log.d(TAG, "insert: inserting " + serverAddress);
        SQLiteDatabase writableDatabase = todoDatabase.getWritableDatabase();
        ContentValues data = ServerEntryContentValues.toContentValues(serverAddress);
        return QueryTemplates.insert(writableDatabase, TABLE_NAME, data);
    }

    @SuppressWarnings("unchecked")
    private List<ServerAddress> parseCursor(Cursor cursor) {
        List<ServerAddress> result = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ServerAddress added = new ServerAddressCursor(cursor).parseModel();
                    result.add(added);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        } else {
            return (ArrayList<ServerAddress>) Collections.EMPTY_LIST;
        }
    }

}
