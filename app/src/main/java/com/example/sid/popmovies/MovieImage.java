package com.example.sid.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sid on 19/3/16.
 */
public class MovieImage implements Parcelable{
    int image;

    public MovieImage(int image) {
        this.image = image;
    }

    private MovieImage(Parcel in) {
        image = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
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
}
