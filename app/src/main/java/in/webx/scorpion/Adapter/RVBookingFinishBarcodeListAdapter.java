package in.webx.scorpion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.BookingDocketList_Tbl;
import in.webx.scorpion.R;

public class RVBookingFinishBarcodeListAdapter extends RecyclerView.Adapter<RVBookingFinishBarcodeListAdapter.RVBookingFinishBarcodeList> {
    Context context;
    ArrayList<BookingDocketList_Tbl> docketBarCodeSeriesTbls;

    public RVBookingFinishBarcodeListAdapter(ArrayList<BookingDocketList_Tbl> docketBarCodeSeriesTbls, Context context) {
        this.context = context;
        this.docketBarCodeSeriesTbls = docketBarCodeSeriesTbls;
    }

    @NonNull
    @Override
    public RVBookingFinishBarcodeList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_booking_finish_docket_barcode_list, parent, false);
        return new RVBookingFinishBarcodeList(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RVBookingFinishBarcodeList holder, int position) {
        holder.tv_docket.setText(docketBarCodeSeriesTbls.get(position).getDocketNo());
        holder.tv_scanPkg.setText(docketBarCodeSeriesTbls.get(position).getScanPackages()+"");
        holder.tv_totalPkg.setText(docketBarCodeSeriesTbls.get(position).getNoofPkgs()+"");

    }

    @Override
    public int getItemCount() {
        return docketBarCodeSeriesTbls.size();
    }

    public static class RVBookingFinishBarcodeList extends RecyclerView.ViewHolder {

        TextView tv_docket, tv_scanPkg,tv_totalPkg;

        public RVBookingFinishBarcodeList(@NonNull View itemView) {
            super(itemView);

            tv_docket = itemView.findViewById(R.id.tv_docket);
            tv_scanPkg = itemView.findViewById(R.id.tv_scanPkg);
            tv_totalPkg = itemView.findViewById(R.id.tv_totalPkg);
        }
    }
}
