package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.webx.scorpion.Adapter.RVTHCListAdapter;
import in.webx.scorpion.Model.GTLFSList;
import in.webx.scorpion.Model.GetStockUpdateSelectionData;
import in.webx.scorpion.Model.GetThcDetails;
import in.webx.scorpion.Model.GetThcListForStock;
import in.webx.scorpion.Model.RequestGetStockUpdateSelectionData;
import in.webx.scorpion.Model.RequestGetThcListForStock;
import in.webx.scorpion.Model.ThcDocketBarCodeSeries;
import in.webx.scorpion.Model.ThcDocketDetail;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryProcess;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
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

public class InwardTHCListActivity extends AppCompatActivity {

    RecyclerView rvPendingThcList;
    ImageView progressBar;
    public RelativeLayout rlProgressBar;
    public static boolean IsStockUpdate = false;
    public static boolean thcListClick;
    String branchCode;
    Integer companyCode;

    ImageView ivBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "InwardTHCListActivity");
        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_inward_thc_list);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Define Function
        init();
    }

    private void init() {

        SharedPreference.getInstance(this);

        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);

        //Define Function
        backpress();
        getdata();
    }

    //get Thc list
    private void getdata() {

        if (CommonMethod.isNetworkConnected(this)) {

            RequestGetThcListForStock requestGetThcListForStock = new RequestGetThcListForStock();

            requestGetThcListForStock.setCompanyCode(companyCode);
            requestGetThcListForStock.setBranchCode(branchCode);

            progressBar = findViewById(R.id.progressbar);
            rlProgressBar = findViewById(R.id.rl_progressbar);

            Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

            Log.d("TAG", "Thc ListForStock Req: " + new Gson().toJson(requestGetThcListForStock));

            rlProgressBar.setVisibility(View.VISIBLE);

            Call<GetThcListForStock> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .getthclistforstock(requestGetThcListForStock);
            call.enqueue(new Callback<GetThcListForStock>() {
                @Override
                public void onResponse(@NonNull Call<GetThcListForStock> call, @NonNull Response<GetThcListForStock> response) {
                    Log.d("TAG", "Thc ListForStock Response");
                    if (response.body() != null) {
                        GetThcListForStock getThcListForStock = response.body();

//                    Log.v("TAG", "Response : " + new Gson().toJson(response.body()));

                        if (getThcListForStock.getGtlfsList() != null) {
                            addrealm(getThcListForStock.getGtlfsList());
                            rlProgressBar.setVisibility(View.GONE);
                        } else {
                            rlProgressBar.setVisibility(View.GONE);
                            Toast.makeText(InwardTHCListActivity.this, "No data faund", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        rlProgressBar.setVisibility(View.GONE);
                        Toast.makeText(InwardTHCListActivity.this, "No data faund", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetThcListForStock> call, @NonNull Throwable t) {

                    rlProgressBar.setVisibility(View.GONE);

                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    Toast.makeText(InwardTHCListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", " error : " + t);
                }
            });
        }
    }

    // Thc List add realm
    private void addrealm(List<GTLFSList> gtlfsList) {

        List<THCHeader_Tbl> thcHeaderlist = new ArrayList<>();
        realm.executeTransaction(realm -> {

            ArrayList<String> thcInformation = new ArrayList<>();

            for (GTLFSList glslLists : gtlfsList) {
                thcInformation.add(glslLists.getThcNo());
            }

            RealmResults<THCHeader_Tbl> thcHeaderInformationTables = realm.where(THCHeader_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                    .findAll();

            for (THCHeader_Tbl thcHeaderInformationTable : thcHeaderInformationTables) {

                String Thc = thcHeaderInformationTable.getThcNo();

                if (!thcInformation.contains(Thc)) {

                    RealmResults<docketBarCodeSeries_Tbl> lsOrThcDocketBarcodeList = realm.where(docketBarCodeSeries_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                            .equalTo("LsThcNo", Thc).findAll();

                    if (lsOrThcDocketBarcodeList != null)
                        lsOrThcDocketBarcodeList.deleteAllFromRealm();

                    RealmResults<ThcDocketList_Tbl> thcDocketDetailList = realm.where(ThcDocketList_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                            .equalTo("ThcNo", Thc).findAll();
                    if (thcDocketDetailList != null)
                        thcDocketDetailList.deleteAllFromRealm();

                    thcHeaderInformationTable.deleteFromRealm();
                }
            }

            for (GTLFSList glslLists : gtlfsList) {
                THCHeader_Tbl thcHeader = realm.where(THCHeader_Tbl.class)
                        .equalTo("thcNo", glslLists.getThcNo())
                        .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                        .findFirst();

                THCHeader_Tbl THCHeader = new THCHeader_Tbl();
                if (thcHeader == null) {
                    THCHeader.setThcNo(glslLists.getThcNo());

                    THCHeader.setThcDate(glslLists.getThcDate());
                    THCHeader.setToBhCode(glslLists.getToBhCode());
                    THCHeader.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                    THCHeader.setTotalDockets(glslLists.getTotalDockets());
                    THCHeader.setTotalPackages(glslLists.getTotalDockets());
                    THCHeader.setTotalActualWeight(glslLists.getTotalPackages());
                    THCHeader.setTotalCftWeight(glslLists.getTotalCftWeight());
                    THCHeader.setTotalLoadPackages(glslLists.getTotalLoadPackages());
                    THCHeader.setTotalLoadActualWeight(glslLists.getTotalLoadActualWeight());
                    THCHeader.setTotalLoadCftWeight(glslLists.getTotalLoadCftWeight());
                    THCHeader.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                    thcHeaderlist.add(THCHeader);
                }
            }
            try {
                realm.insertOrUpdate(thcHeaderlist);
            } catch (Exception e) {
                Log.d("TAG", "add Thc Details Erorr : " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        });
        rvPendingThcList = findViewById(R.id.rvPendingThcList);

        RealmResults<THCHeader_Tbl> thcHeaderList = realm.where(THCHeader_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                .findAll();
        ArrayList<THCHeader_Tbl> thcHeaderArrayList = new ArrayList<>(realm.copyFromRealm(thcHeaderList));

        rvPendingThcList.setLayoutManager(new LinearLayoutManager(InwardTHCListActivity.this, RecyclerView.VERTICAL, false));
        RVTHCListAdapter thcPackagesAdapter = new RVTHCListAdapter(thcHeaderArrayList, InwardTHCListActivity.this, companyCode, branchCode);
        rvPendingThcList.setAdapter(thcPackagesAdapter);
    }

    public void getbarcode(String thcNo, View itemView) {

        itemView.setClickable(false);
        SharedPreference.setStringValue(ConstantData.SP_THC_NO, thcNo);

        if (CommonMethod.isNetworkConnected(this)) {

            RequestGetStockUpdateSelectionData request = new RequestGetStockUpdateSelectionData();

            request.setBranchCode(branchCode);
            request.setCompanyCode(companyCode.toString());
            request.setThcNo(thcNo);

            rlProgressBar.setVisibility(View.VISIBLE);

            Call<GetThcDetails> call1 = MyRetro.createSecureServiceApp(this, APIService.class)
                    .getThcDetails(request);
            call1.enqueue(new Callback<GetThcDetails>() {
                @Override
                public void onResponse(@NonNull Call<GetThcDetails> call
                        , @NonNull Response<GetThcDetails> response) {

                    GetThcDetails getThcDetails = response.body();

                    if (getThcDetails.getSuccess()) {
//                    Log.d("TAG", "Response inward : " + new Gson().toJson(response.body()));
                        if (getThcDetails.getDocketDetailList().size() > 0) {
                            addbarcoderealm(getThcDetails, thcNo);
                            getStockUpdateSelectionData(request);
                            itemView.setClickable(true);
                        } else {
                            itemView.setClickable(true);
                            Toast.makeText(InwardTHCListActivity.this, "DocketDetailList Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        itemView.setClickable(true);
                        Toast.makeText(InwardTHCListActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                    rlProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<GetThcDetails> call, @NonNull Throwable t) {
                    rlProgressBar.setVisibility(View.GONE);
                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    Log.e("TAG", " error : " + t);
                    itemView.setClickable(true);
                }
            });
        } else {
            itemView.setClickable(true);
        }
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
                    Toast.makeText(InwardTHCListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
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

    private void addbarcoderealm(GetThcDetails getThcDetails, String thcNo) {
        realm.executeTransaction(realm -> {
            ArrayList<ThcDocketList_Tbl> thcDocketList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> docketBarCodeList = new ArrayList<>();

            for (ThcDocketDetail thcDocketDetail : getThcDetails.getDocketDetailList()) {
                ThcDocketList_Tbl thcDocket = new ThcDocketList_Tbl();
                RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class)
                        .equalTo("dockNo", thcDocketDetail.getDockNo())
                        .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("dockSf", thcDocketDetail.getDockSf()).findAll();

                if (thcDocketListTbls.size() == 0) {
                    thcDocket.setDockNo(thcDocketDetail.getDockNo());
                    thcDocket.setDockSf(thcDocketDetail.getDockSf());
                    thcDocket.setDockDate("");
                    thcDocket.setOrigin(thcDocketDetail.getOrigin());
                    thcDocket.setDestination(thcDocketDetail.getDestination());
                    thcDocket.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
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
                        docketBarCode.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                        docketBarCode.setBarcodeScanned(false);
                        docketBarCode.setBcScannedDatetime("");
                        docketBarCode.setContinueScanned(false);
                        docketBarCode.setLsThcNo(thcDocketDetail.getThcNo());
                        docketBarCode.setBarcodeType("Inward");
                        docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                        docketBarCodeList.add(docketBarCode);
                    }
                } /*else {
                    for (ThcDocketBarCodeSeries docketBarCodeSeries : thcDocketDetail.getDocketBarCodeSeries()) {
                        docketBarCodeSeries_Tbl docketBarCode = new docketBarCodeSeries_Tbl();
                        Log.d("TAG", "addbarcoderealm: "+docketBarCodeSeries.getIsBarcodeScanned());
//                        if (docketBarCodeSeries.getIsBarcodeScanned().equalsIgnoreCase("true")) {
                        if (docketBarCodeSeries.getIsBarcodeScanned() != null) {

                            docketBarCode.setBcSerialNo(docketBarCodeSeries.getBcSerialNo());
                            docketBarCode.setCompanyCode(Integer.valueOf(docketBarCodeSeries.getCompanyCode()));
                            docketBarCode.setDockNo(thcDocketDetail.getDockNo());
                            docketBarCode.setDockSf(thcDocketDetail.getDockSf());
                            docketBarCode.setBarcodeScanned(docketBarCodeSeries.getIsBarcodeScanned().equalsIgnoreCase("true"));
                            docketBarCode.setBcScannedDatetime("");
                            docketBarCode.setContinueScanned(false);
                            docketBarCode.setLsThcNo(thcDocketDetail.getThcNo());
                            docketBarCode.setBarcodeType("Inward");
                            docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                            docketBarCodeList.add(docketBarCode);
                        }

                    }
                }*/
            }
            try {
                realm.insertOrUpdate(docketBarCodeList);
                realm.insertOrUpdate(thcDocketList);
            } catch (Exception e) {
                Log.d("TAG", "addThcBarcodeDetails erorr : " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        });

        Intent intent = new Intent(this, THCBarcodePickupActivity.class);
        intent.putExtra("ThcNo", thcNo);
        this.startActivity(intent);
        this.rlProgressBar.setVisibility(View.GONE);
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