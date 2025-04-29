package in.webx.scorpion.Adapter;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;
import static in.webx.scorpion.Fragment.DialogNavigationFragment.rl_progressBar;
import static io.realm.Realm.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import in.webx.scorpion.BuildConfig;
import in.webx.scorpion.Model.GetInsertELKLog;
import in.webx.scorpion.Model.RequestInsertELKLog;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.activity.BookingScanActivity;
import in.webx.scorpion.activity.DRSConfirmationActivity;
import in.webx.scorpion.activity.InwardTHCListActivity;
import in.webx.scorpion.Model.StoreField;
import in.webx.scorpion.activity.MainActivity;
import in.webx.scorpion.activity.OutwardLSListActivity;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.PRSGenerationActivity;
import in.webx.scorpion.activity.PRSUpdateActivity;
import in.webx.scorpion.activity.QuickBookingActivity;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import in.webx.scorpion.util.ZipManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RVMainAdapter extends RecyclerView.Adapter<RVMainAdapter.RVMainViewHolder> implements View.OnClickListener {

    Context mainActivity;
    StoreField storeField;
    boolean b;

    Uri path;
    Uri pathforzip;
    ArrayList<Uri> uris;
    ArrayList<Uri> urisforzip;
    ArrayList<String> urisLocation;

    public RVMainAdapter(Context mainActivity, StoreField storeField, boolean b) {
        super();
        this.mainActivity = mainActivity;
        this.storeField = storeField;
        this.b = b;

        SharedPreference.getInstance(mainActivity);
    }

    @NonNull
    @Override
    public RVMainAdapter.RVMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view;
        if (b) {
            view = layoutInflater.inflate(R.layout.item_field_main_layout, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.item_field_nav_layout, parent, false);
        }
        return new RVMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVMainViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.feildimage.setImageResource(storeField.getImagelist().get(position));
        if (b) {
            holder.feildimage.setBackgroundColor(storeField.getColor().get(position));
        }
        holder.feildname.setText(storeField.getImagenamelist().get(position));
        holder.itemView.setOnClickListener(view -> {

            Log.d("TAG", "onBindViewHolder : position " + position);

            if (CommonMethod.isNetworkConnected(mainActivity)) {

//                if (position < 3)
//                {

                holder.itemView.setClickable(false);

//                Intent intent = new Intent();
                Log.d("TAG", " status : " + b);

                switch (storeField.getImagenamelist().get(position)) {
                    case "Outward Scan":
                        Log.d("TAG", "set Ouward");
                        SharedPreference.setIntValue(ConstantData.SP_KEY_ISSELECTMENU, 0);
                        Intent intent = new Intent(mainActivity, OutwardLSListActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case "Inward Scan":
                        Log.d("TAG", "set Inward");
                        SharedPreference.setIntValue(ConstantData.SP_KEY_ISSELECTMENU, 1);
                        Intent intent1 = new Intent(mainActivity, InwardTHCListActivity.class);
                        mainActivity.startActivity(intent1);
                        break;
                    case "PRS Gen":
                        Intent intent2 = new Intent(mainActivity, PRSGenerationActivity.class);
                        mainActivity.startActivity(intent2);
                        break;
                    case "PRS Update":
                        Intent intent3 = new Intent(mainActivity, PRSUpdateActivity.class);
                        mainActivity.startActivity(intent3);
                        break;
                    case "Booking Scan":
                        Intent intent4 = new Intent(mainActivity, BookingScanActivity.class);
                        mainActivity.startActivity(intent4);
                        break;
                    case "DRS Confirmation":
                        Intent intent5 = new Intent(mainActivity, DRSConfirmationActivity.class);
                        mainActivity.startActivity(intent5);
                        break;
                    case "Quick Booking":
                        Intent intent6 = new Intent(mainActivity, QuickBookingActivity.class);
                        mainActivity.startActivity(intent6);
                        break;
                    case "Share LogFile":
                        try {

                            String path1 = CommonMethod.getApplicationDirectoryForLog(
                                            CommonMethod.SubDirectory.APP_LOG_DIRECTORY, mainActivity, true)
                                    .getAbsolutePath();

                            File directory = new File(path1);
                            File[] files = directory.listFiles();

                            ArrayList<String> datestring = new ArrayList<String>();
                            for (File file : files) {
                                datestring.add(file.getName());
                            }
                            Log.d("TAGFIRST", String.valueOf(datestring));

                            Collections.sort(datestring, new Comparator<String>() {

                                @Override
                                public int compare(String o1, String o2) {
                                    return o1.compareTo(o2);
                                }
                            });
                            uris = new ArrayList<Uri>();
                            urisLocation = new ArrayList<>();

                            for (String date : datestring) {
                                if (date.equalsIgnoreCase("webXvelocity.zip")) {
                                    continue;
                                }
                                File filelocation = new File(path1, date);
                                Log.d("TAGSECOND", String.valueOf(datestring));
                                path = FileProvider.getUriForFile(mainActivity, in.webx.scorpion.BuildConfig.APPLICATION_ID + ".provider", filelocation);
                                Log.d("TAG", "email onClick path : " + path);
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                Date today = dateFormat.parse(CommonMethod.getCurrentDate());
                                String todayDate = dateFormat.format(today.getTime());
                                String lastDate = dateFormat.format(today.getTime() - (1000 * 60 * 60 * 24));

                                String[] split = date.split("\\.");
                                String secondSubString = split[0];
                                String[] split1 = secondSubString.split("_");
                                String secondSubString1 = split1[0];

                                String getDate1 = secondSubString1;

                                if (todayDate.compareTo(getDate1) == 0 || lastDate.compareTo(getDate1) == 0) {
                                    uris.add(path);
                                    urisLocation.add(filelocation.getAbsolutePath());

                                }
                            }

                            Collections.sort(uris, new Comparator<Uri>() {

                                @Override
                                public int compare(Uri o1, Uri o2) {
                                    return o2.compareTo(o1);
                                }
                            });

                            Log.d("TAGURILIST", String.valueOf(uris));
                            String[] uriFiles = new String[urisLocation.size()];
                            for (int i = 0; i < urisLocation.size(); i++) {
                                uriFiles[i] = urisLocation.get(i);
                            }

                            String backupDBPath = CommonMethod.getApplicationDirectoryForLog(
                                            CommonMethod.SubDirectory.APP_LOG_DIRECTORY, getApplicationContext(), true)
                                    .getAbsolutePath();

                            ZipManager zip = new ZipManager();
                            zip.zip(uriFiles, backupDBPath + "/webXvelocity.zip");

                            File filelocation = new File(CommonMethod.getApplicationDirectoryForLog(
                                            CommonMethod.SubDirectory.APP_LOG_DIRECTORY, mainActivity, true)
                                    .getAbsolutePath(), "webXvelocity.zip");
                            pathforzip = FileProvider.getUriForFile(mainActivity, BuildConfig.APPLICATION_ID + ".provider", filelocation);
                            urisforzip = new ArrayList<>();
                            urisforzip.add(pathforzip);

                            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            emailIntent.setType("application/zip");

                            String to[] = {"vishal@webxpress.in"};
                            String cc[] = {"rinkesh@webxpress.in"};
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                            emailIntent.putExtra(Intent.EXTRA_CC, cc);

                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Team \n PFA Log File Attachment...");
                            emailIntent.putExtra(Intent.EXTRA_STREAM, urisforzip);
                            mainActivity.startActivity(emailIntent);
                        } catch (Exception e) {
                            Log.d("TAG", "onBindViewHolder: "+e.getMessage());
                            CommonMethod.appendLogs(e.getMessage());
                            firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                    +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                            holder.itemView.setClickable(true);
                            e.printStackTrace();
                        }

                        break;
                }

                holder.itemView.setClickable(true);

                MainActivity.progressbarShow();
                if (rl_progressBar != null)
                    rl_progressBar.setVisibility(View.VISIBLE);

                RequestInsertELKLog requestInsertELKLog = new RequestInsertELKLog();

                requestInsertELKLog.setUserId(SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME));
                requestInsertELKLog.setApplication("Velocity");
                requestInsertELKLog.setTenantId(SharedPreference.getStringValue(ConstantData.SP_KEY_COMPANY_CODE));
                requestInsertELKLog.setModuleName(storeField.getImagenamelist().get(position));
                requestInsertELKLog.setModuleType(storeField.getImagenamelist().get(position));
                requestInsertELKLog.setEventDateTime(CommonMethod.getCurrentDateTimeNew());
                requestInsertELKLog.setDocumentNo("");
                requestInsertELKLog.setEventType(storeField.getImagenamelist().get(position));
                requestInsertELKLog.setAppType(ConstantData.AppType);
                requestInsertELKLog.setEn(ConstantData.AppType);
                requestInsertELKLog.setEventRandomNo("E101");
                requestInsertELKLog.setLoginRandomNo("L101");

                Call<GetInsertELKLog> call = MyRetro.createSecureServiceApp(mainActivity, APIService.class).getInsertELKLog(requestInsertELKLog);
                call.enqueue(new Callback<GetInsertELKLog>() {
                    @Override
                    public void onResponse(@NonNull Call<GetInsertELKLog> call, @NonNull Response<GetInsertELKLog> response) {

                        if (response.body() != null) {


                        } else {
                            holder.itemView.setClickable(true);
                            Toast.makeText(mainActivity, "Data not found", Toast.LENGTH_SHORT).show();
                        }

                        MainActivity.progressbarClose();

                        if (rl_progressBar != null)
                            rl_progressBar.setVisibility(View.GONE);
                        holder.itemView.setClickable(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetInsertELKLog> call, @NonNull Throwable t) {

                        holder.itemView.setClickable(true);

                        CommonMethod.appendLogs("--- error --- : " + t.getMessage());
                        firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                                +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                        MainActivity.progressbarClose();

                        if (rl_progressBar != null)
                            rl_progressBar.setVisibility(View.GONE);
                        Toast.makeText(mainActivity, "Something Wrong", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", " error : " + t);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeField.getImagelist().size();
    }

    @Override
    public void onClick(View view) {
    }

    public static class RVMainViewHolder extends RecyclerView.ViewHolder {

        public ImageView feildimage;
        public TextView feildname;

        public RVMainViewHolder(@NonNull View itemView) {
            super(itemView);
            feildimage = itemView.findViewById(R.id.iv_fieldImage);
            feildname = itemView.findViewById(R.id.tv_fieldName);
            itemView.setClickable(true);
        }
    }
}