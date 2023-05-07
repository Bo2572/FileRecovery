package com.filerecovery.photorecovery.model.modul.recoveryvideo.Model;

import java.io.Serializable;

public class VideoModel implements Serializable {
    boolean isCheck = false;
    long lastModifiedPhoto;
    String pathPhoto;
    long sizePhoto;
    String timeDuration;
    String typeFile;

    public VideoModel(String str, long j, long j2, String str2, String str3) {
        this.pathPhoto = str;
        this.lastModifiedPhoto = j;
        this.sizePhoto = j2;
        this.typeFile = str2;
        this.timeDuration = str3;
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

    public String getTypeFile() {
        return this.typeFile;
    }

    public void setTypeFile(String str) {
        this.typeFile = str;
    }

    public String getTimeDuration() {
        return this.timeDuration;
    }

    public void setTimeDuration(String str) {
        this.timeDuration = str;
    }
}
