package aabramov.com.todomanager;

import aabramov.com.todomanager.configuration.RetrofitConfiguration;
import aabramov.com.todomanager.persistence.db.TodoDatabase;
import aabramov.com.todomanager.service.UserService;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * @author Andrii Abramov on 11/25/16.
 */

public class TodoApplication extends Application {

    public static final String PREFERENCES_NAME = TodoApplication.class.getName() + "_preferences";

    private static TodoApplication application;

    private RetrofitConfiguration retrofitConfiguration;

    private static UserService userService;

    private TodoDatabase todoDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        retrofitConfiguration = new RetrofitConfiguration();
        todoDatabase = new TodoDatabase(getApplicationContext());

        userService = retrofitConfiguration.createService(UserService.class);

    }

    public static TodoApplication getApplication() {
        return application;
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofitConfiguration.createService(serviceClass);
    }

    public RetrofitConfiguration getRetrofitConfiguration() {
        return retrofitConfiguration;
    }

    public TodoDatabase getTodoDatabase() {
        return todoDatabase;
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }
}
