package in.webx.scorpion.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import in.webx.scorpion.R;
import in.webx.scorpion.util.AppendLog;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public ProgressDialog dialog;
    public long _splashTime = 2000;
    public static int tryCount = 0;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);
        setToolBar();
        SharedPreference.getInstance(this);
        /*Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));
*/
        //Define Function
        Log.d("TAG", "splash screen");
        handlePermission();
//        handleLocationPermission();
    }

    private void setToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.colorPrimaryDark_Transparent));
        }
    }

    public static String[] permissionList() {
        Log.d("TAG", "SDK_INT: "+Build.VERSION.SDK_INT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            return new String[]{
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION};
        }else {
            return new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

    }

    /**
     * handle dynamic Permission
     */
    private void handlePermission() {
        Dexter.withActivity(this).withPermissions(permissionList())
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        Log.d("TAG", "SDK_INT: "+Build.VERSION.SDK_INT);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            if (report.getDeniedPermissionResponses() != null
                                    && report.getDeniedPermissionResponses().size() > 0) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            } else {
                                callNextScreen();
                            }
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            callNextScreen();
                            /*if (!Environment.isExternalStorageManager()) {
                                showFileAccessSettingsDialog();
                            } else {
                                callNextScreen();
                            }*/
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread().check();
    }

    private void handleLocationPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        permissionList())
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.getDeniedPermissionResponses() != null
                                && report.getDeniedPermissionResponses().size() > 0) {
                            showSettingsDialog();
                        } else {
                            callNextScreen();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showFileAccessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to access the management of all files. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openStorageSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void openStorageSettings() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            boolean shouldShowRequestPermissionRationale = false;
            String[] permissionList = permissionList();
            for (String permission : permissionList) {
                permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                callNextScreen();
            } else {
                handlePermission();
//                handleLocationPermission();
            }
        }
    }
    /**
     * call next activity thread
     */
    private void callNextScreen() {new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        wait(_splashTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    goNextScreen();
                }
            }
        }.start();
    }

    private void goNextScreen() {
        Log.d("TAG", "goNextScreen");
        Intent intent;

        CommonMethod.appendLogs(" ");
        AppendLog.deleteLog(this);
        SharedPreference.getInstance(this);

        if (SharedPreference.getBooleanValue(ConstantData.SP_COMPANY_STATUS)) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, RegisterActivity.class);
        }
        startActivity(intent);
        finish();
    }
}