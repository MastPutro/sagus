package com.example.sagu.history;

public class itemHistory {
    String meja, harga, mejaid, reservasiId, tanggal;
    Boolean status;

    public itemHistory() {
        this.meja = null;
        this.harga = null;
        this.mejaid = null;
        this.reservasiId = null;
        this.status = null;
        this.tanggal = null;
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

    public String getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(String reservasiId) {
        this.reservasiId = reservasiId;
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
}
