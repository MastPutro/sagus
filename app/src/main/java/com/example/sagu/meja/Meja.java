package com.example.sagu.meja;

public class Meja {
    public String mejan, keterangan, key;

    public Meja() {
        this.mejan = null;
        this.keterangan = null;
        this.key = null;
    }

    public String getMejan() {
        return mejan;
    }

    public void setMejan(String mejan) {
        this.mejan = mejan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
