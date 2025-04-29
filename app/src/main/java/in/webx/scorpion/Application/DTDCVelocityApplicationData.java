package in.webx.scorpion.Application;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import in.webx.scorpion.util.AppendLog;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DTDCVelocityApplicationData extends MultiDexApplication {
    private static Context context;
    public static Realm realm;
    public static FirebaseCrashlytics firebaseCrashlytics;

    public static Context getAppContext() {
        return DTDCVelocityApplicationData.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        conectrealm();
        DTDCVelocityApplicationData.context = getApplicationContext();

        firebaseCrashlytics=FirebaseCrashlytics.getInstance();
    }

    private void appendLogs(String logtext) {
        try {
            AppendLog log = new AppendLog();
            log.appendLog(context, logtext, ConstantData.CONST_LOG_FILE);
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    + SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            e.printStackTrace();
        }
    }


    public void conectrealm() {

        Realm.init(getApplicationContext());

        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .name("velocity.realm")
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .allowWritesOnUiThread(true)
                        .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getInstance(config);

    }

}
