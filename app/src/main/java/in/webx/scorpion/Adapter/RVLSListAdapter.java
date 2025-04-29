package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.activity.OutwardLSListActivity;
import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.LsHader_Tbl;

public class RVLSListAdapter extends RecyclerView.Adapter<RVLSListAdapter.RVLSListViewHolder> {

    ArrayList<LsHader_Tbl> haderList;
    OutwardLSListActivity activity;

    public RVLSListAdapter(ArrayList<LsHader_Tbl> haderList, OutwardLSListActivity activity) {
        this.haderList = haderList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RVLSListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_outward_ls_list_layout, parent, false);
        return new RVLSListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVLSListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.lsNo.setText(haderList.get(position).getLsNo());
        holder.lsDate.setText(haderList.get(position).getLsDate());
        holder.destination.setText(haderList.get(position).getDestination()+"");
        holder.itemView.setOnClickListener(view -> activity.getbarcode(haderList.get(position).getLsNo(),holder.itemView));

    }

    @Override
    public int getItemCount() {
        return haderList.size();
    }

    public static class RVLSListViewHolder extends RecyclerView.ViewHolder {

        TextView lsNo, lsDate, destination, isStatus;
        LinearLayout ll_status;

        public RVLSListViewHolder(@NonNull View itemView) {
            super(itemView);

            lsNo = itemView.findViewById(R.id.tv_lsno);
            lsDate = itemView.findViewById(R.id.tv_date);
            destination = itemView.findViewById(R.id.tv_destination);
            ll_status = itemView.findViewById(R.id.ll_status);
            isStatus = itemView.findViewById(R.id.tv_lsStatus);
            itemView.setClickable(true);
        }
    }
}