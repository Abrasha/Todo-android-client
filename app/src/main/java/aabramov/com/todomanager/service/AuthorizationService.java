package aabramov.com.todomanager.service;

import aabramov.com.todomanager.model.User;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public interface AuthorizationService {

    @POST("/authorize")
    Call<User> authorize(@Header("username") String username, @Header("password") String password);

    @POST("/users")
    Call<User> register(@Header("username") String username, @Header("password") String password);
}
