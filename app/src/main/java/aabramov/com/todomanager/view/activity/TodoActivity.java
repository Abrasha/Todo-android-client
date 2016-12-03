package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.adapter.UserTodosAdapter;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.component.LinearRecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aabramov.com.todomanager.configuration.PreferenceKeys.KEY_USER_ID;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class TodoActivity extends AppCompatActivity {

    private static final String TAG = TodoActivity.class.getName();

    private static final int REQUEST_CODE_GENERATE_TODOS = 100;

    @BindView(R.id.lvTodos)
    LinearRecyclerView lvTodos;

    @BindView(R.id.action_toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_add_todo)
    FloatingActionButton fabAddTodo;

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private UserTodosAdapter userTodosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTodoDialog();
            }
        });

        String currentUserId = loadCurrentUser();

        loadTodos(currentUserId);
    }

    public static void start(Context context, String userId) {
        Intent todoActivity = new Intent(context, TodoActivity.class);
        todoActivity.putExtra(KEY_USER_ID, userId);
        context.startActivity(todoActivity);
    }

    private void showAddTodoDialog() {

    }

    private String loadCurrentUser() {
        Intent intent = getIntent();
        String currentUserId = intent.getStringExtra(KEY_USER_ID);

        if (currentUserId == null) {
            currentUserId = TodoApplication.getApplication().getCurrentUserId();
        }
        return currentUserId;
    }

    private void loadTodos(String currentUserId) {
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
