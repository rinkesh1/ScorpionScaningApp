package in.webx.scorpion.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BarcodeReceiver extends BroadcastReceiver {
    private BarcodeListener listener;

    public BarcodeReceiver(BarcodeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("nlscan.action.SCANNER_RESULT".equals(intent.getAction())) {
            String barcodeData = intent.getStringExtra("SCAN_BARCODE1");
            if (listener != null) {
                listener.onBarcodeScanned(barcodeData);
            }
        }
    }

}

