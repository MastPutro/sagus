package com.example.sagu.menu;

public class Item {
    public String dataHarga, dataMenu, dataImage, key;

    public Item() {
        this.dataMenu = null;
        this.dataHarga = null;
        this.dataImage = null;
        this.key = null;

    }

    public String getDataHarga() {
        return dataHarga;
    }

    public void setDataHarga(String dataHarga) {
        this.dataHarga = dataHarga;
    }

    public String getDataMenu() {
        return dataMenu;
    }

    public void setDataMenu(String dataMenu) {
        this.dataMenu = dataMenu;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
