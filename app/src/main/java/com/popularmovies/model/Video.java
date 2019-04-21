package com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Video class. Some getters and setters, as well as the empty public constructor, despite being
 * flagged as "never used" by Android Studio, are required by the Jackson Converter.
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video implements Parcelable {

    public Video() {
        //Empty constructor
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("site")
    private String site;

    @JsonProperty("type")
    private String type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return this.site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(site);
        parcel.writeString(type);
    }

    protected Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        site = in.readString();
        type = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
