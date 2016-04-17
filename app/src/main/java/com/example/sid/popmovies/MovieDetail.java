package com.example.sid.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by sid on 19/3/16.
 */
public class MovieDetail implements Parcelable{
    private int id;
    private String posterUrl;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private LocalDate releaseDate;
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
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        releaseDate = LocalDate.parse(releaseDateStr, formatter);
        this.backdropUrl = "http://image.tmdb.org/t/p/w500" + backdropUrl;
    }

    private MovieDetail(Parcel in) {
        posterUrl = in.readString();
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
        dest.writeString(releaseDate.toString());
        dest.writeString(backdropUrl);
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    @Override
    public String toString() {
        return id + " " + posterUrl + " " + originalTitle + " " + overview.substring(0, 10) + " " + voteAverage +
                " " + releaseDate.toString() + " " + backdropUrl;
    }
}
