package in.webx.scorpion.util;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static io.realm.Realm.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import in.webx.scorpion.R;

public class CommonMethod {
    static final String DATEFORMAT = "dd MMM yyyy hh:mm:ss aaa";
    public static ProgressDialog dialog;
    public static int i = 0;
    ArrayList<String> msgList = new ArrayList<>();

    Context context;

    @SuppressWarnings("deprecation")
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(
                        context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    // Here Check Network Connection
    public static boolean isNetworkConnected(Context activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            alertDialogBoxWithOk("Connection Error",
                    "Please connect with active network connection and Try again.", activity);
            return false;
        } else {
            return true;
        }
    }

    public static void alertDialogBoxWithOk(String title, String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> {
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static File getApplicationDirectory(SubDirectory subFolderName,
                                               Context activity, boolean isPublic) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File internalDownloadDirectory = new File(getMountedPaths(),
                    activity.getResources().getString(R.string.app_name));
            if (!internalDownloadDirectory.exists()) {
                internalDownloadDirectory.mkdirs();
            }
            internalDownloadDirectory.setReadable(true);
            internalDownloadDirectory.setWritable(true);
            return internalDownloadDirectory;
        } else {
            File directory;
            if (isPublic) {
                directory = new File(Environment.getExternalStorageDirectory()
                        + "/"
                        + activity.getResources().getString(R.string.app_name));
            } else {
                directory = activity.getFilesDir();
            }
            if (directory == null
                    || (!directory.exists() && !directory.mkdirs())) {
                return null;
            }
            if (subFolderName != null) {
                directory = new File(directory, subFolderName.toString());
                if (directory == null
                        || (!directory.exists() && !directory.mkdirs())) {
                    return null;
                }
            }
            return directory;
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            try {
                InputMethodManager imm = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        }
    }

    public float distFrom(double lat1, double lng1, double lat2, double lng2) {
        try {
            double earthRadius = 3958.75;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                    * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;

            int meterConversion = 1609;

            return (float) (dist * meterConversion);
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
        return (float) 0;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String product = Build.PRODUCT;
        return capitalize(manufacturer) + " " + model + "--Brand " + brand;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    // Here Show Progress Dialog
    public static void showProgressDialog(Activity activity, String dialogText) {
        if (activity != null && dialog == null) {
            if (!activity.isFinishing()) {
                dialog = ProgressDialog.show(activity, "", dialogText, true, false, null);
            }
        }
    }

    // Here Cancel Progress Dialog
    public static void cancelProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); // 2014/08/06 15:59:48
        return dateFormat.format(date);
    }

    public static String getCurrentDateStringNew() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); // 2014/08/06 15:59:48
        return dateFormat.format(date);
    }

    public static Date addSecondToTime(Date dt, int seconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dt.toInstant().plusSeconds(seconds);
        } else {
            dt.setSeconds(dt.getSeconds() + seconds);
        }
        return dt;
    }

    public static long getCurrentTimeEpoch() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }

    public static boolean isMyServiceRunning(Activity context, String className) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDeviceSupportCamera(Activity act) {
        // this device has a camera
        // no camera on this device
        return act.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }

    public static boolean checkGooglePlayServices(Activity activity) {
        final int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, 1);
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

    public static void showToastMessage(Context activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static enum SubDirectory {
        APP_LOG_DIRECTORY("log"), APP_DISPATCH_DIRECTORY("Dispatch Images"), APP_SIGNATURE_DIRECTORY("Signature"), APP_SIGN_DIRECTORY("Signature"), APP_AUDIO_DIRECTORY("audio"), APP_IMAGE_DIRECTORY(
                "image"), APP_VIDEO_DIRECTORY("video"), APP_APK_DIRECTORY("APK");;
        private final String subDirectoryName;

        private SubDirectory(String subDirectoryName) {
            this.subDirectoryName = subDirectoryName;
        }

        @Override
        public String toString() {
            return subDirectoryName;
        }
    }

    private static String getMountedPaths() {
        String sdcardPath = "";
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = runtime.exec("mount");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        String line;
        BufferedReader br = new BufferedReader(isr);
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat")) {// TF card
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        sdcardPath = columns[1];
                    }
                } else if (line.contains("fuse")) {// internal storage
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        sdcardPath = columns[1];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sdcardPath;
    }

    public static File getApplicationDirectoryForLog(SubDirectory subFolderName,
                                                     Context activity, boolean isPublic) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File directory;
            if (isPublic) {

                directory = new File(getAppContext().getFilesDir().getAbsolutePath()
                        + "/webXvelocity");
//                + activity.getResources().getString("webXvelocity")

            } else {
                directory = activity.getFilesDir();
            }
            if (directory == null
                    || (!directory.exists() && !directory.mkdirs())) {
                return null;
            }
            if (subFolderName != null) {
                directory = new File(directory, subFolderName.toString());
                if (directory == null
                        || (!directory.exists() && !directory.mkdirs())) {
                    return null;
                }
            }
            return directory;
        } else {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File internalDownloadDirectory = new File(getMountedPaths(), "webXvelocity");
                if (!internalDownloadDirectory.exists()) {
                    internalDownloadDirectory.mkdirs();
                }
                internalDownloadDirectory.setReadable(true);
                internalDownloadDirectory.setWritable(true);
                return internalDownloadDirectory;
            } else {
                File directory;
                if (isPublic) {
                    directory = new File(Environment.getExternalStorageDirectory()
                            + "/webXvelocity");
                } else {
                    directory = activity.getFilesDir();
                }
                if (directory == null
                        || (!directory.exists() && !directory.mkdirs())) {
                    return null;
                }
                if (subFolderName != null) {
                    directory = new File(directory, subFolderName.toString());
                    if (directory == null
                            || (!directory.exists() && !directory.mkdirs())) {
                        return null;
                    }
                }
                return directory;
            }
        }
    }

    /*public static File getApplicationDirectoryForLog(SubDirectory subFolderName,
                                                     Context activity, boolean isPublic) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File internalDownloadDirectory = new File(getMountedPaths(), "webXvelocity");
            if (!internalDownloadDirectory.exists()) {
                internalDownloadDirectory.mkdirs();
            }
            internalDownloadDirectory.setReadable(true);
            internalDownloadDirectory.setWritable(true);
            return internalDownloadDirectory;
        } else {
            File directory;
            if (isPublic) {
                directory = new File(Environment.getExternalStorageDirectory()
                        + "/" + "webXvelocity");
            } else {
                directory = activity.getFilesDir();
            }
            if (directory == null || (!directory.exists() && !directory.mkdirs())) {
                return null;
            }
            if (subFolderName != null) {
                directory = new File(directory, subFolderName.toString());
                if (directory == null || (!directory.exists() && !directory.mkdirs())) {
                    return null;
                }
            }
            return directory;
        }
    }*/

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    @SuppressLint("SimpleDateFormat")
    public String getCurrentDateTimeForLog() {
        String str_date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
            Date date = new Date();
            System.out.println(dateFormat.format(date)); // 2014/08/06 15:59:48
            str_date = dateFormat.format(date).toString();
            return str_date;
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
        return str_date;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateTime() {
        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); // 2014/08/06 15:59:48
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); // 08/09/2000
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentYear() {
        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); // 2000
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateTimeFormat")
    public static String getCurrentDateTimeNew() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aaa", Locale.US);
        Date date = new Date();
        String currentDateTime = dateFormat.format(date);
        currentDateTime = replaceDotwithSpace(currentDateTime);
        return dateFormat.format(date);
    }

    @SuppressLint("FormatDate")
    public static String getCurrentDateWithFormat(String format) {
        if(format == "")
            format = "yyyyMM-dd";
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        Date date = new Date();
        String currentDateTime = dateFormat.format(date);
        currentDateTime = replaceDotwithSpace(currentDateTime);
        return dateFormat.format(date);
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String replaceDotwithSpace(String timeStamp) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (timeStamp.contains("."))
                timeStamp = timeStamp.replace(".", "");
        }
        return timeStamp;
    }


    public static void showAlertDailogueWithOK(Context context, String title,
                                               String message, String posiBtn) {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setPositiveButton(posiBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.show();
    }

    public static boolean isMyServiceRunning(Context context, String className) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Date GetUTCdatetimeAsDate() {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(new Date());
    }

    public static Date GetUTCdatetimeAsDate1(Date date) {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString1(date));
    }

    public static String GetUTCdatetimeAsString1(Date today) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(new Date(today.getTime() - (1000 * 60 * 60 * 24)));
    }

    public static Date StringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }

    public static String getCurrentDateForLog() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void appendLogs(String logtext) {
        try {
            AppendLog log = new AppendLog();
            log.appendLog(getApplicationContext(), logtext, ConstantData.CONST_LOG_FILE);

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static void appendLogss(String logtext,int i) {
        updateFireBase(logtext,i);
        try {
//            AppendLog log = new AppendLog();
//            log.appendLog(getApplicationContext(), logtext, ConstantData.CONST_LOG_FILE);

        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    static ArrayList<String> mList = new ArrayList<>();
    public static void updateFireBase(String logtext,int i) {
        mList.add(logtext);
        if(i == 4){
            CommonMethod.i = 0;
            pushDataInFirebase(mList);
            mList.clear();
        }
    }

    private static void pushDataInFirebase(ArrayList<String> mList) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, String> uData = new HashMap<>();
        uData.put("Timestamp", CommonMethod.getCurrentDateTimeNew());
        uData.put("Message", String.valueOf(mList));
        String companyAlias = SharedPreference.getStringValue(ConstantData.SP_COMPANY_NAME) ;
        String userName = SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME);
        String thcNo = SharedPreference.getStringValue(ConstantData.SP_THC_NO);

        if (companyAlias == null || companyAlias.isEmpty() || companyAlias.trim().isEmpty())
        {
            companyAlias = "NONE";
        }
        if (userName == null || userName.isEmpty() || userName.trim().isEmpty())
        {
            userName = "NONE";
        }
        if (thcNo == null || thcNo.isEmpty() || thcNo.trim().isEmpty())
        {
            thcNo = "THC";
        }

        db.collection("ScanApp").document(companyAlias)
                .collection(userName).document(CommonMethod.getCurrentDateWithFormat("yyyyMMdd"))
                .collection(CommonMethod.getCurrentDateWithFormat("HHmmss"))
                .document(UUID.randomUUID().toString())
                .set(uData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d("TAG", "DocumentSnapshot successfully written!");
//                        Toast.makeText(getApplicationContext(), "Added Successfully!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error writing document", e);
//                        Toast.makeText(getApplicationContext(), "Failed to add Student data!", Toast.LENGTH_LONG).show();

                    }
                });
    }

    /*public static void appendLogs(String logtext) {
        try {
            AppendLog log = new AppendLog();
            log.appendLog(Application.context, logtext, ConstantData.CONST_LOG_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
