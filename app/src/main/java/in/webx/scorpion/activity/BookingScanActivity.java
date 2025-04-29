package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Model.BookingDocketBarCodeSeries;
import in.webx.scorpion.Model.BookingDocketDetail;
import in.webx.scorpion.Model.BookingDocketList_Tbl;
import in.webx.scorpion.Model.BookingdocketBarCodeSeries_Tbl;
import in.webx.scorpion.Model.GetBookingScanData;
import in.webx.scorpion.Model.RequestBookingScanData;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingScanActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_start_scan;
    ImageView ivBackPress, progressBar;
    RelativeLayout rl_progressBar;
    TextInputEditText et_vehno, et_dockno;
    String branchCode, userId;
    Integer companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_booking_scan);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        init();
    }

    private void init() {
        btn_start_scan = findViewById(R.id.btn_start_scan);
        ivBackPress = findViewById(R.id.ivBackPress);
        et_vehno = findViewById(R.id.et_vehno);
        et_dockno = findViewById(R.id.et_dockno);

        btn_start_scan.setOnClickListener(this);
        ivBackPress.setOnClickListener(this);

        //SharedPreferences
        SharedPreference.getInstance(this);

        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);
        userId = SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_scan:
                getData();
                break;
            case R.id.ivBackPress:
                finish();
                break;
        }
    }

    private void getData() {
        if (!et_vehno.getText().toString().trim().isEmpty() || !et_dockno.getText().toString().trim().isEmpty()) {

            if(CommonMethod.isNetworkConnected(this)) {

                //Request for get Booking Scan Data
                RequestBookingScanData bookingScanData = new RequestBookingScanData();

                bookingScanData.setCompanyCode(companyCode.toString());
                bookingScanData.setCurrentLocation(branchCode);
                bookingScanData.setVehicleNo(et_vehno.getText().toString().trim());
                bookingScanData.setDocketNo(et_dockno.getText().toString().trim());
                bookingScanData.setUserId(userId);

                progressBar = findViewById(R.id.progressbar);
                rl_progressBar = findViewById(R.id.rl_progressbar);

                Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);
                rl_progressBar.setVisibility(View.VISIBLE);

                Call<GetBookingScanData> call = MyRetro.createSecureServiceApp(this, APIService.class).getBookingScandata(bookingScanData);
                call.enqueue(new Callback<GetBookingScanData>() {
                    @Override
                    public void onResponse(@NonNull Call<GetBookingScanData> call, @NonNull Response<GetBookingScanData> response) {

                        et_vehno.getText().clear();
                        et_dockno.getText().clear();

                        GetBookingScanData getBookingScanData = response.body();
                        if (getBookingScanData != null) {
                            if (getBookingScanData.getBookingScan_List().size() > 0) {
                                // Scan data add into realm
                                addrealm(getBookingScanData);
                                rl_progressBar.setVisibility(View.GONE);
                            } else {
                                rl_progressBar.setVisibility(View.GONE);
                                Toast.makeText(BookingScanActivity.this, "No docket found for scan to given criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            rl_progressBar.setVisibility(View.GONE);
                            Toast.makeText(BookingScanActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetBookingScanData> call, @NonNull Throwable t) {
                        rl_progressBar.setVisibility(View.GONE);
                        CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    }
                });
            }
        } else {
            Toast.makeText(BookingScanActivity.this, " Please Fill Vehicle or Docket Details", Toast.LENGTH_LONG).show();
        }
    }

    private void addrealm(GetBookingScanData getBookingDetails) {
        SharedPreference.setStringValue(ConstantData.SP_KEY_VEHICLENO, getBookingDetails.getBookingScan_List().get(0).getVehicleNo());

        realm.executeTransaction(realm -> {
            ArrayList<BookingDocketList_Tbl> bookingDocketList = new ArrayList<>();
            ArrayList<BookingdocketBarCodeSeries_Tbl> docketBarCodeList = new ArrayList<>();

            for (BookingDocketDetail bookingDocketDetail : getBookingDetails.getBookingScan_List()) {
                BookingDocketList_Tbl bookingDocket = new BookingDocketList_Tbl();

                RealmResults<BookingDocketList_Tbl> thcDocketListTbls = realm.where(BookingDocketList_Tbl.class)
                        .equalTo("docketNo", bookingDocketDetail.getDocketNo()).findAll();

                if (thcDocketListTbls.size() == 0) {

                    bookingDocket.setDocketNo(bookingDocketDetail.getDocketNo());
                    bookingDocket.setOrigin(bookingDocketDetail.getOrigin());
                    bookingDocket.setNoofPkgs(bookingDocketDetail.getNoofPkgs());
                    bookingDocket.setVehicleNo(bookingDocketDetail.getVehicleNo());
                    bookingDocket.setDocketStatus(bookingDocketDetail.getDocketStatus());
                    bookingDocket.setScanDocket(false);
                    bookingDocket.setScanPackages(0);
                    bookingDocket.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                    bookingDocketList.add(bookingDocket);

                    for (BookingDocketBarCodeSeries docketBarCodeSeries : bookingDocketDetail.getLst_BCSeries()) {
                        BookingdocketBarCodeSeries_Tbl docketBarCode = new BookingdocketBarCodeSeries_Tbl();
                        docketBarCode.setBcSeriesNo(docketBarCodeSeries.getBcSeriesNo());
                        docketBarCode.setCompanyCode(branchCode);
                        docketBarCode.setDocketNo(bookingDocketDetail.getDocketNo());
                        docketBarCode.setScanned(false);
                        docketBarCode.setVehicleNo(bookingDocketDetail.getVehicleNo());

                        docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                        docketBarCodeList.add(docketBarCode);
                    }
                }
            }
            try {
                realm.insertOrUpdate(docketBarCodeList);
                realm.insertOrUpdate(bookingDocketList);
            } catch (Exception e) {
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                Log.d("TAG", " Erorr : " + e.getMessage());
            }
        });
        startActivity(new Intent(BookingScanActivity.this, BookingBarcodePickupActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}