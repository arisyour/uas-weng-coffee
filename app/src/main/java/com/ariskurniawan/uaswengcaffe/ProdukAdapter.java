package com.ariskurniawan.uaswengcaffe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    private List<Produk> produkList;
    private Context context;
    private DatabaseHelper databaseHelper;

    public ProdukAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk produk = produkList.get(position);
        holder.tvNamaProduk.setText(produk.getNama());

        holder.btnUpdate.setOnClickListener(v -> {
            // TODO: Implementasi navigasi ke UpdateActivity
            Toast.makeText(context, "Update produk: " + produk.getNama(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Popup konfirmasi hapus
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi")
                    .setMessage("Yakin ingin menghapus produk ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        databaseHelper.deleteProduct(produk.getId());
                        produkList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, produkList.size());
                        Toast.makeText(context, "Produk dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaProduk;
        Button btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
