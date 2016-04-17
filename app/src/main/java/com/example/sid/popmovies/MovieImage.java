package com.example.sid.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sid on 19/3/16.
 */
public class MovieImage implements Parcelable{
    private String imageUrl;

    public MovieImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private MovieImage(Parcel in) {
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

    public final Parcelable.Creator<MovieImage> CREATOR = new Parcelable.Creator<MovieImage>() {
        @Override
        public MovieImage createFromParcel(Parcel source) {
            return new MovieImage(source);
        }

        @Override
        public MovieImage[] newArray(int size) {
            return new MovieImage[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
