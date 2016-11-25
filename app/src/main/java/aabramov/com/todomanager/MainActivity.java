package aabramov.com.todomanager;

import aabramov.com.todomanager.model.Priority;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.service.UserService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        final TextView usersResults = (TextView) findViewById(R.id.requestResult1);
        final TextView userResult = (TextView) findViewById(R.id.requestResult2);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.129.203.50:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final UserService userService = retrofit.create(UserService.class);

        findViewById(R.id.btnGetUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userService.listUsers().enqueue(new Callback<List<UserDetails>>() {
                    @Override
                    public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                        usersResults.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                        usersResults.setText(t.toString());
                    }
                });
            }
        });

        findViewById(R.id.btnGetUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = new Todo();
                todo.setBody("I WAS ADDED VIA REST");
                todo.setTitle("nice title");
                todo.setWhen(new Date());
                todo.setPriority(Priority.LOW);
                todo.setTags(Arrays.asList("qwe", "asd"));

                userService.addUserTodo("5837ce4f439e5129ca852361", todo).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        userResult.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        userResult.setText(t.toString());
                    }
                });
            }
        });

    }
}
