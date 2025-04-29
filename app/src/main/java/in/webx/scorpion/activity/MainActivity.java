package in.webx.scorpion.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import in.webx.scorpion.Adapter.RVMainAdapter;
import in.webx.scorpion.Fragment.DialogNavigationFragment;
import in.webx.scorpion.Model.StoreField;
import in.webx.scorpion.R;
import in.webx.scorpion.Services.UploadofflineBulkData;

public class MainActivity extends AppCompatActivity {

    public static ImageView iv_nav_menu,progressBar;
    public static RelativeLayout rl_progressBar;
    RecyclerView recyclerView;
    RVMainAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    StoreField storeField = new StoreField();

    public static void progressbarShow() {
        rl_progressBar.setVisibility(View.VISIBLE);
    }

    public static void progressbarClose() {
        rl_progressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    getResources().getColor(
                            R.color.StatusBarTransparent));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("TAG", "PackageName: "+getPackageName());

//        IntentFilter filter = new IntentFilter("com.honeywell.barcode.action.BARCODE_DATA");
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("ScanTest", "Broadcast received in MainActivity");
//                String data = intent.getStringExtra("data");
//                Toast.makeText(context, "Data: " + data, Toast.LENGTH_SHORT).show();
//            }
//        }, filter);

        //Function Declare
        startService();
        adddata();
        setrecycleview();
    }

    private void startService() {
        Log.d("TAG", "startService Check");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MainActivity.this,
                    UploadofflineBulkData.class));
        } else {
            startService(new Intent(MainActivity.this,
                    UploadofflineBulkData.class));
        }
    }

    // set up the RecyclerView
    private void setrecycleview() {
        recyclerView = findViewById(R.id.rv_main);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RVMainAdapter(this, storeField, true);
        recyclerView.setAdapter(adapter);
    }

    //AddData
    private void adddata() {

        ArrayList<Integer> image = new ArrayList<>();
        ArrayList<String> imageName = new ArrayList<>();
        ArrayList<Integer> imageColor = new ArrayList<>();

        progressBar = findViewById(R.id.progressbar);
        rl_progressBar = findViewById(R.id.rl_progressbar);
        Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

/*        image.add(R.drawable.booking);
        imageName.add("Docket Booking");
        imageColor.add(Color.parseColor("#EEBE5C"));

        image.add(R.drawable.qr_code_scanner);
        imageName.add("DB with Barcode");
        imageColor.add(Color.parseColor("#5A8AEC"));

        image.add(R.drawable.local_shipping);
        imageName.add("Docket Delivery");
        imageColor.add(Color.parseColor("#EEBE5C"));

        image.add(R.drawable.location_searching);
        imageName.add("Tracking");
        imageColor.add(Color.parseColor("#5A8AEC"));*/

        image.add(R.drawable.outward);
        imageName.add("Outward Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        image.add(R.drawable.inward);
        imageName.add("Inward Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        image.add(R.drawable.bookinglist);
        imageName.add("Booking Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        // PRS Generation
        image.add(R.drawable.list);
        imageName.add("PRS Gen");
        imageColor.add(Color.parseColor("#2DF5F5"));

        // DRS Generation
        image.add(R.drawable.list);
        imageName.add("DRS Confirmation");
        imageColor.add(Color.parseColor("#2DF5F5"));

        // Quick Booking
        image.add(R.drawable.list);
        imageName.add("Quick Booking");
        imageColor.add(Color.parseColor("#2DF5F5"));

        // PRS Update
//        image.add(R.drawable.checklist);
//        imageName.add("PRS Update");
//        imageColor.add(Color.parseColor("#2DF5F5"));

        /*image.add(R.drawable.print);
        imageName.add("Reprint Barcode");
        imageColor.add(Color.parseColor("#2FC796"));*/

        storeField.setImagelist(image);
        storeField.setImagenamelist(imageName);
        storeField.setColor(imageColor);
    }

    //Navigation Menu
    public void navmenu(View view) {
        iv_nav_menu = findViewById(R.id.iv_nav_menu);
        iv_nav_menu.setClickable(false);

        Fragment fragment = new DialogNavigationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.LL_Main_Container, fragment).addToBackStack(null).commit();
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}