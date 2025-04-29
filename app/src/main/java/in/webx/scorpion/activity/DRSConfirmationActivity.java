package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Model.DRSGenHeader;
import in.webx.scorpion.Model.GetStockUpdateSelectionData;
import in.webx.scorpion.Model.GetThcDetails;
import in.webx.scorpion.Model.PRSBarcodeDetails;
import in.webx.scorpion.Model.PRSDocketDetails;
import in.webx.scorpion.Model.RequestGetStockUpdateSelectionData;
import in.webx.scorpion.Model.RequestPRSGenData;
import in.webx.scorpion.Model.RmPRSBarcodeModel;
import in.webx.scorpion.Model.RmPRSDocketModel;
import in.webx.scorpion.Model.RmPRSHeaderModel;
import in.webx.scorpion.Model.ThcDocketBarCodeSeries;
import in.webx.scorpion.Model.ThcDocketDetail;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryProcess;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.WareHouse;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DRSConfirmationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_validate_document;
    ImageView ivBackPress, progressBar;
    RelativeLayout rl_progressBar;
    TextInputEditText et_drsno, et_docketno;
    Activity activity = this;

    String branchCode;
    Integer companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_drsconfirmation);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        init();


    }

    private void init() {

        btn_validate_document = findViewById(R.id.btn_validate_document);
        ivBackPress = findViewById(R.id.ivBackPress);
        et_docketno = findViewById(R.id.et_docketno);
        et_drsno = findViewById(R.id.et_drsno);
        rl_progressBar = findViewById(R.id.rl_progressbar);


        btn_validate_document.setOnClickListener(this);
        ivBackPress.setOnClickListener(this);

        //Define Function
        backpress();

        SharedPreference.getInstance(this);

        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_validate_document:
                if (!et_drsno.getText().toString().trim().isEmpty())
                {
                    if (CommonMethod.isNetworkConnected(this)) {

                        RequestPRSGenData prsGen = new RequestPRSGenData();
                        prsGen.setVehicleNo(et_drsno.getText().toString().trim());
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

                                        Intent intent = new Intent(DRSConfirmationActivity.this, DRSBarcodePickupActivity.class);
                                        intent.putExtra("vNo", getPRSDocketList.getVehicleno());
                                        startActivity(intent);
                                    } else {
                                        rl_progressBar.setVisibility(View.GONE);
                                        Toast.makeText(DRSConfirmationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    rl_progressBar.setVisibility(View.GONE);
                                    Toast.makeText(DRSConfirmationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<RmPRSHeaderModel> call, @NonNull Throwable t) {
                                rl_progressBar.setVisibility(View.GONE);
                                CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                                Toast.makeText(DRSConfirmationActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", " error : " + t);
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(DRSConfirmationActivity.this, " Please Fill Details", Toast.LENGTH_LONG).show();
                }
                pickupsummary();
                break;
        }

    }

    private void addPRSDetails(RmPRSHeaderModel getPRSHeader) {
        Log.d("TAG", "Data Inserted");

        realm.executeTransaction(realm -> {

            ArrayList<PRSDocketDetails> docketList = new ArrayList<>();
            ArrayList<PRSBarcodeDetails> barcodeList = new ArrayList<>();

            DRSGenHeader prsHeader = new DRSGenHeader();
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
            prsHeader.setDrstype(getPRSHeader.getPrstype() + "");
            prsHeader.setTotalWeight(getPRSHeader.getTotalWeight() + "");
            prsHeader.setCapacityutilization(getPRSHeader.getCapacityutilization() + "");
            prsHeader.setStartKM(getPRSHeader.getStartKM() + "");
            prsHeader.setEndKM(getPRSHeader.getEndKM() + "");
            prsHeader.setFinancialDetail(getPRSHeader.getFinancialDetail() + "");
            prsHeader.setFinancialYear(getPRSHeader.getFinancialYear() + "");
            prsHeader.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            prsHeader.setManualDrsNo(getPRSHeader.getManualPrsNo() + "");
            prsHeader.setMarketVehicle(getPRSHeader.getMarketVehicle() + "");
            prsHeader.setDrsDate(getPRSHeader.getPrsDate() + "");
            prsHeader.setDrsTime(getPRSHeader.getPrsTime() + "");
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
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                Log.d("TAG", "addPRSDetails error: " + e.getMessage());
            }
        });
    }

    private void pickupsummary() {
        final Dialog dialog = new Dialog(DRSConfirmationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_drs_summary);
        TextView txt_DRS_no, txt_Docket, txt_Packages, txt_gen_date, txt_booked_at;
        TextView txt_driver_name, txt_veh_no, txt_veh_type, txt_odometer_read, txt_weight, txt_capacity;
        TextView txt_vendor_name, txt_person_name;
        TextView tvGenerateDRS;
        TextView tv_header;


        txt_DRS_no = dialog.findViewById(R.id.txt_DRS_no);
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
        tvGenerateDRS = dialog.findViewById(R.id.tvGenerateDRS);
        tv_header = dialog.findViewById(R.id.tv_header);



        txt_DRS_no.setText("");
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
        if (activity instanceof DRSConfirmationActivity) {
            tvGenerateDRS.setText(getResources().getString(R.string.label_proceed_to_scan));
            tv_header.setText(R.string.label_to_be_scan_summary);

        } else {
            tvGenerateDRS.setText(getResources().getString(R.string.label_finish_drs_scan));
            tv_header.setText(R.string.label_drs_scan_summary);

        }
        ImageView iv_close_docket = dialog.findViewById(R.id.iv_closePendingDocketList);
        iv_close_docket.setOnClickListener(v -> dialog.cancel());

        tvGenerateDRS.setOnClickListener(v -> {
            dialog.cancel();
            getbarcode("VH/BNGB/1920/000009");
//            Intent i = new Intent(DRSConfirmationActivity.this, DRSBarcodePickupActivity.class);
//            startActivity(i);
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void getbarcode(String thcNo) {

        SharedPreference.setStringValue(ConstantData.SP_THC_NO, thcNo);

        if(CommonMethod.isNetworkConnected(this)) {

            RequestGetStockUpdateSelectionData request = new RequestGetStockUpdateSelectionData();

            request.setBranchCode(branchCode);
            request.setCompanyCode(companyCode.toString());
            request.setThcNo(thcNo);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetThcDetails> call1 = MyRetro.createSecureServiceApp(this, APIService.class).getThcDetails(request);
            call1.enqueue(new Callback<GetThcDetails>() {
                @Override
                public void onResponse(@NonNull Call<GetThcDetails> call, @NonNull Response<GetThcDetails> response) {

                    GetThcDetails getThcDetails = response.body();

                    if (getThcDetails != null) {
                        Log.d("TAG", "Response : " + new Gson().toJson(response.body()));
                        if (getThcDetails.getDocketDetailList().size() > 0) {
                            addbarcoderealm(getThcDetails, thcNo);
                            getStockUpdateSelectionData(request);
                        } else {
                            Toast.makeText(DRSConfirmationActivity.this, "DocketDetailList Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DRSConfirmationActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                    rl_progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<GetThcDetails> call, @NonNull Throwable t) {
                    rl_progressBar.setVisibility(View.GONE);
                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    Log.e("TAG", " error : " + t);
                }
            });
        }else {
        }
    }

    private void addbarcoderealm(GetThcDetails getThcDetails, String thcNo) {
        realm.executeTransaction(realm -> {
            ArrayList<ThcDocketList_Tbl> thcDocketList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> docketBarCodeList = new ArrayList<>();

            for (ThcDocketDetail thcDocketDetail : getThcDetails.getDocketDetailList()) {
                ThcDocketList_Tbl thcDocket = new ThcDocketList_Tbl();
                RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class)
                        .equalTo("dockNo", thcDocketDetail.getDockNo())
                        .equalTo("dockSf", thcDocketDetail.getDockSf()).findAll();

                if (thcDocketListTbls.size() == 0) {
                    thcDocket.setDockNo(thcDocketDetail.getDockNo());
                    thcDocket.setDockSf(thcDocketDetail.getDockSf());
                    thcDocket.setDockDate("");
                    thcDocket.setOrigin(thcDocketDetail.getOrigin());
                    thcDocket.setDestination(thcDocketDetail.getDestination());
                    thcDocket.setTotalActualWeight(Double.valueOf(thcDocketDetail.getTotalActualWeight()));
                    thcDocket.setTotalCftWeight(Double.valueOf(thcDocketDetail.getTotalCftWeight()));
                    thcDocket.setTotalPackages(thcDocketDetail.getTotalPackages());
                    thcDocket.setTotalLoadPackages(thcDocketDetail.getTotalLoadPackages());
                    thcDocket.setThcNo(thcDocketDetail.getThcNo());
                    thcDocket.setTcNo(thcDocketDetail.getTcNo());
                    thcDocket.setScanPackages(0);
                    thcDocket.setTotalLoadActualWeight(0.0);
                    thcDocket.setTotalLoadCftWeight(0.0);
                    thcDocket.setScanDocket(false);
                    thcDocket.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                    thcDocketList.add(thcDocket);

                    for (ThcDocketBarCodeSeries docketBarCodeSeries : thcDocketDetail.getDocketBarCodeSeries()) {
                        docketBarCodeSeries_Tbl docketBarCode = new docketBarCodeSeries_Tbl();
                        docketBarCode.setBcSerialNo(docketBarCodeSeries.getBcSerialNo());
                        docketBarCode.setCompanyCode(Integer.valueOf(docketBarCodeSeries.getCompanyCode()));
                        docketBarCode.setDockNo(thcDocketDetail.getDockNo());
                        docketBarCode.setDockSf(thcDocketDetail.getDockSf());
                        docketBarCode.setBarcodeScanned(false);
                        docketBarCode.setBcScannedDatetime("");
                        docketBarCode.setContinueScanned(false);
                        docketBarCode.setLsThcNo(thcDocketDetail.getThcNo());
                        docketBarCode.setBarcodeType("Inward");
                        docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                        docketBarCodeList.add(docketBarCode);
                    }
                }
            }
            try {
                realm.insertOrUpdate(docketBarCodeList);
                realm.insertOrUpdate(thcDocketList);
            } catch (Exception e) {
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                Log.d("TAG", "addThcBarcodeDetails erorr : " + e.getMessage());
            }
        });

        Intent intent = new Intent(this, DRSBarcodePickupActivity.class);
        intent.putExtra("ThcNo", thcNo);
        this.startActivity(intent);
        this.rl_progressBar.setVisibility(View.GONE);
    }

    private void getStockUpdateSelectionData(RequestGetStockUpdateSelectionData request) {
        Call<GetStockUpdateSelectionData> call = MyRetro.createSecureServiceApp(this, APIService.class).getStockUpdateSelectionData(request);
        call.enqueue(new Callback<GetStockUpdateSelectionData>() {
            @Override
            public void onResponse(@NonNull Call<GetStockUpdateSelectionData> call, @NonNull Response<GetStockUpdateSelectionData> response) {
                if (response.body() != null) {
                    GetStockUpdateSelectionData StockUpdateSelection = response.body();
                    addReasonrealm(StockUpdateSelection);
                } else {
                    Toast.makeText(DRSConfirmationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetStockUpdateSelectionData> call, @NonNull Throwable t) {
                CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                Log.e("TAG", " error : " + t);
            }
        });
    }

    private void addReasonrealm(GetStockUpdateSelectionData response) {
        realm.executeTransaction(realm -> {

            RealmResults<ArrivalCondition> arrivalConditions = realm.where(ArrivalCondition.class).findAll();
            RealmResults<WareHouse> wareHouses = realm.where(WareHouse.class).findAll();
            RealmResults<DeliveryProcess> deliveryProcesses = realm.where(DeliveryProcess.class).findAll();
            RealmResults<DeliveryType> deliveryTypes = realm.where(DeliveryType.class).findAll();

            arrivalConditions.deleteAllFromRealm();
            wareHouses.deleteAllFromRealm();
            deliveryProcesses.deleteAllFromRealm();
            deliveryTypes.deleteAllFromRealm();

            realm.insertOrUpdate(response.getArrivalCondition());
            realm.insertOrUpdate(response.getWareHouse());
            realm.insertOrUpdate(response.getDeliveryProcess());
            realm.insertOrUpdate(response.getDeliveryType());

            ArrivalCondition arrivalCondition = realm.where(ArrivalCondition.class)
                    .equalTo("CodeId", 0).findFirst();
            if (arrivalCondition != null)
                arrivalCondition.deleteFromRealm();

            WareHouse wareHouse = realm.where(WareHouse.class)
                    .equalTo("CodeId", "0").findFirst();
            if (wareHouse != null)
                wareHouse.deleteFromRealm();

            DeliveryProcess deliveryProcesse = realm.where(DeliveryProcess.class)
                    .equalTo("CodeId", "0").findFirst();
            if (deliveryProcesse != null)
                deliveryProcesse.deleteFromRealm();

            DeliveryType deliveryType = realm.where(DeliveryType.class)
                    .equalTo("CodeId", "0").findFirst();
            if (deliveryType != null)
                deliveryType.deleteFromRealm();
        });
    }

    private void backpress() {
        ivBackPress = findViewById(R.id.ivBackPress);
        ivBackPress.setOnClickListener(v -> finish());
    }
}