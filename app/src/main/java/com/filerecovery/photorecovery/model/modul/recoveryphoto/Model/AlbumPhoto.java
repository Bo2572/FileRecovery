package com.filerecovery.photorecovery.model.modul.recoveryphoto.Model;

import java.util.ArrayList;

public class AlbumPhoto {
    long lastModified;
    ArrayList<PhotoModel> listPhoto;
    String str_folder;

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long j) {
        this.lastModified = j;
    }

    public String getStr_folder() {
        return this.str_folder;
    }

    public void setStr_folder(String str) {
        this.str_folder = str;
    }

    public ArrayList<PhotoModel> getListPhoto() {
        return this.listPhoto;
    }

    public void setListPhoto(ArrayList<PhotoModel> arrayList) {
        this.listPhoto = arrayList;
    }
}
