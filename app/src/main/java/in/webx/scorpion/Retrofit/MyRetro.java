package in.webx.scorpion.Retrofit;

import static in.webx.scorpion.util.CommonMethod.appendLogs;
import static in.webx.scorpion.util.CommonMethod.appendLogss;

import android.content.Context;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetro {

    private static Retrofit retrofitApp;

    private static OkHttpClient.Builder httpClientApp = new OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS);

    private static Retrofit.Builder builderAuth = new Retrofit.Builder()
            .baseUrl(ConstantData.BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderApp = new Retrofit.Builder()
            .baseUrl(ConstantData.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createSecureServiceApp(Context context, Class<S> serviceClass) {
        httpClientApp.addInterceptor(logging);
        httpClientApp.addInterceptor(new AuthInterceptor(context));
        builderApp.client(httpClientApp.build());

        if (retrofitApp == null)
            retrofitApp = builderApp.build();
        return retrofitApp.create(serviceClass);
    }

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {

        if (message.contains("{\"") || message.contains("FAILED") || message.contains(ConstantData.BASE_URL) ||
                message.contains(ConstantData.BASE_AUTH_URL) || message.contains(ConstantData.CONSTANT_VELOCITY_LOGFILE) ||
                message.contains(ConstantData.CONSTANT_VELOCITY_TEST_INSERTELKLOG) ) {
            CommonMethod.i++;
            if (message.startsWith("-->")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    appendLogs("\n");
                }
                appendLogs("------------------------------------\n");
                appendLogs("Date " + CommonMethod.getCurrentDateTimeNew());
                message = message.replace("--> POST", "-- INPUT : \n");
            } else if (message.startsWith("<-- 200")) {
                message = "-- OUTPUT : ";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                message = message + "\n";
            }
            appendLogs(message);
            appendLogss(message,CommonMethod.i);

        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);
}