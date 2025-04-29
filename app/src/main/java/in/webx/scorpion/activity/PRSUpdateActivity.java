package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import in.webx.scorpion.Model.GetPRSUpdateData;
import in.webx.scorpion.Model.RequestPRSUpdateData;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PRSUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_validate_detail;
    ImageView ivBackPress, progressBar;
    RelativeLayout rl_progressBar;
    TextInputEditText et_vehno, et_bookedby, et_dockno;
    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_prsupdate);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        init();
    }

    private void init() {
        btn_validate_detail = findViewById(R.id.btn_validate_detail);
        ivBackPress = findViewById(R.id.ivBackPress);
        et_vehno = findViewById(R.id.et_vehno);
        et_bookedby = findViewById(R.id.et_bookedby);
        et_dockno = findViewById(R.id.et_dockno);

        btn_validate_detail.setOnClickListener(this);
        ivBackPress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_validate_detail) {
            if (!et_vehno.getText().toString().trim().isEmpty() || !et_bookedby.getText().toString().trim().isEmpty() || !et_dockno.getText().toString().trim().isEmpty()) {
                if (CommonMethod.isNetworkConnected(this)) {
                    RequestPRSUpdateData requestPRSUpdateData = new RequestPRSUpdateData();
                    requestPRSUpdateData.setVehicleNumber(et_vehno.getText().toString().trim());
                    requestPRSUpdateData.setBookedBy(et_bookedby.getText().toString().trim());
                    requestPRSUpdateData.setDocketNumber(et_dockno.getText().toString().trim());

                    progressBar = findViewById(R.id.progressbar);
                    rl_progressBar = findViewById(R.id.rl_progressbar);

                    Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);
                    rl_progressBar.setVisibility(View.VISIBLE);

                    Call<GetPRSUpdateData> call;
                    call = MyRetro.createSecureServiceApp(this, APIService.class).getPRSUpdatedata(requestPRSUpdateData);
                    call.enqueue(new Callback<GetPRSUpdateData>() {
                        @Override
                        public void onResponse(@NonNull Call<GetPRSUpdateData> call, @NonNull Response<GetPRSUpdateData> response) {
                            if (response.body() != null) {
                                rl_progressBar.setVisibility(View.GONE);
                                pickupsummary();
                            } else {
                                rl_progressBar.setVisibility(View.GONE);
                                Toast.makeText(PRSUpdateActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<GetPRSUpdateData> call, @NonNull Throwable t) {
                            rl_progressBar.setVisibility(View.GONE);
                            CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                    + SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                            Toast.makeText(PRSUpdateActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", " error : " + t);
                        }
                    });
                }
            } else {
                Toast.makeText(PRSUpdateActivity.this, " Please Fill Details", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pickupsummary() {
        final Dialog dialog = new Dialog(PRSUpdateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pickup_summary);
        TextView txt_PRS_no, txt_Docket, txt_Packages, txt_gen_date, txt_booked_at;
        TextView txt_driver_name, txt_veh_no, txt_veh_type, txt_odometer_read, txt_weight, txt_capacity;
        TextView txt_vendor_name, txt_person_name;
        TextView tvGeneratePRS;

        txt_PRS_no = dialog.findViewById(R.id.txt_PRS_no);
        txt_Docket = dialog.findViewById(R.id.txt_Docket);
        txt_Packages = dialog.findViewById(R.id.txt_Packages);
        txt_gen_date = dialog.findViewById(R.id.txt_gen_date);
        txt_booked_at = dialog.findViewById(R.id.txt_booked_at);
        txt_driver_name = dialog.findViewById(R.id.txt_driver_name);
        txt_veh_no = dialog.findViewById(R.id.txt_veh_no);
        txt_veh_type = dialog.findViewById(R.id.txt_veh_type);
        txt_odometer_read = dialog.findViewById(R.id.txt_odometer_read);
        txt_weight = dialog.findViewById(R.id.txt_weight);
        txt_capacity = dialog.findViewById(R.id.txt_capacity);
        txt_vendor_name = dialog.findViewById(R.id.txt_vendor_name);
        txt_person_name = dialog.findViewById(R.id.txt_person_name);
        tvGeneratePRS = dialog.findViewById(R.id.tvGeneratePRS);

        txt_PRS_no.setText("");
        txt_Docket.setText("");
        txt_Packages.setText("");
        txt_gen_date.setText("");
        txt_booked_at.setText("");
        txt_driver_name.setText("");
        txt_veh_no.setText("");
        txt_veh_type.setText("");
        txt_odometer_read.setText("");
        txt_weight.setText("");
        txt_capacity.setText("");
        txt_vendor_name.setText("");
        txt_person_name.setText("");
        if (activity instanceof PRSGenerationActivity) {
            tvGeneratePRS.setText("GENERATE PRS");
        } else {
            tvGeneratePRS.setText("PROCEED TO SCAN");
        }
        ImageView iv_close_docket = dialog.findViewById(R.id.iv_closePendingDocketList);
        iv_close_docket.setOnClickListener(v -> dialog.cancel());

        tvGeneratePRS.setOnClickListener(v -> {
            dialog.cancel();
            Intent i = new Intent(PRSUpdateActivity.this, PRSBarcodePickupActivity.class);
            startActivity(i);
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}