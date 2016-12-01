package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.service.TodoService;
import aabramov.com.todomanager.service.UserService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class GenerateRandomTodosActivity extends AppCompatActivity {

    private final String TAG = GenerateRandomTodosActivity.class.getName();

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private TodoService todoService = TodoApplication.getApplication().getService(TodoService.class);

    @BindView(R.id.progressGenerating)
    ProgressBar progressGenerating;

    @BindView(R.id.dropdownSelectUser)
    Spinner dropdownUsers;

    @BindView(R.id.etTodosCount)
    EditText etCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_random_todos);
        ButterKnife.bind(this);

        findViewById(R.id.btnGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performGenerateTodos();
            }
        });

        progressGenerating.setVisibility(View.VISIBLE);
        userService.getAllUsers().enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                UserAdapter userAdapter = new UserAdapter(response.body());
                dropdownUsers.setAdapter(userAdapter);
                progressGenerating.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                progressGenerating.setVisibility(View.GONE);
                Toast.makeText(GenerateRandomTodosActivity.this, "Error loading users", LENGTH_SHORT).show();
            }
        });


    }

    private void performGenerateTodos() {
        progressGenerating.setVisibility(View.VISIBLE);
        UserDetails selectedItem = (UserDetails) dropdownUsers.getSelectedItem();
        int count = Integer.parseInt(etCount.getText().toString());
        todoService.generateTodoForUser(selectedItem.getId(), count).enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                progressGenerating.setVisibility(View.GONE);
                Toast.makeText(GenerateRandomTodosActivity.this, "Error generating todos", LENGTH_SHORT).show();
            }
        });

    }

    private class UserAdapter extends ArrayAdapter<UserDetails> {

        UserAdapter(List<UserDetails> objects) {
            super(GenerateRandomTodosActivity.this, android.R.layout.simple_spinner_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View result = convertView;

            if (result == null) {
                result = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null, false);
            }

            TextView itemText = (TextView) result.findViewById(android.R.id.text1);
            UserDetails user = getItem(position);
            if (user != null) {
                itemText.setText(user.getUsername());
            } else {
                itemText.setText("null at position #" + position);
            }
            return result;
        }
    }


}
