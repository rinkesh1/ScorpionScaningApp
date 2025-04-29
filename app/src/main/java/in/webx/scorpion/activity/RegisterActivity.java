package in.webx.scorpion.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.activity.SplashActivity.tryCount;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.File;
import java.util.Objects;

import in.webx.scorpion.Model.GetCompanyDetail;
import in.webx.scorpion.Model.RequestCompanyNameModule;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText registrationcode;

    ImageView progressBar;
    RelativeLayout rl_progressbar;

    Button btn_register;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_register);
        tryCount = 0;

        // Define Function
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.colorPrimaryDark_Transparent));
        }
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));*/

        rl_progressbar = findViewById(R.id.rl_progressbar);
        progressBar = findViewById(R.id.progressbar);

        File file = new File(Environment.getExternalStorageDirectory() + "/webXvelocity");

        file.mkdir();
    }

    //Init Function
    private void init() {

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(view -> register());
        registrationcode = findViewById(R.id.et_registrationcode);

        SharedPreference.getInstance(this);
    }

    //Onclick Register
    public void register() {
        Log.d("TAG", "Add firebase");
        btn_register.setClickable(false);
        String strregistrationcode = Objects.requireNonNull(registrationcode.getText()).toString().trim();
        if (!strregistrationcode.equals("")) {
            if (CommonMethod.isNetworkConnected(this)) {

                RequestCompanyNameModule requestCompanyNameModule = new RequestCompanyNameModule();
                requestCompanyNameModule.setCompanyAlias(strregistrationcode);
                Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

                rl_progressbar.setVisibility(View.VISIBLE);
                Log.d("TAG", "register Request: "+new Gson().toJson(requestCompanyNameModule));

                Call<GetCompanyDetail> getCompanyDetailCall = MyRetro.createSecureServiceApp(this, APIService.class)
                        .getcompanydetail(requestCompanyNameModule);
                getCompanyDetailCall.enqueue(new Callback<GetCompanyDetail>() {
                    @Override
                    public void onResponse(@NonNull Call<GetCompanyDetail> call, @NonNull Response<GetCompanyDetail> response) {
                        GetCompanyDetail model = response.body();
                        rl_progressbar.setVisibility(View.GONE);
                        if (model != null) {
                            Log.d("TAG","Tenet Response :"+new Gson().toJson(model));
                            Integer companyCode = model.getCompanyCode();
                            String companyImage = model.getCompanYLOGO();

                            SharedPreference.setIntValue(ConstantData.SP_COMPANY_CODE, companyCode);
                            SharedPreference.setStringValue(ConstantData.SP_COMPANY_IMAGE, companyImage);
                            SharedPreference.setStringValue(ConstantData.SP_COMPANY_NAME, strregistrationcode);
                            SharedPreference.setBooleanValue(ConstantData.SP_COMPANY_STATUS, true);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            btn_register.setClickable(true);
                            registrationcode.setError("enter valid CompanyName");
                            Toast.makeText(RegisterActivity.this, " Wrong CompanyName", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetCompanyDetail> call, @NonNull Throwable t) {
                        rl_progressbar.setVisibility(View.GONE);
                        CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                        btn_register.setClickable(true);
                    }
                });
            }
        }
    }



    //Onclick Need Help
    public void needhelp(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_need_help);

        LinearLayout NH_Done = dialog.findViewById(R.id.NH_done);

        NH_Done.setOnClickListener(v -> dialog.dismiss());

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