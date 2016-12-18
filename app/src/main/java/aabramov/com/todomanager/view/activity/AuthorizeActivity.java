package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.model.adapter.UserDetailsAdapter;
import aabramov.com.todomanager.service.AuthorizationService;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.component.InstantAutoCompleteView;
import aabramov.com.todomanager.view.component.LinearRecyclerView;
import aabramov.com.todomanager.view.component.RecyclerItemClickListener;
import aabramov.com.todomanager.view.fragment.AddServerDialog;
import aabramov.com.todomanager.view.fragment.GenerateUserDialog;
import aabramov.com.todomanager.view.fragment.SelectServerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class AuthorizeActivity extends AppCompatActivity {

    private static final String TAG = AuthorizeActivity.class.getName();

    @BindView(R.id.etUsername)
    InstantAutoCompleteView etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnListAllUsers)
    Button btnListAllUsers;

    @BindView(R.id.btnAuthorize)
    Button btnAuthorize;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @BindView(R.id.lvUsers)
    LinearRecyclerView lvUsers;

    @BindView(R.id.progressAuthorizing)
    ProgressBar progressAuthorizing;

    @BindView(R.id.action_toolbar)
    Toolbar toolbar;

    @BindView(R.id.refreshLayoutUsers)
    SwipeRefreshLayout refreshLayoutUsers;

    private UserDetailsAdapter userDetailsAdapter;

    private UserService userService;
    private AuthorizationService authorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);

        refreshLayoutUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutUsers.setRefreshing(true);
                fetchUsers();
            }
        });

        userService = TodoApplication.getApplication().getService(UserService.class);
        authorizationService = TodoApplication.getApplication().getService(AuthorizationService.class);

        initViews();
    }

    private void initViews() {

        setSupportActionBar(toolbar);

        initRecycleView();

        initOnClickListeners();

        loadUsernames();
    }

    private void initOnClickListeners() {
        etUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUsername.showDropDown();
            }
        });

        btnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuthorization();
            }
        });

        btnListAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUsers();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
    }

    private void fetchUsers() {
        userDetailsAdapter.fetchUserDetails();
        refreshLayoutUsers.setRefreshing(false);
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initRecycleView() {
        userDetailsAdapter = new UserDetailsAdapter();

        lvUsers.setHasFixedSize(false);
        lvUsers.addOnItemTouchListener(createOnClickListener());
        lvUsers.setAdapter(userDetailsAdapter);
    }

    @NonNull
    private RecyclerItemClickListener createOnClickListener() {
        return new RecyclerItemClickListener(getApplicationContext(), lvUsers, new UserAuthorizeClickListener());
    }

    private void performAuthorization() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        progressAuthorizing.setVisibility(View.VISIBLE);

        authorizationService.authorize(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressAuthorizing.setVisibility(GONE);
                if (response.code() == 500) {
                    Toast.makeText(AuthorizeActivity.this, "Wrong password or username", Toast.LENGTH_LONG).show();
                } else {
                    doLogin(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: authorization failed", t);
            }
        });


    }

    private void doLogin(User user) {
        String userId = user.getId();
        TodoApplication.getApplication().setCurrentUserId(userId);
        startTodoActivity(userId);
    }

    private void loadUsernames() {
        userService.getUsernames().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                etUsername.setAdapter(createUserDetailsAdapter(response.body()));
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch usernames.", LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private ArrayAdapter<String> createUserDetailsAdapter(List<String> usernames) {
        return new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, usernames);
    }

    private void startTodoActivity(String userId) {
        progressAuthorizing.setVisibility(GONE);
        TodoActivity.start(this, userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_add_server:
                showAddServerDialog();
                return true;

            case R.id.menu_item_select_server:
                showSelectServerDialog();
                return true;

            case R.id.menu_item_generate_users:
                showGenerateUsersDialog();

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showGenerateUsersDialog() {
        GenerateUserDialog.newInstance(new GenerateUserDialog.OnUsersGeneratedListener() {
            @Override
            public void onUsersGenerated(int count) {
                generateUsers(count);
            }
        }).show(getSupportFragmentManager(), GenerateUserDialog.class.getName());
    }

    private void generateUsers(int count) {
        userService.generateUsers(count).enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                userDetailsAdapter.setUserDetails(response.body());
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void showAddServerDialog() {
        AddServerDialog.newInstance().show(getSupportFragmentManager(), AddServerDialog.class.getName());
    }

    private void showSelectServerDialog() {
        SelectServerDialog.newInstance().show(getSupportFragmentManager(), SelectServerDialog.class.getName());
    }

    private class UserAuthorizeClickListener implements RecyclerItemClickListener.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            UserDetails userId = userDetailsAdapter.getAtPosition(position);
            startTodoActivity(userId.getId());
        }

        @Override
        public void onLongItemClick(View view, int position) {
            Log.d(TAG, "onLongItemClick: clicked on " + position);
        }

    }

}
