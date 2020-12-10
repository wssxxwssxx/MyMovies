package com.example.mymoves.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int uniquId;
    private int id;
    private int vote_count;
    private String title;
    private String original_title;
    private String overview;
    private String poster_path;
    private String big_poster;
    private String backdrop_path;
    private double vote_average;
    private String release_date;

    public Movie(int uniquId,int id, int vote_count, String title, String original_title, String overview, String poster_path,String big_poster, String backdrop_path, double vote_average, String release_date) {
        this.uniquId = uniquId;
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.big_poster = big_poster;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    @Ignore
    public Movie(int id, int vote_count, String title, String original_title, String overview, String poster_path,String big_poster, String backdrop_path, double vote_average, String release_date) {
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.big_poster = big_poster;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public String getBig_poster() {
        return big_poster;
    }

    public int getUniquId() {
        return uniquId;
    }

    public void setUniquId(int uniquId) {
        this.uniquId = uniquId;
    }

    public void setBig_poster(String big_poster) {
        this.big_poster = big_poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
