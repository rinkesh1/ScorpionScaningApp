package in.webx.scorpion.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.R;
import in.webx.scorpion.RealmDatabase.docketBarCodeSeries_Tbl;

public class ThcBarcodeListAdapter extends RecyclerView.Adapter<ThcBarcodeListAdapter.ThcBarcodeListViewHolder> {

    Context context;
    ArrayList<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls;

    public ThcBarcodeListAdapter(ArrayList<docketBarCodeSeries_Tbl> docketBarCodeSeriesTbls, Context context) {

        this.docketBarCodeSeriesTbls = docketBarCodeSeriesTbls;
        this.context = context;

    }

    @NonNull
    @Override
    public ThcBarcodeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_thc_pending_docket_barcode_list, parent, false);
        return new ThcBarcodeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThcBarcodeListViewHolder holder, int position) {
        holder.tv_dktNo.setText(docketBarCodeSeriesTbls.get(position).getDockNo());
        holder.tv_barcodeNo.setText(docketBarCodeSeriesTbls.get(position).getBcSerialNo());
    }

    @Override
    public int getItemCount() {
        return docketBarCodeSeriesTbls.size();
    }

    public static class ThcBarcodeListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dktNo, tv_barcodeNo;

        public ThcBarcodeListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_dktNo = itemView.findViewById(R.id.tv_dktNo);
            tv_barcodeNo = itemView.findViewById(R.id.tv_barcodeNo);
        }
    }
}
