package in.webx.scorpion.Services;

/***
 *
 * @
 * @version 1.0
 *
 */

import static android.app.PendingIntent.FLAG_MUTABLE;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.karumi.dexter.BuildConfig;
import java.io.File;
import java.util.ArrayList;

import in.webx.scorpion.Model.DocketUpdateScan;
import in.webx.scorpion.Model.ScanDocketDeviceDetails;
import in.webx.scorpion.Model.ScanDocketResponse;
import in.webx.scorpion.Model.ScanDocketTHCModel;
import in.webx.scorpion.Model.ScanTHCDocketDetailsModel;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;
import in.webx.scorpion.RealmDatabase.docketDetailList_Tbl;
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

public class UploadofflineBulkData extends Service {

    private long refreshInterval;
    File filePath;
    int count = 0;
    double getRam;
    private Realm realm = Realm.getDefaultInstance();
    String getModelNumber, deviceVersionNo, brand, appVersion;
    RelativeLayout rl_progressBar;

    private void scheduleNextServiceRun() {
        Intent i = new Intent(UploadofflineBulkData.this,
                UploadofflineBulkData.class);
        PendingIntent sender = PendingIntent.getService(
                UploadofflineBulkData.this, 0, i, FLAG_MUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                + refreshInterval, sender);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + intent);
        showNotification();

        String strrefreshInterval = ConstantData.REFRESH_INTERVEL_LAT_LON;
        if (strrefreshInterval.equalsIgnoreCase("1")) {
            refreshInterval = 1 * 60 * 1000;
        } else if (strrefreshInterval.equalsIgnoreCase("10")) {
            refreshInterval = 10 * 60 * 1000;
        } else if (strrefreshInterval.equalsIgnoreCase("20")) {
            refreshInterval = 20 * 60 * 1000;
        } else if (strrefreshInterval.equalsIgnoreCase("30")) {
            refreshInterval = 30 * 60 * 1000;
        } else if (strrefreshInterval.equalsIgnoreCase("40")) {
            refreshInterval = 40 * 60 * 1000;
        }

        Log.d("TAG", "Call Service");
        getRam = getMemorySizeInBytes();
        getModelNumber = Build.MODEL;
        deviceVersionNo = Build.VERSION.RELEASE;
        brand = Build.MANUFACTURER;
        appVersion = BuildConfig.VERSION_NAME;

        Log.d("TAG", "SP_KEY_ISSELECTMENU: "+SharedPreference.getIntValue(ConstantData.SP_KEY_ISSELECTMENU));

        if(SharedPreference.getIntValue(ConstantData.SP_KEY_ISSELECTMENU) == 0){
            Log.d("TAG", "Docket LS");
            pushChunkDataOnServer();
        }else if(SharedPreference.getIntValue(ConstantData.SP_KEY_ISSELECTMENU) == 1){
            Log.d("TAG", "Docket THC");
            pushTHCDockets();
        }

        RestartService();
       // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void pushTHCDockets() {
        String thcNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        long scanTHCDocketCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .equalTo("LsThcNo", thcNo).count();
        Log.d("TAG", "scan THCDockets count: "+scanTHCDocketCount);

        if(scanTHCDocketCount >= 50){
            RealmResults<docketBarCodeSeries_Tbl> pkgList = realm.where(docketBarCodeSeries_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("isBarcodeScanned", true)
                    .equalTo("LsThcNo", thcNo).findAll();

            if(pkgList.size() != 0){
                ArrayList<ScanTHCDocketDetailsModel> scanDkt = new ArrayList<>();
                ScanDocketTHCModel scanHeader = new ScanDocketTHCModel();

                scanHeader.setDeviceDetails(getModelNumber + "," + brand + "," + getRam);
                scanHeader.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                scanHeader.setOSVersion(deviceVersionNo);
                scanHeader.setAppVersion(CommonMethod.getAppVersionName(this));
                scanHeader.setDeviceId(CommonMethod.getDeviceId(this));
                scanHeader.setDeviceStorage(String.valueOf(getRam));
                scanHeader.setScanLocation(SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));
                scanHeader.setIMEINo("");
                scanHeader.setModelName(Build.MODEL);
                RealmResults<docketBarCodeSeries_Tbl> docketTHCList = realm.where(docketBarCodeSeries_Tbl.class)
                        .equalTo("LsThcNo", thcNo)
                        .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                        .equalTo("isBarcodeScanned", true)
                        .notEqualTo("barcodeType",ConstantData.SP_KEY_EXTRA)
                        .equalTo("isServerPush", false)
                        .limit(50).findAll();

                for(docketBarCodeSeries_Tbl d : docketTHCList){
                    ScanTHCDocketDetailsModel model = new ScanTHCDocketDetailsModel();
                    model.setBCSerialNo(d.getBcSerialNo());
                    model.setCompanyCode(d.getCompanyCode());
                    model.setDockNo(d.getDockNo());
                    model.setDockSf(d.getDockSf());
                    model.setIsManual(false);
                    if(d.getBarcodeType().equalsIgnoreCase(ConstantData.SP_KEY_EXTRA)){
                        model.setExtraBarcode(true);
                    }else {
                        model.setExtraBarcode(false);
                    }
                    model.setIsBarcodeScanned(d.getBarcodeScanned());
                    model.setTHCno(d.getLsThcNo());
                    model.setPackages(1);
                    model.setVolume(d.getActualweight());
                    model.setWeight(d.getActualweight());
                    if(d.getBarcodeType().equalsIgnoreCase(ConstantData.SP_KEY_DAMAGE)){
                        model.setDamage(true);
                    }else {
                        model.setDamage(false);
                    }
                    model.setPilferage(false);

                    scanDkt.add(model);
                }
                scanHeader.setDetails(scanDkt);
                pushTHCDockerAPI(scanHeader,docketTHCList);
            }

        }
    }

    private void pushTHCDockerAPI(ScanDocketTHCModel scanHeader, RealmResults<docketBarCodeSeries_Tbl> docketTHCList) {
        if(scanHeader.getDetails().size() > 0){
            ArrayList<docketBarCodeSeries_Tbl> dktList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> dktTempList = new ArrayList<>();
            dktList.addAll(realm.copyFromRealm(docketTHCList));
            Log.d("TAG", "push THCDocker API: "+new Gson().toJson(scanHeader));
            Call<ScanDocketResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .packageTHCUpdate(scanHeader);
            call.enqueue(new Callback<ScanDocketResponse>() {
                @Override
                public void onResponse(Call<ScanDocketResponse> call, Response<ScanDocketResponse> response) {
                    if (response.body() != null) {
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
        }
    }

    private void pushChunkDataOnServer() {
        String lsNo = SharedPreference.getStringValue(ConstantData.SP_LS_NO);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        long scanPackageCount = realm.where(docketBarCodeSeries_Tbl.class)
                .equalTo("LsThcNo", lsNo)
                .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                .equalTo("isBarcodeScanned", true)
                .count();
        Log.d("TAG", "Scan Package Count: "+scanPackageCount);

        if(scanPackageCount >= 50) {

            RealmResults<docketDetailList_Tbl> docketDetailList = realm.where(docketDetailList_Tbl.class)
                    .equalTo("user", SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME))
                    .equalTo("scanDocket", true)
                    .equalTo("lsNo", lsNo).findAll();

            if (docketDetailList != null) {
                ArrayList<DocketUpdateScan> docketDetailsList = new ArrayList<>();

                ScanDocketDeviceDetails docket = new ScanDocketDeviceDetails();
                docket.setEntryBy(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                docket.setAppVersion(CommonMethod.getAppVersionName(this));
                docket.setOSVersion(deviceVersionNo);
                docket.setDeviceDetails(getModelNumber + "," + brand + "," + getRam);
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
                        .equalTo("isServerPush", false)
                        .limit(50).findAll();

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
                pushDocketOnServer(docket,docketBarCodeList);
            }
        }
    }

    private void pushDocketOnServer(ScanDocketDeviceDetails docketDetailsList, RealmResults<docketBarCodeSeries_Tbl> docketBarCodeList) {
        Log.d("TAG", "pushDocketOnServer Request: "+new Gson().toJson(docketDetailsList));

        if(docketDetailsList.getDetails().size() > 0) {
            ArrayList<docketBarCodeSeries_Tbl> prsDcoketList = new ArrayList<>();
            ArrayList<docketBarCodeSeries_Tbl> tempList = new ArrayList<>();
            prsDcoketList.addAll(realm.copyFromRealm(docketBarCodeList));

            Call<ScanDocketResponse> call = MyRetro.createSecureServiceApp(this, APIService.class)
                    .packageUpdate(docketDetailsList);
            call.enqueue(new Callback<ScanDocketResponse>() {
                @Override
                public void onResponse(Call<ScanDocketResponse> call, Response<ScanDocketResponse> response) {
                    if (response.body() != null) {
                        ScanDocketResponse res = response.body();
                        Log.d("TAG", "scan Docket Response: " + new Gson().toJson(prsDcoketList));
                        if (res.isSuccess()) {
                            Realm realm = Realm.getDefaultInstance();
                            for (docketBarCodeSeries_Tbl docket : prsDcoketList) {
                                Log.d("TAG", "onResponse Docket: " + docket.getBcSerialNo());
                                docket.setServerPush(true);
                                tempList.add(docket);
                            }
                            realm.executeTransactionAsync(r -> {
                                r.insertOrUpdate(tempList);
                            });


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

    private void RestartService() {
        scheduleNextServiceRun();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @SuppressWarnings("unused")


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ForegroundServiceType")
    private void showNotification() {
//        Log.d("TAG", "mobile version: "+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 26) {
            Intent notificationIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ConstantData.CHANNEL_ID_FOR_UPLOAD)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.label_notification_data_sync_in_progress))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setAutoCancel(false);
            Notification notification = builder.build();
            //        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            //        notification.flags |= Notification.FLAG_NO_CLEAR;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(ConstantData.CHANNEL_ID_FOR_UPLOAD,
                        ConstantData.CHANNEL_NAME_FOR_UPLOAD,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(false);
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }
            try {
                startForeground(ConstantData.NOTIFICATION_ID_FOR_UPLOAD, notification);
            } catch (Exception e) {
                CommonMethod.appendLogs(e.getMessage());
            }
        }
    }

}
