package aabramov.com.todomanager.view.activity;

import aabramov.com.todomanager.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import static aabramov.com.todomanager.configuration.PreferenceKeys.KEY_USER_ID;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class TodoActivity extends AppCompatActivity {

    public static final String TAG = TodoActivity.class.getName();

    private String currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();

        currentUserId = intent.getStringExtra(KEY_USER_ID);

    }
}
