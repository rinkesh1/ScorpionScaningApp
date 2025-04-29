package in.webx.scorpion.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.webx.scorpion.Model.InvoiceDataModel;
import in.webx.scorpion.R;

public class RVInvoiceAdapter extends RecyclerView.Adapter<RVInvoiceAdapter.RVInvoiceViewHolder>{
    ArrayList<InvoiceDataModel> mData;
    Context mInflater;


    public RVInvoiceAdapter(Context context, ArrayList<InvoiceDataModel> data) {
        this.mInflater = context;
        this.mData = data;
    }


    @NonNull
    @Override
    public RVInvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invoice_data, parent, false);
        return new RVInvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVInvoiceViewHolder holder, int position) {
        holder.txtinvoiceno.setText(mData.get(position).getInvoiceNo());
        holder.txtinvoicevalue.setText(mData.get(position).getInvoicevalue());
        holder.txtdelete.setOnClickListener(view -> {
            mData.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RVInvoiceViewHolder extends RecyclerView.ViewHolder {

        TextView txtinvoiceno;
        TextView txtinvoicevalue;
        TextView txtdelete;

        public RVInvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtinvoiceno = itemView.findViewById(R.id.txt_invoiceno);
            txtinvoicevalue = itemView.findViewById(R.id.txt_invoicevalue);
            txtdelete = itemView.findViewById(R.id.txt_delete);
        }
    }
}
