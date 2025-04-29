package in.webx.scorpion.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class HoneywellBarcodeReceiver extends BroadcastReceiver {

    public interface BarcodeListener {
        void onBarcodeScanned(String data);
    }

    private BarcodeListener listener;

    public HoneywellBarcodeReceiver(BarcodeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.honeywell.barcode.action.BARCODE_DATA".equals(intent.getAction())) {
            String scannedData = intent.getStringExtra("data");
            Log.d("TAG", "Scanned barcode: " + scannedData);
            Toast.makeText(context, "Barcode: " + scannedData, Toast.LENGTH_SHORT).show();
            if (scannedData != null && listener != null) {
                listener.onBarcodeScanned(scannedData);
            }
        }
    }
}
