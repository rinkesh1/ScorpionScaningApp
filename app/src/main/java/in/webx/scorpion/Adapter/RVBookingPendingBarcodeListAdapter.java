package in.webx.scorpion.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.BookingdocketBarCodeSeries_Tbl;
import in.webx.scorpion.R;

public class RVBookingPendingBarcodeListAdapter extends RecyclerView.Adapter<RVBookingPendingBarcodeListAdapter.RVBookingBarcodeListViewHolder>{
    Context context;
    ArrayList<BookingdocketBarCodeSeries_Tbl> docketBarCodeSeriesTbls;

    public RVBookingPendingBarcodeListAdapter(ArrayList<BookingdocketBarCodeSeries_Tbl> docketBarCodeSeriesTbls, Context context) {

        this.docketBarCodeSeriesTbls = docketBarCodeSeriesTbls;
        this.context = context;

    }

    @NonNull
    @Override
    public RVBookingBarcodeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_thc_pending_docket_barcode_list, parent, false);
        return new RVBookingBarcodeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVBookingPendingBarcodeListAdapter.RVBookingBarcodeListViewHolder holder, int position) {
        holder.tv_dktNo.setText(docketBarCodeSeriesTbls.get(position).getDocketNo());
        holder.tv_barcodeNo.setText(docketBarCodeSeriesTbls.get(position).getBcSeriesNo());
    }

    @Override
    public int getItemCount() {
        return docketBarCodeSeriesTbls.size();
    }

    public static class RVBookingBarcodeListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dktNo, tv_barcodeNo;

        public RVBookingBarcodeListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_dktNo = itemView.findViewById(R.id.tv_dktNo);
            tv_barcodeNo = itemView.findViewById(R.id.tv_barcodeNo);
        }
    }
}
