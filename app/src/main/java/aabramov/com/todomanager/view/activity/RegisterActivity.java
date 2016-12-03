package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserExistsDto;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.fragment.InformationDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getName();

    @BindView(R.id.etNewUsername)
    EditText etNewUsername;

    @BindView(R.id.btnCheckIfExists)
    Button btnCheckIfExists;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    private final UserService userService = TodoApplication.getApplication().getService(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        initClickHandlers();
    }

    private void initClickHandlers() {
        btnCheckIfExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegistration();
            }
        });

    }

    private void performRegistration() {
        String username = etNewUsername.getText().toString();
        if (usernameIsValid(username)) {
            validateUsername(username);
        } else {
            InformationDialog.show("Error", "No username provided", getSupportFragmentManager());
        }
    }

    private boolean usernameIsValid(CharSequence username) {
        return !TextUtils.isEmpty(username);
    }

    private void validateUsername(String username) {
        userService.usernameExists(username).enqueue(new Callback<UserExistsDto>() {
            @Override
            public void onResponse(Call<UserExistsDto> call, Response<UserExistsDto> response) {
                processExistsResponse(response.body());
            }

            @Override
            public void onFailure(Call<UserExistsDto> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void processExistsResponse(UserExistsDto details) {
        if (details.getExists()) {
            InformationDialog.show("Error", "User with such name already exists", getSupportFragmentManager());
        } else {
            registerUser(details.getUsername());
        }
    }

    private void registerUser(String username) {
        User userToAdd = new User();
        userToAdd.setUsername(username);

        userService.addUser(userToAdd).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                onUserAdded(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void onUserAdded(User user) {
        TodoActivity.start(this, user.getId());
    }

}
