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
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import device.common.ScanConst;
import device.sdk.ScanManager;
import in.webx.scorpion.Adapter.DRSDocketListAdapter;
import in.webx.scorpion.Adapter.RVThcScaneAdapter;
import in.webx.scorpion.Adapter.ThcBarcodeListAdapter;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ArrivalCondition;
import in.webx.scorpion.RealmDatabase.DeliveryType;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.RealmDatabase.WareHouse;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.ScanResultReceiver;
import in.webx.scorpion.util.SharedPreference;
import io.realm.Realm;
import io.realm.RealmResults;

public class DRSBarcodePickupActivity extends AppCompatActivity implements View.OnClickListener {
    Activity activity = this;
    private TextView tvScan, tvManually, select, tvThcSave;
    private ColorStateList def;
    private DecoratedBarcodeView dbvBarcode;
    private boolean isContinueScanned = false;
    private EditText edtDocketNo;
    ImageView ivNavBack;
    int completePkg;
    Button btScan;
    RecyclerView rvScannedDocket;
    TextView tv_header;

    private boolean IsHHTDevice, IsHHTNewlandDevice, IsHHTUROVODevice, IsPM85Device, IsPM80Device, IsPM75Device;
    private static ScanManager mScanner;
    private ScanResultReceiver receiver;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    boolean IsContinuousScanning = true;
    private EditText etDocketNo;
    private InputMethodManager imm;
    private android.app.AlertDialog mDialog = null;

    private String thcNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_drsbarcode_pickup);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        //Define Function
        init();

    }

    private void init() {

        checkDeviceType();
        mScanner = new ScanManager();

        SharedPreference.getInstance(this);

        thcNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);

        tv_header = findViewById(R.id.tv_header);
        tvScan = findViewById(R.id.tv_scan);
        tvManually = findViewById(R.id.tv_manually);
        tvScan.setOnClickListener(this);
        tvManually.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = tvManually.getTextColors();
        etDocketNo = findViewById(R.id.etDocketNo);
        rvScannedDocket = findViewById(R.id.rv_scannedDocket);

        ivNavBack = findViewById(R.id.iv_nav_back);
        ImageView ivthcrefresh = findViewById(R.id.iv_drs_refresh);
        dbvBarcode = findViewById(R.id.dbv_barcode);
        edtDocketNo = findViewById(R.id.edt_docketNo);
        btScan = findViewById(R.id.btn_scan);
        LinearLayout ll_thcPending = findViewById(R.id.ll_drs_pending);
        LinearLayout ll_thcComplete = findViewById(R.id.ll_drs_complete);
        tvThcSave = findViewById(R.id.tv_drs_Save);

        ivNavBack.setOnClickListener(v -> thcclose());
        ivthcrefresh.setOnClickListener(v -> refresh());
        btScan.setOnClickListener(v -> manuallyscan());
        ll_thcPending.setOnClickListener(v -> showDocketList(false));
        ll_thcComplete.setOnClickListener(v -> showDocketList(true));
//        tvThcSave.setOnClickListener(v -> save());
        tvThcSave.setOnClickListener(v -> pickupsummary());


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

    private void hideDefaultKeyboard(View et) {
        getMethodManager().hideSoftInputFromWindow(et.getWindowToken(), 0);
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
        Log.d("TAG", " barcode : " + barcode);

        if (!IsHHTDevice) {
            new Handler().postDelayed(() -> {
                dbvBarcode.pause();
                dbvBarcode.resume();
            }, 1000);
        }
        /*if(!IsHHTNewlandDevice){
            if (!IsHHTDevice || !IsHHTNewlandDevice || !IsPM85Device || !IsPM80Device) {
                new Handler().postDelayed(() -> {
                    dbvBarcode.pause();
                    dbvBarcode.resume();
                }, 1000);
            }
        }*/
        if (barcode.length() != 0 && !barcode.equalsIgnoreCase("")) {
            realm.executeTransaction(realm -> {
                docketBarCodeSeries_Tbl model = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("bcSerialNo", barcode)
                        .equalTo("LsThcNo", thcNo).findFirst();

                if (model != null) {
                    if (model.getBarcodeScanned()) {
                        CommonMethod.showToastMessage(DRSBarcodePickupActivity.this, "This barcode is already scanned.");
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
                    CommonMethod.showToastMessage(DRSBarcodePickupActivity.this, "Invalid barcode.");
                }
            });
        } else {
            IsContinuousScanning = true;
            etDocketNo.requestFocus();
            CommonMethod.showToastMessage(DRSBarcodePickupActivity.this, "Invalid barcode.");
        }

        getCount();
//        setrvscanneddocketadapter();
        IsContinuousScanning = true;
        etDocketNo.requestFocus();
    }

    //Set PendingCount
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
            realm.insertOrUpdate(thcHeader);
        }

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


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvScannedDocket.setLayoutManager(linearLayoutManager);
        RVThcScaneAdapter thcScaneAdapter = new RVThcScaneAdapter(docketDetailArrayList);
        rvScannedDocket.setAdapter(thcScaneAdapter);
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

    //Barcode Scan Manually
    private void manuallyscan() {
        isContinueScanned = false;
        String barcode = edtDocketNo.getText().toString().trim();
        Log.d("TAG", "onClick: " + barcode);
        scanBarcode(barcode);
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

    //onResume
    public void onResume() {
        super.onResume();
        if (InwardTHCListActivity.IsStockUpdate) {
            InwardTHCListActivity.IsStockUpdate = false;
            DRSBarcodePickupActivity.this.finish();
        }
        Log.d("TAG", "onResume: " + IsHHTDevice);
        if (IsHHTUROVODevice || IsHHTNewlandDevice) {
            Log.d("TAG", "check urovo");
            initScanner();
        } else if (IsPM80Device || IsPM85Device || IsPM75Device) {
            Log.d("TAG", "check PM80 :" + mScanner.aDecodeGetDecodeEnable());
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
        Log.d("TAG", "initScanner: " + mScanner);
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
        super.onPause();
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
            Log.d("TAG", "onPause: " + IsPM80Device);
            if (IsHHTDevice) {
                if (mScanner != null) {
                    mScanner.aDecodeSetResultType(mBackupResultType);
                }
            } else {
                dbvBarcode.pause();
            }
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            Log.e("TAG", " error : " + e.getMessage());
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

    //onClick Pending or Completed
    private void showDocketList(boolean stetus) {

        final Dialog dialog = new Dialog(DRSBarcodePickupActivity.this);
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

        rvpendingDocket.setLayoutManager(new LinearLayoutManager(DRSBarcodePickupActivity.this, RecyclerView.VERTICAL, false));
        DRSDocketListAdapter thcPendingBarcodeAdapter = new DRSDocketListAdapter(thcDocketArrayList, DRSBarcodePickupActivity.this, stetus);
        rvpendingDocket.setAdapter(thcPendingBarcodeAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    //onClick Save
    @SuppressLint("SetTextI18n")
    private void save() {

        if (completePkg == 0) {
            Toast.makeText(this, "Scan atleast one barcode", Toast.LENGTH_SHORT).show();
        } else {
            tvThcSave.setClickable(false);

            final Dialog dialog = new Dialog(DRSBarcodePickupActivity.this);
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

            ArrayAdapter<String> arrivalConditionAdapter = new ArrayAdapter<>(DRSBarcodePickupActivity.this, android.R.layout.simple_spinner_item, ArrivalConditionList);
            arrivalConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAlertArrivalCondition.setAdapter(arrivalConditionAdapter);

            //Spinner spAlertGoDown
            final AutoCompleteTextView spAlertGoDown = dialog.findViewById(R.id.priview_Godown);

            RealmResults<WareHouse> wareHousesList = realm.where(WareHouse.class).findAll();

            ArrayList<String> GoDownList = new ArrayList<>();

            for (WareHouse wareHouses : wareHousesList)
                GoDownList.add(wareHouses.getCodeDesc());

            ArrayAdapter<String> goDownAdapter = new ArrayAdapter<>(DRSBarcodePickupActivity.this, android.R.layout.simple_spinner_item, GoDownList);
            goDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAlertGoDown.setAdapter(goDownAdapter);

            //Spinner spDEPSReason
            final AutoCompleteTextView spDelivaryType = dialog.findViewById(R.id.priview_DelivaryType);

            RealmResults<DeliveryType> DelivaryTypeList = realm.where(DeliveryType.class).findAll();

            ArrayList<String> deliveryType = new ArrayList<>();

            for (DeliveryType delivery : DelivaryTypeList)
                deliveryType.add(delivery.getCodeDesc());

            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<>(DRSBarcodePickupActivity.this, android.R.layout.simple_spinner_item, deliveryType);
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
                    if (chack()) {
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
                        Intent intent = new Intent(DRSBarcodePickupActivity.this, DocketThcSummaryActivity.class);
                        DRSBarcodePickupActivity.this.startActivity(intent);
                    }
                }

                private boolean chack() {
                    if (ArrivalCondition[0] == null || ArrivalCondition[0].equals("")) {
                        Toast.makeText(DRSBarcodePickupActivity.this, "Enter ArrivalCondition", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (Godown[0].equals("") || Godown[0] == null) {
                        Toast.makeText(DRSBarcodePickupActivity.this, "Enter Godown", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (DelivaryType[0] == null || DelivaryType[0].equals("")) {
                        Toast.makeText(DRSBarcodePickupActivity.this, "Enter DelivaryType", Toast.LENGTH_SHORT).show();
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

    public void showBarcodeList(boolean stetus, String dockNo) {

        Log.d("TAG", "onClick: ");
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

    private void pickupsummary() {
        final Dialog dialog = new Dialog(DRSBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_drs_summary);
        TextView txt_DRS_no, txt_Docket, txt_Packages, txt_gen_date, txt_booked_at;
        TextView txt_driver_name, txt_veh_no, txt_veh_type, txt_odometer_read, txt_weight, txt_capacity;
        TextView txt_vendor_name, txt_person_name;
        TextView tvGenerateDRS;
        TextView tv_header;

        tv_header = dialog.findViewById(R.id.tv_header);
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
//            getbarcode("VH/BNGB/1920/000009");
//            Intent i = new Intent(DRSConfirmationActivity.this, DRSBarcodePickupActivity.class);
//            startActivity(i);
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}