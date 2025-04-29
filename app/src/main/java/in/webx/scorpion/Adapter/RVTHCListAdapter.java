package in.webx.scorpion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.activity.InwardTHCListActivity;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.THCHeader_Tbl;
import in.webx.scorpion.util.SharedPreference;

public class RVTHCListAdapter extends RecyclerView.Adapter<RVTHCListAdapter.RVTHCListViewHolder> {

    ArrayList<THCHeader_Tbl> thcHeaderList;
    InwardTHCListActivity activity;
    Integer companyCode;
    String branchCode;


    public RVTHCListAdapter(ArrayList<THCHeader_Tbl> thcHeaderList, InwardTHCListActivity activity, Integer companyCode, String branchCode) {
        this.thcHeaderList = thcHeaderList;
        this.activity = activity;
        this.branchCode = branchCode;
        this.companyCode = companyCode;
        SharedPreference.getInstance(activity);
    }

    @NonNull
    @Override
    public RVTHCListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_inward_thc_list_layout, parent, false);
        return new RVTHCListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVTHCListViewHolder holder, int position) {
        holder.thc.setText(thcHeaderList.get(position).getThcNo());
        holder.date.setText(thcHeaderList.get(position).getThcDate());

        holder.itemView.setOnClickListener(v -> activity.getbarcode(thcHeaderList.get(position).getThcNo(),holder.itemView));


    }

    @Override
    public int getItemCount() {
        return thcHeaderList.size();
    }

    public static class RVTHCListViewHolder extends RecyclerView.ViewHolder {

        TextView thc, date;

        public RVTHCListViewHolder(@NonNull View itemView) {
            super(itemView);
            thc = itemView.findViewById(R.id.tv_thc);
            date = itemView.findViewById(R.id.tv_date);

            itemView.setClickable(true);
        }
    }
}