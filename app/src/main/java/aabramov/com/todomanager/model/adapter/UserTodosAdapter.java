package aabramov.com.todomanager.model.adapter;

import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.service.UserService;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class UserTodosAdapter extends RecyclerView.Adapter<UserTodosAdapter.UserTodosViewHolder> {

    public static final String TAG = UserTodosAdapter.class.getName();

    private UserService userService = TodoApplication.getApplication().getService(UserService.class);
    private User currentUser;

    public UserTodosAdapter(User currentUser) {
        this.currentUser = currentUser;
        fetchUser();
    }

    @Override
    public UserTodosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new UserTodosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserTodosViewHolder holder, int position) {
        Todo item = currentUser.getTodos().get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvBody.setText(item.getBody());
    }

    @Override
    public int getItemCount() {
        return currentUser.getTodos().size();
    }

    public void fetchUser() {
        userService.getUserTodos(currentUser.getId()).enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                currentUser.setTodos(response.body());
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed to fetch user todos", t);
            }
        });
    }

    protected static class UserTodosViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvBody;

        public UserTodosViewHolder(View container) {
            super(container);
            tvTitle = (TextView) container.findViewById(android.R.id.text1);
            tvBody = (TextView) container.findViewById(android.R.id.text2);
        }
    }


}
