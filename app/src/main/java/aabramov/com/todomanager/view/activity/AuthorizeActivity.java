package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.model.adapter.UserDetailsAdapter;
import aabramov.com.todomanager.view.fragment.AddServerDialog;
import aabramov.com.todomanager.view.fragment.SelectServerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class AuthorizeActivity extends AppCompatActivity {

    public static final String TAG = AuthorizeActivity.class.getName();

    private EditText etUsername;
    private Button btnAuthorize;
    private Button btnListAllUsers;
    private RecyclerView lvUsers;

    private UserDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

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

        etUsername = (EditText) findViewById(R.id.etUsername);
        btnAuthorize = (Button) findViewById(R.id.btnAuthorize);

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
        SelectServerDialog.newInstance().show(getSupportFragmentManager(), AddServerDialog.class.getName());
    }
}
