package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.BookingDocketList_Tbl;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.BookingBarcodePickupActivity;

public class RVBookingScaneAdapter extends RecyclerView.Adapter<RVBookingScaneAdapter.RVBookingScaneViewHolder>{

    BookingBarcodePickupActivity bookingBarcodePickupActivity;
    ArrayList<BookingDocketList_Tbl> getEScanListDetail;

    public RVBookingScaneAdapter(ArrayList<BookingDocketList_Tbl> getEScanListDetail, BookingBarcodePickupActivity bookingBarcodePickupActivity) {
        this.bookingBarcodePickupActivity = bookingBarcodePickupActivity;
        this.getEScanListDetail = getEScanListDetail;
    }

    @NonNull
    @Override
    public RVBookingScaneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_scanned_list, parent, false);
        return new RVBookingScaneViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVBookingScaneViewHolder holder, int position) {
        holder.tv_LsNo.setText(getEScanListDetail.get(position).getVehicleNo()+"");
        holder.tv_docket.setText(getEScanListDetail.get(position).getDocketNo());
        holder.tv_scanPkg.setText(getEScanListDetail.get(position).getScanPackages()+"");
        holder.tv_totalPkg.setText(getEScanListDetail.get(position).getNoofPkgs()+"");
        holder.tv_scannedBarcode.setText(getEScanListDetail.get(position).getLastBcSerialNo());
    }

    @Override
    public int getItemCount() {
        return getEScanListDetail.size();
    }

    public static class RVBookingScaneViewHolder extends RecyclerView.ViewHolder {

        TextView tv_LsNo, tv_docket , tv_scanPkg, tv_totalPkg, tv_scannedBarcode;

        public RVBookingScaneViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_LsNo = itemView.findViewById(R.id.tv_LsNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            tv_scannedBarcode = itemView.findViewById(R.id.tv_scannedBarcode);
        }
    }
}
