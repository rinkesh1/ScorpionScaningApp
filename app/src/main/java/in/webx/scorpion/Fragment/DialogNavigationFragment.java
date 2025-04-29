package in.webx.scorpion.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.webx.scorpion.Adapter.RVMainAdapter;
import in.webx.scorpion.Model.StoreField;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.LoginActivity;
import in.webx.scorpion.activity.MainActivity;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;

public class DialogNavigationFragment extends Fragment {

    View view;
    TextView logout, tv_app_version;
    StoreField storeField = new StoreField();
    ArrayList<Integer> image = new ArrayList<>();
    ArrayList<String> imageName = new ArrayList<>();
    ArrayList<Integer> imageColor = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RVMainAdapter adapter;
    String appVersion;
    public static RelativeLayout rl_progressBar;
    ImageView progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dialog_navigation, container, false);

        logout = view.findViewById(R.id.logout);
        SharedPreference.getInstance(getActivity());

        progressBar = view.findViewById(R.id.progressbar);
        rl_progressBar = view.findViewById(R.id.rl_progressbar);
        Glide.with(this).asGif().load(R.drawable.infinityloader).into(progressBar);

        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            Log.e("TAG"," Error : "+e2.getMessage());
        }

        TextView etUser = view.findViewById(R.id.et_user);
        etUser.setText(SharedPreference.getStringValue(ConstantData.SP_KEY_BIKER_NAME)+" : "+
                SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : "+
                SharedPreference.getStringValue(ConstantData.SP_KEY_BRANCH_CODE));

        assert info != null;
        appVersion = info.versionName;

        //Define Function
        logout();
        appversion();
        adddata();
        setrecycleview();

        return view;
    }

    //AddData
    private void adddata() {

        image.add(R.drawable.outward);
        imageName.add("Outward Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        image.add(R.drawable.inward);
        imageName.add("Inward Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        image.add(R.drawable.bookinglist);
        imageName.add("Booking Scan");
        imageColor.add(Color.parseColor("#2DF5F5"));

        image.add(R.drawable.sand_mail);
        imageName.add("Share LogFile");
        imageColor.add(Color.parseColor("#2DF5F5"));

        storeField.setImagelist(image);
        storeField.setImagenamelist(imageName);
        storeField.setColor(imageColor);
    }

    // set up the RecyclerView
    private void setrecycleview() {

        recyclerView = view.findViewById(R.id.rv_field_nav);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RVMainAdapter(getActivity(), storeField, false);
        recyclerView.setAdapter(adapter);

    }

    //Application Version
    @SuppressLint("SetTextI18n")
    private void appversion() {

        tv_app_version = view.findViewById(R.id.tv_app_version);

        tv_app_version.setText("Version : " + appVersion);
    }

    //Logout onClick
    private void logout() {

        logout.setOnClickListener(v -> {
            SharedPreference.setStringValue(ConstantData.SP_KEY_PASSWORD, "");
            SharedPreference.setBooleanValue(ConstantData.SP_REMEMBER, false);
            SharedPreference.setBooleanValue(ConstantData.SP_COMPANY_STATUS, false);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.iv_nav_menu.setClickable(true);
    }
}