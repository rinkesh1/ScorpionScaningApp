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

import in.webx.scorpion.Model.PRSDocketDetails;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.PRSBarcodePickupActivity;

public class PRSGenDocketAdapter extends RecyclerView.Adapter<PRSGenDocketAdapter.viewHolder> {

    PRSBarcodePickupActivity activity;
    ArrayList<PRSDocketDetails> docketList;
    boolean status;

    public PRSGenDocketAdapter(ArrayList<PRSDocketDetails> docketList, PRSBarcodePickupActivity prsBarcodePickupActivity, boolean status) {
        this.activity = prsBarcodePickupActivity;
        this.docketList = docketList;
        this.status = status;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thc_pending_docket_list,parent,false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tv_THCNo.setText(docketList.get(position).getDocketNo());
        holder.tv_docket.setText(docketList.get(position).getVehicleno());
        holder.tv_scanPkg.setText(docketList.get(position).getPackageScanCount()+"");
        holder.tv_totalPkg.setText(docketList.get(position).getPackages()+"");
        holder.cv_thcRoot.setOnClickListener(v -> activity.showPendingBarcode(docketList.get(position).getDocketNo(),docketList.get(position).getVehicleno(),status));
    }

    @Override
    public int getItemCount() {
        return docketList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        TextView tv_THCNo, tv_docket, tv_scanPkg, tv_totalPkg;
        CardView cv_thcRoot;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_THCNo = itemView.findViewById(R.id.tv_THCNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            cv_thcRoot = itemView.findViewById(R.id.cv_thcRoot);
        }
    }
}
