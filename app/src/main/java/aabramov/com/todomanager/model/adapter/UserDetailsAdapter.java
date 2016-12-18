package aabramov.com.todomanager.model.adapter;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.service.UserService;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailsViewHolder> {

    public static final String TAG = UserDetailsAdapter.class.getName();

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private List<UserDetails> userDetailsList = new ArrayList<>(0);

    @Override
    public UserDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_details, parent, false);
        return new UserDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserDetailsViewHolder holder, int position) {
        UserDetails item = userDetailsList.get(position);
        holder.tvUsername.setText(item.getUsername());
        holder.tvUserId.setText(item.getId());
    }

    @Override
    public int getItemCount() {
        return userDetailsList.size();
    }

    public UserDetails getAtPosition(int position) {
        return userDetailsList.get(position);
    }

    public void fetchUserDetails() {
        userService.getAllUsers().enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> call, Response<List<UserDetails>> response) {
                setUserDetails(response.body());
            }

            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed to fetch users", t);
            }
        });
    }

    public void setUserDetails(List<UserDetails> users) {
        userDetailsList.clear();
        userDetailsList.addAll(users);
        notifyDataSetChanged();
    }

    static class UserDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUsername)
        TextView tvUsername;

        @BindView(R.id.tvUserId)
        TextView tvUserId;

        UserDetailsViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }


}
