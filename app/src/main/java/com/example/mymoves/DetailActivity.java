package com.example.mymoves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoves.adapters.ReviewAdapter;
import com.example.mymoves.adapters.TreillerAdapter;
import com.example.mymoves.data.FavoriteMovie;
import com.example.mymoves.data.MainViewModel;
import com.example.mymoves.data.Movie;
import com.example.mymoves.data.Review;
import com.example.mymoves.data.Treiller;
import com.example.mymoves.utils.JSONUtils;
import com.example.mymoves.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private ImageView imageViewAddToFavotit;
    private TextView title;
    private TextView originalTitle;
    private TextView rating;
    private TextView releaseDate;
    private TextView description;

    private RecyclerView recyclerViewTreiller;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TreillerAdapter treillerAdapter;
    private ScrollView scrollViewInfo;

    private MainViewModel viewModel;
    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private int id;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
        setContentView(R.layout.activity_detail);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        title = findViewById(R.id.textViewTitle);
        originalTitle = findViewById(R.id.textViewOriginalTitle);
        rating = findViewById(R.id.textViewRating);
        releaseDate = findViewById(R.id.textViewRelease);
        imageViewAddToFavotit = findViewById(R.id.imageViewAddToFavotit);
        description = findViewById(R.id.textViewOverview);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);
        final Intent intent = getIntent();
        if(intent.hasExtra("id") && intent != null){
          id = intent.getIntExtra("id",-1);
        }
        else{
            finish();
        }
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id);
        Picasso.get().load(movie.getBig_poster()).placeholder(R.drawable.draw).into(imageViewBigPoster);
        title.setText(movie.getTitle());
        originalTitle.setText(movie.getOriginal_title());
        description.setText(movie.getOverview());
        releaseDate.setText(movie.getRelease_date());
        rating.setText(Double.toString(movie.getVote_average()));
        setFavorite();
        recyclerViewTreiller = findViewById(R.id.recyclerViewTreiller);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        reviewAdapter = new ReviewAdapter();
        treillerAdapter = new TreillerAdapter();
        treillerAdapter.setOnClickLTreillerListener(new TreillerAdapter.onClickLTreillerListener() {
            @Override
            public void onTreillerClick(String url) {
                Intent intentToTreiller = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTreiller);
            }
        });
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTreiller.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTreiller.setAdapter(treillerAdapter);
        JSONObject jsonObject = NetworkUtils.getJSONForVideos(movie.getId());
        JSONObject jsonObject1 = NetworkUtils.getJSONForReviews(movie.getId());
        ArrayList<Treiller> treillers = JSONUtils.getTreillerFromJSON(jsonObject);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObject1);
        reviewAdapter.setReviews(reviews);
        treillerAdapter.setTreillers(treillers);
        scrollViewInfo.smoothScrollTo(0,0);
    }

    public void onClickChengeFavorite(View view) {
        if(favoriteMovie == null){
            viewModel.InsertFavoriteAllMovie(new FavoriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
        }
        else{

            viewModel.deleteFavoriteMovie(favoriteMovie);
            Toast.makeText(this, R.string.delete_to_favorite, Toast.LENGTH_SHORT).show();
        }
        setFavorite();
    }

    private void setFavorite(){
        favoriteMovie = viewModel.getFavoriteMovieById(id);
        if(favoriteMovie == null){
            imageViewAddToFavotit.setImageResource(R.drawable.starblack);

        }
        else{
            imageViewAddToFavotit.setImageResource(R.drawable.star);
        }
    }
}