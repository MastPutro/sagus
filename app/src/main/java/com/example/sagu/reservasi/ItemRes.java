package com.example.sagu.reservasi;

public class ItemRes {
    String meja, harga, mejaid, tanggal, reservasiId;
    Boolean status;

    public ItemRes() {
        this.meja = null;
        this.harga = null;
        this.mejaid = null;
        this.status = null;
        this.tanggal = null;
        this.reservasiId = null;
    }

    public String getMeja() {
        return meja;
    }

    public void setMeja(String meja) {
        this.meja = meja;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getMejaid() {
        return mejaid;
    }

    public void setMejaid(String mejaid) {
        this.mejaid = mejaid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(String reservasiId) {
        this.reservasiId = reservasiId;
    }
}
