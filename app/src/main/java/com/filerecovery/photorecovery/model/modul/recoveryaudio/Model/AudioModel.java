package com.filerecovery.photorecovery.model.modul.recoveryaudio.Model;

public class AudioModel {
    boolean isCheck = false;
    long lastModifiedPhoto;
    String pathPhoto;
    long sizePhoto;

    public AudioModel(String str, long j, long j2) {
        this.pathPhoto = str;
        this.lastModifiedPhoto = j;
        this.sizePhoto = j2;
    }

    public long getLastModified() {
        return this.lastModifiedPhoto;
    }

    public void setLastModified(long j) {
        this.lastModifiedPhoto = j;
    }

    public void setSizePhoto(long j) {
        this.sizePhoto = j;
    }

    public boolean getIsCheck() {
        return this.isCheck;
    }

    public void setIsCheck(boolean z) {
        this.isCheck = z;
    }

    public long getSizePhoto() {
        return this.sizePhoto;
    }

    public String getPathPhoto() {
        return this.pathPhoto;
    }

    public void setPathPhoto(String str) {
        this.pathPhoto = str;
    }
}
