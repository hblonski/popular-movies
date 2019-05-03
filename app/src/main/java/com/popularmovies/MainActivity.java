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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.popularmovies.adapter.MovieListAdapter;
import com.popularmovies.network.themoviedb.MovieListSortOrder;
import com.popularmovies.network.themoviedb.viewmodel.MoviesViewModel;

public class MainActivity extends AppCompatActivity {

    private final static int VISIBLE_THRESHOLD = 6;

    private MoviesViewModel moviesViewModel;

    private MovieListSortOrder currentSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        GridView moviesGridView = findViewById(R.id.movies_grid_view);
        final MovieListAdapter movieListAdapter = new MovieListAdapter(this);
        moviesGridView.setAdapter(movieListAdapter);

        //Infinite scroll
        moviesGridView.setOnScrollListener(new GridView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItemPosition = firstVisibleItem + visibleItemCount;
                if (!moviesViewModel.isLoading() &&
                        firstVisibleItem > 0 &&
                        (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
                    moviesViewModel.fetchNextMoviesListPage();
                }
            }
        });

        observeDataChange(movieListAdapter);
    }

    private void observeDataChange(final MovieListAdapter movieListAdapter) {
        moviesViewModel.getMoviesList().observe(this, movies -> {
            movieListAdapter.setMovieList(movies);
            handleNoResultsLoaded(movies != null ? movies.size() : 0);
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
                if (selectedItemLabel.equals(getResources().getString(R.string.popular))) {
                    currentSortOrder = MovieListSortOrder.POPULAR;
                } else {
                    currentSortOrder = MovieListSortOrder.TOP_RATED;
                }
                moviesViewModel.setCurrentSortOrder(currentSortOrder);
                moviesViewModel.fetchNextMoviesListPage();
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
