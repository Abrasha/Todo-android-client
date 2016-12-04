package aabramov.com.todomanager.model.adapter;

import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.service.TodoService;
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

import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class UserTodosAdapter extends RecyclerView.Adapter<UserTodosAdapter.UserTodosViewHolder> {

    public static final String TAG = UserTodosAdapter.class.getName();

    private TodoService todoService = TodoApplication.getApplication().getService(TodoService.class);
    private User currentUser;

    public UserTodosAdapter(User currentUser) {
        this.currentUser = currentUser;
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

    public void fetchTodos() {
        todoService.getUserTodos(currentUser.getId()).enqueue(new Callback<List<Todo>>() {
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

    static class UserTodosViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView tvTitle;

        @BindView(android.R.id.text2)
        TextView tvBody;

        UserTodosViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }


}
