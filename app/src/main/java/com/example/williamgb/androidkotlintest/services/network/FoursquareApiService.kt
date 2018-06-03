package com.example.williamgb.androidkotlintest.services.network

import com.example.williamgb.androidkotlintest.models.Model
import io.reactivex.Observable
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.foursquare.com/v2/"
private const val VENUES_ENDPOINT = "venues/search"
public const val SEATTLE_CENTER_COORD = "47.6062,-122.3321"
private const val RADIUS_PARAM = "radius=20000" // 20Km from city center
private const val SEATTLE_LL_PARAM = "ll="+SEATTLE_CENTER_COORD
private const val CLIENT_ID = "VT2LIERXDSVYQCTTBWLJLRQTK3UURVZXBIYHKW1ZRLIGHXAB"
private const val CLIENT_SECRET = "CVRB350EQJXJMSS0LG0EVBWKECJLNCIGTOLKNECOEDKHDAT1"
private const val VERSION_PARAM = "v=20180527"
const val SEATTLE_VENUES_SEARCH_ENDPOINT =
    VENUES_ENDPOINT + "?" +
    SEATTLE_LL_PARAM +
    "&" + RADIUS_PARAM +
    "&" + VERSION_PARAM +
    "&client_id=" + CLIENT_ID +
    "&client_secret=" + CLIENT_SECRET


interface FoursquareApiService {

    @GET(SEATTLE_VENUES_SEARCH_ENDPOINT)
    fun getSeattleVenues(
        @Query("query") query: String
    ): Observable<Model.FoursquareResponse>

    companion object {
        fun create(): FoursquareApiService {

            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(FoursquareApiService::class.java)
        }
    }
}