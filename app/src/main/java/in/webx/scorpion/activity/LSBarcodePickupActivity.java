package in.webx.scorpion.activity;

import static device.common.ScanConst.INTENT_EVENT;
import static device.common.ScanConst.INTENT_USERMSG;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;
import static in.webx.scorpion.Retrofit.AuthHelper.refreshToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.telephony.TelephonyManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.karumi.dexter.BuildConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import device.common.ScanConst;
import device.sdk.ScanManager;
import in.webx.scorpion.Adapter.LsBarcodeListAdapter;
import in.webx.scorpion.Adapter.LsDocketListAdapter;
import in.webx.scorpion.Adapter.RVScanListAdapter;
import in.webx.scorpion.Model.DocketBarCodeSeries;
import in.webx.scorpion.Model.DocketList;
import in.webx.scorpion.Model.DocketUpdateScan;
import in.webx.scorpion.Model.ExtraBcSerialListModel;
import in.webx.scorpion.Model.ExtraBcSerialModel;
import in.webx.scorpion.Model.LS_headerRequest;
import in.webx.scorpion.Model.LS_headerResponse;
import in.webx.scorpion.Model.ManifestRequestModel;
import in.webx.scorpion.Model.ScanDocketDeviceDetails;
import in.webx.scorpion.Model.ScanDocketResponse;
import in.webx.scorpion.Model.TempDataStore;
import in.webx.scorpion.Model.tchdr;
import in.webx.scorpion.Model.tctrn;
import in.webx.scorpion.Model.wtd;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.LsHader_Tbl;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.RealmDatabase.docketDetailList_Tbl;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.Services.BarcodeHoneyListener;
import in.webx.scorpion.Services.BarcodeListener;
import in.webx.scorpion.Services.BarcodeReceiver;
import in.webx.scorpion.Services.HoneywellBarcodeReceiver;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.ScanResultReceiver;
import in.webx.scorpion.util.SharedPreference;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LSBarcodePickupActivity extends AppCompatActivity implements View.OnClickListener , BarcodeListener {

    private TextView tvScan, tvManually, select, tvCompletePkg, tvCompleteDkt, mTvExtrapkg;
    private ColorStateList def;
    private EditText edt_docketNo, etDocketNo;
    private boolean isContinueScanned = false;
    private DecoratedBarcodeView dbv_barcode;
    private InputMethodManager imm;
    private android.app.AlertDialog mDialog = null;

    ImageView progressBar, iv_close, iv_refresh;
    RelativeLayout rl_progressBar;
    RecyclerView rvScannedDocket;
    double getRam;
    String getModelNumber, deviceVersionNo, brand, appVersion;
    private BarcodeReceiver barcodeReceiver;
    private HoneywellBarcodeReceiver honeywellReceiver;
    String lsNo;
    int finalScanPKG = 0;
    private AppCompatTextView mTVExtraPkg;
    View view;

    private boolean IsHHTDevice, IsHHTUROVODevice, IsHHTNewlandDevice, IsPM85Device,
            IsPM80Device, IsPM75Device,IsHoneyWell;
    private static ScanManager mScanner;
    private ScanResultReceiver receiver;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    boolean IsContinuousScanning = true;
    ArrayList<docketDetailList_Tbl> md = new ArrayList<>();

    String branchCode;
    Integer companyCode;
    private BroadcastReceiver scanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences
        SharedPreference.getInstance(this);
//        honeywellReceiver = new HoneywellBarcodeReceiver(this);
        companyCode = SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE);
        branchCode = SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE);

        lsNo = getIntent().getStringExtra("LSNo");
        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_ls_barcode_pickup);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarTransparent));
        }

        progressBar = findViewById(R.id.progressbar);
        rl_progressBar = findViewById(R.id.rl_progressbar);



        IntentFilter filter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(barcodeReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }

        init();

    }

    //Init
    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        checkDeviceType();

        mScanner = new ScanManager();

        tvScan = findViewById(R.id.tv_scan);
        tvManually = findViewById(R.id.tv_manually);
        tvScan.setOnClickListener(this);
        tvManually.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = tvManually.getTextColors();
        TextView tv_header = findViewById(R.id.tv_header);
        TextView tv_save = findViewById(R.id.tv_ls_Save);
        dbv_barcode = findViewById(R.id.dbv_barcode);
        etDocketNo = findViewById(R.id.etDocketNo);
        rvScannedDocket = findViewById(R.id.rv_lsScannedDocket);
        mTvExtrapkg = findViewById(R.id.tv_extra_pkg);
        iv_close = findViewById(R.id.iv_ls_close);
        iv_refresh = findViewById(R.id.iv_ls_refresh);
        edt_docketNo = findViewById(R.id.edt_docketNo);
        Button btn_scan = findViewById(R.id.btn_scan);
        LinearLayout ll_pending = findViewById(R.id.ll_ls_pending);
        LinearLayout ll_complete = findViewById(R.id.ll_ls_complete);

        mTVExtraPkg = findViewById(R.id.tv_extra_dkt);

        ll_pending.setOnClickListener(v -> showDocketList(false));
        ll_complete.setOnClickListener(v -> showDocketList(true));

        tv_header.setText(R.string.label_scan_ls_packages);
        tv_save.setText(R.string.label_finish_ls_scan);

        //Define Function
        getCount();

        //onCLick Refresh
        iv_refresh.setOnClickListener(v -> {
            startActivity(getIntent());
            finish();
        });

        //onCLick Close
        iv_close.setOnClickListener(v -> finish());

        //Barcode Scan Camera
        dbv_barcode.decodeContinuous(new BarcodeCallback() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void barcodeResult(BarcodeResult result) {
                dbv_barcode.resume();
                Log.d("TAG", "barcodeResult");
                isContinueScanned = true;
                scanBarcode(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });

        //Barcode Scan Manually
        btn_scan.setOnClickListener(view -> {
            isContinueScanned = false;
            String barcode = edt_docketNo.getText().toString().trim();
            scanBarcode(barcode);
        });

//        if (IsContinuousScanning && (IsHHTDevice || IsHHTNewlandDevice)) {
        if (IsContinuousScanning && (IsHHTDevice)) {
            Log.d("TAG", "check Edit Text: "+IsHHTDevice);
            etDocketNo.requestFocus();
            etDocketNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        if (IsHHTDevice) {
            dbv_barcode.setVisibility(View.GONE);
            dbv_barcode.pause();
        }

        etDocketNo.setOnTouchListener((v, event) -> {
            v.onTouchEvent(event);
            if (IsContinuousScanning)
                hideDefaultKeyboard(v);
            return true;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String barcode = intent.getStringExtra("SCAN_BARCODE1");

                        if (barcode != null) {
                            Log.d("TAG", "Check 2");
                            scanBarcode(barcode);
                        } else {
                            Toast.makeText(getApplicationContext(), "Barcode Not getting.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Scan Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }, new IntentFilter("nlscan.action.SCANNER_RESULT"), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String barcode = intent.getStringExtra("SCAN_BARCODE1");

                        if (barcode != null) {
                            Log.d("TAG", "Check 3");
                            scanBarcode(barcode);
                        } else {
                            Toast.makeText(getApplicationContext(), "Barcode Not getting.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Scan Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }, new IntentFilter("nlscan.action.SCANNER_RESULT"));
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

    //Tab (Scan & Manually)
    @Override
    public void onClick(View view) {

        LinearLayout ll_scanManually = findViewById(R.id.ll_scanManually);

        if (view.getId() == R.id.tv_scan) {
            select.animate().x(0).setDuration(100);
            tvScan.setTextColor(Color.WHITE);
            tvManually.setTextColor(def);
            if (IsHHTDevice) {
                dbv_barcode.setVisibility(View.GONE);
                dbv_barcode.pause();
            } else {
                dbv_barcode.setVisibility(View.VISIBLE);
            }
            ll_scanManually.setVisibility(View.GONE);

        } else if (view.getId() == R.id.tv_manually) {
            tvScan.setTextColor(def);
            tvManually.setTextColor(Color.WHITE);
            int size = tvManually.getWidth();
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
//        dbv_barcode.pause();
        Toast.makeText(LSBarcodePickupActivity.this, barcode, Toast.LENGTH_LONG).show();
        if (!IsHHTDevice) {
            new Handler().postDelayed(() -> {
                dbv_barcode.pause();
                dbv_barcode.resume();
            }, 1000);
        }

       /* if(!IsHHTNewlandDevice){
            if (!IsHHTDevice || !IsHHTNewlandDevice  || !IsPM85Device || !IsPM80Device) {
                new Handler().postDelayed(() -> {
                    dbv_barcode.resume();
                }, 1000);
            }
        }*/

        if (barcode.length() != 0 && !barcode.equalsIgnoreCase("")) {
            rl_progressBar.setVisibility(View.VISIBLE);
            realm.executeTransaction(realm -> {
                docketBarCodeSeries_Tbl docketBarCode = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("bcSerialNo", barcode)
                        .equalTo("LsThcNo", lsNo)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .findFirst();

                if (docketBarCode != null) {
                    if (docketBarCode.getBarcodeScanned()) {
                        CommonMethod.showToastMessage(LSBarcodePickupActivity.this, "This barcode is already scanned.");
                        rl_progressBar.setVisibility(View.GONE);
                        IsContinuousScanning = true;
                        etDocketNo.setText("");
                        etDocketNo.requestFocus();
                    } else {
//                            Log.d("TAG", "scanBarcode Update: "+isContinueScanned);
                        beepSound();
                        docketBarCode.setContinueScanned(isContinueScanned);
                        docketBarCode.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                        docketBarCode.setBarcodeScanned(true);
                        realm.insertOrUpdate(docketBarCode);
                        updateDocketData(realm, docketBarCode.getDockNo(), docketBarCode.getBcSerialNo(), docketBarCode.getDockSf());
                        rl_progressBar.setVisibility(View.GONE);
                    }
                } else {
                    IsContinuousScanning = true;
                    etDocketNo.requestFocus();
                    CommonMethod.showToastMessage(LSBarcodePickupActivity.this, "Invalid barcode.");
                    /*CommonMethod.showAlertDailogueWithOK(LSBarcodePickupActivity.this, getString(R.string.alert),
                            "Do you want to add Extra barcode..?", getString(R.string.action_ok));*/
                    /*showInvalidSuccessPackageDialog(new TempDataStore() {
                        @Override
                        public void sample(boolean flag) {
                            Log.d("TAG", "Flag Data :" + flag);
                            if (flag) {
                                Log.d("TAG", "sample True Barcode :" + barcode);
                                realm.executeTransaction(r -> {
                                    docketBarCodeSeries_Tbl getBarcode = realm.where(docketBarCodeSeries_Tbl.class)
                                            .equalTo("bcSerialNo", barcode)
                                            .equalTo("LsThcNo", lsNo)
                                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                            .findFirst();

                                    if (getBarcode == null) {
                                        Log.d("TAG", "check barcode Null :" + SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE) + "");
                                        docketBarCodeSeries_Tbl bc = new docketBarCodeSeries_Tbl();
                                        beepSound();
                                        bc.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                                        bc.setLsThcNo(lsNo);
                                        bc.setBcSerialNo(barcode);
                                        bc.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                                        bc.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                                        bc.setBarcodeScanned(true);
                                        bc.setBarcodeType(ConstantData.SP_KEY_EXTRA);
                                        realm.insertOrUpdate(bc);

                                        getCountExtra();
                                        IsContinuousScanning = true;
                                        etDocketNo.requestFocus();
                                        return;
                                    }
                                });
                            } else {
                                Log.d("TAG", "sample False");
                            }
                        }
                    });*/
                    rl_progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            IsContinuousScanning = true;
            etDocketNo.requestFocus();
            CommonMethod.showToastMessage(LSBarcodePickupActivity.this, "Invalid barcode.");
        }
        getCount();
//        setrvscanneddocketadapter();
        IsContinuousScanning = true;
        etDocketNo.requestFocus();
    }

    private void showInvalidSuccessPackageDialog(TempDataStore mDialogListener) {

        final Dialog dialog = new Dialog(LSBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_term_of_success_services);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.sample(false);
                Log.d("TAG", "onClick Close");
//                resumeScanner();
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.sample(true);
                Log.d("TAG", "onClick Accept");
//                resumeScanner();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    protected void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void updateDocketData(Realm realm, String dockNo, String bcSerialNo, String dockSf) {

        docketDetailList_Tbl docketDetailList = realm.where(docketDetailList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("dockNo", dockNo)
                .equalTo("dockSf", dockSf)
                .findFirst();

        if (docketDetailList != null) {
            docketDetailList.setScanPackages(docketDetailList.getScanPackages() + 1);
            docketDetailList.setScanDocket(true);
            docketDetailList.setLastBcSerialNo(bcSerialNo);
            realm.insertOrUpdate(docketDetailList);
        }
        LsHader_Tbl getValidLoadingSheetData = realm.where(LsHader_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("lsNo", lsNo).findFirst();

        if (getValidLoadingSheetData != null) {
            getValidLoadingSheetData.setTotalScanBarcode(getValidLoadingSheetData.getTotalScanBarcode() + 1);
            realm.insertOrUpdate(getValidLoadingSheetData);
        }
        IsContinuousScanning = true;
        etDocketNo.setText("");
        etDocketNo.requestFocus();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume Device: " + IsHHTDevice);
        if (IsHoneyWell) {

            scanReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if ("com.honeywell.barcode.action.BARCODE_DATA".equals(intent.getAction())) {
                        String scannedData = intent.getStringExtra("data");
                        if (scannedData != null) {
                            scanBarcode(scannedData);
                            Log.d("TAG", "Scanned onReceive: "+scannedData);
//                            Toast.makeText(context, "Scanned: " + scannedData, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
            IntentFilter filter = new IntentFilter("com.honeywell.barcode.action.BARCODE_DATA");
            registerReceiver(scanReceiver, filter);

        }else if (IsHHTUROVODevice || IsHHTNewlandDevice) {
            Log.d("TAG", "onResume IsHoneyWell: " + mScanner.aDecodeGetDecodeEnable());
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

        receiver = new ScanResultReceiver(etDocketNo);
        registerReceiver(receiver, filter);
    }

    //onPause
    @Override
    public void onPause() {
        try {
            if (IsHoneyWell) {
                try {
                    unregisterReceiver(scanReceiver);
                } catch (Exception e) {
                    Log.e("TAG", "Barcode receiver unregistration error: " + e.getMessage());
                }
            }else {
                if (receiver != null) {
                    unregisterReceiver(receiver);
                }
                if (IsHHTDevice) {
                    if (mScanner != null) {
                        Log.d("TAG", "onPause 1");
                        mScanner.aDecodeSetResultType(mBackupResultType);
                    }
                } else {
                    dbv_barcode.pause();
                }
            }

        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            Log.e("TAG", e.getMessage());
        }
        super.onPause();
    }

    //Set PendingCount
    @SuppressLint("SetTextI18n")
    private void getCount() {

        TextView tvPendingDkt = findViewById(R.id.tv_pending_dkt);
        TextView tvPendingPkg = findViewById(R.id.tv_pending_pkg);

        tvCompleteDkt = findViewById(R.id.tv_complete_dkt);
        tvCompletePkg = findViewById(R.id.tv_complete_pkg);

        int pendingDkt = 0, completeDkt = 0, pendingPkg = 0, completePkg = 0;
        ArrayList<docketDetailList_Tbl> docketDetailArrayList = new ArrayList<>();

        RealmResults<docketDetailList_Tbl> docketDetailList = realm.where(docketDetailList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("lsNo", lsNo).findAll();



        for (docketDetailList_Tbl docketDetail : docketDetailList) {
            if ((!docketDetail.getScanPackages().equals(docketDetail.getTotalPackages()) && docketDetail.isPartialCheck())) {
                pendingDkt++;
            }

            if (docketDetail.getScanDocket() && docketDetail.isPartialCheck()) {
                completeDkt++;
            }

            if (docketDetail.getScanDocket()) {
                docketDetailArrayList.add(docketDetail);
            }

            if(docketDetail.isPartialCheck()){
                completePkg = completePkg + docketDetail.getScanPackages();
                pendingPkg = pendingPkg + docketDetail.getTotalLoadPackages();
            }
        }

        pendingPkg = pendingPkg - completePkg;

        tvPendingPkg.setText(pendingPkg + "");
        tvCompletePkg.setText(completePkg + "");
        tvPendingDkt.setText(pendingDkt + "");
        tvCompleteDkt.setText(completeDkt + "");

        long extraPkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", lsNo)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .count();
        Log.d("TAG", "get Extra Docket Count: " + extraPkgCount);
        mTvExtrapkg.setText(Long.toString(extraPkgCount));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvScannedDocket.setLayoutManager(linearLayoutManager);
        RVScanListAdapter scannedDocketAdapter = new RVScanListAdapter(docketDetailArrayList, this);
        rvScannedDocket.setAdapter(scannedDocketAdapter);
    }

    private void getCountExtra() {

        RealmResults<docketBarCodeSeries_Tbl> extraPkg = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("LsThcNo", lsNo)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .findAll();
        Log.d("TAG", "getCountExtra: " + extraPkg.size());
        mTvExtrapkg.setText(extraPkg.size() + "");
    }

    //Set CompleteCount
    @SuppressLint("SetTextI18n")
    private void completeCount() {
        tvCompleteDkt = findViewById(R.id.tv_complete_dkt);
        tvCompletePkg = findViewById(R.id.tv_complete_pkg);

        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeries;
        docketBarCodeSeries = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("dockNo", getIntent().getStringExtra("DockNo"))
                .equalTo("isBarcodeScanned", true).findAll();

        tvCompletePkg.setText(docketBarCodeSeries.size() + "");
    }

    //onClick Pending or Completed
    private void showDocketList(Boolean stetus) {

        Log.d("TAG", "onClick pending :" + lsNo);
        final Dialog dialog = new Dialog(LSBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pending_docket_list);

        ImageView ivCloseDocket = dialog.findViewById(R.id.iv_closePendingDocketList);
        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);

        if (stetus)
            tv_header.setText(R.string.label_completed_docket);
        else
            tv_header.setText(R.string.label_pending_docket);


        ivCloseDocket.setOnClickListener(v -> dialog.cancel());
        try {
            RealmResults<docketDetailList_Tbl> resultDocketList =
                    realm.where(docketDetailList_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("lsNo", lsNo).findAll();
            ArrayList<docketDetailList_Tbl> docketList = new ArrayList<>();
            for (docketDetailList_Tbl docketDetail : resultDocketList) {
                if (!stetus && !docketDetail.getTotalPackages().equals(docketDetail.getScanPackages())) {
                    docketList.add(realm.copyFromRealm(docketDetail));
                } else if (stetus && docketDetail.getScanDocket())
                    docketList.add(realm.copyFromRealm(docketDetail));
            }

            RecyclerView rvPendingDocket = dialog.findViewById(R.id.rv_pendingDocket);

            rvPendingDocket.setLayoutManager(new LinearLayoutManager(LSBarcodePickupActivity.this, RecyclerView.VERTICAL, false));
            LsDocketListAdapter docketBarcodeAdapter = new LsDocketListAdapter(docketList, stetus, this);
            rvPendingDocket.setAdapter(docketBarcodeAdapter);

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);

        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            Log.d("TAG", "error :" + e.getMessage());
        }
    }

    //onClick Save
    @SuppressLint("SetTextI18n")
    public void save(View view) {
        refreshToken(this);
        Log.d("TAG", "click save");
        boolean isPartial = IsCheckPartialPKG();
        Log.d("TAG", "isPartial: "+isPartial);
        if(!isPartial){
            return;
        }

        int completeCount;
        completeCount = Integer.parseInt(tvCompleteDkt.getText().toString().trim());
        if (completeCount > 0) {
            if (!getIntent().getBooleanExtra("flag", false)) {

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_ls_complete_pickup);

                TextView tvAlertLSNo, tvAlertTotalNoOfDockets, tvAlertTotalNoOfScannedDockets, tvAlertTotalLoadPackages, tvAlertTotalScannedPackages;

                tvAlertLSNo = dialog.findViewById(R.id.tvAlertLSNo);
                tvAlertTotalNoOfDockets = dialog.findViewById(R.id.tvAlertTotalNoOfDockets);
                tvAlertTotalNoOfScannedDockets = dialog.findViewById(R.id.tvAlertTotalNoOfScannedDockets);
                tvAlertTotalLoadPackages = dialog.findViewById(R.id.tvAlertTotalLoadPackages);
                tvAlertTotalScannedPackages = dialog.findViewById(R.id.tvAlertTotalScannedPackages);

                String lsNo = SharedPreference.getStringValue(ConstantData.SP_LS_NO);
                int totalLoadPackages = SharedPreference.getIntValue(ConstantData.SP_TOTAL_PACKAGES);
                int totalNoOfDockets = SharedPreference.getIntValue(ConstantData.SP_TOTAL_DOCKETS);

                RealmResults<docketDetailList_Tbl> docketDetailListTbl;
                docketDetailListTbl = realm.where(docketDetailList_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("lsNo", lsNo)
                        .equalTo("IsPartialCheck", true)
                        .equalTo("scanDocket", true).findAll();

                RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("LsThcNo", lsNo)
                        .equalTo("isBarcodeScanned", true).findAll();

                tvAlertLSNo.setText(lsNo);
                tvAlertTotalNoOfDockets.setText("" + totalNoOfDockets);
//                tvAlertTotalNoOfScannedDockets.setText("" + docketDetailListTbl.size());
                tvAlertTotalNoOfScannedDockets.setText("" + tvCompleteDkt.getText());
                tvAlertTotalLoadPackages.setText("" + totalLoadPackages);
                tvAlertTotalScannedPackages.setText("" + tvCompletePkg.getText());
//                tvAlertTotalScannedPackages.setText("" + docketBarCodeSeriesTbls.size());

                ImageView alertDismiss = dialog.findViewById(R.id.iv_AlertDismiss);

                alertDismiss.setOnClickListener(v -> dialog.dismiss());

                TextView alertFinalizeAndMakeMF = dialog.findViewById(R.id.tvAlertFinalizeAndMakeMF);

                alertFinalizeAndMakeMF.setOnClickListener(v -> {
                    dialog.dismiss();

                    realm.executeTransaction(realm -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LSBarcodePickupActivity.this);
                        builder.setMessage("Do you want to create Manifest?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", (dialog1, id) -> {
                            dialog1.cancel();
                            rl_progressBar.setVisibility(View.VISIBLE);
                            Log.d("TAG", "click yes");
//                            updateExtraDocketAPI();
                            generateManifest();
//                            mfUpload();
                        });

                        builder.setNegativeButton("No", (dialog12, id) -> dialog12.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    });
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            } else {

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_thc_complete_pickup);

                ImageView alertDismiss = dialog.findViewById(R.id.iv_AlertDismiss);

                alertDismiss.setOnClickListener(v -> dialog.dismiss());

                TextView alertFinalizeAndMakeMF = dialog.findViewById(R.id.tvAlertFinalizeAndMakeMF);

                alertFinalizeAndMakeMF.setOnClickListener(v -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully Stock Updated", Toast.LENGTH_SHORT).show();
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        } else {
            Toast.makeText(this, "Scan atleast one barcode", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean IsCheckPartialPKG() {
        RealmResults<docketDetailList_Tbl> rmDetails = realm.where(docketDetailList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("lsNo", lsNo)
                .equalTo("scanDocket", true).findAll();

        boolean IsPartialScanAllow = SharedPreference.getBooleanValue(ConstantData.SP_KEY_ISPARTIALSCAN);
        Log.d("TAG", "IsCheckPartial falge from API :"+IsPartialScanAllow);
        ArrayList<String> strDktList = new ArrayList<>();
        if (!IsPartialScanAllow) {
            if (rmDetails.size() > 0) {
                int totalPKG = 0;
                int totalScanPKG = 0;

                for (docketDetailList_Tbl details : rmDetails) {

                    if (details.isPartialCheck()) {
                        Log.d("TAG", "IsCheckPartial True");
                        totalPKG = totalPKG + details.getTotalLoadPackages();
                        totalScanPKG = totalScanPKG + details.getScanPackages();
                        finalScanPKG = totalScanPKG + details.getScanPackages();
                    }
                    if ((details.getTotalLoadPackages() != details.getScanPackages()) && details.isPartialCheck()) {
                        strDktList.add(details.getDockNo());
                    }
                }
                Log.d("TAG", "STR Docket List: " + strDktList);
                Log.d("TAG", "totalPKG: " + totalPKG);
                Log.d("TAG", "totalScanPKG: " + totalScanPKG);
                if(totalPKG == totalScanPKG){
                    return true;
                }else {
                    if (strDktList.size() > 0) {
                        showPartialDocketListDialog(strDktList);
                    }
                    return false;
                }

            }
        }
        return true;
    }

    private void showPartialDocketListDialog(ArrayList<String> strDktList) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_partial_docket);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView mTvDkt = dialog.findViewById(R.id.tv_dkt);
        String partialDocket = "";
        for (int i = 0; i < strDktList.size(); i++) {
            partialDocket += strDktList.get(i) + "\n";
        }
        mTvDkt.setText(partialDocket);

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void updateExtraDocketAPI() {
        Log.d("TAG", "updateExtraDocketAPI :" + lsNo);
        ArrayList<docketBarCodeSeries_Tbl> extraList = new ArrayList<>();

        RealmResults<docketBarCodeSeries_Tbl> extraPkg = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("LsThcNo", lsNo)
                .equalTo("isBarcodeScanned", true)
                .equalTo("isServerPush", false)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .findAll();
        Log.d("TAG", "updateExtraDocketAPI 1 Size :" + extraPkg.size());
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
            Log.d("TAG", "update Extra Request: " + new Gson().toJson(model));
            Log.d("TAG", "Loader Visible");
            rl_progressBar.setVisibility(View.VISIBLE);
            /*Call<ScanDocketResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .extraBarcodeUpdate(model);
            call.enqueue(new Callback<ScanDocketResponse>() {
                @Override
                public void onResponse(Call<ScanDocketResponse> call, Response<ScanDocketResponse> response) {
                    Log.d("TAG", "check Status Code: " + response.code());
                    rl_progressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        ScanDocketResponse res = response.body();
                        Log.d("TAG", "scan Extra Docket Response: " + new Gson().toJson(res));

                        if (res.isSuccess()) {
                            Realm realm = Realm.getDefaultInstance();
                            for (docketBarCodeSeries_Tbl docket : extraList) {
                                docket.setServerPush(true);
                                tempList.add(docket);
                            }
                            realm.executeTransaction(r -> {
                                r.insertOrUpdate(tempList);
                            });
                            generateManifest();
                        } else {
                            CommonMethod.alertDialogBoxWithOk("Alert", res.getMessage(), getApplicationContext());
                            CommonMethod.appendLogs(res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ScanDocketResponse> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                }
            });*/
        } else {
            Log.d("TAG", "updateManifest");
            generateManifest();
        }
    }

    private void generateManifest() {
        if (CommonMethod.isNetworkConnected(this)) {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            getRam = getMemorySizeInBytes();
            getModelNumber = Build.MODEL;
            deviceVersionNo = Build.VERSION.RELEASE;
            brand = Build.MANUFACTURER;
            appVersion = BuildConfig.VERSION_NAME;

            RealmResults<docketDetailList_Tbl> docketDetailList = realm.where(docketDetailList_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("scanDocket", true)
                    .equalTo("IsPartialCheck", true)
                    .equalTo("lsNo", lsNo).findAll();

            if (docketDetailList != null) {

                ArrayList<DocketUpdateScan> docketDetailsList = new ArrayList<>();

                ScanDocketDeviceDetails docket = new ScanDocketDeviceDetails();
                docket.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                docket.setOSVersion(deviceVersionNo);
                docket.setDeviceDetails(getModelNumber + "," + brand + "," + getRam);
                docket.setAppVersion(CommonMethod.getAppVersionName(this));
                docket.setDeviceId(CommonMethod.getDeviceId(this));
                docket.setDeviceStorage(String.valueOf(getRam));
                docket.setScanLocation(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                docket.setIMEINo("");
                docket.setModelName(Build.MODEL);


                RealmResults<docketBarCodeSeries_Tbl> docketBarCodeList = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("LsThcNo", lsNo)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("isBarcodeScanned", true)
                        .equalTo("barcodeType", "Outward")
                        .equalTo("isServerPush", false).findAll();

                for (docketBarCodeSeries_Tbl barcode : docketBarCodeList) {
                    DocketUpdateScan dkt = new DocketUpdateScan();
                    dkt.setCompanyCode(barcode.getCompanyCode());
                    dkt.setLSNo(barcode.getLsThcNo());
                    dkt.setDOCKNO(barcode.getDockNo());
                    dkt.setDOCKSF(barcode.getDockSf());
                    dkt.setBCSerialNo(barcode.getBcSerialNo());
                    dkt.setPackages(1);   //when scan docket
                    dkt.setWeight(barcode.getActualweight());  //totalActualWeight
                    dkt.setVolume(barcode.getActualCFweight());  //totalLoadActualWeight
                    dkt.setIsManual(false);
                    docketDetailsList.add(dkt);
                }
                docket.setDetails(docketDetailsList);
                Log.d("TAG", "Update log: " + new Gson().toJson(docket));
                pushDocketOnServer(docket, docketBarCodeList);
            }

        }
    }

    private void pushDocketOnServer(ScanDocketDeviceDetails docketDetailsList, RealmResults<docketBarCodeSeries_Tbl> docketBarCodeList) {
        Log.d("TAG", "pushDocketOnServer Request: " + new Gson().toJson(docketDetailsList));
//        rl_progressBar.setVisibility(View.VISIBLE);
        if (docketDetailsList.getDetails().size() > 0) {
            ArrayList<docketBarCodeSeries_Tbl> prsDcoketList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> tempList = new ArrayList<>();
            prsDcoketList.addAll(realm.copyFromRealm(docketBarCodeList));

            Call<ScanDocketResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .packageUpdate(docketDetailsList);
            call.enqueue(new Callback<ScanDocketResponse>() {
                @Override
                public void onResponse(Call<ScanDocketResponse> call, Response<ScanDocketResponse> response) {
                    Log.d("TAG", "check Status Code");
//                    rl_progressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        ScanDocketResponse res = response.body();
                        if (res.isSuccess()) {
                            Realm realm = Realm.getDefaultInstance();
                            for (docketBarCodeSeries_Tbl docket : prsDcoketList) {
                                Log.d("TAG", "onResponse Docket: " + docket.getBcSerialNo());
                                docket.setServerPush(true);
                                tempList.add(docket);
                            }
                            realm.executeTransaction(r -> {
                                r.insertOrUpdate(tempList);
                            });
                            updateHeaderAPI();
                        } else {
                            CommonMethod.alertDialogBoxWithOk("Alert", res.getMessage(), getApplicationContext());
                            CommonMethod.appendLogs(res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ScanDocketResponse> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        } else {
            updateHeaderAPI();
        }


    }

    private void updateHeaderAPI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String getRam = String.valueOf(getMemorySizeInBytes());
        LsHader_Tbl getValidLoadingSheetData = new LsHader_Tbl();

        RealmResults<docketDetailList_Tbl> scanDocketList = realm.where(docketDetailList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("scanDocket", true)
                .equalTo("lsNo", lsNo).findAll();

        getValidLoadingSheetData = realm.where(LsHader_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("lsNo", lsNo).findFirst();

        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeList = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", lsNo)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("isServerPush", true)
                .findAll();

        double weightCount = (double) realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", lsNo)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("isServerPush", true)
                .sum("actualweight");

        double volumeCount = (double) realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", lsNo)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("isServerPush", true)
                .sum("actualCFweight");

        ArrayList<DocketList> docList = new ArrayList<>();
        if (getValidLoadingSheetData != null) {
            LS_headerRequest hdr = new LS_headerRequest();
            for (docketDetailList_Tbl dktList : scanDocketList) {
                DocketList d = new DocketList();
                if (!dktList.isPartialCheck()) {
                    d.setDocketNo(dktList.getDockNo());
                    d.setDocketSF(dktList.getDockSf());

                    docList.add(d);
                }
            }
            hdr.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
            hdr.setLSNo(lsNo);
            hdr.setLSSF(".");
            hdr.setLSDate(CommonMethod.getCurrentDateStringNew());
//            hdr.setDockets(scanDocketList.size());
//            hdr.setPackages(docketBarCodeList.size());
            hdr.setDockets(Integer.parseInt(tvCompleteDkt.getText().toString()));
            hdr.setPackages(Integer.parseInt(tvCompletePkg.getText().toString()));
            hdr.setWeight(weightCount);
            hdr.setVolume(volumeCount);
            hdr.setDocketList(docList);
            hdr.setUpdateBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
            hdr.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            hdr.setAppVersion(CommonMethod.getAppVersionName(this));
            hdr.setOSVersion(deviceVersionNo);
            hdr.setDeviceDetails(getModelNumber + "," + brand + "," + getRam);
            hdr.setDeviceStorage(getRam);
            hdr.setDeviceId(CommonMethod.getDeviceId(this));
            hdr.setScanLocation(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            hdr.setIMEINo("");
            hdr.setModelName(Build.MODEL);
            pushHeaderServer(hdr);
            Log.d("TAG", "updateHeaderAPI: " + new Gson().toJson(hdr));
        }
    }

    private void pushHeaderServer(LS_headerRequest hdr) {
        Log.d("TAG", "Header Request: " + new Gson().toJson(hdr));
//        rl_progressBar.setVisibility(View.VISIBLE);
        Call<LS_headerResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                .manifestgeneration(hdr);
        call.enqueue(new Callback<LS_headerResponse>() {
            @Override
            public void onResponse(Call<LS_headerResponse> call, Response<LS_headerResponse> response) {
                if (response.body() != null) {
                    LS_headerResponse res = response.body();
//                    Log.d("TAG", "scan Header Response: " + new Gson().toJson(res));
                    Log.d("TAG", "Loader GONE");
                    rl_progressBar.setVisibility(View.GONE);
                    if (res.getIsSuccess()) {
                        try {
                            realm.executeTransaction(r -> {
                                LsHader_Tbl lsHader = r.where(LsHader_Tbl.class)
                                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .equalTo("lsNo", lsNo)
                                        .findFirst();

//                                assert lsHader != null;
                                lsHader.deleteFromRealm();

                                RealmResults<docketBarCodeSeries_Tbl> barcodeData = r.where(docketBarCodeSeries_Tbl.class)
                                        .equalTo("LsThcNo", lsNo)
                                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .findAll();
                                barcodeData.deleteAllFromRealm();

                                RealmResults<docketDetailList_Tbl> docketData = r.where(docketDetailList_Tbl.class)
                                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .equalTo("lsNo", lsNo).findAll();
                                docketData.deleteAllFromRealm();
                            });

                            AlertDialog.Builder builder = new AlertDialog.Builder(LSBarcodePickupActivity.this);
                            builder.setMessage(res.getMfCode() + "\n" + "Manifest generated successfully..");
                            builder.setPositiveButton("Ok", (dialog, id) -> {
                                dialog.cancel();
                                finish();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } catch (Exception e) {
                            rl_progressBar.setVisibility(View.GONE);
                            CommonMethod.appendLogs(e.getMessage());
                            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                            Log.d("TAG", "App Crash: " + e.getMessage());
                        }


                    } else {
                        rl_progressBar.setVisibility(View.GONE);
                        CommonMethod.alertDialogBoxWithOk("Alert", res.getErrorMessage(), getApplicationContext());
                        CommonMethod.appendLogs(res.getErrorMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LS_headerResponse> call, Throwable t) {
                rl_progressBar.setVisibility(View.GONE);
                CommonMethod.appendLogs(t.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
            }
        });
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

    private ManifestRequestModel createRequest() {
        ManifestRequestModel manifestRequest = new ManifestRequestModel();

        realm.executeTransaction(realm -> {
            String date = CommonMethod.getCurrentDateStringNew();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);

            manifestRequest.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE) + "");
            manifestRequest.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            manifestRequest.setDate(date);
            manifestRequest.setFinyear(String.valueOf(year));

            ArrayList<tchdr> tchdr = new ArrayList<>();
            LsHader_Tbl getValidLoadingSheetData = new LsHader_Tbl();

            RealmResults<docketDetailList_Tbl> scanDocketList = realm.where(docketDetailList_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("scanDocket", true)
                    .equalTo("lsNo", lsNo).findAll();
            Log.d("TAG", "Scan Docket Size: " + scanDocketList.size());

            getValidLoadingSheetData = realm.where(LsHader_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("lsNo", lsNo).findFirst();

            if (getValidLoadingSheetData != null) {

                int totalLoadPkgs = getValidLoadingSheetData.getTotalScanBarcode();
                float totalLoadActWt = (float) ((getValidLoadingSheetData.getTotalActualWeight() / getValidLoadingSheetData.getTotalPackages()) * getValidLoadingSheetData.getTotalScanBarcode());
                float totalLoadCftWt = (float) ((getValidLoadingSheetData.getTotalLoadCftWeight() / getValidLoadingSheetData.getTotalPackages()) * getValidLoadingSheetData.getTotalScanBarcode());

                tchdr temptchdr = new tchdr();
                temptchdr.setLSNO(lsNo);
                temptchdr.setTCNO("");
                temptchdr.setTCSF(".");
                temptchdr.setTCDT(date);
                temptchdr.setTCBR(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                temptchdr.setTOBH_CODE(getValidLoadingSheetData.getDestination());
//                temptchdr.setTOT_DKT(getValidLoadingSheetData.getTotalDockets());
                temptchdr.setTOT_DKT(scanDocketList.size());
                temptchdr.setTOT_PKGS(totalLoadPkgs);
//                temptchdr.setTOT_ACTUWT(getValidLoadingSheetData.getTotalActualWeight());
//                temptchdr.setTOT_CFTWT(getValidLoadingSheetData.getTotalLoadCftWeight());
                temptchdr.setTOT_ACTUWT(totalLoadActWt);
                temptchdr.setTOT_CFTWT(totalLoadCftWt);
                temptchdr.setTOT_LOAD_PKGS(totalLoadPkgs);
                temptchdr.setTOT_LOAD_ACTWT(totalLoadActWt);
                temptchdr.setTOT_LOAD_CFTWT(totalLoadCftWt);
                temptchdr.setTCHDRFLAG("Y");
                temptchdr.setTFLAG_YN("N");
                temptchdr.setENTRYBY(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                temptchdr.setROUTE_CODE("-");
                temptchdr.setIsBCProcess(getValidLoadingSheetData.getIsBcProcess());

                tchdr.add(temptchdr);
            }

            manifestRequest.setTchdr(tchdr);

            ArrayList<tctrn> tctrn = new ArrayList<>();

            RealmResults<docketDetailList_Tbl> docketDetailList = realm.where(docketDetailList_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("scanDocket", true)
                    .equalTo("lsNo", lsNo).findAll();

            if (docketDetailList != null) {
                for (docketDetailList_Tbl docketDetailListTbl : docketDetailList) {
                    tctrn tempTctrn = new tctrn();
                    float totalLoadActWt = (float) ((docketDetailListTbl.getTotalActualWeight() / docketDetailListTbl.getTotalPackages()) * docketDetailListTbl.getScanPackages());
                    float totalLoadCftWt = (float) ((docketDetailListTbl.getTotalLoadCftWeight() / docketDetailListTbl.getTotalPackages()) * docketDetailListTbl.getScanPackages());

                    tempTctrn.setTCNO(lsNo);
                    tempTctrn.setTCSF(".");
                    tempTctrn.setDOCKNO(docketDetailListTbl.getDockNo());
                    tempTctrn.setDOCKSF(docketDetailListTbl.getDockSf());
                    tempTctrn.setLOADPKGSNO(docketDetailListTbl.getScanPackages() + "");
                    tempTctrn.setLOADACTUWT(totalLoadActWt + "");
                    tempTctrn.setLOADCFTWT(totalLoadCftWt + "");

                    ArrayList<DocketBarCodeSeries> docketBarCodeSeries = new ArrayList<>();

                    RealmResults<docketBarCodeSeries_Tbl> docketBarCodeList = realm.where(docketBarCodeSeries_Tbl.class)
                            .equalTo("LsThcNo", lsNo)
                            .equalTo("dockNo", docketDetailListTbl.getDockNo())
                            .equalTo("dockSf", docketDetailListTbl.getDockSf())
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("isBarcodeScanned", true).findAll();
                    for (docketBarCodeSeries_Tbl lsDocketBarCode : docketBarCodeList) {
                        Log.d("TAG", "BC Sr Number: " + lsDocketBarCode.getBcSerialNo());
                        DocketBarCodeSeries docketBarCode = new DocketBarCodeSeries();

                        docketBarCode.setDockNo(lsDocketBarCode.getDockNo());
                        docketBarCode.setDockSf(lsDocketBarCode.getDockSf());
                        docketBarCode.setBcSerialNo(lsDocketBarCode.getBcSerialNo());
                        docketBarCode.setBcsrNo(lsDocketBarCode.getBcsrNumber());
                        docketBarCode.setIsBarcodeScanned(lsDocketBarCode.getBarcodeScanned());
                        docketBarCode.setBcScannedDatetime(lsDocketBarCode.getBcScannedDatetime());
                        docketBarCode.setIsContinueScanned(lsDocketBarCode.getContinueScanned());

                        docketBarCodeSeries.add(docketBarCode);
                    }
                    tempTctrn.setDocketBarCodeSeries(docketBarCodeSeries);
                    tctrn.add(tempTctrn);
                }
            }

            manifestRequest.setTctrn(tctrn);
            ArrayList<wtd> wtds = new ArrayList<>();

            if (docketDetailList != null) {
                for (docketDetailList_Tbl docketDetailListTbl : docketDetailList) {
                    wtd tempWtd = new wtd();
                    float totalLoadActWt = (float) ((docketDetailListTbl.getTotalActualWeight() / docketDetailListTbl.getTotalPackages()) * docketDetailListTbl.getScanPackages());
                    float totalLoadCftWt = (float) ((docketDetailListTbl.getTotalLoadCftWeight() / docketDetailListTbl.getTotalPackages()) * docketDetailListTbl.getScanPackages());

                    tempWtd.setDOCKSF(docketDetailListTbl.getDockSf());
                    tempWtd.setLOADACTUWT(totalLoadActWt + "");
                    tempWtd.setLOADCFTWT(totalLoadCftWt + "");
                    tempWtd.setDOCKNO(docketDetailListTbl.getDockNo());
                    assert getValidLoadingSheetData != null;
                    tempWtd.setTOBH_CODE(getValidLoadingSheetData.getDestination());
                    tempWtd.setTCDT(date);
                    tempWtd.setLOADPKGSNO(docketDetailListTbl.getScanPackages() + "");

                    wtds.add(tempWtd);
                }
            }

            manifestRequest.setWtds(wtds);

            ArrayList<Object> nonSelDKT = new ArrayList<>();
            manifestRequest.setNonSelDKT(nonSelDKT);
            ArrayList<Object> extraScannedDkts = new ArrayList<>();
            manifestRequest.setExtraScannedDkts(extraScannedDkts);

        });
        return manifestRequest;
    }

    private void checkDeviceType() {
        String deviceName = new CommonMethod().getDeviceName();
        Log.d("TAG", "checkDeviceType: "+deviceName);
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
        IsHoneyWell = deviceName.contains(ConstantData.Honeywell);

//        Toast.makeText(LSBarcodePickupActivity.this, deviceName + "," + IsPM75Device, Toast.LENGTH_LONG).show();
    }

    protected void onDestroy() {
        if (IsHHTDevice) {
            if (mScanner != null) {
                mScanner.aDecodeSetResultType(mBackupResultType);
            }
            mScanner = null;
        }

        if (barcodeReceiver != null) {
            unregisterReceiver(barcodeReceiver);
        }
        super.onDestroy();
    }

    public void showBarcodeList(Boolean stetus, String dockNo, String dockSf) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_complete_barcode_list);

        ImageView iv_closePendingBarcodeList = dialog.findViewById(R.id.iv_closePendingBarcodeList);
        iv_closePendingBarcodeList.setOnClickListener(v1 -> dialog.dismiss());

        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);

        if (stetus)
            tv_header.setText(R.string.label_completed_barcode);
        else
            tv_header.setText(R.string.label_pending_barcode);

        Log.d("TAG", "status: " + stetus);
        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeries = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("dockNo", dockNo)
                .equalTo("dockSf", dockSf)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("barcodeType", "Outward")
                .equalTo("isBarcodeScanned", stetus).findAll();

        ArrayList<docketBarCodeSeries_Tbl> docketList = new ArrayList<>(realm.copyFromRealm(docketBarCodeSeries));

        Log.d("TAG", "showBarcodeList: " + docketList.size());

        RecyclerView rvPendingBarcode = dialog.findViewById(R.id.rv_pendingBarcode);

        rvPendingBarcode.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        LsBarcodeListAdapter barcodeListAdapter = new LsBarcodeListAdapter(docketList);
        rvPendingBarcode.setAdapter(barcodeListAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    protected void resumeScanner() {

        if (!dbv_barcode.isActivated())
            dbv_barcode.resume();
        //Log.d("peeyush-pause", "paused: false");
    }

    public void addPartialDocket(String dockNo, String dockSf, boolean isChecked) {
        Log.d("TAG", "PartialDocket: " + dockNo);
        docketDetailList_Tbl rmDoc = realm.where(docketDetailList_Tbl.class)
                .equalTo("dockNo", dockNo)
                .equalTo("dockSf", dockSf)
                .findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (rmDoc != null) {
                    rmDoc.setPartialCheck(isChecked);
                }
                realm.insertOrUpdate(rmDoc);
                getCount();
            }
        });


    }

    @Override
    public void onBarcodeScanned(String barcodeData) {
        scanBarcode(barcodeData);
    }

//    @Override
//    public void onHBarcodeScanned(String barcodeData) {
//        Toast.makeText(this, "Scanned: " + barcodeData, Toast.LENGTH_SHORT).show();
//        scanBarcode(barcodeData);
//    }
}