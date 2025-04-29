package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.activity.LSBarcodePickupActivity;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketDetailList_Tbl;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;

public class RVScanListAdapter extends RecyclerView.Adapter<RVScanListAdapter.RVEScanListViewHolder> {

    LSBarcodePickupActivity LSBarcodePickupActivity;
    ArrayList<docketDetailList_Tbl> getEScanListDetail;

    public RVScanListAdapter(ArrayList<docketDetailList_Tbl> getEScanListDetail, LSBarcodePickupActivity LSBarcodePickupActivity) {
        this.LSBarcodePickupActivity = LSBarcodePickupActivity;
        this.getEScanListDetail = getEScanListDetail;
    }

    @NonNull
    @Override
    public RVEScanListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_ls_adapter_scanned_list, parent, false);
        return new RVEScanListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVEScanListViewHolder holder, int position) {
        holder.tv_LsNo.setText(getEScanListDetail.get(position).getLsNo() + "");
        holder.tv_docket.setText(getEScanListDetail.get(position).getDockNo() + getEScanListDetail.get(position).getDockSf());
        holder.tv_scanPkg.setText(getEScanListDetail.get(position).getScanPackages() + "");
        holder.tv_totalPkg.setText(getEScanListDetail.get(position).getTotalLoadPackages() + "");
        holder.tv_scannedBarcode.setText(getEScanListDetail.get(position).getLastBcSerialNo());
        holder.mCbSelect.setChecked(getEScanListDetail.get(position).isPartialCheck());

        if(SharedPreference.getBooleanValue(ConstantData.SP_KEY_ISPARTIALSCAN)){
            holder.mCbSelect.setVisibility(View.GONE);
        }

        holder.mCbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("TAG", "Select LS Dcoket: " + isChecked);
                LSBarcodePickupActivity.addPartialDocket(getEScanListDetail.get(position).getDockNo(), getEScanListDetail.get(position).getDockSf(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getEScanListDetail.size();
    }

    public static class RVEScanListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_LsNo, tv_docket, tv_scanPkg, tv_totalPkg, tv_scannedBarcode;
        CheckBox mCbSelect;

        public RVEScanListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_LsNo = itemView.findViewById(R.id.tv_LsNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            tv_scannedBarcode = itemView.findViewById(R.id.tv_scannedBarcode);
            mCbSelect = itemView.findViewById(R.id.cb_select);
        }
    }
}
