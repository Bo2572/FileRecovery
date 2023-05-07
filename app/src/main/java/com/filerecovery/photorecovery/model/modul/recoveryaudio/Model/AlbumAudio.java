package com.filerecovery.photorecovery.model.modul.recoveryaudio.Model;

import java.util.ArrayList;

public class AlbumAudio {
    long lastModified;
    ArrayList<AudioModel> listPhoto;
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

    public ArrayList<AudioModel> getListPhoto() {
        return this.listPhoto;
    }

    public void setListPhoto(ArrayList<AudioModel> arrayList) {
        this.listPhoto = arrayList;
    }
}
