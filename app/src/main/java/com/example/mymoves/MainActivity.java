package com.example.mymoves;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoves.adapters.MovieAdapter;
import com.example.mymoves.data.MainViewModel;
import com.example.mymoves.data.Movie;
import com.example.mymoves.utils.JSONUtils;
import com.example.mymoves.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private Switch aSwitch;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private ProgressBar progressBar;

    private static int LOADER_ID = 133;
    private LoaderManager loaderManager;

    private static int page = 1;
    private static boolean isLoading = false;
    private static int methodOfSort;

    private MainViewModel viewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private int getColumCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width/185 > 2? width/185 : 2;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemMain:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavorite:
                Intent intent1 = new Intent(this,FavoriteActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewPosters);
        aSwitch = findViewById(R.id.switchSort);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerView.setLayoutManager(new GridLayoutManager(this,getColumCount()));
        progressBar = findViewById(R.id.progressBarLoading);
        movieAdapter = new MovieAdapter();
        loaderManager = LoaderManager.getInstance(this);
        recyclerView.setAdapter(movieAdapter);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                setMethodOfSort(isChecked);
            }
        });
        aSwitch.setChecked(false);
        movieAdapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if(!isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                    downloadData(methodOfSort,page);
                }
            }
        });

        LiveData<List<Movie>> listLiveData = viewModel.getListLiveData();
        listLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(page == 1){
                    movieAdapter.setMovies(movies);
                }
            }
        });
    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        aSwitch.setChecked(false);

    }

    public void onClickSetTopRated(View view) {
        setMethodOfSort(true);
        aSwitch.setChecked(true);
    }

    private void setMethodOfSort(boolean isCheck){
        if(isCheck){
            methodOfSort = NetworkUtils.TOP_RATED;
            textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewPopularity.setTextColor(getResources().getColor(R.color.Extract_White_Color));
        }
        else{
            methodOfSort = NetworkUtils.POPULARITY;
            textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewTopRated.setTextColor(getResources().getColor(R.color.Extract_White_Color));
        }
        downloadData(methodOfSort,page);
    }

    private void downloadData(int methodOfSort, int page){
        URL url = NetworkUtils.buildURL(methodOfSort,page);
        Bundle bundle = new Bundle();
        bundle.putString("url",url.toString());
        loaderManager.restartLoader(LOADER_ID,bundle,this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader= new NetworkUtils.JSONLoader(this,args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if(movies != null && !movies.isEmpty()) {
            if(page == 1) {
                viewModel.deleteAllMovie();
                movieAdapter.clear();
            }
            for(Movie movie:movies){
                viewModel.InsertAllMovie(movie);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}