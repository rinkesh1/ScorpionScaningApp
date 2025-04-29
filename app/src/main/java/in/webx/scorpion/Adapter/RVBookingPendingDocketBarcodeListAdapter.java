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

import in.webx.scorpion.Model.BookingDocketList_Tbl;
import in.webx.scorpion.R;
import in.webx.scorpion.activity.BookingBarcodePickupActivity;
import in.webx.scorpion.util.SharedPreference;

public class RVBookingPendingDocketBarcodeListAdapter extends RecyclerView.Adapter<RVBookingPendingDocketBarcodeListAdapter.RVBookingDocketBarcodeListViewHolder>{
    ArrayList<BookingDocketList_Tbl> bookingDocketListTbls;
    BookingBarcodePickupActivity bookingBarcodePickupActivity;
    Boolean stetus;

    public RVBookingPendingDocketBarcodeListAdapter(ArrayList<BookingDocketList_Tbl> bookingDocketListTbls, BookingBarcodePickupActivity bookingBarcodePickupActivity, Boolean stetus) {
        this.bookingDocketListTbls = bookingDocketListTbls;
        this.bookingBarcodePickupActivity = bookingBarcodePickupActivity;
        SharedPreference.getInstance(bookingBarcodePickupActivity);
        this.stetus = stetus;
    }

    @NonNull
    @Override
    public RVBookingDocketBarcodeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_thc_pending_docket_list, parent, false);
        return new RVBookingDocketBarcodeListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVBookingDocketBarcodeListViewHolder holder, int position) {

        holder.tv_THCNo.setText(bookingDocketListTbls.get(position).getVehicleNo());
        holder.tv_docket.setText(bookingDocketListTbls.get(position).getDocketNo());
        holder.tv_scanPkg.setText(bookingDocketListTbls.get(position).getScanPackages()+"");
        holder.tv_totalPkg.setText(bookingDocketListTbls.get(position).getNoofPkgs()+"");
        holder.itemView.setOnClickListener(v -> bookingBarcodePickupActivity.showBarcodeList(stetus,bookingDocketListTbls.get(position).getDocketNo()));
    }

    @Override
    public int getItemCount() {
        return bookingDocketListTbls.size();
    }

    public static class RVBookingDocketBarcodeListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_THCNo, tv_docket, tv_scanPkg, tv_totalPkg;
        CardView cv_thcRoot;

        public RVBookingDocketBarcodeListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_THCNo = itemView.findViewById(R.id.tv_THCNo);
            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
            cv_thcRoot = itemView.findViewById(R.id.cv_thcRoot);
        }
    }
}
