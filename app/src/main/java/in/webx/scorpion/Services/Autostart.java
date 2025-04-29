package in.webx.scorpion.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import in.webx.scorpion.util.CommonMethod;


public class Autostart extends BroadcastReceiver {

    public static final String START_TRACKING = "START_TRACKING";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {



           /* Intent myIntent = new Intent(context, UploadOfflineBulkData.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(myIntent);
            } else {
                context.startService(myIntent);
            }*/
            if (! CommonMethod.isMyServiceRunning(context, UploadofflineBulkData.class.getName())) {
                Intent myIntent = new Intent(context, UploadofflineBulkData.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(myIntent);
                } else {
                    context.startService(myIntent);
                }
            }

            if (! CommonMethod.isMyServiceRunning(context, UploadofflineBulkData.class.getName())) {
                Intent myIntent = new Intent(context, UploadofflineBulkData.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(myIntent);
                } else {
                    context.startService(myIntent);
                }
            }
        } else if (intent.getAction().equals(START_TRACKING)) {

        }
    }
}
