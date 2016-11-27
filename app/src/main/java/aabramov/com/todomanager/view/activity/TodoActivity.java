package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.adapter.UserTodosAdapter;
import aabramov.com.todomanager.service.UserService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aabramov.com.todomanager.configuration.PreferenceKeys.KEY_USER_ID;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class TodoActivity extends AppCompatActivity {

    public static final String TAG = TodoActivity.class.getName();

    private RecyclerView lvTodos;

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private UserTodosAdapter userTodosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();
        String currentUserId = intent.getStringExtra(KEY_USER_ID);

        if (currentUserId == null) {
            currentUserId = TodoApplication.getApplication().getCurrentUserId();
        }

        Toast.makeText(getApplicationContext(), "Current user Id: " + currentUserId, LENGTH_SHORT).show();

        lvTodos = (RecyclerView) findViewById(R.id.lvTodos);
        lvTodos.setLayoutManager(new LinearLayoutManager(this));

        userService.getUser(currentUserId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userTodosAdapter = new UserTodosAdapter(response.body());
                lvTodos.setAdapter(userTodosAdapter);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: call", t);
                Toast.makeText(getApplicationContext(), "Failed to fetch user.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
