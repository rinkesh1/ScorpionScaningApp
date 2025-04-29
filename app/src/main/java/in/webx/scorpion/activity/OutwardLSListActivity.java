package in.webx.scorpion.activity;

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

import in.webx.scorpion.Adapter.RVLSListAdapter;
import in.webx.scorpion.Model.DocketBarCodeSeries;
import in.webx.scorpion.Model.DocketDetail;
import in.webx.scorpion.Model.GLSLList;
import in.webx.scorpion.Model.GetLodingSheetList;
import in.webx.scorpion.Model.GetValidLoadingSheetData;
import in.webx.scorpion.Model.RequestGetLodingSheetList;
import in.webx.scorpion.Model.RequestGetValidLoadingSheetData;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.LsHader_Tbl;
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

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

public class OutwardLSListActivity extends AppCompatActivity {

    RecyclerView rvPendingLSList;
    ImageView progressBar;
    RelativeLayout rl_progressBar;
    String branchCode;
    Integer companyCode;

    GetLodingSheetList getLodingSheetList = new GetLodingSheetList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "OutwardLS List");

        //SharedPreferences
        SharedPreference.getInstance(this);

        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_outward_ls_list);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarTransparent));

        rvPendingLSList = findViewById(R.id.rvPendingLSList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Define Function
        getdata();
    }

    //Call API
    private void getdata() {

        if (CommonMethod.isNetworkConnected(this)) {

            RequestGetLodingSheetList getLodingSheet = new RequestGetLodingSheetList();
            getLodingSheet.setCompanyCode(companyCode);
            getLodingSheet.setBranchCode(branchCode);

//        Log.d("TAG", "getdata: " + new Gson().toJson(getLodingSheet));

            progressBar = findViewById(R.id.progressbar);
            rl_progressBar = findViewById(R.id.rl_progressbar);

            Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

            rl_progressBar.setVisibility(View.VISIBLE);
            Log.d("TAG", "getdata Input: " + new Gson().toJson(getLodingSheet));
            Call<GetLodingSheetList> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .getloadingsheetlist(getLodingSheet);
            call.enqueue(new Callback<GetLodingSheetList>() {
                @Override
                public void onResponse(@NonNull Call<GetLodingSheetList> call, @NonNull Response<GetLodingSheetList> response) {

                    if (response.body() != null) {
                        getLodingSheetList = response.body();
//                    Log.v("TAG", "Response : " + new Gson().toJson(response.body()));

                        if (getLodingSheetList.getIsSuccess()) {
                            addrealm(getLodingSheetList.getList());
                            rl_progressBar.setVisibility(View.GONE);
                        } else {
                            rl_progressBar.setVisibility(View.GONE);
                            Toast.makeText(OutwardLSListActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        rl_progressBar.setVisibility(View.GONE);
                        Toast.makeText(OutwardLSListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetLodingSheetList> call, @NonNull Throwable t) {
                    rl_progressBar.setVisibility(View.GONE);
                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    Toast.makeText(OutwardLSListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "error : " + t);
                }
            });
        }
    }

    //Connect Realm Database
    //Add Data in Realm Database
    private void addrealm(List<GLSLList> glslLists) {

        List<LsHader_Tbl> tempLsList = new ArrayList<>();

        realm.executeTransaction(realm -> {
            ArrayList<String> lsInformation = new ArrayList<>();
            for (GLSLList glslList : glslLists) {
                lsInformation.add(glslList.getLsNo());
            }

            RealmResults<LsHader_Tbl> lsHeaderInformationes = realm.where(LsHader_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                    .findAll();

            for (LsHader_Tbl lsHeaderInformation : lsHeaderInformationes) {
                String LsNo = lsHeaderInformation.getLsNo();
                if (!lsInformation.contains(LsNo)) {
                    RealmResults<docketBarCodeSeries_Tbl> lsOrThcDocketBarcodes = realm.where(docketBarCodeSeries_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                            .equalTo("LsThcNo", LsNo).findAll();
                    if (lsOrThcDocketBarcodes != null)
                        lsOrThcDocketBarcodes.deleteAllFromRealm();
                    RealmResults<docketDetailList_Tbl> lsDocketDetails = realm.where(docketDetailList_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                            .equalTo("lsNo", LsNo).findAll();
                    if (lsDocketDetails != null)
                        lsDocketDetails.deleteAllFromRealm();

                    lsHeaderInformation.deleteFromRealm();
                }
            }

            for (GLSLList lsList : glslLists) {
                LsHader_Tbl LsHader = realm.where(LsHader_Tbl.class)
                        .equalTo("lsNo", lsList.getLsNo())
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                        .findFirst();
                LsHader_Tbl lsHader = new LsHader_Tbl();

                if (LsHader == null) {
                    lsHader.setLsNo(lsList.getLsNo());

                    SharedPreference.setIntValue(ConstantData.SP_COMPANY_CODE, lsList.getCompanyCode());
                    lsHader.setCompanyCode(lsList.getCompanyCode());
                    lsHader.setLsDate(lsList.getLsDate());
                    lsHader.setDestination(lsList.getDestination());
                    lsHader.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                    lsHader.setTotalDockets(lsList.getTotalDockets());
                    lsHader.setTotalPackages(lsList.getTotalPackages());
                    lsHader.setTotalActualWeight(lsList.getTotalActualWeight());
                    lsHader.setTotalCftWeight(lsList.getTotalCftWeight());
                    lsHader.setTotalLoadPackages(lsList.getTotalLoadPackages());
                    lsHader.setTotalLoadActualWeight(lsList.getTotalLoadActualWeight());
                    lsHader.setTotalLoadCftWeight(lsList.getTotalLoadCftWeight());
                    lsHader.setIsBcProcess(lsList.getIsBcProcess());
                    lsHader.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

                    tempLsList.add(lsHader);
                }
            }

            try {
                realm.insertOrUpdate(tempLsList);
            } catch (Exception e) {
                Log.d("TAG", "add Ls Details erorr : " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }

            RealmResults<LsHader_Tbl> haderList = realm.where(LsHader_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                    .findAll();

            ArrayList<LsHader_Tbl> haderArrayList = new ArrayList<>(haderList);

            rvPendingLSList.setLayoutManager(new LinearLayoutManager(OutwardLSListActivity.this, RecyclerView.VERTICAL, false));
            RVLSListAdapter rvLsListAdapter = new RVLSListAdapter(haderArrayList, OutwardLSListActivity.this);
            rvPendingLSList.setAdapter(rvLsListAdapter);
        });
    }

    public void BackPressed(View view) {
        finish();
    }

    public void getbarcode(String lsNo, View itemView) {

        itemView.setClickable(false);

        if (CommonMethod.isNetworkConnected(this)) {

            Log.d("TAG", "onBindViewHolder: " + lsNo);

            RequestGetValidLoadingSheetData requestLoadingSheetData = new RequestGetValidLoadingSheetData();

            requestLoadingSheetData.setlSno(lsNo);
            requestLoadingSheetData.setCompanyCode(companyCode);
            requestLoadingSheetData.setBranchCode(branchCode);

//        Log.d("TAG", "request: " + new Gson().toJson(requestLoadingSheetData));

            final GetValidLoadingSheetData[] getValidLoadingSheetData = {new GetValidLoadingSheetData()};

            progressBar = findViewById(R.id.progressbar);
            rl_progressBar = findViewById(R.id.rl_progressbar);

            Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);
            rl_progressBar.setVisibility(View.VISIBLE);
            Call<GetValidLoadingSheetData> call;
            call = MyRetro.createSecureServiceApp(this, APIService.class).getvalidloadingsheetdata(requestLoadingSheetData);
            call.enqueue(new Callback<GetValidLoadingSheetData>() {
                @Override
                public void onResponse(@NonNull Call<GetValidLoadingSheetData> call, @NonNull Response<GetValidLoadingSheetData> response) {
                    if (response.body() != null) {
                        getValidLoadingSheetData[0] = response.body();

//                    Log.v("TAG", "Response : " + new Gson().toJson(response.body()));
                        rl_progressBar.setVisibility(View.GONE);

                        if (getValidLoadingSheetData[0].getIsSuccess()) {
                            addBarcoderealm(getValidLoadingSheetData[0].getDocketDetailList(), lsNo);

                            SharedPreference.setStringValue(ConstantData.SP_LS_NO, getValidLoadingSheetData[0].getLsNo());
                            SharedPreference.setIntValue(ConstantData.SP_TOTAL_DOCKETS, getValidLoadingSheetData[0].getTotalDockets());
                            SharedPreference.setIntValue(ConstantData.SP_TOTAL_PACKAGES, getValidLoadingSheetData[0].getTotalPackages());
                            itemView.setClickable(true);
                        } else {
                            itemView.setClickable(true);
                            Toast.makeText(OutwardLSListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        itemView.setClickable(true);
                        Toast.makeText(OutwardLSListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetValidLoadingSheetData> call, @NonNull Throwable t) {

                    itemView.setClickable(true);
                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                    Toast.makeText(OutwardLSListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                    Log.e("TAG", " error : " + t);
                }
            });
        } else {
            itemView.setClickable(true);
        }
    }

    private void addBarcoderealm(List<DocketDetail> docketDetailList, String lsNo) {
        realm.executeTransaction(realm -> {
            ArrayList<docketDetailList_Tbl> lsDocketArrayList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> docketBarCodeList = new ArrayList<>();

            for (DocketDetail docketDetail : docketDetailList) {
                RealmResults<docketDetailList_Tbl> lsDocketList = realm.where(docketDetailList_Tbl.class)
                        .equalTo("dockNo", docketDetail.getDockNo())
                        .equalTo("dockSf", docketDetail.getDockSf())
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("branchCode", SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE))
                        .findAll();

                if (lsDocketList.size() == 0) {

                    docketDetailList_Tbl lsDocket = new docketDetailList_Tbl();

                    lsDocket.setDockNo(docketDetail.getDockNo());
                    lsDocket.setCompanyCode(docketDetail.getCompanyCode());
                    lsDocket.setDockSf(docketDetail.getDockSf());
                    lsDocket.setDockDate(docketDetail.getDockDate());
                    lsDocket.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                    lsDocket.setOrigin(docketDetail.getOrigin() + "");
                    lsDocket.setDestination(docketDetail.getDestination() + "");
                    lsDocket.setTotalActualWeight(docketDetail.getTotalActualWeight());
                    lsDocket.setTotalCftWeight(docketDetail.getTotalCftWeight());
                    lsDocket.setTotalPackages(docketDetail.getTotalPackages());
                    lsDocket.setTotalLoadPackages(docketDetail.getTotalLoadPackages());
                    lsDocket.setTotalLoadActualWeight(docketDetail.getTotalLoadActualWeight());
                    lsDocket.setTotalLoadCftWeight(docketDetail.getTotalLoadCftWeight());
                    lsDocket.setScanPackages(0);
                    lsDocket.setScanDocket(false);
                    lsDocket.setLsNo(lsNo);
                    lsDocket.setLastBcSerialNo("");
                    lsDocket.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                    lsDocket.setPartialCheck(true);

                    double cfWeight = docketDetail.getTotalLoadCftWeight() / docketDetail.getTotalPackages();
                    double acWeight = docketDetail.getTotalLoadActualWeight() / docketDetail.getTotalPackages();
                    int scanPackage = 0;
                    for (DocketBarCodeSeries docketBarCodeSeries : docketDetail.getDocketBarCodeSeries()) {
                        docketBarCodeSeries_Tbl docketBarCode = new docketBarCodeSeries_Tbl();

                        docketBarCode.setBcSerialNo(docketBarCodeSeries.getBcSerialNo());
                        docketBarCode.setCompanyCode(docketBarCodeSeries.getCompanyCode());
                        docketBarCode.setDockNo(docketBarCodeSeries.getDockNo());
                        docketBarCode.setDockSf(docketBarCodeSeries.getDockSf());
                        docketBarCode.setBarcodeScanned(docketBarCodeSeries.getIsBarcodeScanned());
                        docketBarCode.setBcScannedDatetime(docketBarCodeSeries.getBcScannedDatetime() + "");
                        docketBarCode.setContinueScanned(docketBarCodeSeries.getIsContinueScanned());
                        docketBarCode.setLsThcNo(lsNo);
                        docketBarCode.setBarcodeType("Outward");
                        docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                        docketBarCode.setActualweight(acWeight);
                        docketBarCode.setActualCFweight(cfWeight);
                        docketBarCode.setServerPush(docketBarCodeSeries.getIsBarcodeScanned());
                        if (docketBarCodeSeries.getIsBarcodeScanned()) {
                            scanPackage++;
                            lsDocket.setScanDocket(true);
                        }
                        docketBarCodeList.add(docketBarCode);
                    }
                    lsDocket.setScanPackages(scanPackage);
                    lsDocketArrayList.add(lsDocket);
                }
                /*else {
                    double cfWeight = docketDetail.getTotalLoadCftWeight() / docketDetail.getTotalPackages();
                    double acWeight = docketDetail.getTotalLoadActualWeight() / docketDetail.getTotalPackages();
                    int scanPackage = 0;

                    for (DocketBarCodeSeries docketBarCodeSeries : docketDetail.getDocketBarCodeSeries()) {
                        docketBarCodeSeries_Tbl docketBarCode = new docketBarCodeSeries_Tbl();

                        if(docketBarCodeSeries.getIsBarcodeScanned()) {
                            docketBarCode.setBarcodeScanned(docketBarCodeSeries.getIsBarcodeScanned());

                            docketBarCode.setBcSerialNo(docketBarCodeSeries.getBcSerialNo());
                            docketBarCode.setCompanyCode(docketBarCodeSeries.getCompanyCode());
                            docketBarCode.setDockNo(docketBarCodeSeries.getDockNo());
                            docketBarCode.setDockSf(docketBarCodeSeries.getDockSf());
                            docketBarCode.setBcScannedDatetime(docketBarCodeSeries.getBcScannedDatetime() + "");
                            docketBarCode.setContinueScanned(docketBarCodeSeries.getIsContinueScanned());
                            docketBarCode.setLsThcNo(lsNo);
                            docketBarCode.setBarcodeType("Outward");
                            docketBarCode.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                            docketBarCode.setActualweight(acWeight);
                            docketBarCode.setActualCFweight(cfWeight);
                            docketBarCode.setServerPush(docketBarCodeSeries.getIsBarcodeScanned());

                            docketBarCodeList.add(docketBarCode);
                        }
                    }
                }*/
            }
            Log.d("TAG", "LS Docket Count: " + lsDocketArrayList.size());
            try {
                realm.insertOrUpdate(lsDocketArrayList);
                realm.insertOrUpdate(docketBarCodeList);
            } catch (Exception e) {
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                Log.d("TAG", "addLsBarcodeDetails error: " + e.getMessage());
            }
        });

        Intent intent = new Intent(this, LSBarcodePickupActivity.class);
        intent.putExtra("LSNo", lsNo);
        startActivity(intent);
    }
}