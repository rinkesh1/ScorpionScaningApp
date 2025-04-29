package in.webx.scorpion.Retrofit;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.activity.SplashActivity.tryCount;

import android.content.Context;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import in.webx.scorpion.Model.AuthenticationInputModel;
import in.webx.scorpion.Model.TokenGet;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    public static void setTokenInSharedPrefrence(Context context, String token, int expiresIn)
    {
        SharedPreference.getInstance(context);
        SharedPreference.setStringValue(ConstantData.SP_KEY_TOKEN,token);
        long timeEpoch = CommonMethod.getCurrentTimeEpoch()+ expiresIn;
        SharedPreference.setLongValue(ConstantData.SP_KEY_TOKEN_EXPIRY,timeEpoch);
    }

    public static String getToken(Context context)
    {
        SharedPreference.getInstance(context);
        return SharedPreference.getStringValue( ConstantData.SP_KEY_TOKEN);
    }

    public static void refreshToken(Context context)
    {
        long expiry = SharedPreference.getLongValue( ConstantData.SP_KEY_TOKEN_EXPIRY);
        if(CommonMethod.getCurrentTimeEpoch() >= expiry)
        {
            if (CommonMethod.isNetworkConnected(context))
            {

                SharedPreference.getInstance(context);

                AuthenticationInputModel loginInputModel = new AuthenticationInputModel();
                loginInputModel.setUserName(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                loginInputModel.setPassword(SharedPreference.getStringValue(ConstantData.SP_KEY_PASSWORD));
                loginInputModel.setCompanyCode(String.valueOf(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE)));//new
                loginInputModel.setCompanyAlias(SharedPreference.getStringValue(ConstantData.SP_COMPANY_NAME));
                loginInputModel.setFCMToken(ConstantData.SP_KEY_FCMToken);
                loginInputModel.setAuthType(ConstantData.SP_KEY_AuthType);

                /*String strUserName = SharedPreference.getStringValue( ConstantData.SP_KEY_USERNAME);
                String strPassword = SharedPreference.getStringValue( ConstantData.SP_KEY_PASSWORD);
                String strCompanyCode = SharedPreference.getStringValue( ConstantData.SP_KEY_COMPANY_CODE);

                AuthenticationInputModel loginInputModel = new AuthenticationInputModel();
                loginInputModel.setUserName(strUserName);
                loginInputModel.setPassword(strPassword);
                loginInputModel.setCompanyCode(strCompanyCode);*/

                Call<TokenGet> requestlogin = MyRetro.createSecureServiceApp(context.getApplicationContext(), APIService.class)
                        .gettoken(loginInputModel);
                requestlogin.enqueue(new Callback<TokenGet>() {
                    @Override
                    public void onResponse(@NonNull Call<TokenGet> call, @NonNull Response<TokenGet> response) {
                        CommonMethod.cancelProgressDialog();
                        if(response.body() != null) {
                            tryCount = 0;
                            TokenGet model = response.body();
                            Log.d("TAG", new Gson().toJson(model));
                            String accesstoken = model.getToken();
                            setTokenInSharedPrefrence(context, accesstoken, model.getExpires_In());
                        }else {
                            Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TokenGet> call, @NonNull Throwable t) {
                        Log.e("TAG", String.valueOf(t));
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    }
                });
            }
        }
    }
}
