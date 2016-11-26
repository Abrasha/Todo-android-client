package aabramov.com.todomanager.configuration;

import aabramov.com.todomanager.model.ServerAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * @author Andrii Abramov on 11/26/16.
 */

public class RetrofitConfiguration {

    public static final String TAG = RetrofitConfiguration.class.getName();

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Retrofit retrofit;
    private Gson gson;
    private ServerAddress serverAddress;
    private HostInterceptor interceptor = new HostInterceptor();

    public RetrofitConfiguration(ServerAddress serverAddress) {

        this.serverAddress = serverAddress;

        gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();

        setUpRetrofit();
    }

    private void setUpRetrofit() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(serverAddress.getAsString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    private final class HostInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            HttpUrl newUrl = request.url().newBuilder()
                    .scheme(serverAddress.getProtocol())
                    .host(serverAddress.getHostname())
                    .port(serverAddress.getPort())
                    .build();

            request = request.newBuilder()
                    .url(newUrl)
                    .build();

            return chain.proceed(request);
        }

    }

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public ServerAddress getServerAddress() {
        return new ServerAddress(serverAddress.getProtocol(), serverAddress.getHostname(), serverAddress.getPort());
    }

    public void setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

}
