package com.example.sid.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sid on 19/3/16.
 */
public class MovieDetail implements Parcelable{
    private String imageUrl;

    public MovieDetail(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private MovieDetail(Parcel in) {
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }

    public final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
