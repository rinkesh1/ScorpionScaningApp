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

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;

public class RVThcScaneAdapter extends RecyclerView.Adapter<RVThcScaneAdapter.RVThcScaneViewHolder> {

    ArrayList<ThcDocketList_Tbl> getScanListDetail;

    public RVThcScaneAdapter(ArrayList<ThcDocketList_Tbl> getScanListDetail) {
        this.getScanListDetail = getScanListDetail;
    }

    @NonNull
    @Override
    public RVThcScaneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_scanned_list, parent, false);
        return new RVThcScaneViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVThcScaneViewHolder holder, int position) {
        holder.tvLsNo.setText(getScanListDetail.get(position).getThcNo()+"");
        holder.tvdocket.setText(getScanListDetail.get(position).getDockNo() + getScanListDetail.get(position).getDockSf());
        holder.tvscanPkg.setText(getScanListDetail.get(position).getScanPackages()+"");
        holder.tvtotalPkg.setText(getScanListDetail.get(position).getTotalLoadPackages()+"");
        holder.tvscannedBarcode.setText(getScanListDetail.get(position).getLastBcSerialNo());

        if(holder.tvtotalPkg.getText() == holder.tvscanPkg.getText()){
            holder.mCbSelect.setChecked(true);
        }else {
            holder.mCbSelect.setChecked(false);
        }

        holder.mCbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("TAG", "Select Dcoket: "+isChecked);
                Log.d("TAG", "adapter Ls: "+holder.tvLsNo.getText());
                Log.d("TAG", "adapter pkg: "+holder.tvtotalPkg.getText());
                Log.d("TAG", "adapter Scan PKG: "+holder.tvscanPkg.getText());
                if(holder.tvtotalPkg.getText() == holder.tvscanPkg.getText()){
                    Log.d("TAG", "you cant deSelect");
                }else {
                    Log.d("TAG", "De-Select");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getScanListDetail.size();
    }

    public static class RVThcScaneViewHolder extends RecyclerView.ViewHolder {

        TextView tvLsNo, tvdocket , tvscanPkg, tvtotalPkg, tvscannedBarcode;
        CheckBox mCbSelect;
        public RVThcScaneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLsNo = itemView.findViewById(R.id.tv_LsNo);
            tvdocket = itemView.findViewById(R.id.tv_docket);
            tvscanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tvtotalPkg = itemView.findViewById(R.id.tv_totalPkg);
            tvscannedBarcode = itemView.findViewById(R.id.tv_scannedBarcode);
            mCbSelect = itemView.findViewById(R.id.cb_select);
        }
    }
}