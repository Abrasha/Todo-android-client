package aabramov.com.todomanager.persistence.db.cursor;

import aabramov.com.todomanager.model.ServerAddress;
import android.database.Cursor;

/**
 * @author Andrii Abramov on 9/15/16.
 */
public class ServerAddressCursor extends CursorWrapper<ServerAddress> {

    public ServerAddressCursor(Cursor dbCursor) {
        super(dbCursor);
    }

    @Override
    public ServerAddress parseModel() {
        return fillInModel(getCursor());
    }

    private ServerAddress fillInModel(Cursor cursor) {
        String protocol = cursor.getString(1);
        String hostname = cursor.getString(2);
        int port = cursor.getInt(3);
        return new ServerAddress(protocol, hostname, port);
    }
}