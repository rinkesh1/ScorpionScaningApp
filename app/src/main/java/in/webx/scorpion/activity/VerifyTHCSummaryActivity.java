package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Adapter.DocketListAdapter;
import in.webx.scorpion.Model.ExtraScannedDkt;
import in.webx.scorpion.Model.GetStockUpdate;
import in.webx.scorpion.Model.THCStockUpdate;
import in.webx.scorpion.Model.ThcStockDetail;
import in.webx.scorpion.Model.ThcStockDocketBarCodeSeries;
import in.webx.scorpion.Model.VerifyDocketitemModel;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
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

public class VerifyTHCSummaryActivity extends AppCompatActivity {

    private MaterialTextView mTvExtraVerify, mTvDamageVerify, mTvSortVerify, mTvScanVerify;
    private TextView mTvheader;
    private RecyclerView mRvSort;
    private EditText mEtSearch;
    private ImageButton mBtclear;
    private DocketListAdapter mAdapter;
    private ImageView ivBackPress;
    private String THCNo;
    public Toolbar toolbar;
    private ImageView SmBack;
    private TextView SmStockUpdate;
    private RelativeLayout rl_progressbar;
    String damage,Extra,shortVale;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_verify_thcsummary);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.StatusBarTransparent));
        }
        init();
    }

    public void init() {
        SharedPreference.getInstance(this);
        toolbar = findViewById(R.id.toolbar);
        THCNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);
        SmBack = findViewById(R.id.Sm_back);
        SmStockUpdate = findViewById(R.id.Sm_StockUpdate);
        SmStockUpdate.setOnClickListener(v -> stockupdate());
        rl_progressbar = findViewById(R.id.rl_progressbar);

        mTvSortVerify = findViewById(R.id.txt_SortVerify);
        mTvDamageVerify = findViewById(R.id.txt_damageVerify);
        mTvExtraVerify = findViewById(R.id.txt_ExtraVerify);
        mTvScanVerify = findViewById(R.id.txt_ScanVerify);
        backpress();
        long extraPkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("LsThcNo", SharedPreference.getStringValue(ConstantData.SP_THC_NO))
                .equalTo("isBarcodeScanned", true)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .count();

        long damagePkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", SharedPreference.getStringValue(ConstantData.SP_THC_NO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("barcodeType", ConstantData.SP_KEY_DAMAGE)
                .count();

        long sortPkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", SharedPreference.getStringValue(ConstantData.SP_THC_NO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("barcodeType", ConstantData.SP_KEY_INWARD)
                .equalTo("isBarcodeScanned", false)
                .count();

        long scanPkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", SharedPreference.getStringValue(ConstantData.SP_THC_NO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("barcodeType", ConstantData.SP_KEY_INWARD)
                .equalTo("isBarcodeScanned", true)
                .count();

//        Log.d("TAG", "Damage: " + damagePkgCount);
//        Log.d("TAG", "sort: " + sortPkgCount);
//        Log.d("TAG", "Extra: " + extraPkgCount);
//        Log.d("TAG", "Scanned: " + scanPkgCount);

        mTvSortVerify.setPaintFlags(mTvSortVerify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTvDamageVerify.setPaintFlags(mTvDamageVerify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTvExtraVerify.setPaintFlags(mTvExtraVerify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mTvScanVerify.setPaintFlags(mTvScanVerify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mTvSortVerify.setText(sortPkgCount+"");
        mTvDamageVerify.setText(damagePkgCount+"");
        mTvExtraVerify.setText(extraPkgCount+"");
        mTvScanVerify.setText(scanPkgCount+"");

        ArrayList<VerifyDocketitemModel> sortList = new ArrayList<>();
        ArrayList<VerifyDocketitemModel> extraList = new ArrayList<>();
        ArrayList<VerifyDocketitemModel> damagetList = new ArrayList<>();
        ArrayList<VerifyDocketitemModel> scanList = new ArrayList<>();

        RealmResults<docketBarCodeSeries_Tbl> rmBarcodeList =
                realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("LsThcNo", SharedPreference.getStringValue(ConstantData.SP_THC_NO))
                        .findAll();

        ArrayList<docketBarCodeSeries_Tbl> barcodeList = new ArrayList<>(rmBarcodeList);
        for(docketBarCodeSeries_Tbl b : barcodeList){
            VerifyDocketitemModel model = new VerifyDocketitemModel();
            if(b.getBarcodeType().equalsIgnoreCase("Damage")){
                model.setBcSerialNo(b.getBcSerialNo());
                model.setDockNo(b.getDockNo());
                damagetList.add(model);
            }else if(b.getBarcodeType().equalsIgnoreCase(ConstantData.SP_KEY_EXTRA)){
                model.setBcSerialNo(b.getBcSerialNo());
                model.setDockNo(b.getDockNo());
                extraList.add(model);
            }else if(b.getBarcodeType().equalsIgnoreCase(ConstantData.SP_KEY_INWARD) && b.getBarcodeScanned()){
                model.setBcSerialNo(b.getBcSerialNo());
                model.setDockNo(b.getDockNo());
                scanList.add(model);
            }else if(!b.getBarcodeScanned()){
                model.setBcSerialNo(b.getBcSerialNo());
                model.setDockNo(b.getDockNo());
                sortList.add(model);
            }
        }
//        Log.d("TAG", "Damage List: " + damagetList.size());
//        Log.d("TAG", "sort List: " + sortList.size());
//        Log.d("TAG", "Extra List: " + extraList.size());

        mTvSortVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG", "Short Verify");
                showSortDialog("Short Package",sortList);
            }
        });
        mTvDamageVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG", "Damage Verify");
                showSortDialog("Damage Package",damagetList);
            }
        });
        mTvExtraVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG", "Extra Verify :"+extraList);
                showSortDialog("Extra Package",extraList);
            }
        });
        mTvScanVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("TAG", "scaned Verify");
                showSortDialog("Scan Package",scanList);
            }
        });
    }

    private void stockupdate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyTHCSummaryActivity.this);
        builder.setMessage("Do you want to Stock Update?");
        builder.setPositiveButton("Ok", (dialog, id) -> {

            dialog.cancel();
            SmStockUpdate.setClickable(false);

            THCStockUpdate thcStockUpdate = requestTHCStockUpdate();;

//            Log.d("TAG", "stockupdate req: "+new Gson().toJson(thcStockUpdate));
            try {
                if(CommonMethod.isNetworkConnected(this)) {
                    rl_progressbar.setVisibility(View.VISIBLE);

                    Call<GetStockUpdate> call = MyRetro.createSecureServiceApp(this, APIService.class)
                            .getstockUpdate(thcStockUpdate);
                    call.enqueue(new Callback<GetStockUpdate>() {
                        @Override
                        public void onResponse(@NonNull Call<GetStockUpdate> call, @NonNull Response<GetStockUpdate> response) {
//                            Log.d("TAG", "Stock THC Update Response: "+response.code());
                            if(response.code() == 500){
                                CommonMethod.showToastMessage(VerifyTHCSummaryActivity.this,"please try again..");
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
                                        Toast.makeText(VerifyTHCSummaryActivity.this, stockUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(VerifyTHCSummaryActivity.this, stockUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<GetStockUpdate> call, @NonNull Throwable t) {

                            CommonMethod.appendLogs("--- error --- : " + t.getMessage());
//                            Log.e("TAG", " error : " + t);
                            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                            SmStockUpdate.setClickable(true);

                            AlertDialog.Builder builder = new AlertDialog.Builder(VerifyTHCSummaryActivity.this);
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
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        });
        builder.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void realmchange() {

        RealmResults<THCHeader_Tbl> thcHeaderTbls = realm.where(THCHeader_Tbl.class).equalTo("thcNo", THCNo).findAll();
        RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class).equalTo("ThcNo", THCNo).findAll();
        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class).equalTo("LsThcNo", THCNo).findAll();

        /*RealmResults<ExtraDocketListModel> extList = realm.where(ExtraDocketListModel.class)
                .equalTo("userId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .findAll();*/

        realm.executeTransaction(realm -> {
            if (thcHeaderTbls != null)
                thcHeaderTbls.deleteAllFromRealm();
            if (thcDocketListTbls != null)
                thcDocketListTbls.deleteAllFromRealm();
            if (docketBarCodeSeriesTbls != null)
                docketBarCodeSeriesTbls.deleteAllFromRealm();
            /*if(extList != null)
                extList.deleteAllFromRealm();*/
        });

        InwardTHCListActivity.IsStockUpdate = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyTHCSummaryActivity.this);
        builder.setMessage("THC Updated Successfully");
        builder.setPositiveButton("Ok", (dialog, id) -> {
            dialog.cancel();
            finish();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private THCStockUpdate requestTHCStockUpdate() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String getRam = String.valueOf(getMemorySizeInBytes());

        THCStockUpdate thcStockUpdate = new THCStockUpdate();

        thcStockUpdate.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
        thcStockUpdate.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
        thcStockUpdate.setEmpId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
        thcStockUpdate.setFinYear(CommonMethod.getCurrentYear());
        thcStockUpdate.setIsDEPSFromAndroid("");
        thcStockUpdate.setAppVersion(CommonMethod.getAppVersionName(this));
        thcStockUpdate.setDeviceId(CommonMethod.getDeviceId(this));
        thcStockUpdate.setDeviceStorage(getRam);
        thcStockUpdate.setScanLocation(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
        thcStockUpdate.setIMEINo("");
        thcStockUpdate.setModelName(Build.MODEL);

        Log.d("TAG", "Extra PKG THCNo: "+THCNo);
//        Log.d("TAG", "Extra PKG Extra: "+ConstantData.SP_KEY_EXTRA);

        ArrayList<ExtraScannedDkt> extraScannedDkt = new ArrayList<>();
        RealmResults<docketBarCodeSeries_Tbl> extList = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("LsThcNo", THCNo)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .findAll();

        Log.d("TAG", "Extra PKG Req: "+extList.size());

        for(docketBarCodeSeries_Tbl ex : extList){
            ExtraScannedDkt m = new ExtraScannedDkt();
            m.setCompanyCode(ex.getCompanyCode());
            m.setbCSerialNo(ex.getBcSerialNo());
            m.setDockNo(ex.getDockNo());
            m.setDockSf(ex.getDockSf());
            m.setPackages(1);
            m.setWeight(1.0);

            extraScannedDkt.add(m);
        }
        thcStockUpdate.setExtraScannedDkts(extraScannedDkt);

        ArrayList<ThcStockDetail> thcStockDetails = new ArrayList<>();

        RealmResults<ThcDocketList_Tbl> thcDocketListTbls = realm.where(ThcDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("ThcNo", THCNo).findAll();

        for (ThcDocketList_Tbl thcDocketListTbl : thcDocketListTbls) {
            long damegeQty = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("LsThcNo", THCNo)
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("dockNo", thcDocketListTbl.getDockNo())
                    .equalTo("barcodeType", ConstantData.SP_KEY_DAMAGE)
                    .count();
            long ExtraQty = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("LsThcNo", THCNo)
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("dockNo", thcDocketListTbl.getDockNo())
                    .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                    .count();
            long shortQty = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("LsThcNo", THCNo)
                    .equalTo("isBarcodeScanned", false)
                    .equalTo("dockNo", thcDocketListTbl.getDockNo())
                    .equalTo("barcodeType", ConstantData.SP_KEY_INWARD)
                    .count();

//            Log.d("TAG", "Docket No: "+thcDocketListTbl.getDockNo());
//            Log.d("TAG", "damegeQty: "+damegeQty);
//            Log.d("TAG", "ExtraQty: "+ExtraQty);
            Log.d("TAG", "shortQty: "+shortQty);

            if(damegeQty == 0){
                damage = "N";
            }else {
                damage = "Y";
            }

            if(ExtraQty == 0){
                Extra = "N";
            }else {
                Extra = "Y";
            }

            if(shortQty == 0){
                shortVale = "N";
            }else{
                shortVale = "Y";
            }

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
            thcStockDetail.setIsDamage(damage);
            thcStockDetail.setDamageQty(Double.valueOf(damegeQty));
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
            thcStockDetail.setIsShort(shortVale);
            thcStockDetail.setShortQty(Double.valueOf(shortQty));
            thcStockDetail.setShortReason("");
            thcStockDetail.setShortFile("");
            thcStockDetail.setShortScanImageExtension("");
            thcStockDetail.setShortScanImageName("");
            thcStockDetail.setIsExtra(Extra);
            thcStockDetail.setExtraQty(Double.valueOf(ExtraQty));
            thcStockDetail.setExtraReason("");
            thcStockDetail.setExtraFile("");
            thcStockDetail.setExtraScanImageExtension("");
            thcStockDetail.setExtraScanImageName("");
            thcStockDetail.setbCSerials("");
            thcStockDetail.setsUDate(CommonMethod.getCurrentDateStringNew());

            ArrayList<ThcStockDocketBarCodeSeries> thcStockDocketBarCodeSeries = new ArrayList<>();

            /*RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class)
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
            }*/
            thcStockDetail.setDocketBarCodeSeries(thcStockDocketBarCodeSeries);

            thcStockDetails.add(thcStockDetail);
        }
        thcStockUpdate.setThcStockDetail(thcStockDetails);

        return thcStockUpdate;
    }

    public double getMemorySizeInBytes() {
        Context context = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        //long totalMemory = memoryInfo.totalMem;

        double totalMemory = memoryInfo.totalMem / 1073741824.0;
        return totalMemory;
    }

    private void backpress() {
        ivBackPress = findViewById(R.id.ivBackPress);
        ivBackPress.setOnClickListener(v -> finish());
    }

    private void showSortDialog(String check,ArrayList<VerifyDocketitemModel> sortList) {
        Log.d("TAG", "check String: "+check);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mTvheader = dialog.findViewById(R.id.tv_header);
        mRvSort = dialog.findViewById(R.id.rv_sort);
        mEtSearch = dialog.findViewById(R.id.et_search);
        mBtclear = dialog.findViewById(R.id.bt_clear);
        mTvheader.setText(check);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(),sortList);
            }
        });
        mBtclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });

        mRvSort.setLayoutManager(new LinearLayoutManager(VerifyTHCSummaryActivity.this, RecyclerView.VERTICAL, false));
        mAdapter = new DocketListAdapter(VerifyTHCSummaryActivity.this,sortList,check);
        mRvSort.setAdapter(mAdapter);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void filter(String barcode, ArrayList<VerifyDocketitemModel> sortList) {
        ArrayList<VerifyDocketitemModel> filteredList = new ArrayList<>();
        for(VerifyDocketitemModel item:sortList){
            if(item.getBcSerialNo().toLowerCase().contains(barcode.toLowerCase())){
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

}