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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Model.PRSBarcodeDetails;
import in.webx.scorpion.Model.PRSDocketDetails;
import in.webx.scorpion.Model.PRSGenHeader;
import in.webx.scorpion.Model.RequestPRSGenData;
import in.webx.scorpion.Model.RmPRSBarcodeModel;
import in.webx.scorpion.Model.RmPRSDocketModel;
import in.webx.scorpion.Model.RmPRSHeaderModel;
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

public class PRSGenerationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_validate_detail;
    ImageView ivBackPress, progressBar;
    RelativeLayout rl_progressBar;
    TextInputEditText et_vehno, et_bookedby, et_dockno;
    String branchCode, userId;
    Integer companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_prsgeneration);

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

        SharedPreference.getInstance(this);

        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);
        userId = SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_validate_detail:
                if (!Objects.requireNonNull(et_vehno.getText()).toString().trim().isEmpty()) {
                    if (CommonMethod.isNetworkConnected(this)) {

                        RequestPRSGenData prsGen = new RequestPRSGenData();
                        prsGen.setVehicleNo(et_vehno.getText().toString().trim());
                        prsGen.setCompanyCode(companyCode.toString());
                        prsGen.setBranchCode(branchCode);

                        progressBar = findViewById(R.id.progressbar);
                        rl_progressBar = findViewById(R.id.rl_progressbar);

                        Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);
                        rl_progressBar.setVisibility(View.VISIBLE);

                        Log.d("TAG", "get PRS list Data: " + new Gson().toJson(prsGen));
                        Call<RmPRSHeaderModel> call = MyRetro.createSecureServiceApp(this, APIService.class)
                                .getPRSGendataList(prsGen);
                        call.enqueue(new Callback<RmPRSHeaderModel>() {
                            @Override
                            public void onResponse(@NonNull Call<RmPRSHeaderModel> call, @NonNull Response<RmPRSHeaderModel> response) {
                                if (response.body() != null) {
                                    RmPRSHeaderModel getPRSDocketList = response.body();

                                    if (getPRSDocketList.getIsSuccess()) {
                                        addPRSDetails(getPRSDocketList);
                                        rl_progressBar.setVisibility(View.GONE);

                                        Intent intent = new Intent(PRSGenerationActivity.this, PRSBarcodePickupActivity.class);
                                        intent.putExtra("vNo", getPRSDocketList.getVehicleno());
                                        startActivity(intent);
                                    } else {
                                        rl_progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PRSGenerationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    rl_progressBar.setVisibility(View.GONE);
                                    Toast.makeText(PRSGenerationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<RmPRSHeaderModel> call, @NonNull Throwable t) {
                                rl_progressBar.setVisibility(View.GONE);
                                CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                                Toast.makeText(PRSGenerationActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", " error : " + t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(PRSGenerationActivity.this, " Please Fill Details", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivBackPress:
                finish();
                break;
        }
    }

    private void addPRSDetails(RmPRSHeaderModel getPRSHeader) {
        Log.d("TAG", "Data Inserted");

        realm.executeTransaction(realm -> {

            ArrayList<PRSDocketDetails> docketList = new ArrayList<>();
            ArrayList<PRSBarcodeDetails> barcodeList = new ArrayList<>();

            PRSGenHeader prsHeader = new PRSGenHeader();
            prsHeader.setUserId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            prsHeader.setCompanyCode(getPRSHeader.getCompanyCode());
            prsHeader.setEntryBy(getPRSHeader.getEntryBy() + "");
            prsHeader.setEngineNo(getPRSHeader.getEngineNo() + "");
            prsHeader.setCurrentBranch(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            prsHeader.setIsSuccess(getPRSHeader.getIsSuccess());
            prsHeader.setMessage(getPRSHeader.getMessage() + "");
            prsHeader.setVehicleno(getPRSHeader.getVehicleno() + "");
            prsHeader.setVehicletype(getPRSHeader.getVehicletype() + "");
            prsHeader.setFtlype(getPRSHeader.getFtlype() + "");
            prsHeader.setWeightLoaded(getPRSHeader.getWeightLoaded() + "");
            prsHeader.setEhengineno(getPRSHeader.getEhengineno() + "");
            prsHeader.setChasisno(getPRSHeader.getChasisno() + "");
            prsHeader.setRcbkno(getPRSHeader.getRcbkno() + "");
            prsHeader.setVehicleregdt(getPRSHeader.getVehicleregdt() + "");
            prsHeader.setVehiclepermitdt(getPRSHeader.getVehiclepermitdt() + "");
            prsHeader.setInsurancevaliditydt(getPRSHeader.getInsurancevaliditydt() + "");
            prsHeader.setFitnessvaliditydt(getPRSHeader.getFitnessvaliditydt() + "");
            prsHeader.setVendorcode(getPRSHeader.getVendorcode() + "");
            prsHeader.setVendortype(getPRSHeader.getVendortype() + "");
            prsHeader.setVendorname(getPRSHeader.getVendorname() + "");
            prsHeader.setDrivername(getPRSHeader.getDrivername() + "");
            prsHeader.setDriver1Licence(getPRSHeader.getDriver1Licence() + "");
            prsHeader.setDrivermobileno(getPRSHeader.getDrivermobileno() + "");
            prsHeader.setLicenseno(getPRSHeader.getLicenseno() + "");
            prsHeader.setIssuebyrto(getPRSHeader.getIssuebyrto() + "");
            prsHeader.setLicenseveldt(getPRSHeader.getLicenseveldt() + "");
            prsHeader.setPrstype(getPRSHeader.getPrstype() + "");
            prsHeader.setTotalWeight(getPRSHeader.getTotalWeight() + "");
            prsHeader.setCapacityutilization(getPRSHeader.getCapacityutilization() + "");
            prsHeader.setStartKM(getPRSHeader.getStartKM() + "");
            prsHeader.setEndKM(getPRSHeader.getEndKM() + "");
            prsHeader.setFinancialDetail(getPRSHeader.getFinancialDetail() + "");
            prsHeader.setFinancialYear(getPRSHeader.getFinancialYear() + "");
            prsHeader.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            prsHeader.setManualPrsNo(getPRSHeader.getManualPrsNo() + "");
            prsHeader.setMarketVehicle(getPRSHeader.getMarketVehicle() + "");
            prsHeader.setPrsDate(getPRSHeader.getPrsDate() + "");
            prsHeader.setPrsTime(getPRSHeader.getPrsTime() + "");
            prsHeader.setRcBookNo(getPRSHeader.getRcBookNo() + "");
            prsHeader.setIsOverLoad(getPRSHeader.getIsOverLoad() + "");

            for (RmPRSDocketModel d : getPRSHeader.getDocketDetails()) {
                PRSDocketDetails dkt = new PRSDocketDetails();

                RealmResults<PRSDocketDetails> prsDocketList = realm.where(PRSDocketDetails.class)
                        .equalTo("docketNo", d.getDocketNo())
                        .equalTo("docketSufix", d.getDocketSufix()).findAll();

                if (prsDocketList.size() == 0) {
                    dkt.setDocketID(prsHeader.getVehicleno() + "|" + d.getDocketNo());
                    dkt.setUserId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                    dkt.setDocketNo(d.getDocketNo());
                    dkt.setCustomerRefNo(d.getCustomerRefNo());
                    dkt.setDocketSufix(d.getDocketSufix());
                    dkt.setDocketDate(d.getDocketDate());
                    dkt.setPaybas(d.getPaybas());
                    dkt.setOrigin(d.getOrigin());
                    dkt.setDestination(d.getDestination());
                    dkt.setTotalPendingPackages(d.getTotalPendingPackages());
                    dkt.setTotalArrivedPackages(d.getTotalArrivedPackages());
                    dkt.setTotalArrivedWeight(d.getTotalArrivedWeight());
                    dkt.setChargedWeight(d.getChargedWeight());
                    dkt.setPackages(d.getPackages());
                    dkt.setCewbNo(d.getCewbNo());
                    dkt.setContractId(d.getContractId());
                    dkt.setContractAmount(d.getContractAmount());
                    dkt.setChargeRate(d.getChargeRate());
                    dkt.setRateType(d.getRateType());
                    dkt.setRemark(d.getRemark());
                    dkt.setMinCharge(d.getMinCharge());
                    dkt.setMaxCharge(d.getMaxCharge());
                    dkt.setScannedPackages(d.getScannedPackages());
                    dkt.setVehicleno(d.getVehicleno());
                    dkt.setDocketScanned(false);

                    docketList.add(dkt);

                    for (RmPRSBarcodeModel b : d.getPRS_BCSeries()) {
                        PRSBarcodeDetails bcode = new PRSBarcodeDetails();
                        bcode.setBarcodeId(prsHeader.getVehicleno() + "|" + d.getDocketNo() + "|" + b.getBcSeriesNo());
                        bcode.setUserId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                        bcode.setDocketNo(b.getDocketNo());
                        bcode.setVehicleno(b.getVehicleno());
                        bcode.setBcSeriesNo(b.getBcSeriesNo());
                        bcode.setIsScanned(b.getIsScanned());

                        barcodeList.add(bcode);
                    }
                }
            }
            try {
                realm.insertOrUpdate(prsHeader);
                realm.insertOrUpdate(docketList);
                realm.insertOrUpdate(barcodeList);
            } catch (Exception e) {
                Log.d("TAG", "addPRSDetails error: " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        });
    }
}