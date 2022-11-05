package com.example.socalbeach4life;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;
import java.io.IOException;

public class NearbySearch {
    public PlacesSearchResponse run(double lat, double lng, int radius, String term, PlaceType type, String key){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();
        LatLng location = new LatLng(lat, lng);
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
        return request;
    }
}