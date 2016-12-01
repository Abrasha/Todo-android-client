package aabramov.com.todomanager.service;

import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public interface UserService {

    @GET("/users")
    Call<List<UserDetails>> getAllUsers();

    @GET("/users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @GET("/usernames")
    Call<List<String>> getUsernames();

    @GET("/usernames/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

}
