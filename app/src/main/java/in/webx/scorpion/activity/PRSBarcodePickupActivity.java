package in.webx.scorpion.activity;

import static device.common.ScanConst.INTENT_EVENT;
import static device.common.ScanConst.INTENT_USERMSG;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import device.common.ScanConst;
import device.sdk.ScanManager;
import in.webx.scorpion.Adapter.BarcodePendingAdapter;
import in.webx.scorpion.Adapter.PRSGenDocketAdapter;
import in.webx.scorpion.Adapter.ScanDcoketListAdapter;
import in.webx.scorpion.Model.GenratePRSModel;
import in.webx.scorpion.Model.PRSBarcodeDetails;
import in.webx.scorpion.Model.PRSDocketDetails;
import in.webx.scorpion.Model.PRSGenHeader;
import in.webx.scorpion.Model.RmPRSBarcodeModel;
import in.webx.scorpion.Model.RmPRSDocketModel;
import in.webx.scorpion.Model.RmPRSHeaderModel;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.ScanResultReceiver;
import in.webx.scorpion.util.SharedPreference;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PRSBarcodePickupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_scan, tv_manually, select, tvGeneratePRS;
    private ColorStateList def;
    private ImageView progressBar;
    public RelativeLayout rl_progressBar;
    private DecoratedBarcodeView dbv_barcode;
    boolean IsContinuousScanning = true;
    private EditText etDocketNo,edt_docketNo;
    private boolean IsHHTDevice, IsHHTNewlandDevice, IsHHTUROVODevice, IsPM85Device, IsPM80Device, IsPM75Device;
    private static ScanManager mScanner;
    private ScanResultReceiver receiver;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private InputMethodManager imm;
    private Button btn_scan;
    private AppCompatTextView mTvPendingPkg, mTvPendingDkt, mTVcomplete_pkg, mTV_complete_dkt, mTVprsSave;
    private LinearLayout mll_prsPending, mll_prscomplete;
    private android.app.AlertDialog mDialog = null;
    private String vehicleno;

    private final ArrayList<PRSDocketDetails> docketList = new ArrayList<>();
    private final ArrayList<PRSBarcodeDetails> barcodeList = new ArrayList<>();
    private final ArrayList<PRSBarcodeDetails> completebarcodeList = new ArrayList<>();
    private final ArrayList<PRSDocketDetails> completedocketList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_prsbarcode_pickup);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        //Define Function
        init();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        checkDeviceType();
        mScanner = new ScanManager();

        SharedPreference.getInstance(this);
        
        tv_scan = findViewById(R.id.tv_scan);
        tv_manually = findViewById(R.id.tv_manually);
        tv_scan.setOnClickListener(this);
        tv_manually.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = tv_manually.getTextColors();
        dbv_barcode = findViewById(R.id.dbv_barcode);
        etDocketNo = findViewById(R.id.etDocketNo);
        btn_scan = findViewById(R.id.btn_scan);
        mTvPendingPkg = findViewById(R.id.tv_pending_pkg);
        mTvPendingDkt = findViewById(R.id.tv_pending_dkt);
        mll_prsPending = findViewById(R.id.ll_prs_pending);
        mll_prscomplete = findViewById(R.id.ll_prs_complete);
        mTV_complete_dkt = findViewById(R.id.tv_complete_dkt);
        mTVcomplete_pkg = findViewById(R.id.tv_complete_pkg);
        mTVprsSave = findViewById(R.id.tv_prs_Save);

        edt_docketNo = findViewById(R.id.edt_docketNo);
        btn_scan = findViewById(R.id.btn_scan);

        ImageView iv_nav_back = findViewById(R.id.iv_nav_back);
        /* ivthcclose = findViewById(R.id.iv_thc_close);*/
        ImageView ivprsrefresh = findViewById(R.id.iv_prs_refresh);

        iv_nav_back.setOnClickListener(v -> thcclose());
        ivprsrefresh.setOnClickListener(v -> refresh());
        btn_scan.setOnClickListener(v -> manuallyscan());

        //Barcode Scan Camera
        dbv_barcode.decodeContinuous(new BarcodeCallback() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void barcodeResult(BarcodeResult result) {
                dbv_barcode.resume();

                scanBarcode(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });

        if (IsContinuousScanning && IsHHTDevice) {
            etDocketNo.requestFocus();
            etDocketNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    String strDocketNo = etDocketNo.getText().toString().trim();
                    if (IsContinuousScanning && !strDocketNo.equals("")) {
                        Log.d("TAG", "afterTextChanged: " + strDocketNo);
                        scanBarcode(strDocketNo);
                        etDocketNo.setText("");
                        etDocketNo.requestFocus();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        etDocketNo.setOnTouchListener((v, event) -> {
            v.onTouchEvent(event);
            if (IsContinuousScanning)
                hideDefaultKeyboard(v);
            return true;
        });

        if (IsHHTDevice || IsHHTNewlandDevice) {
            dbv_barcode.setVisibility(View.GONE);
            dbv_barcode.pause();
        }

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {

                    String barcode = intent.getStringExtra("SCAN_BARCODE1");

                    if (barcode != null) {
                        Log.d("TAG", "get Scan Data: " + barcode);
                        scanBarcode(barcode);
                    } else {

                        Toast.makeText(getApplicationContext(), "Barcode Not getting.", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Scan Failed", Toast.LENGTH_LONG).show();
                }
            }
        }, new IntentFilter("nlscan.action.SCANNER_RESULT"));

        setData();
        bindAdapter();
        mll_prsPending.setOnClickListener(v -> showDocketList(false));

        mll_prscomplete.setOnClickListener(v -> showDocketList(true));

        mTVprsSave.setOnClickListener(v -> {
            RealmResults<PRSDocketDetails> getUpdateData = realm.where(PRSDocketDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("vehicleno", vehicleno)
                    .findAll();
            int scanDocket = 0, scanPackage = 0, totalPackage = 0;

            for (PRSDocketDetails updateSummary : getUpdateData) {
                if (updateSummary.getPackageScanCount() > 0) {
                    scanDocket++;
                    scanPackage += updateSummary.getPackageScanCount();
                }
                totalPackage += updateSummary.getPackages();
            }
            Log.d("TAG", "onClick: "+scanPackage+"/"+totalPackage);
            if(scanPackage == totalPackage){
                mTVprsSave.setClickable(false);
                pickupsummary(scanDocket,getUpdateData.size(),scanPackage,totalPackage);
            }else {
                CommonMethod.showToastMessage(PRSBarcodePickupActivity.this,"Please Scan All Packages");
                mTVprsSave.setClickable(true);
            }

        });
    }

    private void showDocketList(boolean status) {
        Log.d("TAG", "Status: " + status);
        final Dialog dialog = new Dialog(PRSBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thc_pending_docket_list);
        ImageView iv_close_docket = dialog.findViewById(R.id.iv_closePendingDocketList);
        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);
        iv_close_docket.setOnClickListener(v -> dialog.cancel());
        RecyclerView rvpendingDocket = dialog.findViewById(R.id.rv_pendingDocket);

        rvpendingDocket.setLayoutManager(new LinearLayoutManager(PRSBarcodePickupActivity.this, RecyclerView.VERTICAL, false));
        docketList.clear();
        RealmResults<PRSDocketDetails> docketDetails = realm.where(PRSDocketDetails.class)
                .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("vehicleno", vehicleno).findAll();
//            docketList.addAll(realm.copyFromRealm(docketDetails));
        if (!status) {
            tv_header.setText(R.string.label_pending_docket);
            for (PRSDocketDetails pendingDKT : docketDetails) {
                if (pendingDKT.getPackages() != pendingDKT.getPackageScanCount()) {
                    docketList.add(pendingDKT);
                }
            }
        } else {
            tv_header.setText(R.string.label_completed_docket);
            for (PRSDocketDetails pendingDKT : docketDetails) {
                if(pendingDKT.getDocketScanned())
                    docketList.add(pendingDKT);
            }
        }
        PRSGenDocketAdapter docketAdapter = new PRSGenDocketAdapter(docketList, this, status);
        rvpendingDocket.setAdapter(docketAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        int pendingDkt = 0;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            vehicleno = null;
        } else {
            vehicleno = extras.getString("vNo");
        }
        try {
            RealmResults<PRSBarcodeDetails> barcodeDetails = realm.where(PRSBarcodeDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("isScanned", false)
                    .equalTo("vehicleno", vehicleno).findAll();
            barcodeList.addAll(realm.copyFromRealm(barcodeDetails));

            RealmResults<PRSDocketDetails> dktDetails = realm.where(PRSDocketDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("vehicleno", vehicleno).findAll();

            for (PRSDocketDetails details : dktDetails) {
                if (details.getPackages() != details.getPackageScanCount()) {
                    pendingDkt++;
                }
            }

            mTvPendingDkt.setText(pendingDkt + "");
            mTvPendingPkg.setText(String.valueOf(barcodeDetails.size()));

            RealmResults<PRSDocketDetails> completeDocketDetails = realm.where(PRSDocketDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("isDocketScanned", true)
                    .equalTo("vehicleno", vehicleno).findAll();
            completedocketList.addAll(realm.copyFromRealm(completeDocketDetails));

            RealmResults<PRSBarcodeDetails> completebarcodeDetails = realm.where(PRSBarcodeDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("isScanned", true)
                    .equalTo("vehicleno", vehicleno).findAll();
            completebarcodeList.addAll(realm.copyFromRealm(completebarcodeDetails));

            mTVcomplete_pkg.setText(String.valueOf(completebarcodeDetails.size()));
            mTV_complete_dkt.setText(String.valueOf(completeDocketDetails.size()));

        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }

    }

    private void hideDefaultKeyboard(View et) {
        getMethodManager().hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private InputMethodManager getMethodManager() {
        if (this.imm == null) {
            this.imm = (InputMethodManager) this.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        }
        return this.imm;
    }

    private void checkDeviceType() {
        String deviceName = new CommonMethod().getDeviceName();
        IsHHTDevice = deviceName.contains(ConstantData.UROVO)
                || deviceName.contains(ConstantData.Honeywell)
                || deviceName.contains(ConstantData.NEWLAND)
                || deviceName.contains(ConstantData.SCANNER_PM85)
                || deviceName.contains(ConstantData.SCANNER_PM80)
                || deviceName.contains(ConstantData.SCANNER_PM75);

        IsHHTUROVODevice = deviceName.contains(ConstantData.UROVO);
        IsHHTNewlandDevice = deviceName.contains(ConstantData.NEWLAND);
        IsPM85Device = deviceName.contains(ConstantData.SCANNER_PM85);
        IsPM80Device = deviceName.contains(ConstantData.SCANNER_PM80);
        IsPM75Device = deviceName.contains(ConstantData.SCANNER_PM75);
    }

    //Barcode Scan Manually
    private void manuallyscan() {
        String barcode = edt_docketNo.getText().toString().trim();
        scanBarcode(barcode);
    }

    @Override
    public void onClick(View view) {
        LinearLayout ll_scanManually = findViewById(R.id.ll_scanManually);

        if (view.getId() == R.id.tv_scan) {
            select.animate().x(0).setDuration(100);
            tv_scan.setTextColor(Color.WHITE);
            tv_manually.setTextColor(def);
            if (IsHHTDevice) {
                dbv_barcode.setVisibility(View.GONE);
                dbv_barcode.pause();
            } else {
                dbv_barcode.setVisibility(View.VISIBLE);
            }
            ll_scanManually.setVisibility(View.GONE);
        } else if (view.getId() == R.id.tv_manually) {
            tv_scan.setTextColor(def);
            tv_manually.setTextColor(Color.WHITE);
            int size = tv_manually.getWidth();
            select.animate().x(size).setDuration(100);
            if (IsHHTDevice) {
                dbv_barcode.setVisibility(View.GONE);
                dbv_barcode.pause();
            } else {
                dbv_barcode.setVisibility(View.GONE);
            }
            ll_scanManually.setVisibility(View.VISIBLE);
        }
    }

    //Scan Barcode
    private void scanBarcode(String barcode) {
        Log.d("TAG", " barcode : " + barcode);
        if (!IsHHTDevice) {
            new Handler().postDelayed(() -> {
                dbv_barcode.pause();
                dbv_barcode.resume();
            }, 1000);
        }
        if (barcode.length() != 0 && !barcode.equalsIgnoreCase("")) {
            realm.executeTransaction(e -> {
                PRSBarcodeDetails model = realm.where(PRSBarcodeDetails.class)
                        .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("vehicleno", vehicleno)
                        .equalTo("bcSeriesNo", barcode)
                        .findFirst();

                if (model != null) {
                    if (model.getIsScanned()) {
                        beepSound();
                        CommonMethod.showToastMessage(this, "This Barcode is already scanned.");
                    } else {
                        model.setIsScanned(true);
                        realm.insertOrUpdate(model);

                        PRSDocketDetails updateDocket = realm.where(PRSDocketDetails.class)
                                .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                .equalTo("docketNo", model.getDocketNo())
                                .equalTo("vehicleno", model.getVehicleno())
                                .findFirst();
                        if (updateDocket != null) {
                            updateDocket.setPackageScanCount(updateDocket.getPackageScanCount() + 1);
                            updateDocket.setLastScanPackage(barcode);
                            updateDocket.setDocketScanned(true);
                            realm.insertOrUpdate(updateDocket);
                        }

                        if (updateDocket.getDocketScanned()) {
                            setData();
                            bindAdapter();
                        }
                    }
                }
            });
        }
    }

    private void bindAdapter() {
        RecyclerView mRVScannedDocket = findViewById(R.id.rv_scannedDocket);

        RealmResults<PRSDocketDetails> scanDockets = realm.where(PRSDocketDetails.class)
                .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isDocketScanned", true).findAll();
        ArrayList<PRSDocketDetails> scanDocketList = new ArrayList<>(realm.copyFromRealm(scanDockets));

        mRVScannedDocket.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ScanDcoketListAdapter adapter = new ScanDcoketListAdapter(this, scanDocketList);
        mRVScannedDocket.setAdapter(adapter);
    }

    private void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public void showPendingBarcode(String docketNo, String vNumber, boolean status) {
        Log.d("TAG", "showPendingBarcode :" + docketNo + " | " + vNumber);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thc_pending_barcode_list);

        ImageView iv_closePendingBarcodeList = dialog.findViewById(R.id.iv_closePendingBarcodeList);
        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);

        iv_closePendingBarcodeList.setOnClickListener(v1 -> dialog.cancel());

        RecyclerView rv_pendingBarcode = dialog.findViewById(R.id.rv_pendingBarcode);
        Log.d("TAG", "showPendingBarcode: " + status);
        if (!status) {
            tv_header.setText(R.string.label_pending_barcode);
        } else {
            tv_header.setText(R.string.label_completed_barcode);
        }
        RealmResults<PRSBarcodeDetails> pendingBarcode = realm.where(PRSBarcodeDetails.class)
                .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("docketNo", docketNo)
                .equalTo("isScanned", status)
                .equalTo("vehicleno", vNumber).findAll();

        ArrayList<PRSBarcodeDetails> barcodeList = new ArrayList<>();
        barcodeList.addAll(realm.copyFromRealm(pendingBarcode));

        rv_pendingBarcode.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        BarcodePendingAdapter adapter = new BarcodePendingAdapter(barcodeList, this);
        rv_pendingBarcode.setAdapter(adapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    //onCLick Close
    private void thcclose() {
        finish();
    }

    //onCLick Refresh
    private void refresh() {
        startActivity(getIntent());
        finish();
    }

    //onResume
    public void onResume() {
        super.onResume();
        if (IsHHTUROVODevice || IsHHTNewlandDevice) {
            initScanner();
        } else if (IsPM80Device || IsPM85Device || IsPM75Device) {
            if (mScanner.aDecodeGetDecodeEnable() == 1 || mScanner.aDecodeGetDecodeEnable() == 0) {
                if (getEnableDialog().isShowing()) {
                    getEnableDialog().dismiss();
                }
                initScanner();
            } else {
                if (!getEnableDialog().isShowing()) {
                    getEnableDialog().show();
                }
            }
        } else {
            dbv_barcode.resume();
        }
    }

    private android.app.AlertDialog getEnableDialog() {
        if (mDialog == null) {
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.app_name);
            dialog.setMessage("Your scanner is disabled.Do you want to enable it?");

            dialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel),
                    (dialog1, which) -> finish());
            dialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                    (dialog12, which) -> {
                        Intent intent = new Intent(ScanConst.LAUNCH_SCAN_SETTING_ACITON);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog12.dismiss();
                    });
            dialog.setCancelable(false);
            mDialog = dialog;
        }
        return mDialog;
    }

    private void initScanner() {

        if (mScanner != null) {
            mBackupResultType = mScanner.aDecodeGetResultType();
            mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_USERMSG);
        filter.addAction(INTENT_EVENT);

        receiver = new ScanResultReceiver();
        registerReceiver(receiver, filter);
    }

    //onPause
    @Override
    public void onPause() {
        super.onPause();
        try {
            if (receiver != null)
                unregisterReceiver(receiver);

            if (IsHHTDevice) {
                if (mScanner != null) {
                    mScanner.aDecodeSetResultType(mBackupResultType);
                }
            } else {
                dbv_barcode.pause();
            }
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));

        }
    }

    @SuppressLint("SetTextI18n")
    private void pickupsummary(int scanDocket, int totalDocket, int scanPackage, int totalPackage) {
        final Dialog dialog = new Dialog(PRSBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pickup_gen_summary);
        AppCompatTextView mTVDockets, mTVPackages, mTVvehicleno,mTvGendate
                ,mTvBookedAt,mTvDriverName,mTvVehNo,mTvVehType,mTvWeight,mTvCapacity,mTvVendorName,mTvPersonName;
        EditText mEdtEndKm,mEdtStartKm;

        tvGeneratePRS = dialog.findViewById(R.id.tvGeneratePRS);
        mTVDockets = dialog.findViewById(R.id.txt_Docket);
        mTVPackages = dialog.findViewById(R.id.txt_Packages);
        mTVvehicleno = dialog.findViewById(R.id.txt_vehicleno);
        mTvGendate = dialog.findViewById(R.id.txt_gendate);
        mTvBookedAt = dialog.findViewById(R.id.txt_bookedAt);
        mTvDriverName = dialog.findViewById(R.id.txt_driverName);
        mTvVehNo = dialog.findViewById(R.id.txt_vehNo);
        mTvVehType = dialog.findViewById(R.id.txt_vehType);
//        mTvOdometerRead = dialog.findViewById(R.id.txt_odometerRead);
        mTvWeight = dialog.findViewById(R.id.txt_weight);
        mTvCapacity = dialog.findViewById(R.id.txt_capacity);
        mTvVendorName = dialog.findViewById(R.id.txt_vendorName);
        mTvPersonName = dialog.findViewById(R.id.txt_personName);

        rl_progressBar = dialog.findViewById(R.id.rl_progressbar);

        mEdtStartKm = dialog.findViewById(R.id.edt_startkm);
        mEdtEndKm = dialog.findViewById(R.id.edt_endtkm);

        Log.d("TAG", "Scan Dockets: " + scanDocket + " / " + totalDocket);

        mTVDockets.setText(scanDocket + " / " + totalDocket);
        mTVPackages.setText(scanPackage + " / " + totalPackage);

        PRSGenHeader genHeader = realm.where(PRSGenHeader.class)
                .equalTo("vehicleno", vehicleno).findFirst();

        if(genHeader.getVehicleno().length() != 0 || !genHeader.getVehicleno().equalsIgnoreCase("")){
            mTVvehicleno.setText(genHeader.getVehicleno());
        }

        mTvGendate.setText(CommonMethod.getCurrentDateTime());
        mTvBookedAt.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
        mTvDriverName.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
        mTvVehNo.setText(genHeader.getVehicleno());
        mTvVehType.setText(genHeader.getVehicletype());
        mTvWeight.setText(genHeader.getTotalWeight());
        mTvCapacity.setText(genHeader.getCapacityutilization());
        mTvVendorName.setText(genHeader.getVendorname());
        mTvPersonName.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));

        tvGeneratePRS.setOnClickListener(v -> {
            tvGeneratePRS.setClickable(false);
            int stkm = 0,enkm = 0;
            if(!mEdtStartKm.getText().toString().isEmpty() || !mEdtStartKm.getText().toString().equalsIgnoreCase("")){
                Log.d("TAG", "Start Km: "+mEdtStartKm.getText().toString());
                stkm = Integer.parseInt(mEdtStartKm.getText().toString());
            }else {
                tvGeneratePRS.setClickable(true);
                mEdtStartKm.requestFocus();
                CommonMethod.showToastMessage(PRSBarcodePickupActivity.this,"Enter Starting kilometer");
                return;
            }

            if(!mEdtEndKm.getText().toString().isEmpty() || !mEdtEndKm.getText().toString().equalsIgnoreCase("")){
                Log.d("TAG", "End Km: "+mEdtEndKm.getText().toString());
                enkm = Integer.parseInt(mEdtEndKm.getText().toString());
            }else {
                tvGeneratePRS.setClickable(true);
                mEdtEndKm.requestFocus();
                CommonMethod.showToastMessage(PRSBarcodePickupActivity.this,"Enter Ending kilometer");
                return;
            }

            if(stkm != 0 && enkm != 0){
                if(stkm < enkm){
                    generatePRS(genHeader,mEdtStartKm.getText().toString(),mEdtEndKm.getText().toString());
                }else {
                    tvGeneratePRS.setClickable(true);
                    CommonMethod.showToastMessage(PRSBarcodePickupActivity.this,"Enter Valid End Kilometers");
                }
            }else {
                tvGeneratePRS.setClickable(true);
                CommonMethod.showToastMessage(PRSBarcodePickupActivity.this,"Enter Starting Kilometers greater than 0");
            }
        });
        ImageView iv_close_docket = dialog.findViewById(R.id.iv_closePendingDocketList);

        iv_close_docket.setOnClickListener(v -> {
            mTVprsSave.setClickable(true);
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void generatePRS(PRSGenHeader genHeader,String startKm,String endKm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PRSBarcodePickupActivity.this);
        builder.setMessage("Do you want to Generate PRS?");
        builder.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    dialog.cancel();
                    tvGeneratePRS.setClickable(false);

                    progressBar = findViewById(R.id.progressbar);

                    Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);
                    rl_progressBar.setVisibility(View.VISIBLE);

                    RmPRSHeaderModel prsGenRequest = genratePRSRequest(genHeader,startKm,endKm);

                    try {
                        if(CommonMethod.isNetworkConnected(this)) {
                            Call<GenratePRSModel> call = MyRetro.createSecureServiceApp(this, APIService.class)
                                    .getPRSgenration(prsGenRequest);
                            call.enqueue(new Callback<GenratePRSModel>() {
                                @Override
                                public void onResponse(@NonNull Call<GenratePRSModel> call, @NonNull Response<GenratePRSModel> response) {
                                    tvGeneratePRS.setClickable(true);
                                    rl_progressBar.setVisibility(View.GONE);
                                    if (response.body() != null) {
                                        GenratePRSModel prsModel = response.body();
                                        if (prsModel.getIsSuccess()) {
                                            new SweetAlertDialog(
                                                    PRSBarcodePickupActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText(prsModel.getPrsNo())
                                                    .setContentText(prsModel.getMessage())
                                                    .setConfirmClickListener(sweetAlertDialog -> {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                        truncateAllData();
                                                    }).show();
                                        } else {
                                            new SweetAlertDialog(
                                                    PRSBarcodePickupActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText(prsModel.getMessage()).show();
                                        }
                                    } else {
                                        Toast.makeText(PRSBarcodePickupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<GenratePRSModel> call, @NonNull Throwable t) {
                                    rl_progressBar.setVisibility(View.GONE);
                                    tvGeneratePRS.setClickable(true);
                                    CommonMethod.appendLogs("--- error ---" + t.getMessage());
                                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PRSBarcodePickupActivity.this);
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
                        tvGeneratePRS.setClickable(true);
                        CommonMethod.appendLogs("--- error ---"+e.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                    }
                });
        builder.setNegativeButton("cancal", (dialog, which) -> {
                    tvGeneratePRS.setClickable(true);
                    dialog.cancel();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void truncateAllData() {
        RealmResults<PRSGenHeader> hData = realm.where(PRSGenHeader.class).equalTo("vehicleno", vehicleno).findAll();
        RealmResults<PRSDocketDetails> dData = realm.where(PRSDocketDetails.class).equalTo("vehicleno", vehicleno).findAll();
        RealmResults<PRSBarcodeDetails> bData = realm.where(PRSBarcodeDetails.class).equalTo("vehicleno", vehicleno).findAll();
        realm.executeTransaction(r ->{
            if(hData != null)
                hData.deleteAllFromRealm();
            if (dData != null)
                dData.deleteAllFromRealm();
            if(bData != null)
                bData.deleteAllFromRealm();
        });
        Intent intent = new Intent(PRSBarcodePickupActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private RmPRSHeaderModel genratePRSRequest(PRSGenHeader genHeader,String startKm,String endKm) {

        RmPRSHeaderModel hModel = new RmPRSHeaderModel();
        try{
            hModel.setCompanyCode(genHeader.getCompanyCode());
            hModel.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            hModel.setEngineNo(genHeader.getEngineNo());
            hModel.setCurrentBranch(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            hModel.setIsSuccess(genHeader.getIsSuccess());
            hModel.setMessage(genHeader.getMessage());
            hModel.setVehicleno(genHeader.getVehicleno());
            hModel.setVehicletype(genHeader.getVehicletype());
            hModel.setVehicleTons(genHeader.getVehicleTons());
            hModel.setFtlype(genHeader.getFtlype());
            if(genHeader.getWeightLoaded().equalsIgnoreCase("")){
                hModel.setWeightLoaded("0");
            }else {
                hModel.setWeightLoaded(genHeader.getWeightLoaded());
            }
            hModel.setEhengineno(genHeader.getEhengineno());
            hModel.setChasisno(genHeader.getChasisno());
            hModel.setRcbkno(genHeader.getRcbkno());
            hModel.setVehicleregdt(genHeader.getVehicleregdt());
            hModel.setVehiclepermitdt(genHeader.getVehiclepermitdt());
            hModel.setInsurancevaliditydt(genHeader.getInsurancevaliditydt());
            hModel.setFitnessvaliditydt(genHeader.getFitnessvaliditydt());
            hModel.setVendorcode(genHeader.getVendorcode());
            hModel.setVendortype(genHeader.getVendortype());
            hModel.setVendorname(genHeader.getVendorname());
            hModel.setDriver1Licence(genHeader.getDriver1Licence());
            hModel.setDrivername(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            hModel.setDrivermobileno(genHeader.getDrivermobileno());
            hModel.setLicenseno(genHeader.getLicenseno());
            hModel.setIssuebyrto(genHeader.getIssuebyrto());
            hModel.setLicenseveldt(genHeader.getLicenseveldt());
            hModel.setPrstype(genHeader.getPrstype());
            hModel.setPrsDate(CommonMethod.getCurrentDateTime());
            hModel.setFinancialYear(genHeader.getFinancialYear());
            hModel.setTotalWeight(genHeader.getTotalWeight());
            hModel.setCapacityutilization(genHeader.getCapacityutilization());

            if(genHeader.getFinancialDetail().equalsIgnoreCase(null)
            || genHeader.getFinancialDetail().equalsIgnoreCase("null")){
                hModel.setFinancialDetail("");
            }else {
                hModel.setFinancialDetail(genHeader.getFinancialDetail());
            }

            hModel.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            hModel.setManualPrsNo(genHeader.getManualPrsNo());
            hModel.setMarketVehicle(genHeader.getMarketVehicle());
            hModel.setPrsTime(genHeader.getPrsTime());
            hModel.setRcBookNo(genHeader.getRcBookNo());
            hModel.setIsOverLoad(genHeader.getIsOverLoad());
            hModel.setStartKM(startKm);
            hModel.setEndKM(endKm);
            RealmResults<PRSDocketDetails> getUpdateData = realm.where(PRSDocketDetails.class)
                    .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("vehicleno", vehicleno).findAll();

            ArrayList<RmPRSDocketModel> dList = new ArrayList<>();
            for (PRSDocketDetails updateSummary : getUpdateData) {
                RmPRSDocketModel dModel = new RmPRSDocketModel();
                dModel.setDocketNo(updateSummary.getDocketNo());
                dModel.setCustomerRefNo(updateSummary.getCustomerRefNo());
                dModel.setDocketSufix(updateSummary.getDocketSufix());
                dModel.setPaybas(updateSummary.getPaybas());
                dModel.setOrigin(updateSummary.getOrigin());
                dModel.setDestination(updateSummary.getDestination());
                dModel.setTotalPendingPackages(updateSummary.getTotalPendingPackages());
                dModel.setTotalArrivedPackages(updateSummary.getTotalArrivedPackages());
                dModel.setTotalArrivedWeight(updateSummary.getTotalArrivedWeight());
                dModel.setChargedWeight(updateSummary.getPackages());
                dModel.setPackages((double) updateSummary.getPackageScanCount());
                dModel.setCewbNo(updateSummary.getCewbNo());
                dModel.setContractAmount(updateSummary.getContractAmount());
                dModel.setContractId(updateSummary.getContractId());
                dModel.setChargeRate(updateSummary.getChargeRate());
                dModel.setRateType(updateSummary.getRateType());
                dModel.setRemark(updateSummary.getRemark());
                dModel.setMinCharge(updateSummary.getMinCharge());
                dModel.setMaxCharge(updateSummary.getMaxCharge());
                dModel.setScannedPackages(String.valueOf(updateSummary.getPackageScanCount()));
                dModel.setVehicleno(updateSummary.getVehicleno());

                ArrayList<RmPRSBarcodeModel> bList = new ArrayList<>();

                RealmResults<PRSBarcodeDetails> barcodeDetails = realm.where(PRSBarcodeDetails.class)
                        .equalTo("UserId", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("docketNo", dModel.getDocketNo())
                        .equalTo("vehicleno", vehicleno).findAll();

                for(PRSBarcodeDetails barcodeData:barcodeDetails){
                    RmPRSBarcodeModel bModel = new RmPRSBarcodeModel();
                    bModel.setVehicleno(barcodeData.getVehicleno());
                    bModel.setBcSeriesNo(barcodeData.getBcSeriesNo());
                    bModel.setDocketNo(barcodeData.getDocketNo());
                    bModel.setIsScanned(barcodeData.getIsScanned());
                    bList.add(bModel);
                }
                dModel.setPRS_BCSeries(bList);
                dList.add(dModel);
            }
            hModel.setDocketDetails(dList);
        }catch (Exception e){
            Log.d("TAG", "genratePRSRequest: "+e.getMessage());
        }
        Log.d("TAG", "genratePRSRequest hModel: "+new Gson().toJson(hModel));
        return hModel;
    }
}