package aabramov.com.todomanager.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.model.adapter.UserDetailsAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
}
