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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private static final int REQUEST_CODE_GENERATE_TODOS = 100;

    private RecyclerView lvTodos;
    private Toolbar toolbar;

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private UserTodosAdapter userTodosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);

        toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_add_random_todo:
                showGenerateTodosActivity();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    private void showGenerateTodosActivity() {
        Intent intent = new Intent(getApplicationContext(), GenerateRandomTodosActivity.class);
        startActivityForResult(intent, REQUEST_CODE_GENERATE_TODOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GENERATE_TODOS && resultCode == RESULT_OK) {
            userTodosAdapter.fetchUser();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
