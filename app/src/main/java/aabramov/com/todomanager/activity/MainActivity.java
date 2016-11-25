package aabramov.com.todomanager.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Priority;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.service.UserService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class MainActivity extends AppCompatActivity {

    private UserService userService;

    private TextView usersResults;
    private TextView userResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        usersResults = (TextView) findViewById(R.id.requestResult1);
        userResult = (TextView) findViewById(R.id.requestResult2);


        userService = TodoApplication.getUserService();

        findViewById(R.id.btnGetUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userService.getAllUsers().enqueue(new Callback<List<UserDetails>>() {
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
