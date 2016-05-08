package com.example.sid.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sid on 19/3/16.
 */
public class MovieDetail implements Parcelable{
    private int id;
    private String posterUrl;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private String backdropUrl;

    public MovieDetail(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public MovieDetail(int id, String posterPath, String originalTitle, String overview, double voteAverage, String releaseDateStr, String backdropUrl) {
        this.id = id;
        this.posterUrl = "http://image.tmdb.org/t/p/w154" + posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//        releaseDate = LocalDate.parse(releaseDateStr, formatter);
        this.releaseDate = releaseDateStr;
        this.backdropUrl = "http://image.tmdb.org/t/p/w500" + backdropUrl;
    }

    private MovieDetail(Parcel in) {
        id = in.readInt();
        posterUrl = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//        releaseDate = LocalDate.parse(in.readString(), formatter);
        releaseDate = in.readString();
        this.backdropUrl = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterUrl);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(backdropUrl);
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    @Override
    public String toString() {
        return id + " " + posterUrl + " " + originalTitle + " " + overview.substring(0, 10) + " " + voteAverage +
                " " + releaseDate + " " + backdropUrl;
    }
}
