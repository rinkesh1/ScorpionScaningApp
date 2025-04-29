package in.webx.scorpion.activity;

import static device.common.ScanConst.INTENT_EVENT;
import static device.common.ScanConst.INTENT_USERMSG;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.realm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.webx.scorpion.Services.BarcodeListener;
import in.webx.scorpion.Services.BarcodeReceiver;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.karumi.dexter.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import device.common.ScanConst;
import device.sdk.ScanManager;
import in.webx.scorpion.Adapter.ThcBarcodeListAdapter;
import in.webx.scorpion.Adapter.ThcDocketListAdapter;
import in.webx.scorpion.Adapter.RVThcScaneAdapter;
import in.webx.scorpion.Model.ExtraBcSerialListModel;
import in.webx.scorpion.Model.ExtraBcSerialModel;
import in.webx.scorpion.Model.ExtraDocketListModel;
import in.webx.scorpion.Model.ExtraDocketResponseModel;
import in.webx.scorpion.Model.ScanDocketResponse;
import in.webx.scorpion.Model.ScanDocketTHCModel;
import in.webx.scorpion.Model.ScanTHCDocketDetailsModel;
import in.webx.scorpion.Model.TempDataStore;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.WareHouse;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
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

public class THCBarcodePickupActivity extends AppCompatActivity implements View.OnClickListener, BarcodeListener, HoneywellBarcodeReceiver.BarcodeListener {

    private TextView tvScan, tvManually, select, tvThcSave, mTxtLastScanPkg;
    private ColorStateList def;
    private DecoratedBarcodeView dbvBarcode;
    private boolean isContinueScanned = false;
    private EditText edtDocketNo;
    ImageView ivNavBack;
    int completePkg;
    Button btScan;
    RecyclerView rvScannedDocket;
    String getModelNumber, deviceVersionNo, brand, appVersion;
    double getRam;
    private RelativeLayout rl_progressbar;
    private boolean IsHHTDevice, IsHHTNewlandDevice, IsHHTUROVODevice, IsPM85Device, IsPM80Device, IsPM75Device,IsHoneyWell;
    private static ScanManager mScanner;
    private ScanResultReceiver receiver;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    boolean IsContinuousScanning = true;
    private EditText etDocketNo;
    private InputMethodManager imm;
    private android.app.AlertDialog mDialog = null;
    LinearLayout mLinPkg;
    private String thcNo;
    ArrayList<ExtraBcSerialListModel> exArrList = new ArrayList<>();
    private BroadcastReceiver scanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_thc_barcode_pickup);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.StatusBarTransparent));
        }

        //Define Function
        init();

        /*try {
            AidcManager.create(this, aidcManager -> {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader();

                try {
                    barcodeReader.claim();
                } catch (ScannerUnavailableException e) {
                    e.printStackTrace();
                }

                barcodeReader.addBarcodeListener(new BarcodeReader.BarcodeListener() {
                    @Override
                    public void onBarcodeEvent(BarcodeReadEvent event) {
                        runOnUiThread(() -> {
                            String data = event.getBarcodeData();
                            Log.d("ScannerSDK", "Scanned: " + data);
                            Toast.makeText(getApplicationContext(), "SDK Scan: " + data, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailureEvent(BarcodeFailureEvent e) {
                        Log.e("ScannerSDK", "Scan failed: " + e.toString());
                    }
                });
            });
        } catch (SecurityException e) {
            Log.e("SDK", "Honeywell SDK not available. Falling back to intent-based.");
            // Fallback to intent scanning
        }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        checkDeviceType();
        mScanner = new ScanManager();

        SharedPreference.getInstance(this);

        getRam = getMemorySizeInBytes();
        getModelNumber = Build.MODEL;
        deviceVersionNo = Build.VERSION.RELEASE;
        brand = Build.MANUFACTURER;
        appVersion = BuildConfig.VERSION_NAME;

        thcNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);

        tvScan = findViewById(R.id.tv_scan);
        tvManually = findViewById(R.id.tv_manually);
        tvScan.setOnClickListener(this);
        tvManually.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = tvManually.getTextColors();
        etDocketNo = findViewById(R.id.etDocketNo);
        rvScannedDocket = findViewById(R.id.rv_scannedDocket);
        rl_progressbar = findViewById(R.id.rl_progressbar);

        ivNavBack = findViewById(R.id.iv_nav_back);
        ImageView ivthcrefresh = findViewById(R.id.iv_thc_refresh);
        dbvBarcode = findViewById(R.id.dbv_barcode);
        edtDocketNo = findViewById(R.id.edt_docketNo);
        btScan = findViewById(R.id.btn_scan);
        LinearLayout ll_thcPending = findViewById(R.id.ll_thc_pending);
        LinearLayout ll_thcComplete = findViewById(R.id.ll_thc_complete);
        tvThcSave = findViewById(R.id.tv_thc_Save);
        mTxtLastScanPkg = findViewById(R.id.txtLastScanPkg);
        mLinPkg = findViewById(R.id.lin_pkg);

        ivNavBack.setOnClickListener(v -> thcclose());
        ivthcrefresh.setOnClickListener(v -> refresh());
        btScan.setOnClickListener(v -> manuallyscan());
        ll_thcPending.setOnClickListener(v -> showDocketList(false));
        ll_thcComplete.setOnClickListener(v -> showDocketList(true));
        tvThcSave.setOnClickListener(v -> save());
        THCHeader_Tbl thcHeader = realm.where(THCHeader_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("thcNo", thcNo).findFirst();
        if (thcHeader.getLastBarcodePkg() != null) {
            Log.d("TAG", "init check");
            mLinPkg.setVisibility(View.VISIBLE);
            mTxtLastScanPkg.getPaint().setUnderlineText(true);
            mTxtLastScanPkg.setText(thcHeader.getLastBarcodePkg());
        }

        mTxtLastScanPkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "show damage");
                showExtraPackageDialog("IsDamage", new TempDataStore() {
                    @Override
                    public void sample(boolean flag) {

                        if (flag) {
                            realm.executeTransaction(r -> {
                                docketBarCodeSeries_Tbl getBarcode = realm.where(docketBarCodeSeries_Tbl.class)
                                        .equalTo("bcSerialNo", mTxtLastScanPkg.getText().toString())
                                        .equalTo("LsThcNo", thcNo)
                                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .findFirst();
                                if (getBarcode != null) {
                                    docketBarCodeSeries_Tbl bc = new docketBarCodeSeries_Tbl();

                                    bc.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                                    bc.setLsThcNo(thcNo);
                                    bc.setDockNo(getBarcode.getDockNo());
                                    bc.setDockSf(getBarcode.getDockSf());
                                    bc.setBcSerialNo(mTxtLastScanPkg.getText().toString());
                                    bc.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                                    bc.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                                    bc.setBarcodeScanned(true);
                                    bc.setBarcodeType(ConstantData.SP_KEY_DAMAGE);

                                    realm.insertOrUpdate(bc);
                                    IsContinuousScanning = true;

                                    return;
                                }
                            });

                        } else {
                            Log.d("TAG", "sample False");
                        }
                    }
                });
            }
        });
        //Barcode Scan Camera
        dbvBarcode.decodeContinuous(new BarcodeCallback() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void barcodeResult(BarcodeResult result) {
                isContinueScanned = true;
                dbvBarcode.resume();

                scanBarcode(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });

        getCount();
        updateExtraDocket();
//        setrvscanneddocketadapter();

        if (IsContinuousScanning && (IsHHTDevice)) {
            etDocketNo.requestFocus();
            etDocketNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String strDocketNo = etDocketNo.getText().toString().trim();
                    if (IsContinuousScanning && !strDocketNo.equals("")) {
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
        etDocketNo.setOnTouchListener((v, event) -> {
            v.onTouchEvent(event);
            if (IsContinuousScanning)
                hideDefaultKeyboard(v);
            return true;
        });

        if (IsHHTDevice) {
            dbvBarcode.setVisibility(View.GONE);
            dbvBarcode.pause();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String barcode = intent.getStringExtra("SCAN_BARCODE1");

                        if (barcode != null) {
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

    public double getMemorySizeInBytes() {
        Context context = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        //long totalMemory = memoryInfo.totalMem;

        double totalMemory = memoryInfo.totalMem / 1073741824.0;
        return totalMemory;
    }

    private void hideDefaultKeyboard(View et) {
        getMethodManager().hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private void checkDeviceType() {
        String deviceName = new CommonMethod().getDeviceName();
        IsHHTDevice = deviceName.contains(ConstantData.UROVO)
                || deviceName.contains(ConstantData.NEWLAND)
                || deviceName.contains(ConstantData.Honeywell)
                || deviceName.contains(ConstantData.SCANNER_PM85)
                || deviceName.contains(ConstantData.SCANNER_PM80)
                || deviceName.contains(ConstantData.SCANNER_PM75);

        IsHHTUROVODevice = deviceName.contains(ConstantData.UROVO);
        IsHHTNewlandDevice = deviceName.contains(ConstantData.NEWLAND);
        IsPM85Device = deviceName.contains(ConstantData.SCANNER_PM85);
        IsPM80Device = deviceName.contains(ConstantData.SCANNER_PM80);
        IsPM75Device = deviceName.contains(ConstantData.SCANNER_PM75);
        IsHoneyWell = deviceName.contains(ConstantData.Honeywell);

//        Toast.makeText(THCBarcodePickupActivity.this, deviceName + "," + IsPM75Device, Toast.LENGTH_LONG).show();
    }

    private InputMethodManager getMethodManager() {
        if (this.imm == null) {
            this.imm = (InputMethodManager) this.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        }
        return this.imm;
    }

    //Scan Barcode
    private void scanBarcode(String barcode) {
        Log.d("TAG", " barcode THC : " + barcode);

        if (!IsHHTDevice) {
            new Handler().postDelayed(() -> {
                dbvBarcode.pause();
                dbvBarcode.resume();
            }, 1000);
        }

        if (barcode.length() != 0 && !barcode.equalsIgnoreCase("")) {
            realm.executeTransaction(realm -> {
                docketBarCodeSeries_Tbl model = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("bcSerialNo", barcode)
                        .equalTo("LsThcNo", thcNo).findFirst();

                if (model != null) {
                    if (model.getBarcodeScanned()) {
                        CommonMethod.showToastMessage(THCBarcodePickupActivity.this, "This barcode is already scanned.");
                        etDocketNo.setText("");
                        etDocketNo.requestFocus();
                    } else {
                        beepSound();
                        model.setContinueScanned(isContinueScanned);
                        model.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                        model.setBarcodeScanned(true);
                        realm.insertOrUpdate(model);
                        updateDocketData(realm, model.getDockNo(), model.getBcSerialNo(), model.getDockSf());
                    }
                } else {
                    IsContinuousScanning = true;
                    etDocketNo.requestFocus();
                    CommonMethod.showToastMessage(THCBarcodePickupActivity.this, "Invalid barcode.");
                    showExtraPackageDialog("", new TempDataStore() {
                        @Override
                        public void sample(boolean flag) {
                            if (flag) {
                                Log.d("TAG", "sample Extra");
                                checkExtraBarcode(barcode);

                                /*realm.executeTransaction(r -> {
                                    docketBarCodeSeries_Tbl getBarcode = realm.where(docketBarCodeSeries_Tbl.class)
                                            .equalTo("bcSerialNo", barcode)
                                            .equalTo("LsThcNo", thcNo)
                                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                            .findFirst();

                                    if (getBarcode == null) {
                                        docketBarCodeSeries_Tbl bc = new docketBarCodeSeries_Tbl();
                                        beepSound();
                                        bc.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                                        bc.setLsThcNo(thcNo);
                                        bc.setBcSerialNo(barcode);
                                        bc.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                                        bc.setBcScannedDatetime(CommonMethod.getCurrentDateStringNew());
                                        bc.setBarcodeScanned(true);
                                        bc.setBcsrNumber("1");
                                        bc.setBarcodeType(ConstantData.SP_KEY_EXTRA);
                                        realm.insertOrUpdate(bc);

//                                        getCountExtra();
                                        IsContinuousScanning = true;
                                        etDocketNo.requestFocus();
                                        updateExtraDocket();

                                        return;
                                    }
                                });*/
                            } else {
                                Log.d("TAG", "sample False");
                            }
                        }
                    });
                }
            });
        } else {
            IsContinuousScanning = true;
            etDocketNo.requestFocus();
            CommonMethod.showToastMessage(THCBarcodePickupActivity.this, "Invalid barcode.");
        }

        getCount();
        updateExtraDocket();
//        setrvscanneddocketadapter();
        IsContinuousScanning = true;
        etDocketNo.requestFocus();
    }

    private void checkExtraBarcode(String barcode) {

        ArrayList<ExtraBcSerialListModel> ext = new ArrayList<>();

        ExtraBcSerialModel model = new ExtraBcSerialModel();

        model.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
        model.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));

        ExtraBcSerialListModel xModel = new ExtraBcSerialListModel();
        xModel.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
        xModel.setBCSerialNo(barcode);
        xModel.setPackages(1);
        xModel.setWeight(0.0);

        ext.add(xModel);

        model.setList(ext);
        Log.d("TAG", "checkExtraBarcode: " + new Gson().toJson(model));
        rl_progressbar.setVisibility(View.VISIBLE);

        Call<ExtraDocketResponseModel> call = MyRetro.createSecureServiceApp(this, APIService.class)
                .extraBarcodeUpdate(model);
        call.enqueue(new Callback<ExtraDocketResponseModel>() {
            @Override
            public void onResponse(Call<ExtraDocketResponseModel> call, Response<ExtraDocketResponseModel> response) {
                ExtraDocketResponseModel responseModel = response.body();
                ArrayList<ExtraDocketListModel> extraListValue = new ArrayList<>();
                ArrayList<docketBarCodeSeries_Tbl> bcList = new ArrayList<>();
                rl_progressbar.setVisibility(View.GONE);
                if (responseModel.getList().size() > 0) {
                    Realm realm = Realm.getDefaultInstance();

                    for (ExtraDocketListModel ex : responseModel.getList()) {
                        docketBarCodeSeries_Tbl listModel = new docketBarCodeSeries_Tbl();

                        listModel.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                        listModel.setCompanyCode(ex.getCompanyCode());
                        listModel.setDockNo(ex.getDockNo());
                        listModel.setDockSf(ex.getDockSf());
                        listModel.setBcSerialNo(ex.getBcSerialNo());
                        listModel.setLsThcNo(thcNo);
                        listModel.setBcsrNumber("0");
                        listModel.setBarcodeScanned(true);
                        listModel.setCompanyCode(ex.getCompanyCode());
                        listModel.setBarcodeType(ConstantData.SP_KEY_EXTRA);
                        listModel.setServerPush(false);

                        bcList.add(listModel);

                    }
                    realm.executeTransaction(r -> {
                        r.insertOrUpdate(bcList);
                    });
                    updateExtraDocket();
                } else {
                    Toast.makeText(THCBarcodePickupActivity.this, "Barcode not found in system", Toast.LENGTH_SHORT).show();
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

    public void showExtraPackageDialog(String isDamage, TempDataStore mDialogListener) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        if (isDamage == "IsDamage") {
            dialog.setContentView(R.layout.dialog_term_of_damage_services);
        } else {
            dialog.setContentView(R.layout.dialog_term_of_success_services);
        }

        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.sample(false);
//                resumeScanner();
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.sample(true);
//                resumeScanner();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    protected void resumeScanner() {

        if (!dbvBarcode.isActivated())
            dbvBarcode.resume();
        //Log.d("peeyush-pause", "paused: false");
    }

    protected void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    private void updateDocketData(Realm realm, String dockNo, String bcSerialNo, String dockSf) {
        ThcDocketList_Tbl thcDocketList = realm.where(ThcDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("dockNo", dockNo)
                .equalTo("dockSf", dockSf).findFirst();

        if (thcDocketList != null) {
            thcDocketList.setScanPackages(thcDocketList.getScanPackages() + 1);
            thcDocketList.setScanDocket(true);
            thcDocketList.setLastBcSerialNo(bcSerialNo);
            realm.insertOrUpdate(thcDocketList);
        }

        THCHeader_Tbl thcHeader = realm.where(THCHeader_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("thcNo", thcNo).findFirst();
        if (thcHeader != null) {
            thcHeader.setTotalScanBarcode(thcHeader.getTotalScanBarcode() + 1);
            thcHeader.setLastBarcodePkg(bcSerialNo);
            realm.insertOrUpdate(thcHeader);
        }

        if (thcHeader.getLastBarcodePkg() != null) {
            mLinPkg.setVisibility(View.VISIBLE);
            mTxtLastScanPkg.getPaint().setUnderlineText(true);
            mTxtLastScanPkg.setText(thcHeader.getLastBarcodePkg());
        }

//        mTxtLastScanPkg.setText(thcHeader.getLastBarcodePkg());
        IsContinuousScanning = true;
        etDocketNo.setText("");
        etDocketNo.requestFocus();

    }

    @SuppressLint("SetTextI18n")
    private void getCount() {
        TextView tvPendingDkt = findViewById(R.id.tv_pending_dkt);
        TextView tvPendingPkg = findViewById(R.id.tv_pending_pkg);

        TextView tvCompleteDkt = findViewById(R.id.tv_complete_dkt);
        TextView tvCompletePkg = findViewById(R.id.tv_complete_pkg);


        ArrayList<ThcDocketList_Tbl> docketDetailArrayList = new ArrayList<>();
        int pendingDkt = 0, completeDkt = 0, pendingPkg = 0;
        completePkg = 0;

        RealmResults<ThcDocketList_Tbl> ThcDocketList = realm.where(ThcDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("ThcNo", thcNo).findAll();

        for (ThcDocketList_Tbl docketDetailList : ThcDocketList) {
            if (!docketDetailList.getScanPackages().equals(docketDetailList.getTotalLoadPackages())) {
                pendingDkt++;
            }
            if (docketDetailList.getScanDocket()) {
                completeDkt++;
                docketDetailArrayList.add(docketDetailList);
            }

            completePkg = completePkg + docketDetailList.getScanPackages();
            pendingPkg = pendingPkg + docketDetailList.getTotalLoadPackages();
        }

        pendingPkg = pendingPkg - completePkg;

        tvPendingPkg.setText(pendingPkg + "");
        tvCompletePkg.setText(completePkg + "");
        tvPendingDkt.setText(pendingDkt + "");
        tvCompleteDkt.setText(completeDkt + "");

        updateExtraDocket();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvScannedDocket.setLayoutManager(linearLayoutManager);
        RVThcScaneAdapter thcScaneAdapter = new RVThcScaneAdapter(docketDetailArrayList);
        rvScannedDocket.setAdapter(thcScaneAdapter);
    }

    private void updateExtraDocket() {
        TextView tvExtraPkg = findViewById(R.id.tv_extra_pkg);

        long extraPkgCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("LsThcNo", thcNo)
                .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                .count();

        tvExtraPkg.setText(extraPkgCount + "");

    }

    //onCLick Close
    private void thcclose() {
        finish();
    }

    private void refresh() {
        startActivity(getIntent());
        finish();
    }

    private void manuallyscan() {
        isContinueScanned = false;
        String barcode = edtDocketNo.getText().toString().trim();
        scanBarcode(barcode);
    }

    @Override
    public void onClick(View view) {

        LinearLayout ll_scanManually = findViewById(R.id.ll_scanManually);

        if (view.getId() == R.id.tv_scan) {
            select.animate().x(0).setDuration(100);
            tvScan.setTextColor(Color.WHITE);
            tvManually.setTextColor(def);
            if (IsHHTDevice) {
                dbvBarcode.setVisibility(View.GONE);
                dbvBarcode.pause();
            } else {
                dbvBarcode.setVisibility(View.VISIBLE);
            }
            ll_scanManually.setVisibility(View.GONE);

        } else if (view.getId() == R.id.tv_manually) {
            tvScan.setTextColor(def);
            tvManually.setTextColor(Color.WHITE);
            int size = tvManually.getWidth();
            select.animate().x(size).setDuration(100);
            if (IsHHTDevice) {
                dbvBarcode.setVisibility(View.GONE);
                dbvBarcode.pause();
            } else {
                dbvBarcode.setVisibility(View.GONE);
            }
            ll_scanManually.setVisibility(View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        getCount();
        if (InwardTHCListActivity.IsStockUpdate) {
            InwardTHCListActivity.IsStockUpdate = false;
            THCBarcodePickupActivity.this.finish();
        }
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
            dbvBarcode.resume();
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

    @Override
    public void onPause() {
        super.onPause();
        try {
            if(IsHoneyWell){
                try {
                    unregisterReceiver(scanReceiver);
                } catch (Exception e) {
                    Log.e("TAG", "Barcode receiver unregistration error: " + e.getMessage());
                }
            }
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
            if (IsHHTDevice) {
                if (mScanner != null) {
                    mScanner.aDecodeSetResultType(mBackupResultType);
                }
            } else {
                dbvBarcode.pause();
            }
        } catch (Exception e) {
            Log.e("TAG", " error : " + e.getMessage());
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    @Override
    protected void onDestroy() {
        if (IsHHTDevice) {
            if (mScanner != null) {
                mScanner.aDecodeSetResultType(mBackupResultType);
            }
            mScanner = null;
        }
        super.onDestroy();
    }

    private void showDocketList(boolean stetus) {

        final Dialog dialog = new Dialog(THCBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thc_pending_docket_list);

        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);
        ImageView ivCloseDocket = dialog.findViewById(R.id.iv_closePendingDocketList);
        RecyclerView rvpendingDocket = dialog.findViewById(R.id.rv_pendingDocket);

        if (stetus)
            tv_header.setText(R.string.label_completed_docket);
        else
            tv_header.setText(R.string.label_pending_docket);

        ivCloseDocket.setOnClickListener(v -> dialog.cancel());

        RealmResults<ThcDocketList_Tbl> thcDocketList =
                realm.where(ThcDocketList_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("ThcNo", thcNo).findAll();

        ArrayList<ThcDocketList_Tbl> thcDocketArrayList = new ArrayList<>();

        for (ThcDocketList_Tbl thcDocket : thcDocketList) {
            if (!stetus && !thcDocket.getTotalLoadPackages().equals(thcDocket.getScanPackages()))
                thcDocketArrayList.add(thcDocket);
            else if (stetus && thcDocket.getScanDocket())
                thcDocketArrayList.add(thcDocket);
        }

        rvpendingDocket.setLayoutManager(new LinearLayoutManager(THCBarcodePickupActivity.this, RecyclerView.VERTICAL, false));
        ThcDocketListAdapter thcPendingBarcodeAdapter = new ThcDocketListAdapter(thcDocketArrayList, this, stetus);
        rvpendingDocket.setAdapter(thcPendingBarcodeAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    @SuppressLint("SetTextI18n")
    private void save() {

        if (completePkg == 0) {
            Toast.makeText(this, "Scan atleast one barcode", Toast.LENGTH_SHORT).show();
        } else {
            tvThcSave.setClickable(false);
            exArrList.clear();
            RealmResults<docketBarCodeSeries_Tbl> extraPkg = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("LsThcNo", thcNo)
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("isServerPush", false)
                    .equalTo("bcsrNumber", "1")
                    .equalTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                    .findAll();
            Log.d("TAG", "stockupdate Extra count: " + extraPkg.size());
            if (extraPkg.size() > 0) {
                updateExtraBarcode(extraPkg);
            }

            final Dialog dialog = new Dialog(THCBarcodePickupActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_thc_complete_pickup);

            //onClick Close
            ImageView iv_AlertDismiss = dialog.findViewById(R.id.iv_AlertDismiss);
            iv_AlertDismiss.setOnClickListener(v -> {
                tvThcSave.setClickable(true);
                dialog.dismiss();
            });

            EditText priviewThcNo, priviewDocket, priviewPackage, priviewDateOfBooking, priviewDestination;

            priviewThcNo = dialog.findViewById(R.id.priview_ThcNo);
            priviewDocket = dialog.findViewById(R.id.priview_Docket);
            priviewPackage = dialog.findViewById(R.id.priview_Package);
            priviewDateOfBooking = dialog.findViewById(R.id.priview_DateOfBooking);
            priviewDestination = dialog.findViewById(R.id.priview_Destination);
            /*priview_PersonName = dialog.findViewById(R.id.priview_PersonName);*/

            RealmResults<ThcDocketList_Tbl> thcDocketList = realm.where(ThcDocketList_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("ThcNo", thcNo).findAll();

            int scanDocket = 0, scanPackage = 0, totalPackage = 0;

            for (ThcDocketList_Tbl thcDocket : thcDocketList) {
                if (thcDocket.getScanPackages() > 0) {
                    scanDocket++;
                    scanPackage += thcDocket.getScanPackages();
                }
                totalPackage += thcDocket.getTotalLoadPackages();
            }

            priviewThcNo.setText(thcNo);
            priviewDocket.setText(scanDocket + "/" + thcDocketList.size());
            priviewPackage.setText(scanPackage + "/" + totalPackage);
            priviewDateOfBooking.setText(CommonMethod.getCurrentDateStringNew());
            assert thcDocketList.get(0) != null;
            priviewDestination.setText(thcDocketList.get(0).getDestination());

            //Spinner spAlertArrivalCondition
            final AutoCompleteTextView spAlertArrivalCondition = dialog.findViewById(R.id.priview_ArrivalCondition);

            RealmResults<ArrivalCondition> arrivalConditionList = realm.where(ArrivalCondition.class).findAll();

            ArrayList<String> ArrivalConditionList = new ArrayList<>();

            for (ArrivalCondition arrivalCondition : arrivalConditionList)
                ArrivalConditionList.add(arrivalCondition.getCodeDesc());

            ArrayAdapter<String> arrivalConditionAdapter = new ArrayAdapter<>(THCBarcodePickupActivity.this, android.R.layout.simple_spinner_item, ArrivalConditionList);
            arrivalConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAlertArrivalCondition.setAdapter(arrivalConditionAdapter);

            //Spinner spAlertGoDown
            final AutoCompleteTextView spAlertGoDown = dialog.findViewById(R.id.priview_Godown);

            RealmResults<WareHouse> wareHousesList = realm.where(WareHouse.class).findAll();

            ArrayList<String> GoDownList = new ArrayList<>();

            for (WareHouse wareHouses : wareHousesList)
                GoDownList.add(wareHouses.getCodeDesc());

            ArrayAdapter<String> goDownAdapter = new ArrayAdapter<>(THCBarcodePickupActivity.this, android.R.layout.simple_spinner_item, GoDownList);
            goDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAlertGoDown.setAdapter(goDownAdapter);

            //Spinner spDEPSReason
            final AutoCompleteTextView spDelivaryType = dialog.findViewById(R.id.priview_DelivaryType);

            RealmResults<DeliveryType> DelivaryTypeList = realm.where(DeliveryType.class).findAll();

            ArrayList<String> deliveryType = new ArrayList<>();

            for (DeliveryType delivery : DelivaryTypeList)
                deliveryType.add(delivery.getCodeDesc());

            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(THCBarcodePickupActivity.this, android.R.layout.simple_spinner_item, deliveryType);
            reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDelivaryType.setAdapter(reasonAdapter);

            THCHeader_Tbl thcHeader = realm.where(THCHeader_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("thcNo", thcNo).findFirst();

            assert thcHeader != null;
            final String[] ArrivalCondition = {thcHeader.getArrivalCondition()};
            final String[] Godown = {thcHeader.getGodown()};
            final String[] DelivaryType = {thcHeader.getDeliveryType()};

            spAlertArrivalCondition.setText(thcHeader.getArrivalCondition(), false);
            spAlertArrivalCondition.setOnItemClickListener((parent, view, position, id) -> ArrivalCondition[0] = ArrivalConditionList.get(position));

            spAlertGoDown.setText(thcHeader.getGodown(), false);
            spAlertGoDown.setOnItemClickListener((parent, view, position, id) -> Godown[0] = GoDownList.get(position));

            spDelivaryType.setText(thcHeader.getDeliveryType(), false);
            spDelivaryType.setOnItemClickListener((parent, view, position, id) -> DelivaryType[0] = deliveryType.get(position));

            //onClick StockUpdate
            TextView priview_next = dialog.findViewById(R.id.priview_next);
            priview_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check()) {
                        pushTHCDockets();
                        realm.executeTransaction(realm -> {

                            int ArrivalCode = 0;

                            for (ArrivalCondition arrivalCondition : arrivalConditionList)
                                if (arrivalCondition.getCodeDesc().equals(ArrivalCondition[0]))
                                    ArrivalCode = arrivalCondition.getCodeId();

                            THCHeader_Tbl thcHeader = realm.where(THCHeader_Tbl.class)
                                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                    .equalTo("thcNo", thcNo).findFirst();

                            if (ArrivalCondition[0] != null || !ArrivalCondition[0].equals("")) {
                                assert thcHeader != null;
                                thcHeader.setArrivalCondition(ArrivalCondition[0]);
                                thcHeader.setArrivalCode(ArrivalCode);
                            }
                            if (Godown[0] != null || !Godown[0].equals("")) {
                                assert thcHeader != null;
                                thcHeader.setGodown(Godown[0]);
                            }
                            if (DelivaryType[0] != null || !DelivaryType[0].equals("")) {
                                assert thcHeader != null;
                                thcHeader.setDeliveryType(DelivaryType[0]);
                            }

                            assert thcHeader != null;
                            realm.insertOrUpdate(thcHeader);

                            for (ThcDocketList_Tbl thcDocketDetail : thcDocketList) {
                                ThcDocketList_Tbl thcDocket = realm.where(ThcDocketList_Tbl.class)
                                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                        .equalTo("dockNo", thcDocketDetail.getDockNo())
                                        .equalTo("dockSf", thcDocketDetail.getDockSf())
                                        .equalTo("ThcNo", thcNo).findFirst();

                                if (ArrivalCondition[0] != null || !ArrivalCondition[0].equals("")) {
                                    assert thcDocket != null;
                                    thcDocket.setArrivalCondition(ArrivalCondition[0]);
                                    thcDocket.setArrivalCode(ArrivalCode);
                                }
                                if (Godown[0] != null || !Godown[0].equals("")) {
                                    assert thcDocket != null;
                                    thcDocket.setGodown(Godown[0]);
                                }
                                if (DelivaryType[0] != null || !DelivaryType[0].equals("")) {
                                    assert thcDocket != null;
                                    thcDocket.setDeliveryType(DelivaryType[0]);
                                }
                                assert thcDocket != null;
                                realm.insertOrUpdate(thcDocket);
                            }
                        });
                        dialog.dismiss();

                        Intent intent = new Intent(THCBarcodePickupActivity.this, VerifyTHCSummaryActivity.class);
                        THCBarcodePickupActivity.this.startActivity(intent);
                        /*if(IsupdateDocket){
                            Intent intent = new Intent(THCBarcodePickupActivity.this, VerifyTHCSummaryActivity.class);
                            THCBarcodePickupActivity.this.startActivity(intent);
                        }else {
                            Toast.makeText(THCBarcodePickupActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }

                private boolean check() {
                    if (ArrivalCondition[0] == null || ArrivalCondition[0].equals("")) {
                        Toast.makeText(THCBarcodePickupActivity.this, "Enter ArrivalCondition", Toast.LENGTH_SHORT).show();
                        return false;
                    }

//                    if (Godown[0].equals("") || Godown[0].isEmpty()) {
                    if (Godown[0] == null || Godown[0].equals("")) {
                        Toast.makeText(THCBarcodePickupActivity.this, "Enter Godown", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (DelivaryType[0] == null || DelivaryType[0].equals("")) {
                        Toast.makeText(THCBarcodePickupActivity.this, "Enter DelivaryType", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    tvThcSave.setClickable(true);
                    return true;
                }
            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    private void pushTHCDockets() {
        String thcNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        long scanTHCDocketCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("LsThcNo", thcNo).count();
        Log.d("TAG", "scan THCDockets count: " + scanTHCDocketCount);

        if (scanTHCDocketCount > 0) {
            RealmResults<docketBarCodeSeries_Tbl> pkgList = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("LsThcNo", thcNo).findAll();

            if (pkgList.size() > 0) {
                ArrayList<ScanTHCDocketDetailsModel> scanDkt = new ArrayList<>();
                ScanDocketTHCModel scanHeader = new ScanDocketTHCModel();

                scanHeader.setAppVersion(CommonMethod.getAppVersionName(this));
                scanHeader.setDeviceDetails(getModelNumber + "," + brand + "," + getRam);
                scanHeader.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                scanHeader.setOSVersion(deviceVersionNo);
                scanHeader.setDeviceId(CommonMethod.getDeviceId(this));
                scanHeader.setDeviceStorage(String.valueOf(getRam));
                scanHeader.setScanLocation(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                scanHeader.setIMEINo("");
                scanHeader.setModelName(Build.MODEL);

                RealmResults<docketBarCodeSeries_Tbl> docketTHCList = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("LsThcNo", thcNo)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("isBarcodeScanned", true)
                        .notEqualTo("barcodeType", ConstantData.SP_KEY_EXTRA)
                        .equalTo("isServerPush", false).findAll();

                for (docketBarCodeSeries_Tbl d : docketTHCList) {
                    ScanTHCDocketDetailsModel model = new ScanTHCDocketDetailsModel();
                    model.setBCSerialNo(d.getBcSerialNo());
                    model.setCompanyCode(d.getCompanyCode());
                    model.setDockNo(d.getDockNo());
                    model.setDockSf(d.getDockSf());
                    model.setIsManual(false);
                    model.setExtraBarcode(false);
                    model.setIsBarcodeScanned(d.getBarcodeScanned());
                    model.setTHCno(d.getLsThcNo());
                    model.setPackages(1);
                    model.setVolume(d.getActualweight());
                    model.setWeight(d.getActualweight());
                    if (d.getBarcodeType().equalsIgnoreCase(ConstantData.SP_KEY_DAMAGE)) {
                        model.setDamage(true);
                    } else {
                        model.setDamage(false);
                    }
                    model.setPilferage(false);

                    scanDkt.add(model);
                }
                scanHeader.setDetails(scanDkt);
                pushTHCDockerAPI(scanHeader, docketTHCList);
            }

        }
    }

    private void pushTHCDockerAPI(ScanDocketTHCModel scanHeader, RealmResults<docketBarCodeSeries_Tbl> docketTHCList) {

        if (scanHeader.getDetails().size() > 0) {
            ArrayList<docketBarCodeSeries_Tbl> dktList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> dktTempList = new ArrayList<>();
            dktList.addAll(realm.copyFromRealm(docketTHCList));
            rl_progressbar.setVisibility(View.VISIBLE);
            Log.d("TAG", "push THCDocker API: " + new Gson().toJson(scanHeader));
            Call<ScanDocketResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .packageTHCUpdate(scanHeader);
            call.enqueue(new Callback<ScanDocketResponse>() {
                @Override
                public void onResponse(Call<ScanDocketResponse> call, Response<ScanDocketResponse> response) {
                    if (response.body() != null) {
                        rl_progressbar.setVisibility(View.GONE);
                        ScanDocketResponse res = response.body();
                        Log.d("TAG", "scan THC Docket Response: " + new Gson().toJson(res));
                        if (res.isSuccess()) {
                            Realm realm = Realm.getDefaultInstance();
                            for (docketBarCodeSeries_Tbl docket : dktList) {
                                Log.d("TAG", "onResponse THC Docket: " + docket.getBcSerialNo());
                                docket.setServerPush(true);
                                dktTempList.add(docket);
                            }
                            realm.executeTransactionAsync(r -> {
                                r.insertOrUpdate(dktTempList);
                            });
//                            IsupdateDocket = true;
                        } else {
                            rl_progressbar.setVisibility(View.GONE);
                            CommonMethod.alertDialogBoxWithOk("Alert", res.getMessage(), getApplicationContext());
                            CommonMethod.appendLogs(res.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ScanDocketResponse> call, Throwable t) {
                    rl_progressbar.setVisibility(View.GONE);
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void updateExtraBarcode(RealmResults<docketBarCodeSeries_Tbl> extraPkg) {
        ArrayList<docketBarCodeSeries_Tbl> extraList = new ArrayList<>();

        if (extraPkg.size() > 0) {
            extraList.addAll(realm.copyFromRealm(extraPkg));

            ExtraBcSerialModel model = new ExtraBcSerialModel();

            model.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
            model.setBranchCode(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
            for (docketBarCodeSeries_Tbl md : extraList) {
                ExtraBcSerialListModel xModel = new ExtraBcSerialListModel();
                xModel.setCompanyCode(md.getCompanyCode());
                xModel.setBCSerialNo(md.getBcSerialNo());
                xModel.setDockNo(md.getDockNo());
                xModel.setDockSf(md.getDockSf());
                xModel.setPackages(1);
                xModel.setWeight(md.getActualweight());

                exArrList.add(xModel);
            }
            model.setList(exArrList);

            Log.d("TAG", "updateExtraBarcode Request: " + new Gson().toJson(model));

            Call<ExtraDocketResponseModel> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .extraBarcodeUpdate(model);
            call.enqueue(new Callback<ExtraDocketResponseModel>() {
                @Override
                public void onResponse(Call<ExtraDocketResponseModel> call, Response<ExtraDocketResponseModel> response) {
                    ExtraDocketResponseModel responseModel = response.body();
                    ArrayList<ExtraDocketListModel> extraListValue = new ArrayList<>();
                    ArrayList<docketBarCodeSeries_Tbl> bcList = new ArrayList<>();

                    if (responseModel.getList().size() > 0) {
                        Realm realm = Realm.getDefaultInstance();

                        for (ExtraDocketListModel ex : responseModel.getList()) {
                            docketBarCodeSeries_Tbl listModel = new docketBarCodeSeries_Tbl();

                            listModel.setUser(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                            listModel.setCompanyCode(ex.getCompanyCode());
                            listModel.setDockNo(ex.getDockNo());
                            listModel.setDockSf(ex.getDockSf());
                            listModel.setBcSerialNo(ex.getBcSerialNo());
                            listModel.setBcsrNumber("0");
                            listModel.setBarcodeScanned(true);
                            listModel.setCompanyCode(ex.getCompanyCode());
                            listModel.setBarcodeType(ConstantData.SP_KEY_EXTRA);
                            listModel.setServerPush(false);

                            bcList.add(listModel);

                        }


                        realm.executeTransaction(r -> {
                            r.insertOrUpdate(bcList);
                        });
                    } else {
                        Toast.makeText(THCBarcodePickupActivity.this, "Barcode not found in system", Toast.LENGTH_SHORT).show();
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

    public void showBarcodeList(boolean stetus, String dockNo, String dockSf) {
        Log.d("TAG", "onClick 123: ");
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thc_pending_barcode_list);

        ImageView iv_closePendingBarcodeList = dialog.findViewById(R.id.iv_closePendingBarcodeList);
        RecyclerView rv_pendingBarcode = dialog.findViewById(R.id.rv_pendingBarcode);
        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);

        if (stetus)
            tv_header.setText(R.string.label_completed_barcode);
        else
            tv_header.setText(R.string.label_pending_barcode);

        iv_closePendingBarcodeList.setOnClickListener(v1 -> dialog.cancel());

        RealmResults<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls =
                realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("dockNo", dockNo)
                        .equalTo("dockSf", dockSf)
                        .equalTo("isBarcodeScanned", stetus)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("barcodeType", "Inward").findAll();

        ArrayList<docketBarCodeSeries_Tbl> mDocketBarCodeSeriesTbls = new ArrayList<>(docketBarCodeSeriesTbls);

        rv_pendingBarcode.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ThcBarcodeListAdapter thcBarcodeListAdapter = new ThcBarcodeListAdapter(mDocketBarCodeSeriesTbls, this);
        rv_pendingBarcode.setAdapter(thcBarcodeListAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public void onBarcodeScanned(String barcodeData) {
        Toast.makeText(this, "Scanned: " + barcodeData, Toast.LENGTH_SHORT).show();
        scanBarcode(barcodeData);
    }
}