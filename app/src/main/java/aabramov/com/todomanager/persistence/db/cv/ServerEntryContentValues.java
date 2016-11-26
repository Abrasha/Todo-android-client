package aabramov.com.todomanager.persistence.db.cv;

import aabramov.com.todomanager.model.ServerAddress;
import android.content.ContentValues;

import static aabramov.com.todomanager.persistence.db.TodoDatabase.Tables.Servers.*;

/**
 * @author Andrii Abramov on 9/15/16.
 */
public class ServerEntryContentValues {

    public static ContentValues toContentValues(ServerAddress item) {
        ContentValues result = new ContentValues();

        result.put(COLUMN_HOSTNAME, item.getHostname());
        result.put(COLUMN_PROTOCOL, item.getProtocol());
        result.put(COLUMN_PORT, item.getPort());

        return result;
    }

}