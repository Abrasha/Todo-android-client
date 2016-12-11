package aabramov.com.todomanager.model.adapter;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.UserDetails;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class UserTodosAdapter extends RecyclerView.Adapter<UserTodosAdapter.UserTodosViewHolder> {

    public static final String TAG = UserTodosAdapter.class.getName();

    private TodoService todoService = TodoApplication.getApplication().getService(TodoService.class);
    private UserDetails currentUser;

    private List<Todo> userTodos = new ArrayList<>(0);

    public UserTodosAdapter(UserDetails currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public UserTodosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new UserTodosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserTodosViewHolder holder, int position) {
        Todo item = userTodos.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvBody.setText(item.getBody());
        holder.tvStatus.setText(item.getStatus().toString());
        holder.tvPriority.setText(item.getPriority().toString());
        holder.tvDate.setText(item.getWhen().toString());
    }

    @Override
    public int getItemCount() {
        return userTodos.size();
    }

    public Todo getAtPosition(int position) {
        return userTodos.get(position);
    }

    public void setAtPosition(int position, Todo item) {
        userTodos.set(position, item);
        notifyItemChanged(position);
    }

    public void setItems(List<Todo> items) {
        userTodos.clear();
        userTodos.addAll(items);
        notifyDataSetChanged();
    }

    public void fetchTodos() {
        todoService.getUserTodos(currentUser.getId()).enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                setItems(response.body());
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, "onFailure: failed to fetch user todos", t);
            }
        });
    }

    static class UserTodosViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvBody)
        TextView tvBody;

        @BindView(R.id.tvPriority)
        TextView tvPriority;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        @BindView(R.id.tvDate)
        TextView tvDate;

        UserTodosViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }


}
