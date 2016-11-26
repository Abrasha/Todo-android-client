package aabramov.com.todomanager.persistence.db;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.util.IOUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrii Abramov on 11/26/16.
 */

public class TodoDatabase extends SQLiteOpenHelper {

    public static final String TAG = TodoDatabase.class.getName();

    private final Context context;

    private Map<Class<?>, ?> repositories = new HashMap<>(5);

    public TodoDatabase(Context context) {
        super(context, Metadata.DATABASE_NAME, null, Metadata.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String script = IOUtils.readAll(context.getResources().openRawResource(R.raw.create_db));
        sqLiteDatabase.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    private static final class Metadata {
        private static final String DATABASE_NAME = "todo_database";
        private static final int DATABASE_VERSION = 1;
    }

    public static final class Tables {
        public static class Servers {
            public static final String NAME = "servers";

            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_PROTOCOL = "protocol";
            public static final String COLUMN_HOSTNAME = "hostname";
            public static final String COLUMN_PORT = "port";
        }
    }

    public <T> T getRepository(Class<T> repositoryClass) {
        if (repositories.containsKey(repositoryClass)) {
            return (T) repositories.get(repositoryClass);
        } else {
            return insertRepository(repositoryClass);
        }
    }

    private <T> T insertRepository(Class<T> repositoryClass) {
        try {
            Constructor<T> constructor = repositoryClass.getDeclaredConstructor(TodoDatabase.class);
            return constructor.newInstance(this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Provided constructor with wrong argumnt list");
        }
    }

}
