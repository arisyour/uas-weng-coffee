package com.ariskurniawan.uaswengcaffe;

public class Produk {
    private int id;
    private String nama;
    private String deskripsi;
    private String kategori;
    private double harga;
    private boolean promoDiskon;
    private boolean takeawayTersedia;
    private String foto; // URI atau path gambar
    private String tanggal;

    // Constructor
    public Produk(int id, String nama, String deskripsi, String kategori, double harga,
                  boolean promoDiskon, boolean takeawayTersedia, String foto, String tanggal) {
        this.id = id;
        setNama(nama);
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        setHarga(harga);
        this.promoDiskon = promoDiskon;
        this.takeawayTersedia = takeawayTersedia;
        this.foto = foto;
        this.tanggal = tanggal;
    }

    // Default Constructor
    public Produk() {
        // Default konstruktor
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
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        }
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
        if (harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
        this.harga = harga;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    // Metode Utilitas
    public boolean isMakanan() {
        return "Makanan".equalsIgnoreCase(kategori);
    }

    public boolean isMinuman() {
        return "Minuman".equalsIgnoreCase(kategori);
    }

    @Override
    public String toString() {
        return "Produk{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", kategori='" + kategori + '\'' +
                ", harga=" + harga +
                ", promoDiskon=" + promoDiskon +
                ", takeawayTersedia=" + takeawayTersedia +
                ", foto='" + foto + '\'' +
                ", tanggal='" + tanggal + '\'' +
                '}';
    }
}
