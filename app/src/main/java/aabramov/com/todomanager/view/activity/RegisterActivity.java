package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserExistsDto;
import aabramov.com.todomanager.service.UserService;
import aabramov.com.todomanager.view.fragment.InformationDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindColor;
import butterknife.BindDrawable;
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

    @BindView(R.id.imgUsernameExists)
    ImageView imgUsernameExists;

    @BindColor(android.R.color.holo_red_dark)
    int redColor;

    @BindColor(android.R.color.holo_green_light)
    int greenColor;

    @BindDrawable(R.drawable.ic_done_black)
    Drawable icSuccess;

    @BindDrawable(R.drawable.ic_not_interested_black)
    Drawable icBanned;

    private final UserService userService = TodoApplication.getApplication().getService(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);


//        DrawableCompat.setTint(icBanned, redColor);
//        DrawableCompat.setTint(icSuccess, greenColor);

        initClickHandlers();
    }

    private void initClickHandlers() {
        btnCheckIfExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfUsernameIsBusy();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegistration();
            }
        });

    }

    private void checkIfUsernameIsBusy() {
        String username = parseUsername();
        userService.usernameExists(username).enqueue(new Callback<UserExistsDto>() {
            @Override
            public void onResponse(Call<UserExistsDto> call, Response<UserExistsDto> response) {
                highlightUsername(response.body());
            }

            @Override
            public void onFailure(Call<UserExistsDto> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void highlightUsername(UserExistsDto details) {

        if (details.getExists()) {
            etNewUsername.setTextColor(redColor);
            imgUsernameExists.setImageDrawable(icBanned);
            icBanned.mutate().setColorFilter(redColor, PorterDuff.Mode.DST_ATOP);
            imgUsernameExists.setVisibility(View.VISIBLE);
        } else {
            etNewUsername.setTextColor(greenColor);
            imgUsernameExists.setImageDrawable(icSuccess);
            icSuccess.mutate().setColorFilter(greenColor, PorterDuff.Mode.DST_ATOP);
            imgUsernameExists.setVisibility(View.VISIBLE);
        }

    }

    private void performRegistration() {
        String username = parseUsername();
        if (usernameIsValid(username)) {
            validateUsername(username);
        } else {
            InformationDialog.show("Error", "No username provided", getSupportFragmentManager());
        }
    }

    private String parseUsername() {
        return etNewUsername.getText().toString();
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
