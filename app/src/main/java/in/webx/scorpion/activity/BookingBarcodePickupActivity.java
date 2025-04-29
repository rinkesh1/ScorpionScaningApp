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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import device.common.ScanConst;
import device.sdk.ScanManager;
import in.webx.scorpion.Adapter.RVBookingFinishBarcodeListAdapter;
import in.webx.scorpion.Adapter.RVBookingPendingBarcodeListAdapter;
import in.webx.scorpion.Adapter.RVBookingPendingDocketBarcodeListAdapter;
import in.webx.scorpion.Adapter.RVBookingScaneAdapter;
import in.webx.scorpion.Model.BookingDocketList_Tbl;
import in.webx.scorpion.Model.BookingDocketStockBarCodeSeries;
import in.webx.scorpion.Model.BookingDocketStockDetail;
import in.webx.scorpion.Model.BookingStockUpdate;
import in.webx.scorpion.Model.BookingdocketBarCodeSeries_Tbl;
import in.webx.scorpion.Model.GetBookingStockUpdate;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.ScanResultReceiver;
import in.webx.scorpion.util.SharedPreference;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingBarcodePickupActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean IsHHTDevice, IsHHTNewlandDevice, IsHHTUROVODevice, IsPM85Device, IsPM80Device, IsPM75Device;
    private static ScanManager mScanner;
    private ScanResultReceiver receiver;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    boolean IsContinuousScanning = true;
    private EditText etDocketNo,edt_docketNo;
    private InputMethodManager imm;
    private TextView tv_scan,tv_manually,select,tv_booking_Save;
    private ColorStateList def;
    private DecoratedBarcodeView dbv_barcode;
    ImageView iv_nav_back;
    int completePkg;
    Button btn_scan;
    Dialog dialog;
    private android.app.AlertDialog mDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_booking_barcode_pickup);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.StatusBarTransparent));
        //Define Function
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        Log.d("TAG", "BookingBarcodePickupActivity");
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
        edt_docketNo = findViewById(R.id.edt_docketNo);
        btn_scan = findViewById(R.id.btn_scan);

        iv_nav_back = findViewById(R.id.iv_nav_back);
        ImageView ivbookingrefresh = findViewById(R.id.iv_booking_refresh);

        LinearLayout ll_bookingPending = findViewById(R.id.ll_booking_pending);
        LinearLayout ll_bookingComplete = findViewById(R.id.ll_booking_complete);

        tv_booking_Save = findViewById(R.id.tv_booking_Save);
        iv_nav_back.setOnClickListener(v -> thcclose());
        ivbookingrefresh.setOnClickListener(v -> refresh());
        btn_scan.setOnClickListener(v -> manuallyscan());
        ll_bookingPending.setOnClickListener(v -> showDocketList(false));
        ll_bookingComplete.setOnClickListener(v -> showDocketList(true));
        tv_booking_Save.setOnClickListener(v -> save());


        //Barcode Scan Camera
        dbv_barcode.decodeContinuous(new BarcodeCallback() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void barcodeResult(BarcodeResult result) {
                dbv_barcode.resume();
                scanBarcode(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });

        getCount();
        setrvscanneddocketadapter();

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
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        etDocketNo.setOnTouchListener((v, event) -> {
            v.onTouchEvent(event);
            if (IsContinuousScanning)
                hideDefaultKeyboard(v);
            return true;
        });

        if (IsHHTDevice) {
            dbv_barcode.setVisibility(View.GONE);
            dbv_barcode.pause();
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

       /* registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String barcode = intent.getStringExtra("SCAN_BARCODE1");
                    if (barcode != null) {
//                        Log.d("TAG", "get Scan Data: " + barcode);
                        scanBarcode(barcode);
                    } else {
                        Toast.makeText(getApplicationContext(), "Barcode Not getting.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Scan Failed", Toast.LENGTH_LONG).show();
                }
            }
        }, new IntentFilter("nlscan.action.SCANNER_RESULT"));*/
    }

    @SuppressLint("SetTextI18n")
    private void getCount() {
        TextView mTvpendingDkt = findViewById(R.id.tv_pending_dkt);
        TextView mTvpendingPkg = findViewById(R.id.tv_pending_pkg);

        TextView mTvcompleteDkt = findViewById(R.id.tv_complete_dkt);
        TextView mTvcompletePkg = findViewById(R.id.tv_complete_pkg);

        int pendingDkt = 0, completeDkt = 0, pendingPkg = 0;
        completePkg = 0;


        RealmResults<BookingdocketBarCodeSeries_Tbl> docketBarCodeSeries_tbls;
        docketBarCodeSeries_tbls = realm.where(BookingdocketBarCodeSeries_Tbl.class)
                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)).findAll();

        for (BookingdocketBarCodeSeries_Tbl docketBarCodeSeriesTbl : docketBarCodeSeries_tbls) {
            if (docketBarCodeSeriesTbl.getScanned()) {
                completePkg++;
            } else {
                pendingPkg++;
            }
        }

        RealmResults<BookingDocketList_Tbl> BookingDocketList_Tbls = realm.where(BookingDocketList_Tbl.class)
                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)).findAll();

        for (BookingDocketList_Tbl docketDetailListTbl : BookingDocketList_Tbls) {
            if (!docketDetailListTbl.getScanPackages().equals(docketDetailListTbl.getNoofPkgs())) {
                pendingDkt++;
            }
            if (docketDetailListTbl.getScanDocket()) {
                completeDkt++;
            }
        }

        mTvpendingPkg.setText(pendingPkg + "");
        mTvcompletePkg.setText(completePkg + "");
        mTvpendingDkt.setText(pendingDkt + "");
        mTvcompleteDkt.setText(completeDkt + "");
    }

    private void setrvscanneddocketadapter() {
        RecyclerView rv_scannedDocket = findViewById(R.id.rv_scannedDocket);

        RealmResults<BookingDocketList_Tbl> BookingDocketList_Tbls;
        BookingDocketList_Tbls = realm.where(BookingDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                .equalTo("scanDocket", true).findAll();

//        Log.d("TAG", "Barcode list: " + BookingDocketList_Tbls.size());

        ArrayList<BookingDocketList_Tbl> docketDetailListTbls = new ArrayList<>(BookingDocketList_Tbls);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_scannedDocket.setLayoutManager(linearLayoutManager);
        RVBookingScaneAdapter rvBookingScaneAdapter = new RVBookingScaneAdapter(docketDetailListTbls, this);
        rv_scannedDocket.setAdapter(rvBookingScaneAdapter);
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

//        Toast.makeText(BookingBarcodePickupActivity.this,deviceName+","+IsPM75Device,Toast.LENGTH_LONG).show();
    }

    //onCLick Close
    private void thcclose() {finish();}

    //onCLick Refresh
    private void refresh() {
        startActivity(getIntent());
        finish();
    }

    //Barcode Scan Manually
    private void manuallyscan() {
        String barcode = edt_docketNo.getText().toString().trim();
        Log.d("TAG", "onClick: " + barcode);
        scanBarcode(barcode);
    }

    //onClick Pending or Complete
    private void showDocketList(Boolean stetus) {

        final Dialog dialog = new Dialog(BookingBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thc_pending_docket_list);

        ImageView iv_close_docket = dialog.findViewById(R.id.iv_closePendingDocketList);
        RecyclerView rvpendingDocket = dialog.findViewById(R.id.rv_pendingDocket);
        AppCompatTextView tv_header = dialog.findViewById(R.id.tv_header);

        if (stetus)
            tv_header.setText(R.string.label_completed_docket);
        else
            tv_header.setText(R.string.label_pending_docket);

        iv_close_docket.setOnClickListener(v -> dialog.cancel());

        RealmResults<BookingDocketList_Tbl> thcDocketListTbls =
                realm.where(BookingDocketList_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO)).findAll();

        ArrayList<BookingDocketList_Tbl> mThcDocketListTbls = new ArrayList<>();

        for (BookingDocketList_Tbl thcDocketListTbl : thcDocketListTbls) {
            if (!stetus && !thcDocketListTbl.getNoofPkgs().equals(thcDocketListTbl.getScanPackages())) {
                mThcDocketListTbls.add(realm.copyFromRealm(thcDocketListTbl));
            } else if (stetus && thcDocketListTbl.getScanDocket())
                mThcDocketListTbls.add(realm.copyFromRealm(thcDocketListTbl));
        }

        rvpendingDocket.setLayoutManager(new LinearLayoutManager(BookingBarcodePickupActivity.this, RecyclerView.VERTICAL, false));
        RVBookingPendingDocketBarcodeListAdapter rvbookingPendingDocketBarcodeListAdapter = new RVBookingPendingDocketBarcodeListAdapter(mThcDocketListTbls, this,stetus);
        rvpendingDocket.setAdapter(rvbookingPendingDocketBarcodeListAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
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
        /*if (!IsHHTNewlandDevice) {
            if (!IsHHTDevice) {
                new Handler().postDelayed(() -> {
                    dbv_barcode.pause();
                    dbv_barcode.resume();
                }, 1000);
            }
        }*/
        if (barcode.length() != 0 && !barcode.equalsIgnoreCase("")) {
            realm.executeTransaction(r -> {
                BookingdocketBarCodeSeries_Tbl model;
                model = realm.where(BookingdocketBarCodeSeries_Tbl.class)
                        .equalTo("bcSeriesNo", barcode)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                        .findFirst();

                if (model != null) {
                    if (model.getScanned()) {
                        CommonMethod.showToastMessage(BookingBarcodePickupActivity.this, "This barcode is already scanned.");
                        etDocketNo.setText("");
                        etDocketNo.requestFocus();
                    } else {
                        beepSound();
                        model.setScanned(true);
                        r.insertOrUpdate(model);
                        updateDocketData(r, model.getDocketNo(), model.getBcSeriesNo());
                    }
                } else {
                    IsContinuousScanning = true;
                    etDocketNo.requestFocus();
                    CommonMethod.showToastMessage(BookingBarcodePickupActivity.this, "Invalid barcode.");
                }
            });
        } else {
            IsContinuousScanning = true;
            etDocketNo.requestFocus();
            CommonMethod.showToastMessage(BookingBarcodePickupActivity.this, "Invalid barcode.");
        }
        getCount();
        setrvscanneddocketadapter();
        IsContinuousScanning = true;
        etDocketNo.requestFocus();
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

    private void updateDocketData(Realm realm, String dockNo, String bcSerialNo) {
        BookingDocketList_Tbl bookingDocketList_tbl = realm.where(BookingDocketList_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                .equalTo("docketNo", dockNo).findFirst();

        if (bookingDocketList_tbl != null) {
            bookingDocketList_tbl.setScanPackages(bookingDocketList_tbl.getScanPackages() + 1);
            bookingDocketList_tbl.setScanDocket(true);
            bookingDocketList_tbl.setLastBcSerialNo(bcSerialNo);
            realm.insertOrUpdate(bookingDocketList_tbl);
        }

        IsContinuousScanning = true;
        etDocketNo.setText("");
        etDocketNo.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: " + IsHHTDevice);
        if (IsHHTUROVODevice || IsHHTNewlandDevice) {
            Log.d("TAG", "onResume: " + mScanner.aDecodeGetDecodeEnable());
            initScanner();
        } else if(IsPM80Device || IsPM85Device || IsPM75Device){
            Log.d("TAG", "check PM80 :"+mScanner.aDecodeGetDecodeEnable());
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
        }else {
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
                    (dialog2, which) -> {
                        Intent intent = new Intent(ScanConst.LAUNCH_SCAN_SETTING_ACITON);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog2.dismiss();
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
            Log.e("TAG", " error : " + e.getMessage());
        }
    }

    //onClick Save
    @SuppressLint("SetTextI18n")
    private void save() {

        dialog = new Dialog(BookingBarcodePickupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_complete_pickup);

        //onClick Close
        ImageView iv_AlertDismiss = dialog.findViewById(R.id.iv_AlertDismiss);
        RecyclerView rvBookingDocketListData = dialog.findViewById(R.id.rv_booking_docketList_data);


        iv_AlertDismiss.setOnClickListener(v -> {
            tv_booking_Save.setClickable(true);
            dialog.dismiss();
        });

        EditText priview_ThcNo, priview_Docket, priview_Package, priview_DateOfBooking;

        priview_ThcNo = dialog.findViewById(R.id.priview_ThcNo);
        priview_Docket = dialog.findViewById(R.id.priview_Docket);
        priview_Package = dialog.findViewById(R.id.priview_Package);
        priview_DateOfBooking = dialog.findViewById(R.id.priview_DateOfBooking);

        RealmResults<BookingDocketList_Tbl> thcDocketListTbls = realm.where(BookingDocketList_Tbl.class)
                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)).findAll();

        int scanDocket = 0, scanPackage = 0, totalPackage = 0;

        for (BookingDocketList_Tbl thcDocketList_tbl : thcDocketListTbls) {

            if (thcDocketList_tbl.getScanPackages() > 0) {
                scanDocket++;
                scanPackage += thcDocketList_tbl.getScanPackages();
            }
            totalPackage += thcDocketList_tbl.getNoofPkgs();
        }

        priview_ThcNo.setText(thcDocketListTbls.get(0).getVehicleNo());
        priview_Docket.setText(scanDocket + "/" + thcDocketListTbls.size());
        priview_Package.setText(scanPackage + "/" + totalPackage);
        priview_DateOfBooking.setText(CommonMethod.getCurrentDateStringNew());
        assert thcDocketListTbls.get(0) != null;

        TextView priview_next = dialog.findViewById(R.id.priview_next);
        TextView priview_finish = dialog.findViewById(R.id.priview_finish);
        priview_next.setOnClickListener(v -> stockupdate());
        priview_finish.setOnClickListener(v -> {
            tv_booking_Save.setClickable(true);
            dialog.cancel();
        });

        RealmResults<BookingDocketList_Tbl> bookingDocketListTbls =
                realm.where(BookingDocketList_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                        .equalTo("scanDocket", true).findAll();

        ArrayList<BookingDocketList_Tbl> mThcDocketListTbls = new ArrayList<>(bookingDocketListTbls);

        rvBookingDocketListData.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RVBookingFinishBarcodeListAdapter rvthcCompleteBarcodeListAdapter = new RVBookingFinishBarcodeListAdapter(mThcDocketListTbls, this);
        rvBookingDocketListData.setAdapter(rvthcCompleteBarcodeListAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void stockupdate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BookingBarcodePickupActivity.this);
        builder.setMessage("Do you want to Stock Update?");
        builder.setPositiveButton("Ok", (dialog, id) -> {

                    dialog.cancel();
                    BookingStockUpdate bookingStockUpdate = new BookingStockUpdate();
                    bookingStockUpdate.setCompanyCode(SharedPreference.getIntValue(ConstantData.SP_COMPANY_CODE));
                    bookingStockUpdate.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                    bookingStockUpdate.setCurrentBranch(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));

                    ArrayList<BookingDocketStockDetail> bookingDocketStockDetails = new ArrayList<>();
                    RealmResults<BookingDocketList_Tbl> bookingDocketListTbls = realm.where(BookingDocketList_Tbl.class)
                            .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                            .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO)).findAll();

                    for (BookingDocketList_Tbl bookingDocketListTbl : bookingDocketListTbls) {
                        BookingDocketStockDetail bookingDocketStockDetail = new BookingDocketStockDetail();
                        bookingDocketStockDetail.setDocketNo(bookingDocketListTbl.getDocketNo());
                        bookingDocketStockDetail.setOrigin(bookingDocketListTbl.getOrigin());
                        bookingDocketStockDetail.setNoofPkgs(bookingDocketListTbl.getNoofPkgs());
                        bookingDocketStockDetail.setVehicleNo(bookingDocketListTbl.getVehicleNo());
                        bookingDocketStockDetail.setDocketStatus(bookingDocketListTbl.getDocketStatus());

                        ArrayList<BookingDocketStockBarCodeSeries> bookingDocketStockBarCodeSeries = new ArrayList<>();

                        RealmResults<BookingdocketBarCodeSeries_Tbl> bookingdocketBarCodeSeriesTbls = realm.where(BookingdocketBarCodeSeries_Tbl.class)
                                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                                .equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO))
                                .equalTo("docketNo", bookingDocketListTbl.getDocketNo()).findAll();

                        for (BookingdocketBarCodeSeries_Tbl bookingdocketBarCodeSeries_Tbl : bookingdocketBarCodeSeriesTbls) {
                            BookingDocketStockBarCodeSeries bookingDocketStockBarCode = new BookingDocketStockBarCodeSeries();

                            bookingDocketStockBarCode.setDocketNo(bookingdocketBarCodeSeries_Tbl.getDocketNo());
                            bookingDocketStockBarCode.setBcSeriesNo(bookingdocketBarCodeSeries_Tbl.getBcSeriesNo());
                            bookingDocketStockBarCode.setScanned(bookingdocketBarCodeSeries_Tbl.getScanned());
                            bookingDocketStockBarCodeSeries.add(bookingDocketStockBarCode);
                        }
                        bookingDocketStockDetail.setLst_BCSeries(bookingDocketStockBarCodeSeries);
                        bookingDocketStockDetails.add(bookingDocketStockDetail);
                    }

                    bookingStockUpdate.setBookingScan_List(bookingDocketStockDetails);

                    Log.d("TAG", new Gson().toJson(bookingStockUpdate));

                    try {
                        if(CommonMethod.isNetworkConnected(this)) {
                            Call<GetBookingStockUpdate> call = MyRetro.createSecureServiceApp(this, APIService.class).getBookedScandata(bookingStockUpdate);
                            call.enqueue(new Callback<GetBookingStockUpdate>() {
                                @Override
                                public void onResponse(@NonNull Call<GetBookingStockUpdate> call, @NonNull Response<GetBookingStockUpdate> response) {
                                    if (response.body() != null) {
                                        GetBookingStockUpdate stockUpdate = response.body();

                                        if (stockUpdate.getSuccess()) {
                                            realmchange();
                                        }
                                    } else {
                                        Toast.makeText(BookingBarcodePickupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<GetBookingStockUpdate> call, @NonNull Throwable t) {
                                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                                    CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                                    Log.e("TAG", " error : " + t);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingBarcodePickupActivity.this);
                                    builder.setMessage("Something went wrong.");
                                    builder.setPositiveButton("Ok", (dialog, id) -> {dialog.cancel();
                                        finish();});
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        CommonMethod.appendLogs(e.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                        Log.d("TAG", "stockupdate error : "+e.getMessage());
                    }
                });
        builder.setNegativeButton("cancal", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void realmchange() {

        RealmResults<BookingDocketList_Tbl> thcDocketListTbls = realm.where(BookingDocketList_Tbl.class).equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO)).findAll();
        RealmResults<BookingdocketBarCodeSeries_Tbl> docketBarCodeSeriesTbls = realm.where(BookingdocketBarCodeSeries_Tbl.class).equalTo("vehicleNo", SharedPreference.getStringValue(ConstantData.SP_KEY_VEHICLENO)).findAll();

        realm.executeTransaction(realm -> {
            if (thcDocketListTbls != null)
                thcDocketListTbls.deleteAllFromRealm();
            if (docketBarCodeSeriesTbls != null)
                docketBarCodeSeriesTbls.deleteAllFromRealm();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(BookingBarcodePickupActivity.this);
        builder.setMessage("CNote Updated Successfully");
        builder.setPositiveButton("Ok", (dialog, id) -> {dialog.cancel();
                    finish();});
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (IsHHTDevice) {
            if (mScanner != null) {
                mScanner.aDecodeSetResultType(mBackupResultType);
            }
            mScanner = null;
        }
        super.onDestroy();
    }

    public void showBarcodeList(Boolean stetus, String docketNo) {
//        Log.d("TAG", "onClick: ");
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

        RealmResults<BookingdocketBarCodeSeries_Tbl> docketBarCodeSeriesTbls =
                realm.where(BookingdocketBarCodeSeries_Tbl.class)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("docketNo",docketNo)
                        .equalTo("isScanned",stetus).findAll();

        ArrayList<BookingdocketBarCodeSeries_Tbl> mDocketBarCodeSeriesTbls = new ArrayList<>(docketBarCodeSeriesTbls);

        rv_pendingBarcode.setLayoutManager( new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        RVBookingPendingBarcodeListAdapter rvthcPendingBarcodeListAdapter = new RVBookingPendingBarcodeListAdapter(mDocketBarCodeSeriesTbls,this);
        rv_pendingBarcode.setAdapter(rvthcPendingBarcodeListAdapter);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}