package com.example.mymoves.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorite_movie")
public class FavoriteMovie extends Movie {

    public FavoriteMovie(int uniquId,int id, int vote_count, String title, String original_title, String overview, String poster_path, String big_poster, String backdrop_path, double vote_average, String release_date) {
        super(uniquId,id, vote_count, title, original_title, overview, poster_path, big_poster, backdrop_path, vote_average, release_date);
    }

    @Ignore
    public FavoriteMovie(Movie movie){
        super(movie.getUniquId(),movie.getId(),movie.getVote_count(),movie.getTitle(),movie.getOriginal_title(),movie.getOverview(),movie.getPoster_path(),movie.getBig_poster(),movie.getBackdrop_path(),movie.getVote_average(),movie.getRelease_date());
    }

}
