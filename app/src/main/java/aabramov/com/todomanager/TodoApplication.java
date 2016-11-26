package aabramov.com.todomanager;

import aabramov.com.todomanager.configuration.RetrofitConfiguration;
import aabramov.com.todomanager.model.ServerAddress;
import aabramov.com.todomanager.service.UserService;
import android.app.Application;

/**
 * @author Andrii Abramov on 11/25/16.
 */

public class TodoApplication extends Application {

    private static TodoApplication application;

    private RetrofitConfiguration retrofitConfiguration;

    private static UserService userService;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        retrofitConfiguration = new RetrofitConfiguration(new ServerAddress("http", "192.168.0.104", 8080));

        userService = retrofitConfiguration.createService(UserService.class);

    }

    public static TodoApplication getApplication() {
        return application;
    }

    public static UserService getUserService() {
        return userService;
    }

    public RetrofitConfiguration getRetrofitConfiguration() {
        return retrofitConfiguration;
    }
}
