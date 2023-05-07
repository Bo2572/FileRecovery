package com.filerecovery.photorecovery.model.modul.recoveryvideo.Model;

import java.util.ArrayList;

public class AlbumVideo {
    long lastModified;
    ArrayList<VideoModel> listPhoto;
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

    public ArrayList<VideoModel> getListPhoto() {
        return this.listPhoto;
    }

    public void setListPhoto(ArrayList<VideoModel> arrayList) {
        this.listPhoto = arrayList;
    }
}
