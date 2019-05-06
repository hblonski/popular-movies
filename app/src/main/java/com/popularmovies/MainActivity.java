package com.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.popularmovies.adapter.MovieListAdapter;
import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.data.viewmodel.FavoriteMovieViewModel;
import com.popularmovies.network.themoviedb.MovieListSortOrder;
import com.popularmovies.network.themoviedb.viewmodel.MoviesViewModel;
import com.popularmovies.util.bus.EventBus;
import com.popularmovies.util.bus.MovieEvent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int VISIBLE_THRESHOLD = 6;

    private MoviesViewModel moviesViewModel;

    private MovieListSortOrder currentSortOrder;

    private GridView moviesGridView;

    private FavoriteMovieViewModel favoriteMovieViewModel;

    private List<FavoriteMovie> favoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        favoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);

        moviesGridView = findViewById(R.id.movies_grid_view);
        final MovieListAdapter movieListAdapter = new MovieListAdapter(this);
        moviesGridView.setAdapter(movieListAdapter);

        //Infinite scroll
        moviesGridView.setOnScrollListener(new GridView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Empty
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItemPosition = firstVisibleItem + visibleItemCount;
                if (!moviesViewModel.isLoading() &&
                        firstVisibleItem > 0 &&
                        (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
                    moviesViewModel.fetchNextMoviesPage();
                }
            }
        });

        moviesViewModel.getMoviesList().observe(this, movies -> {
            movieListAdapter.setMovieList(movies);
            handleNoResultsLoaded(movies != null ? movies.size() : 0);
        });

        favoriteMovieViewModel.findAll().observe(this, fm -> favoriteMovies = fm);
        subscribeToEventBus();
    }

    private void subscribeToEventBus() {
        EventBus.getInstance().getObservable().subscribe(e -> {
            if ((e.getType().equals(MovieEvent.Type.LOADED_MOVIE_LIST)
            || e.getType().equals(MovieEvent.Type.LOADED_MOVIE_DETAILS))
            && !e.isSuccess()) {
                Toast.makeText(this,
                        R.string.connection_error_the_movie_db,
                        Toast.LENGTH_LONG).show();
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
                if (selectedItemLabel.equals(getResources().getString(R.string.favorites))) {
                    currentSortOrder = MovieListSortOrder.FAVORITES;
                    moviesViewModel.setCurrentSortOrder(currentSortOrder);
                    moviesViewModel.fetchMovieListDetails(favoriteMovies);
                } else {
                    if (selectedItemLabel.equals(getResources().getString(R.string.popular))) {
                        currentSortOrder = MovieListSortOrder.POPULAR;
                    } else {
                        currentSortOrder = MovieListSortOrder.TOP_RATED;
                    }
                    moviesViewModel.setCurrentSortOrder(currentSortOrder);
                    moviesViewModel.fetchNextMoviesPage();
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
