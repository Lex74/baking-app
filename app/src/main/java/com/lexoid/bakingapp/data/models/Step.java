package com.lexoid.bakingapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getShortDescription() { return this.shortDescription; }

    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public String getVideoURL() { return this.videoURL; }

    public void setVideoURL(String videoURL) { this.videoURL = videoURL; }

    public String getThumbnailURL() { return this.thumbnailURL; }

    public void setThumbnailURL(String thumbnailURL) { this.thumbnailURL = thumbnailURL; }
}
