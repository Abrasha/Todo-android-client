package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.adapter.UserTodosAdapter;
import aabramov.com.todomanager.service.TodoService;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.component.LinearRecyclerView;
import aabramov.com.todomanager.view.component.OnLeftSwipeCallback;
import aabramov.com.todomanager.view.component.OnRightSwipeCallback;
import aabramov.com.todomanager.view.fragment.AddTodoDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private TodoService todoService = TodoApplication.getApplication().getService(TodoService.class);
    private UserTodosAdapter userTodosAdapter;
    private String currentUserId;


    public static void start(Context context, String userId) {
        Intent todoActivity = new Intent(context, TodoActivity.class);
        todoActivity.putExtra(KEY_USER_ID, userId);
        context.startActivity(todoActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initRecyclerView();

        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTodoDialog();
            }
        });

        loadCurrentUser();
        fetchUser();
    }

    private void initRecyclerView() {
        ItemTouchHelper.SimpleCallback removeOnRightSwipeCallback = new OnRightSwipeCallback(this) {
            @Override
            public void onRightSwipe(int position) {
                Log.d(TAG, "onRightSwipe: right swipe on element " + position);
//                openConfirmToRemoveDialog(position);
            }
        };
        ItemTouchHelper.SimpleCallback updateOnLeftSwipeCallback = new OnLeftSwipeCallback(this) {
            @Override
            public void onLeftSwipe(int position) {
                Log.d(TAG, "onLeftSwipe: left swipe on element " + position);
//                updateStreetEntry(position);
            }
        };
        new ItemTouchHelper(removeOnRightSwipeCallback).attachToRecyclerView(lvTodos);
        new ItemTouchHelper(updateOnLeftSwipeCallback).attachToRecyclerView(lvTodos);
    }

    private void showAddTodoDialog() {
        AddTodoDialog.newInstance(new AddTodoDialog.OnDismissListener() {
            @Override
            public void onDismiss(Todo added) {
                addUserTodo(added);
            }
        }).show(getSupportFragmentManager(), AddTodoDialog.class.getSimpleName());
    }

    private void addUserTodo(Todo added) {
        todoService.addUserTodo(currentUserId, added).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                fetchTodos();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private String loadCurrentUser() {
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(KEY_USER_ID);

        if (currentUserId == null) {
            currentUserId = TodoApplication.getApplication().getCurrentUserId();
        }
        return currentUserId;
    }

    private void fetchUser() {
        userService.getUser(currentUserId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userTodosAdapter = new UserTodosAdapter(response.body());
                lvTodos.setAdapter(userTodosAdapter);
                fetchTodos();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: call", t);
                Toast.makeText(getApplicationContext(), "Failed to fetch user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTodos() {
        userTodosAdapter.fetchTodos();
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
            userTodosAdapter.fetchTodos();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
