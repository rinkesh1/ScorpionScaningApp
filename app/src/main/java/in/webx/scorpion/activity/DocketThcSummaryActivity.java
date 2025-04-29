package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;
import static in.webx.scorpion.Retrofit.AuthHelper.refreshToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Adapter.SummaryDocketAdapter;
import in.webx.scorpion.Model.ExtraBcSerialListModel;
import in.webx.scorpion.Model.ExtraBcSerialModel;
import in.webx.scorpion.Model.ExtraDocketListModel;
import in.webx.scorpion.Model.ExtraDocketResponseModel;
import in.webx.scorpion.Model.ExtraScannedDkt;
import in.webx.scorpion.Model.GetStockUpdate;
import in.webx.scorpion.Model.THCStockUpdate;
import in.webx.scorpion.Model.ThcStockDetail;
import in.webx.scorpion.Model.ThcStockDocketBarCodeSeries;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocketThcSummaryActivity extends AppCompatActivity {

    EditText SmSearch;
    ImageView SmFilter, SmBack;
    RecyclerView SmMFDocketList;
    private TextView SmStockUpdate;

    private String THCNo;
    public Toolbar toolbar;
    RelativeLayout rl_progressbar;

    SummaryDocketAdapter summaryDocketAdapter;

    ArrayList<ThcDocketList_Tbl> rviewThcDocketList = new ArrayList<>();
    ArrayList<ThcDocketList_Tbl> tempThcDocketList = new ArrayList<>();
    ArrayList<ThcDocketList_Tbl> mThcDocketListTbls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_docket_thc_summary);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.StatusBarTransparent));
        }
        init();
    }

    private void init() {

        SharedPreference.getInstance(this);

        toolbar = findViewById(R.id.toolbar);

        THCNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);

        SmSearch = findViewById(R.id.Sm_search);
        SmFilter = findViewById(R.id.Sm_filter);
        SmMFDocketList = findViewById(R.id.Sm_MFDocketList);
        SmBack = findViewById(R.id.Sm_back);

        SmStockUpdate = findViewById(R.id.Sm_StockUpdate);
        SmStockUpdate.setOnClickListener(v -> stockupdate());
        rl_progressbar = findViewById(R.id.rl_progressbar);

        TextView smTHCNo = findViewById(R.id.Sm_THCNo);

        smTHCNo.setText(THCNo);

        RealmResults<ThcDocketList_Tbl> thcDocketListTbls =
                realm.where(ThcDocketList_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("ThcNo", THCNo).findAll();

        mThcDocketListTbls.addAll(realm.copyFromRealm(thcDocketListTbls));

        rviewThcDocketList.addAll(mThcDocketListTbls);
        tempThcDocketList.addAll(mThcDocketListTbls);

        SmMFDocketList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        summaryDocketAdapter = new SummaryDocketAdapter(rviewThcDocketList, this);
        SmMFDocketList.setAdapter(summaryDocketAdapter);

        SmBack.setOnClickListener(v -> finish());

        SmFilter.setOnClickListener(v -> filter());

        SmSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                rviewThcDocketList.clear();

                for (ThcDocketList_Tbl thcDocketListTbl : tempThcDocketList) {
                    if (thcDocketListTbl.getDockNo().contains(s)) {
                        rviewThcDocketList.add(thcDocketListTbl);
                    }
                }
                summaryDocketAdapter.notifyDataSetChanged();
            }
        });
    }

    private void stockupdate() {
        refreshToken(DocketThcSummaryActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(DocketThcSummaryActivity.this);
        builder.setMessage("Do you want to Stock Update?");
        builder.setPositiveButton("Ok", (dialog, id) -> {

                    dialog.cancel();
                    SmStockUpdate.setClickable(false);

                    THCStockUpdate thcStockUpdate = requestTHCStockUpdate();

            RealmResults<docketBarCodeSeries_Tbl> extraPkg = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("LsThcNo", THCNo)
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("isServerPush", false)
                    .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                    .findAll();

            Log.d("TAG", "stockupdate Extra count: "+extraPkg.size());
            if(extraPkg.size() > 0){
                updateExtraBarcode(extraPkg);
            }

                    Log.d("TAG", "stockupdate req: "+new Gson().toJson(thcStockUpdate));
                    try {
                        if(CommonMethod.isNetworkConnected(this)) {
                            rl_progressbar.setVisibility(View.VISIBLE);

                            Call<GetStockUpdate> call = MyRetro.createSecureServiceApp(this, APIService.class)
                                    .getstockUpdate(thcStockUpdate);
                            call.enqueue(new Callback<GetStockUpdate>() {
                                @Override
                                public void onResponse(@NonNull Call<GetStockUpdate> call, @NonNull Response<GetStockUpdate> response) {
                                    Log.d("TAG", "Stock THC Update Response: "+response.code());
                                    if(response.code() == 500){
                                        CommonMethod.showToastMessage(DocketThcSummaryActivity.this,"please try again..");
                                        rl_progressbar.setVisibility(View.GONE);
                                        return;
                                    }else {
                                        rl_progressbar.setVisibility(View.GONE);
                                        SmStockUpdate.setClickable(true);
                                        GetStockUpdate stockUpdate = new GetStockUpdate();
                                        if (response.body() != null) {
                                            stockUpdate = response.body();
                                            if (stockUpdate.getSuccess()) {
                                                rl_progressbar.setVisibility(View.GONE);
                                                realmchange();
                                            }else {
                                                Toast.makeText(DocketThcSummaryActivity.this, stockUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(DocketThcSummaryActivity.this, stockUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<GetStockUpdate> call, @NonNull Throwable t) {

                                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                                    Log.e("TAG", " error : " + t);
                                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                                    SmStockUpdate.setClickable(true);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(DocketThcSummaryActivity.this);
                                    builder.setMessage("Something went wrong.");
                                    builder.setPositiveButton("Ok", (dialog, id) -> {
                                        dialog.cancel();
                                        finish();
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        SmStockUpdate.setClickable(true);
                        CommonMethod.appendLogs(e.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                    }
                });
        builder.setNegativeButton("cancal", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateExtraBarcode(RealmResults<docketBarCodeSeries_Tbl> extraPkg) {
        Log.d("TAG", "Extra Docket THC :" + THCNo);
        ArrayList<docketBarCodeSeries_Tbl> extraList = new ArrayList<>();

        if (extraPkg.size() > 0) {
            extraList.addAll(realm.copyFromRealm(extraPkg));

            ArrayList<ExtraBcSerialListModel> exArrList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> tempList = new ArrayList<>();
            ExtraBcSerialModel model = new ExtraBcSerialModel();

            model.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
            model.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            for (docketBarCodeSeries_Tbl md : extraList) {
                ExtraBcSerialListModel xModel = new ExtraBcSerialListModel();
                xModel.setCompanyCode(md.getCompanyCode());
                xModel.setBCSerialNo(md.getBcSerialNo());
                exArrList.add(xModel);
            }
            model.setList(exArrList);

            Log.d("TAG", "updateExtraBarcode Request: "+new Gson().toJson(model));

            Call<ExtraDocketResponseModel> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .extraBarcodeUpdate(model);
            call.enqueue(new Callback<ExtraDocketResponseModel>() {
                @Override
                public void onResponse(Call<ExtraDocketResponseModel> call, Response<ExtraDocketResponseModel> response) {
                    Log.d("TAG", "check Status Code: " + response.code());
                    ExtraDocketResponseModel responseModel = response.body();
                    ArrayList<ExtraDocketListModel> extraListValue = new ArrayList<>();

                    if(responseModel.getList().size() > 0){
                        Realm realm = Realm.getDefaultInstance();

                        for(ExtraDocketListModel ex : responseModel.getList()){
                            ExtraDocketListModel listModel = new ExtraDocketListModel();
                            listModel.setUserId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                            listModel.setCompanyCode(ex.getCompanyCode());
                            listModel.setDockNo(ex.getDockNo());
                            listModel.setDockSf(ex.getDockSf());
                            listModel.setBcSerialNo(ex.getBcSerialNo());
                            listModel.setOrigin(ex.getOrigin());
                            listModel.setDestination(ex.getDestination());

                            extraListValue.add(listModel);

                        }

                        realm.executeTransaction(r ->{
                            r.insertOrUpdate(extraListValue);
                        });
                    }else {
                        Toast.makeText(DocketThcSummaryActivity.this, "Barcode not found in system", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ExtraDocketResponseModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void realmchange() {

        RealmResults<THCHeader_Tbl> thcHeaderTbls = realm.where(THCHeader_Tbl.class).equalTo("thcNo", THCNo).findAll();
        RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class).equalTo("ThcNo", THCNo).findAll();
        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class).equalTo("LsThcNo", THCNo).findAll();

        realm.executeTransaction(realm -> {
            if (thcHeaderTbls != null)
                thcHeaderTbls.deleteAllFromRealm();
            if (thcDocketListTbls != null)
                thcDocketListTbls.deleteAllFromRealm();
            if (docketBarCodeSeriesTbls != null)
                docketBarCodeSeriesTbls.deleteAllFromRealm();
        });

        InwardTHCListActivity.IsStockUpdate = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(DocketThcSummaryActivity.this);
        builder.setMessage("Thc Updated Successfully");
        builder.setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private THCStockUpdate requestTHCStockUpdate() {

        THCStockUpdate thcStockUpdate = new THCStockUpdate();

        thcStockUpdate.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
        thcStockUpdate.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
        thcStockUpdate.setEmpId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
        thcStockUpdate.setFinYear(CommonMethod.getCurrentYear());
        thcStockUpdate.setIsDEPSFromAndroid("");

        ArrayList<ExtraScannedDkt> extraScannedDkt = new ArrayList<>();

        RealmResults<ExtraDocketListModel> extList = realm.where(ExtraDocketListModel.class)
                .equalTo("userId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .findAll();
        for(ExtraDocketListModel ex : extList){
            ExtraScannedDkt m = new ExtraScannedDkt();
            m.setCompanyCode(ex.getCompanyCode());
            m.setbCSerialNo(ex.getBcSerialNo());
            extraScannedDkt.add(m);
        }
        thcStockUpdate.setExtraScannedDkts(extraScannedDkt);

        ArrayList<ThcStockDetail> thcStockDetails = new ArrayList<>();

        RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("ThcNo", THCNo).findAll();

        for (ThcDocketList_Tbl thcDocketListTbl : thcDocketListTbls) {
            ThcStockDetail thcStockDetail = new ThcStockDetail();

            thcStockDetail.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
            thcStockDetail.settCNO(thcDocketListTbl.getTcNo());
            thcStockDetail.setlSNO("");
            thcStockDetail.setdOCKET(thcDocketListTbl.getDockNo());
            thcStockDetail.setdOCKETSF(thcDocketListTbl.getDockSf());
            thcStockDetail.setpKGSTOBEARRIVED(thcDocketListTbl.getTotalLoadPackages());
            thcStockDetail.setpKGSARRIVED(thcDocketListTbl.getScanPackages());
            thcStockDetail.setwTTOBEARRIVED(thcDocketListTbl.getTotalActualWeight());

            double TotalActualWeight = (thcDocketListTbl.getTotalActualWeight() / thcDocketListTbl.getTotalLoadPackages())
                    * thcDocketListTbl.getScanPackages();

            thcStockDetail.setwTARRIVED(TotalActualWeight);
            thcStockDetail.setaRRVCOND(thcDocketListTbl.getArrivalCode() + "");
            thcStockDetail.setgODOWN(thcDocketListTbl.getGodown());
            thcStockDetail.setdELYDATE(CommonMethod.getCurrentDateStringNew());
            thcStockDetail.setdELYSTATUS("N");//Pending
            thcStockDetail.setdELYTIME(CommonMethod.getCurrentDateStringNew());
            thcStockDetail.setdELY_PROCESS("1");
            thcStockDetail.setdELYREASON("");
            thcStockDetail.setdELYPERSON(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            thcStockDetail.setcODDODAMOUNT(0.0);
            thcStockDetail.setcODDODCOLLECTED(0.0);
            thcStockDetail.setcODDODNO("");
            thcStockDetail.settHCno(THCNo);
            thcStockDetail.setIsDeps("N");
            thcStockDetail.setIsDamage("N");
            thcStockDetail.setDamageQty(0.0);
            thcStockDetail.setDamageReason("");
            thcStockDetail.setDamageFile("");
            thcStockDetail.setDamageScanImageExtension("");
            thcStockDetail.setDamageScanImageName("");
            thcStockDetail.setIsPilfered("N");
            thcStockDetail.setPilferedQty(0.0);
            thcStockDetail.setPilferedReason("");
            thcStockDetail.setPilferedFile("");
            thcStockDetail.setPilferedScanImageExtension("");
            thcStockDetail.setPilferedScanImageName("");
            thcStockDetail.setIsShort("Y");
            thcStockDetail.setShortQty(0.0);
            thcStockDetail.setShortReason("");
            thcStockDetail.setShortFile("");
            thcStockDetail.setShortScanImageExtension("");
            thcStockDetail.setShortScanImageName("");
            thcStockDetail.setIsExtra("N");
            thcStockDetail.setExtraQty(0.0);
            thcStockDetail.setExtraReason("");
            thcStockDetail.setExtraFile("");
            thcStockDetail.setExtraScanImageExtension("");
            thcStockDetail.setExtraScanImageName("");
            thcStockDetail.setbCSerials("");
            thcStockDetail.setsUDate(CommonMethod.getCurrentDateStringNew());

            ArrayList<ThcStockDocketBarCodeSeries> thcStockDocketBarCodeSeries = new ArrayList<>();

            RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("dockNo", thcDocketListTbl.getDockNo()).findAll();

            for (docketBarCodeSeries_Tbl docketBarCodeSeriesTbl : docketBarCodeSeriesTbls) {

                ThcStockDocketBarCodeSeries thcStockDocketBarCode = new ThcStockDocketBarCodeSeries();

                thcStockDocketBarCode.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                thcStockDocketBarCode.setDockNo(thcDocketListTbl.getDockNo());
                thcStockDocketBarCode.setDockSf(thcDocketListTbl.getDockSf());
                thcStockDocketBarCode.setbCSerialNo(docketBarCodeSeriesTbl.getBcSerialNo());
                thcStockDocketBarCode.setbCScannedDatetime(docketBarCodeSeriesTbl.getBcScannedDatetime());
                thcStockDocketBarCode.setBarcodeScanned(docketBarCodeSeriesTbl.getBarcodeScanned());
                thcStockDocketBarCode.setContinueScanned(docketBarCodeSeriesTbl.getContinueScanned());

                thcStockDocketBarCodeSeries.add(thcStockDocketBarCode);
            }
            thcStockDetail.setDocketBarCodeSeries(thcStockDocketBarCodeSeries);

            thcStockDetails.add(thcStockDetail);
        }
        thcStockUpdate.setThcStockDetail(thcStockDetails);

        return thcStockUpdate;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filter() {

        final Dialog dialog = new Dialog(DocketThcSummaryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_layout);

        TextView filterAll = dialog.findViewById(R.id.filter_All);
        TextView filterShort = dialog.findViewById(R.id.filter_Short);

        filterAll.setOnClickListener(v -> {
            tempThcDocketList.clear();
            rviewThcDocketList.clear();
            tempThcDocketList.addAll(mThcDocketListTbls);
            rviewThcDocketList.addAll(tempThcDocketList);
            summaryDocketAdapter.notifyDataSetChanged();

            dialog.dismiss();
            SmSearch.setText("");
        });

        filterShort.setOnClickListener(v -> {
            rviewThcDocketList.clear();
            rviewThcDocketList.addAll(tempThcDocketList);
            summaryDocketAdapter.notifyDataSetChanged();

            dialog.dismiss();
            SmSearch.setText("");
        });

        //Spinner spAlertGoDown
        final AutoCompleteTextView filterDestination = dialog.findViewById(R.id.filter_Destination);

        ArrayList<String> destination = new ArrayList<>();
        rviewThcDocketList.clear();

        for (ThcDocketList_Tbl thcDocketList_tbl : mThcDocketListTbls) {
            if (!destination.contains(thcDocketList_tbl.getDestination())) {
                destination.add(thcDocketList_tbl.getDestination());
            }
        }

        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(DocketThcSummaryActivity.this, android.R.layout.simple_spinner_item, destination);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterDestination.setAdapter(destinationAdapter);

        filterDestination.setOnItemClickListener((parent, view, position, id) -> {

            tempThcDocketList.clear();
            rviewThcDocketList.clear();
            for (ThcDocketList_Tbl thcDocketList_tbl : mThcDocketListTbls) {
                if (destination.get(position).equals(thcDocketList_tbl.getDestination())) {
                    tempThcDocketList.add(thcDocketList_tbl);
                }
            }
            rviewThcDocketList.addAll(tempThcDocketList);
            summaryDocketAdapter.notifyDataSetChanged();

            dialog.dismiss();
            SmSearch.setText("");
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}