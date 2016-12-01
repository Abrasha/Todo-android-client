package aabramov.com.todomanager.service;

import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author Andrii Abramov on 12/1/16.
 */
public interface TodoService {

    @POST("/users/{userId}/todos")
    Call<User> addUserTodo(@Path("userId") String userId, @Body Todo todo);

    @GET("/users/{userId}/todos")
    Call<List<Todo>> getUserTodos(@Path("userId") String userId);

    @POST("/users/{userId}/todos/generate")
    Call<List<Todo>> generateTodoForUser(@Path("userId") String userId, @Query("count") int count);

}
