package com.example.sagu.pembayaran;

public class ItemPem {
    String barang, foodid, jumlah, reservasiId;

    public ItemPem() {
        this.barang = null;
        this.foodid = null;
        this.jumlah = null;
        this.reservasiId = null;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(String reservasiId) {
        this.reservasiId = reservasiId;
    }
}
