package com.ariskurniawan.uaswengcaffe;

public class Produk {
    private int id;
    private String nama;
    private String deskripsi;
    private String kategori;
    private double harga;
    private boolean tersediaCabangUtama;
    private boolean promoDiskon;
    private boolean takeawayTersedia;
    private String tanggal;

    // Constructor
    public Produk(int id, String nama, String deskripsi, String kategori, double harga,
                  boolean tersediaCabangUtama, boolean promoDiskon, boolean takeawayTersedia, String tanggal) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.harga = harga;
        this.tersediaCabangUtama = tersediaCabangUtama;
        this.promoDiskon = promoDiskon;
        this.takeawayTersedia = takeawayTersedia;
        this.tanggal = tanggal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public boolean isTersediaCabangUtama() {
        return tersediaCabangUtama;
    }

    public void setTersediaCabangUtama(boolean tersediaCabangUtama) {
        this.tersediaCabangUtama = tersediaCabangUtama;
    }

    public boolean isPromoDiskon() {
        return promoDiskon;
    }

    public void setPromoDiskon(boolean promoDiskon) {
        this.promoDiskon = promoDiskon;
    }

    public boolean isTakeawayTersedia() {
        return takeawayTersedia;
    }

    public void setTakeawayTersedia(boolean takeawayTersedia) {
        this.takeawayTersedia = takeawayTersedia;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
