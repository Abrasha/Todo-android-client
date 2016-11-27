package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.adapter.UserDetailsAdapter;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.component.RecyclerItemClickListener;
import aabramov.com.todomanager.view.fragment.AddServerDialog;
import aabramov.com.todomanager.view.fragment.SelectServerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static aabramov.com.todomanager.configuration.PreferenceKeys.KEY_USER_ID;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class AuthorizeActivity extends AppCompatActivity {

    public static final String TAG = AuthorizeActivity.class.getName();

    private AutoCompleteTextView etUsername;
    private Button btnListAllUsers;
    private Button btnAuthorize;
    private RecyclerView lvUsers;
    private ProgressBar progressAuthorizing;

    private UserDetailsAdapter adapter;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        userService = TodoApplication.getApplication().getService(UserService.class);

        initViews();
    }

    private void initViews() {
        lvUsers = (RecyclerView) findViewById(R.id.lvUsers);
        lvUsers.setHasFixedSize(true);
        lvUsers.setLayoutManager(new LinearLayoutManager(this));
        lvUsers.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), lvUsers, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "onItemClick: clicked on " + position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d(TAG, "onLongItemClick: clicked on " + position);
                    }
                })
        );
        adapter = new UserDetailsAdapter();
        lvUsers.setAdapter(adapter);

        progressAuthorizing = (ProgressBar) findViewById(R.id.progressAuthorizing);

        etUsername = (AutoCompleteTextView) findViewById(R.id.etUsername);
        userService.getUsernames().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayAdapter<String> usernamesAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, response.body());
                etUsername.setAdapter(usernamesAdapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to fetch usernames.", LENGTH_SHORT).show();
            }
        });

        btnAuthorize = (Button) findViewById(R.id.btnAuthorize);
        btnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                progressAuthorizing.setVisibility(View.VISIBLE);
                userService.getUserByUsername(username).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        // TODO: 11/27/16 refactoring extract
                        String requestedId = response.body().getId();
                        TodoApplication.getApplication().setCurrentUserId(requestedId);
                        Intent todoActivity = new Intent(getApplicationContext(), TodoActivity.class);
                        todoActivity.putExtra(KEY_USER_ID, requestedId);
                        progressAuthorizing.setVisibility(View.GONE);
                        startActivity(todoActivity);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "No such user: " + username, LENGTH_SHORT).show();
                        progressAuthorizing.setVisibility(View.GONE);
                    }
                });
            }
        });

        btnListAllUsers = (Button) findViewById(R.id.btnListAllUsers);
        btnListAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.fetchUsernames();
            }
        });

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

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showAddServerDialog() {
        AddServerDialog.newInstance().show(getSupportFragmentManager(), AddServerDialog.class.getName());
    }

    private void showSelectServerDialog() {
        SelectServerDialog.newInstance().show(getSupportFragmentManager(), SelectServerDialog.class.getName());
    }

}
