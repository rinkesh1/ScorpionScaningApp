package in.webx.scorpion.Retrofit;

import static in.webx.scorpion.activity.SplashActivity.tryCount;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context _context;


    public AuthInterceptor(Context context) {
        _context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        long expiry = SharedPreference.getLongValue( ConstantData.SP_KEY_TOKEN_EXPIRY);
        if (CommonMethod.getCurrentTimeEpoch() <= expiry) {
            AuthHelper.refreshToken(_context);
        }
        return AddHeaders(chain);
    }

    private Response AddHeaders(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + AuthHelper.getToken(_context)).build();

        Response response = chain.proceed(request);
        while (!response.isSuccessful() && tryCount < 3 && response.code() == 401) {
            AuthHelper.refreshToken(_context);
            Log.d("intercept", "Request is not successful - " + tryCount);
            tryCount++;
            response = chain.proceed(request);
        }
        // otherwise just pass the original response on
        return response;
    }
}
