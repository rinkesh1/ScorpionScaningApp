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
import java.util.List;

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketDetailList_Tbl;
import in.webx.scorpion.activity.LSBarcodePickupActivity;

public class LsDocketListAdapter extends RecyclerView.Adapter<LsDocketListAdapter.LsDocketListViewHolder> {

    List<docketDetailList_Tbl> lsList;
    Boolean stetus;
    LSBarcodePickupActivity activity;

    public LsDocketListAdapter(ArrayList<docketDetailList_Tbl> docketList, Boolean stetus, LSBarcodePickupActivity activity) {
        this.lsList = docketList;
        this.stetus = stetus;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LsDocketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_pending_docket_list, parent, false);
        return new LsDocketListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LsDocketListViewHolder holder, int position) {
        holder.tvLsNo.setText(lsList.get(position).getLsNo());
        holder.tvDocket.setText(lsList.get(position).getDockNo());
        holder.tvScanPkg.setText(lsList.get(position).getScanPackages()+"");
        holder.tvTotalPkg.setText(String.valueOf(lsList.get(position).getTotalLoadPackages()));

        holder.cvCompleteRoot.setOnClickListener(v -> activity.showBarcodeList(stetus,lsList.get(position).getDockNo(),lsList.get(position).getDockSf()));
    }

    @Override
    public int getItemCount() {
        return lsList.size();
    }

    public static class LsDocketListViewHolder extends RecyclerView.ViewHolder {

        TextView tvLsNo ,tvTotalPkg ,tvScanPkg ,tvDocket;
        CardView cvCompleteRoot;

        public LsDocketListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLsNo = itemView.findViewById(R.id.tv_LsNo);
            tvTotalPkg = itemView.findViewById(R.id.tv_totalPkg);
            tvScanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tvDocket = itemView.findViewById(R.id.tv_docket);
            cvCompleteRoot = itemView.findViewById(R.id.cv_Root);
        }
    }
}
