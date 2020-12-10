package com.example.mymoves.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MovieDataBase movieDataBase;
    private LiveData<List<Movie>> listLiveData;
    private LiveData<List<FavoriteMovie>> liveDataFavotite;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieDataBase = MovieDataBase.getInstance(getApplication());
        listLiveData = movieDataBase.movieDao().getAllMovies();
        liveDataFavotite = movieDataBase.movieDao().getAllFavoriteMovies();
    }

    public Movie getMovieById(int id){
        try {
            return new GetMovieTast().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavoriteMovie getFavoriteMovieById(int id){
        try {
            return new GetFavoriteMovieTast().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<FavoriteMovie>> getLiveDataFavotite() {
        return liveDataFavotite;
    }

    public void deleteAllMovie(){
        new DeleteMovieTast().execute();
    }

    public void InsertAllMovie(Movie movie){
        new InsertMovieTast().execute(movie);
    }

    public void deleteMovie(Movie movie){
        new DeleteTast().execute(movie);
    }

    public void InsertFavoriteAllMovie(FavoriteMovie movie){
        new InsertFavoriteMovieTast().execute(movie);
    }

    public void deleteFavoriteMovie(FavoriteMovie movie){
        new DeleteFavoriteTast().execute(movie);
    }

    public LiveData<List<Movie>> getListLiveData() {
        return listLiveData;
    }

    private static class GetMovieTast extends AsyncTask<Integer,Void,Movie>{

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return movieDataBase.movieDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class GetFavoriteMovieTast extends AsyncTask<Integer,Void,FavoriteMovie>{

        @Override
        protected FavoriteMovie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return movieDataBase.movieDao().getFavoriteMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class DeleteMovieTast extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... integers) {
                 movieDataBase.movieDao().deleteAllMovies();
            return null;
        }
    }

    private static class InsertMovieTast extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... integers) {
            if(integers != null && integers.length > 0){
                movieDataBase.movieDao().insertMovie(integers[0]);
            }
            return null;
        }

    }

    private static class DeleteTast extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... integers) {
            if(integers != null && integers.length > 0){
                movieDataBase.movieDao().deleteMovie(integers[0]);
            }
            return null;
        }

    }

    private static class DeleteFavoriteTast extends AsyncTask<FavoriteMovie,Void,Void>{

        @Override
        protected Void doInBackground(FavoriteMovie... integers) {
            if(integers != null && integers.length > 0){
                movieDataBase.movieDao().deleteFavoriteMovie(integers[0]);
            }
            return null;
        }

    }

    private static class InsertFavoriteMovieTast extends AsyncTask<FavoriteMovie,Void,Void>{

        @Override
        protected Void doInBackground(FavoriteMovie... integers) {
            if(integers != null && integers.length > 0){
                movieDataBase.movieDao().insertFavoriteMovie(integers[0]);
            }
            return null;
        }

    }
}
