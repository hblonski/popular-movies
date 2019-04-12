package com.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

    public final static int VISIBLE_THRESHOLD = 6;

    private MovieController movieController;

    private MovieListSortOrder currentSortOrder;

    private RecyclerView moviesListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieController = ViewModelProviders.of(this).get(MovieController.class);

        moviesListRecyclerView = findViewById(R.id.movies_recycler_view);
        final MovieListAdapter movieListAdapter = new MovieListAdapter(this);
        moviesListRecyclerView.setAdapter(movieListAdapter);

        //Set fixed size for the RecyclerView items, since they'll always contain images of same size
        //See https://stackoverflow.com/questions/28709220/understanding-recyclerview-sethasfixedsize
        moviesListRecyclerView.setHasFixedSize(true);

        //To improve smoothness when scrolling
        moviesListRecyclerView.setItemViewCacheSize(MovieApi.Page.SIZE);

        //Changes the number of columns of the grid based on device orientation
        handleScreenRotationOnRecyclerView();

        //Infinite scroll
        moviesListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((GridLayoutManager)
                        moviesListRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if (!movieController.isLoading() &&
                        (lastVisibleItemPosition + VISIBLE_THRESHOLD) > MovieApi.Page.SIZE && dy > 0) {
                    movieController.fetchNextMoviesListPage(MovieListSortOrder.POPULAR);
                }
            }
        });

        observeDataChange(movieListAdapter);
    }

    private void observeDataChange(final MovieListAdapter movieListAdapter) {
        movieController.getMoviesList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieListAdapter.setMovieList(movies);
                handleNoResultsLoaded(movies.size());
            }
        });
    }

    private void handleNoResultsLoaded(int resultsCount) {
        //If there are no results, a "No results" label is loaded on the screen
        TextView noResultsLoadedLabel = findViewById(R.id.label_no_results);
        if (resultsCount == 0) {
            noResultsLoadedLabel.setVisibility(View.VISIBLE);
        } else {
            noResultsLoadedLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleScreenRotationOnRecyclerView();
    }

    private void handleScreenRotationOnRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            moviesListRecyclerView.setLayoutManager(
                    new GridLayoutManager(this, NUMBER_OF_COLUMNS_VERTICAL));
        } else {
            moviesListRecyclerView.setLayoutManager(
                    new GridLayoutManager(this, NUMBER_OF_COLUMNS_HORIZONTAL));
        }
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedItem = (TextView) parent.getChildAt(0);
                selectedItem.setTextColor(Color.WHITE);

                String selectedItemLabel = selectedItem.getText().toString();
                if (selectedItemLabel.equals(getResources().getString(R.string.popular))) {
                    currentSortOrder = MovieListSortOrder.POPULAR;
                    movieController.fetchNextMoviesListPage(currentSortOrder);
                } else {
                    currentSortOrder = MovieListSortOrder.TOP_RATED;
                    movieController.fetchNextMoviesListPage(currentSortOrder);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Empty
            }
        });

        return true;
    }

    /**
     * Creates a {@link Fragment} on top of the activity.
     *
     * @param fragment the {@link Fragment}
     */
    public void createFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,
                R.anim.slide_down,
                R.anim.slide_up,
                R.anim.slide_down);
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        //Necessary so that the user can navigate back
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
