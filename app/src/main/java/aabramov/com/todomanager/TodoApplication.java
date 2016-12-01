package aabramov.com.todomanager;

import aabramov.com.todomanager.configuration.RetrofitConfiguration;
import aabramov.com.todomanager.persistence.db.TodoDatabase;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import static aabramov.com.todomanager.configuration.PreferenceKeys.KEY_USER_ID;

/**
 * @author Andrii Abramov on 11/25/16.
 */

public class TodoApplication extends Application {

    private static final String TAG = TodoApplication.class.getName();

    private static final String PREFERENCES_NAME = TodoApplication.class.getName() + "_preferences";
    private static TodoApplication application;

    private RetrofitConfiguration retrofitConfiguration;
    private TodoDatabase todoDatabase;

    private String currentUserId;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: application init");

        application = this;

        retrofitConfiguration = new RetrofitConfiguration();
        todoDatabase = new TodoDatabase(getApplicationContext());
        currentUserId = getSharedPreferences().getString(KEY_USER_ID, null);
    }

    public static TodoApplication getApplication() {
        Log.d(TAG, "getApplication: requested " + TodoApplication.class.getSimpleName());
        return application;
    }

    public <T> T getService(Class<T> serviceClass) {
        Log.d(TAG, "getService: requested " + serviceClass);
        return retrofitConfiguration.createService(serviceClass);
    }

    public RetrofitConfiguration getRetrofitConfiguration() {
        Log.d(TAG, "getRetrofitConfiguration: requested " + RetrofitConfiguration.class.getSimpleName());
        return retrofitConfiguration;
    }

    public TodoDatabase getTodoDatabase() {
        Log.d(TAG, "getTodoDatabase: requested " + TodoDatabase.class.getSimpleName());
        return todoDatabase;
    }

    public SharedPreferences getSharedPreferences() {
        Log.d(TAG, "getSharedPreferences: requested " + SharedPreferences.class.getSimpleName());
        return getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

}
