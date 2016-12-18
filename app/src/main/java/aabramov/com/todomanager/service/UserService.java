package aabramov.com.todomanager.service;

import aabramov.com.todomanager.model.User;
import aabramov.com.todomanager.model.UserDetails;
import aabramov.com.todomanager.model.UserExistsDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/usernames/exists/{username}")
    Call<UserExistsDto> usernameExists(@Path("username") String username);

    @GET("/users/details/{userId}")
    Call<UserDetails> getUserDetails(@Path("userId") String userId);

    @POST("/users/generate")
    Call<List<UserDetails>> generateUsers(@Query("count") int count);
}
