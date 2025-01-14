package com.ariskurniawan.uaswengcaffe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wengCaffe.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Column Names
    private static final String TABLE_PRODUCT = "product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "nama";
    private static final String COLUMN_DESCRIPTION = "deskripsi";
    private static final String COLUMN_CATEGORY = "kategori";
    private static final String COLUMN_PRICE = "harga";
    private static final String COLUMN_BRANCH_AVAILABLE = "tersedia_cabang_utama";
    private static final String COLUMN_PROMO_DISCOUNT = "promo_diskon";
    private static final String COLUMN_TAKEAWAY_AVAILABLE = "takeaway_tersedia";
    private static final String COLUMN_DATE = "tanggal";

    // Create Table Query
    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_BRANCH_AVAILABLE + " INTEGER, " +
                    COLUMN_PROMO_DISCOUNT + " INTEGER, " +
                    COLUMN_TAKEAWAY_AVAILABLE + " INTEGER, " +
                    COLUMN_DATE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    // CRUD Operations

    // Insert Data
    public long insertProduct(String name, String description, String category, double price,
                              boolean branchAvailable, boolean promoDiscount, boolean takeawayAvailable, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BRANCH_AVAILABLE, branchAvailable ? 1 : 0);
        values.put(COLUMN_PROMO_DISCOUNT, promoDiscount ? 1 : 0);
        values.put(COLUMN_TAKEAWAY_AVAILABLE, takeawayAvailable ? 1 : 0);
        values.put(COLUMN_DATE, date);
        return db.insert(TABLE_PRODUCT, null, values);
    }

    // Get All Data
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCT, null);
    }

    // Update Data
    public int updateProduct(int id, String name, String description, String category, double price,
                             boolean branchAvailable, boolean promoDiscount, boolean takeawayAvailable, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_BRANCH_AVAILABLE, branchAvailable ? 1 : 0);
        values.put(COLUMN_PROMO_DISCOUNT, promoDiscount ? 1 : 0);
        values.put(COLUMN_TAKEAWAY_AVAILABLE, takeawayAvailable ? 1 : 0);
        values.put(COLUMN_DATE, date);
        return db.update(TABLE_PRODUCT, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete Data
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCT, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
