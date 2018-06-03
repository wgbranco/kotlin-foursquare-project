package com.example.williamgb.androidkotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.example.williamgb.androidkotlintest.actions.FoursquareSearchAction
import com.example.williamgb.androidkotlintest.controllers.VenueListAdapter
import com.example.williamgb.androidkotlintest.controllers.mainStore
import com.example.williamgb.androidkotlintest.state.PlaceSearchState
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import tw.geothings.rekotlin.StoreSubscriber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), StoreSubscriber<PlaceSearchState> {

    val TAG = "MainActivity"

    private val mSearchProgressBar: ProgressBar by lazy {
        this.findViewById(R.id.searchProgressBar) as ProgressBar
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Subscribe to ReKotlin Store
        mainStore.subscribe(this){
            it.select {
                it.placeSearchState
            }.skipRepeats { oldState, newState ->
                oldState == newState
            }
        }

        // Detect changes on search input
        searchEditText
        .textChanges()
        .skip(1)
        .map { it -> it.toString() }
        .debounce(500, TimeUnit.MILLISECONDS)
        .filter(Predicate { it: String ->
            return@Predicate it.isNotEmpty() && (it.length > 2)
        })
        .distinctUntilChanged()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            mainStore.dispatch(FoursquareSearchAction(it))
        })

        val context = this
        fab.setOnClickListener({
            val intent = MapVenuesActivity.newIntent(context)
            startActivity(intent)
        })
    }

    override fun newState(state: PlaceSearchState)
    {
        // Progress bar
        searchProgressBar.visibility = if (state.isFetching) View.VISIBLE else View.INVISIBLE

        // Search result list
        val listVenues = state.venues.values.toList()

        // Empty list view
        layoutEmptyList.visibility = if (state.isComplete && !state.isFetching && listVenues.isEmpty()) View.VISIBLE else View.GONE

        // List view
        val adapter = VenueListAdapter(this, listVenues)
        venueListView.adapter = adapter
        val listVisibility = if ( state.isComplete &&
                                        listVenues.isNotEmpty()
                                    ) View.VISIBLE else View.GONE

        venueListView.visibility = listVisibility

        // FAB view
        fab.visibility = listVisibility

        // On Click Listener
        val context = this
        venueListView.setOnItemClickListener { _, _, position, _ ->
            val selectedRecipe = listVenues[position]

            val detailIntent = VenueDetailsActivity.newIntent(context, selectedRecipe)

            startActivity(detailIntent)
        }
    }

    override fun onDestroy()
    {
        mainStore.unsubscribe(this)
        super.onDestroy()
    }

    override fun onBackPressed() = Unit
}
