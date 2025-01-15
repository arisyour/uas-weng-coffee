package com.ariskurniawan.uaswengcaffe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    private final Context context;
    private final List<Produk> produkList;
    private final DatabaseHelper databaseHelper;

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

        // Data Produk
        holder.tvNamaProduk.setText(produk.getNama());
        holder.tvDeskripsiProduk.setText(produk.getDeskripsi());
        holder.tvHargaProduk.setText(String.format("Rp %,d", (int) produk.getHarga()));

        // Menampilkan gambar produk
        if (produk.getFoto() != null && !produk.getFoto().isEmpty()) {
            File imageFile = new File(produk.getFoto());
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                holder.imgProduk.setImageBitmap(bitmap);
            } else {
                Log.e("ProdukAdapter", "Gambar tidak ditemukan: " + produk.getFoto());
                holder.imgProduk.setImageResource(R.drawable.ic_placeholder); // Placeholder jika gambar tidak ditemukan
            }
        } else {
            holder.imgProduk.setImageResource(R.drawable.ic_placeholder); // Placeholder jika tidak ada gambar
        }

        // Tombol Update
        holder.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("PRODUCT_ID", produk.getId());
            context.startActivity(intent);
        });

        // Tombol Delete
        holder.btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Yakin ingin menghapus produk ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        int rowsDeleted = databaseHelper.deleteProduct(produk.getId());
                        if (rowsDeleted > 0) {
                            produkList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, produkList.size());
                            Toast.makeText(context, "Produk dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gagal menghapus produk", Toast.LENGTH_SHORT).show();
                        }
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
        private final ImageView imgProduk;
        private final TextView tvNamaProduk, tvDeskripsiProduk, tvHargaProduk;
        private final Button btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduk = itemView.findViewById(R.id.imgProduk);
            tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk);
            tvDeskripsiProduk = itemView.findViewById(R.id.tvDeskripsiProduk);
            tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
