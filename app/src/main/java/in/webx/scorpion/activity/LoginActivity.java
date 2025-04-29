package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;
import static in.webx.scorpion.Retrofit.AuthHelper.setTokenInSharedPrefrence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static in.webx.scorpion.activity.SplashActivity.tryCount;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Objects;

import in.webx.scorpion.Model.AuthenticationInputModel;
import in.webx.scorpion.Model.TokenGet;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.LsHader_Tbl;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.RealmDatabase.docketDetailList_Tbl;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    String companyCode, appVersion;
    TextInputEditText username, password;
    CheckBox rememberMe;
    ImageView companyLogo;
    Button btn_Login;

    ImageView progressBar;
    RelativeLayout rl_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_login);

        //Define Function
        init();
        setphoto();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));
    }

    //Init Function
    private void init() {

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            CommonMethod.appendLogs(e2.getMessage());
        }

        assert info != null;
        appVersion = info.versionName;

        Log.d("TAG", " appVersion : " + appVersion);

        username = findViewById(R.id.et_userid);
        password = findViewById(R.id.et_password);
        rememberMe = findViewById(R.id.ll_remember_me);
        companyLogo = findViewById(R.id.ll_company_logo);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(view -> login());

        SharedPreference.getInstance(this);

        rememberMe.setChecked(SharedPreference.getBooleanValue(ConstantData.SP_REMEMBER));
    }

    //set Photo
    private void setphoto() {
        String companyImage = SharedPreference.getStringValue(ConstantData.SP_COMPANY_IMAGE);

        byte[] bytes = Base64.decode(companyImage, Base64.DEFAULT);

        // Initialize bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        // set bitmap on imageView
        companyLogo.setImageBitmap(bitmap);

        companyCode = String.valueOf(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));

        if (SharedPreference.getBooleanValue(ConstantData.SP_REMEMBER)) {
            username.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            password.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_PASSWORD));
        }
    }

    //Onclick Login
    public void login() {
        progressBar = findViewById(R.id.progressbar);
        rl_progressBar = findViewById(R.id.rl_progressbar);

        Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

        if (check()) {
            if (CommonMethod.isNetworkConnected(this)) {

                tryCount = 2;

                btn_Login.setClickable(false);
                rl_progressBar.setVisibility(View.VISIBLE);

                AuthenticationInputModel loginInputModel = new AuthenticationInputModel();
                loginInputModel.setUserName(Objects.requireNonNull(username.getText()).toString().trim());
                loginInputModel.setPassword(Objects.requireNonNull(password.getText()).toString().trim());
                loginInputModel.setCompanyCode(String.valueOf(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE)));//new
                loginInputModel.setCompanyAlias(SharedPreference.getStringValue(ConstantData.SP_COMPANY_NAME));
                loginInputModel.setFCMToken(ConstantData.SP_KEY_FCMToken);
                loginInputModel.setAuthType(ConstantData.SP_KEY_AuthType);
                loginInputModel.setAppVersion(CommonMethod.getAppVersionName(this));

                Log.d("TAG", "login Req: "+new Gson().toJson(loginInputModel));
                Call<TokenGet> requestlogin = MyRetro.createSecureServiceApp(this, APIService.class)
                        .gettoken(loginInputModel);
                requestlogin.enqueue(new Callback<TokenGet>() {
                    @Override
                    public void onResponse(@NonNull Call<TokenGet> call, @NonNull Response<TokenGet> response) {

                        CommonMethod.cancelProgressDialog();
                        TokenGet model = response.body();
                        tryCount = 0;

                        if (model != null) {
                            String accesstoken = model.getToken();
                            setTokenInSharedPrefrence(getApplicationContext(), accesstoken, model.getExpires_In());

                            SharedPreference.setStringValue(ConstantData.SP_KEY_USERNAME, username.getText().toString().trim());
                            SharedPreference.setStringValue(ConstantData.SP_KEY_PASSWORD, password.getText().toString().trim());
                            SharedPreference.setStringValue(ConstantData.SP_KEY_BIKER_NAME, model.getUserName());
                            SharedPreference.setStringValue(ConstantData.SP_KEY_BRANCH_CODE, model.getBranch()); //new
                            SharedPreference.setBooleanValue(ConstantData.SP_KEY_ISPARTIALSCAN, model.isIspartialScan()); //new

                            if (rememberMe.isChecked()) {
                                SharedPreference.setBooleanValue(ConstantData.SP_REMEMBER, true);
                            } else {
                                SharedPreference.setBooleanValue(ConstantData.SP_REMEMBER, false);
                            }

                            realm.executeTransaction(realm -> {
                                RealmResults<LsHader_Tbl> lsHaderTbls = realm.where(LsHader_Tbl.class)
                                        .notEqualTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .notEqualTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                                        .findAll();
                                if (lsHaderTbls != null)
                                    lsHaderTbls.deleteAllFromRealm();

                                RealmResults<docketDetailList_Tbl> docketDetailListTbls = realm.where(docketDetailList_Tbl.class)
                                        .notEqualTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .notEqualTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                                        .findAll();
                                if (docketDetailListTbls != null)
                                    docketDetailListTbls.deleteAllFromRealm();

                                RealmResults<THCHeader_Tbl> thcHeaderTbl = realm.where(THCHeader_Tbl.class)
                                        .notEqualTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)).findAll();
                                if (thcHeaderTbl != null)
                                    thcHeaderTbl.deleteAllFromRealm();

                                RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class)
                                        .notEqualTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)).findAll();
                                if (thcDocketListTbls != null)
                                    thcDocketListTbls.deleteAllFromRealm();

                                RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class)
                                        .notEqualTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .notEqualTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                                        .findAll();
                                if (docketBarCodeSeriesTbls != null)
                                    docketBarCodeSeriesTbls.deleteAllFromRealm();
                            });

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            rl_progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            rl_progressBar.setVisibility(View.GONE);
                            btn_Login.setClickable(true);
                            username.setError("enter valid userid");
                            password.setError("enter valid password");
                            Toast.makeText(LoginActivity.this, " Wrong Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TokenGet> call, @NonNull Throwable t) {
                        rl_progressBar.setVisibility(View.GONE);
                        CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                        btn_Login.setClickable(true);
                        Log.e("TAG", String.valueOf(t));
                    }
                });
            }
        }
    }

    //Check Function
    private boolean check() {
        if (Objects.requireNonNull(username.getText()).toString().trim().equals("")
                && username.getText().length() == 0
                && TextUtils.isEmpty(username.getText().toString().trim())) {
            username.setError("Enter Username..");
            return false;
        }

        if (Objects.requireNonNull(password.getText()).toString().trim().equals("")
                && password.getText().length() == 0
                && TextUtils.isEmpty(password.getText().toString().trim())) {
            password.setError("Enter Password..");
            return false;
        }
        return true;
    }

    //Onclick App_Info
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void appinfo(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_app_info);

        // Dialog Close
        LinearLayout NH_Close = dialog.findViewById(R.id.NH_Close);
        NH_Close.setOnClickListener(v -> dialog.dismiss());

        //App Version
        TextView tv_app_version = dialog.findViewById(R.id.tv_app_version);
        tv_app_version.setText("Version : " + appVersion);

        //Companycode
        TextView tv_registrationcode = dialog.findViewById(R.id.tv_registrationcode);

        tv_registrationcode.setText(SharedPreference.getStringValue(ConstantData.SP_COMPANY_NAME));

        //Company Disconnect
        TextView diconnect = dialog.findViewById(R.id.diconnect);
        diconnect.setOnClickListener(view1 -> {
            SharedPreference.setBooleanValue(ConstantData.SP_COMPANY_STATUS, false);
            Intent intent = new Intent(dialog.getContext(), RegisterActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}