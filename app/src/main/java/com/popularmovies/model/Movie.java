package com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.popularmovies.util.FormatUtil;

import java.text.ParseException;

/**
 * Movie class. Some getters and setters, as well as the empty public constructor, despite being
 * flagged as "never used" by Android Studio, are required by the Jackson Converter.
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Parcelable {

    public Movie() {
        //Empty constructor
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("title")
    private String title;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        try {
            this.releaseDate = FormatUtil.formatDate(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            this.releaseDate = null;
        }
    }

    //Parcelable interface methods implementation. This interface is used to allow a Movie object
    //to be passed between activities/fragments.

    //Constructor that takes a Parcel and gives you an object populated with it's values. The order
    //must be the same used in the writeToParcel method (see below).
    private Movie(Parcel in) {
        id = in.readString();
        voteAverage = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    //This is used to regenerate the object. All Parcelables must have a CREATOR
    //that implements these two methods
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }}
