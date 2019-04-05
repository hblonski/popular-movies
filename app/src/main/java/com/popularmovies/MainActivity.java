package com.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.popularmovies.adapter.MovieListAdapter;
import com.popularmovies.model.Movie;
import com.popularmovies.network.MovieApi;
import com.popularmovies.network.MovieController;
import com.popularmovies.network.MovieListSortOrder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int NUMBER_OF_COLUMNS_VERTICAL = 2;

    private final static int NUMBER_OF_COLUMNS_HORIZONTAL = 4;

    private MovieController movieController;

    RecyclerView moviesListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieController = ViewModelProviders.of(this).get(MovieController.class);

        moviesListRecyclerView = findViewById(R.id.movies_recycler_view);
        final MovieListAdapter movieListAdapter = new MovieListAdapter();
        moviesListRecyclerView.setAdapter(movieListAdapter);

        //Set fixed size for the RecyclerView items, since they'll always contain images of same size
        //See https://stackoverflow.com/questions/28709220/understanding-recyclerview-sethasfixedsize
        moviesListRecyclerView.setHasFixedSize(true);

        //To improve smoothness when scrolling
        moviesListRecyclerView.setItemViewCacheSize(MovieApi.Page.SIZE);

        //Changes the number of columns of the grid based on device orientation
        handleScreenRotationOnRecyclerView();

        observeDataChange(movieListAdapter);
        movieController.fetchMovieList(MovieListSortOrder.TOP_RATED, 1);
    }

    private void observeDataChange(final MovieListAdapter movieListAdapter) {
        movieController.getMoviesList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieListAdapter.setMovieList(movies);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleScreenRotationOnRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order_menu, menu);

        MenuItem item = menu.findItem(R.id.sort_order_spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_orders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Changes the selected item text color
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return true;
    }

    private void handleScreenRotationOnRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            moviesListRecyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS_VERTICAL));
        } else {
            moviesListRecyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS_HORIZONTAL));
        }
    }
}
