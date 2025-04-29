package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.PRSDocketDetails;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.PRSBarcodePickupActivity;

public class ScanDcoketListAdapter extends RecyclerView.Adapter<ScanDcoketListAdapter.viewHolder> {

    PRSBarcodePickupActivity contex;
    ArrayList<PRSDocketDetails> scanDocketList;

    public ScanDcoketListAdapter(PRSBarcodePickupActivity prsBarcodePickupActivity, ArrayList<PRSDocketDetails> scanDocketList) {
        this.contex = prsBarcodePickupActivity;
        this.scanDocketList = scanDocketList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scanned_list,parent,false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tv_LsNo.setText(scanDocketList.get(position).getDocketNo());
        holder.tv_docket.setText(scanDocketList.get(position).getVehicleno());
        holder.tv_totalPkg.setText(scanDocketList.get(position).getPackages()+"");
        holder.tv_scanPkg.setText(scanDocketList.get(position).getPackageScanCount()+"");
        holder.tv_scannedBarcode.setText(scanDocketList.get(position).getLastScanPackage());
//        holder.tv_scannedBarcode.setText(scanDocketList.get(position).getDocketNo());
    }

    @Override
    public int getItemCount() {
        return scanDocketList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        TextView tv_LsNo, tv_docket , tv_scanPkg, tv_totalPkg, tv_scannedBarcode;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_LsNo = itemView.findViewById(R.id.tv_LsNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            tv_scannedBarcode = itemView.findViewById(R.id.tv_scannedBarcode);
        }
    }
}
