package in.webx.scorpion.util;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppendLog {
    ArrayList<String> mList = new ArrayList<>();
    public void appendLog(Context context, String text, String fileName) {

        Log.d("TAG", "appendLog check");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                mList.add(text);
                String intStorageDirectory = getAppContext().getFilesDir().toString();
                File folder = new File(intStorageDirectory, "webXvelocity");
                folder.mkdirs();

                File subfolder = new File(folder, "log");
                subfolder.mkdirs();

                File logfile = new File(subfolder, fileName);

                FileOutputStream fileobj = new FileOutputStream(logfile,true);
                byte[] ByteArray = text.getBytes(); //Converts into bytes stream
                fileobj.write(ByteArray); //writing to file
                fileobj.close(); //File closed
            } catch (Exception e) {
                Log.d("TAG", "appendLog erorr : " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        } else {
            try {
                String path = CommonMethod.getApplicationDirectoryForLog(
                                CommonMethod.SubDirectory.APP_LOG_DIRECTORY, context, true)
                        .getAbsolutePath();

                File logFile = new File(path + "/" + fileName);

                if (!logFile.exists()) {
                    try {
                        logFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("TAG", " file error : " + e.getMessage());
                    }
                }
                try {
                    // BufferedWriter for performance, true to set append to file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                    buf.append(text);
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("TAG", "appendLog: " + e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "appendLog: " + e.getMessage());
                CommonMethod.appendLogs(e.getMessage());
                firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                        +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
            }
        }
    }

    public static void deleteLog(Context context) {
        try {
            String path;
            File getFile;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                String intStorageDirectory = getAppContext().getFilesDir().getAbsolutePath();
                File folder = new File(intStorageDirectory, "webXvelocity");
                folder.mkdirs();

                getFile = new File(folder, "log");
                path = getFile.getPath();
//                Log.d("TAG", "deleteLog path : "+path);
            } else {
                path = CommonMethod.getApplicationDirectoryForLog(
                        CommonMethod.SubDirectory.APP_LOG_DIRECTORY, context, true).getAbsolutePath();
                getFile = new File(path);
            }
            String[] fileList = getFile.list();

//            Log.d("TAG", "deleteLog list : "+fileList);

            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date SPDate;
            Date DeviceDate;

            DeviceDate = parser.parse(CommonMethod.getCurrentDate());

            assert fileList != null;
            for (String file : fileList) {

                if(file.contains("webXvelocity")){
                    try {
                        File logFile = new File(path + "/" + file);
                        logFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonMethod.appendLogs(e.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                    }
                }else {
                    String[] fileData = file.split("_", 2);
                    SPDate = parser.parse(fileData[0]);

                    assert SPDate != null;
                    assert DeviceDate != null;
                    long difference = Math.abs(SPDate.getTime() - DeviceDate.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    String dayDifference = Long.toString(differenceDates);

                    if (Integer.parseInt(dayDifference) > 7) {
                        try {
                            File logFile = new File(path + "/" + file);
                            logFile.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonMethod.appendLogs(e.getMessage());
                            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonMethod.appendLogs(e.getMessage());
            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
        }
    }
}