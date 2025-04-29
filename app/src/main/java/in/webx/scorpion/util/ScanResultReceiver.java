package in.webx.scorpion.util;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import device.common.DecodeResult;
import device.common.ScanConst;
import device.sdk.ScanManager;

public class ScanResultReceiver extends BroadcastReceiver {


    private ScanManager mScanner;
    private DecodeResult mDecodeResult;
    private EditText editText;

    public ScanResultReceiver() {
        mScanner = new ScanManager();
        mDecodeResult = new DecodeResult();
        editText = null;
    }

    public ScanResultReceiver(EditText text) {
        mScanner = new ScanManager();
        mDecodeResult = new DecodeResult();
        editText = text;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (ScanConst.INTENT_USERMSG.equals(intent.getAction())) {
                mScanner.aDecodeGetResult(mDecodeResult.recycle());

                String decodeResult = String.valueOf(mDecodeResult);
                Log.d("TAG","mDecodeResult : "+decodeResult);

                if (!mDecodeResult.toString().equalsIgnoreCase(ConstantData.READ_FAIL)) {
                    if (editText != null)
                        editText.setText(mDecodeResult.toString());
                }
            } else if (ScanConst.INTENT_EVENT.equals(intent.getAction())) {

                String decodeValue = intent.getStringExtra(ScanConst.EXTRA_EVENT_DECODE_VALUE);
                if (decodeValue != null
                        && !decodeValue.equalsIgnoreCase(ConstantData.READ_FAIL)) {
                    if (editText != null)
                        editText.setText(decodeValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
        }
    }
}