package in.webx.scorpion.util;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class SharedPreference {

    private static SharedPreferences PREF_APP;
    private static SharedPreferences.Editor editor_app;

    public static String SP_APP_NAME = "SHARED_PREFS_LOGIN";

    /**
     * Here we used Caesar Cipher and value is 5
     */

    public SharedPreference(){}

    public static void getInstance(Context context) {
        if (PREF_APP == null) {
            PREF_APP = context.getSharedPreferences(SP_APP_NAME, Activity.MODE_PRIVATE);
            editor_app = PREF_APP.edit();
        }
    }

    public static void setStringValue(String key, String value) {

        try {
            editor_app.putString(key, Security.encrypt(value, Security.passwordKey));
            editor_app.commit();
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static String getStringValue(String key) {

        String value = PREF_APP.getString(key, "");
        try {
            return Security.decrypt(value,Security.passwordKey);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setIntValue(String key, int value) {

        try {
            editor_app.putInt(key, value);
            editor_app.commit();
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static Integer getIntValue(String key) {
        return PREF_APP.getInt(key, 0);
    }

    public static void setLongValue(String key, long value) {

        try {
            editor_app.putLong(key, value);
            editor_app.commit();
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static long getLongValue(String key) {
        return PREF_APP.getLong(key,0);
    }

    public static void setFloatValue(String key, Float value) {

        try {
            editor_app.putFloat(key, value);
            editor_app.commit();
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static Float getFloatValue(String key) {
        return PREF_APP.getFloat(key, 0.0f);
    }

    public static void setBooleanValue(String key, Boolean value) {

        try {
            editor_app.putBoolean(key, value);
            editor_app.commit();
        } catch (Exception e) {
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }

    public static Boolean getBooleanValue(String key) {

        return PREF_APP.getBoolean(key, false);

    }

    public static void removeValueFromSP(String key) {

        editor_app.remove(key);
        editor_app.commit();
    }
    public static void clearSP() {
        editor_app.clear();
        editor_app.commit();

    }
}