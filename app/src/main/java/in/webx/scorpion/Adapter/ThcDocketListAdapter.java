package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.ThcDocketList_Tbl;
import in.webx.scorpion.activity.THCBarcodePickupActivity;
import in.webx.scorpion.util.SharedPreference;

public class ThcDocketListAdapter extends RecyclerView.Adapter<ThcDocketListAdapter.ThcDocketListViewHolder> {

    ArrayList<ThcDocketList_Tbl> thcDocketListTbls;
    THCBarcodePickupActivity thcBarcodePickupActivity;
    boolean stetus;

    public ThcDocketListAdapter(ArrayList<ThcDocketList_Tbl> thcDocketListTbls, THCBarcodePickupActivity thcBarcodePickupActivity, boolean stetus) {
        this.thcDocketListTbls = thcDocketListTbls;
        this.thcBarcodePickupActivity = thcBarcodePickupActivity;
        SharedPreference.getInstance(thcBarcodePickupActivity);
        this.stetus = stetus;
    }

    @NonNull
    @Override
    public ThcDocketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_thc_pending_docket_list, parent, false);
        return new ThcDocketListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ThcDocketListViewHolder holder, int position) {
        holder.tv_THCNo.setText(thcDocketListTbls.get(position).getThcNo());
        holder.tv_docket.setText(thcDocketListTbls.get(position).getDockNo());
        holder.tv_scanPkg.setText(thcDocketListTbls.get(position).getScanPackages()+"");
        holder.tv_totalPkg.setText(thcDocketListTbls.get(position).getTotalLoadPackages()+"");

        holder.itemView.setOnClickListener(v -> thcBarcodePickupActivity.showBarcodeList(stetus,thcDocketListTbls.get(position).getDockNo(),thcDocketListTbls.get(position).getDockSf()));

    }

    @Override
    public int getItemCount() {
        return thcDocketListTbls.size();
    }

    public static class ThcDocketListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_THCNo, tv_docket, tv_scanPkg, tv_totalPkg;
        CardView cv_thcRoot;

        public ThcDocketListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_THCNo = itemView.findViewById(R.id.tv_THCNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            cv_thcRoot = itemView.findViewById(R.id.cv_thcRoot);
        }
    }
}
