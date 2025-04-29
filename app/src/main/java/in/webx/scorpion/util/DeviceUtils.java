package in.webx.scorpion.util;

public class DeviceUtils {

    public static boolean isCompatibleScannerDevice() {
        String manufacturer = android.os.Build.MANUFACTURER.toLowerCase();
        return manufacturer.contains("urovo") || manufacturer.contains("newland") || manufacturer.contains("honeywell") == false;
    }
}

