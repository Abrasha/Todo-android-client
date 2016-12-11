package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Status;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.model.adapter.UserTodosAdapter;
import aabramov.com.todomanager.service.TodoService;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.component.LinearRecyclerView;
import aabramov.com.todomanager.view.component.OnRightSwipeCallback;
import aabramov.com.todomanager.view.fragment.AddTodoDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.List;

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

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

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
        ItemTouchHelper.SimpleCallback markAsDoneCallback = new OnRightSwipeCallback(this) {
            @Override
            public void onRightSwipe(int position) {
                Log.d(TAG, "onRightSwipe: right swipe on element " + position);
                setDoneStatus(position);
            }
        };
        new ItemTouchHelper(markAsDoneCallback).attachToRecyclerView(lvTodos);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                fetchTodos();
            }
        });


//        ItemTouchHelper.SimpleCallback removeCallback = new OnLeftSwipeCallback(this) {
//            @Override
//            public void onLeftSwipe(int position) {
//                Log.d(TAG, "onLeftSwipe: left swipe on element " + position);
//                deleteItem(position);
//            }
//        };
//        FIXME: 12/11/16 correct url -> wrong response
//        new ItemTouchHelper(removeCallback).attachToRecyclerView(lvTodos);
    }

    private void setDoneStatus(final int position) {
        Todo toDelete = userTodosAdapter.getAtPosition(position);
        toDelete.setStatus(Status.DONE);

        todoService.updateTodoForUser(currentUserId, toDelete.getId(), toDelete).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                userTodosAdapter.notifyItemChanged(position);
                userTodosAdapter.fetchTodos();
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void deleteItem(final int position) {
        Todo toDelete = userTodosAdapter.getAtPosition(position);

        todoService.deleteTodoForUser(currentUserId, toDelete.getId()).enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                userTodosAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

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
                userTodosAdapter.setItems(response.body().getTodos());
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
        userService.getUserDetails(currentUserId).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                userTodosAdapter = new UserTodosAdapter(response.body());
                lvTodos.setAdapter(userTodosAdapter);
                fetchTodos();
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e(TAG, "onFailure: call", t);
                Toast.makeText(getApplicationContext(), "Failed to fetch user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTodos() {
        userTodosAdapter.fetchTodos();
        refreshLayout.setRefreshing(false);
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
