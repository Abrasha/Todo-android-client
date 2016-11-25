package aabramov.com.todomanager;

import aabramov.com.todomanager.service.UserService;
import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Andrii Abramov on 11/25/16.
 */

public class TodoApplication extends Application {

    private static TodoApplication application;

    private static Retrofit retrofit;

    private static UserService userService;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.129.203.50:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userService = retrofit.create(UserService.class);

    }

    public static TodoApplication getApplication() {
        return application;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static UserService getUserService() {
        return userService;
    }
}
