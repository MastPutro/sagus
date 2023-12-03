package com.example.sagu.pesanan;

public class ItemP {
    String fname, fid, famount, harga;

    public ItemP(String fname, String fid, String famount, String harga) {
        this.fname = fname;
        this.fid = fid;
        this.famount = famount;
        this.harga = harga;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFamount() {
        return famount;
    }

    public void setFamount(String famount) {
        this.famount = famount;
    }
}
