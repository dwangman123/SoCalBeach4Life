package com.example.socalbeach4life;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NearbySearch {
    public PlacesSearchResponse run(double lat, double lng, int radius, String term, PlaceType type, String key){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        LatLng location = new LatLng(lat, lng);
        while(request == null || request.results == null ||  request.results.length == 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                request = PlacesApi.nearbySearchQuery(context, location)
                        .radius(radius)
                        .rankby(RankBy.PROMINENCE)
                        .keyword(term)
                        .language("en")
                        .type(type)
                        .await();
            } catch (ApiException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return request;
    }
}