package com.example.socalbeach4life;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;
import java.io.IOException;

public class NearbyBeachSearch {
    public PlacesSearchResponse run(double lat, double lng){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAqYUcSqLwdM7aXMgeKKfxmYwjRf04lAgE")
                .build();
        LatLng location = new LatLng(lat, lng);
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(40000)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("beach")
                    .language("en")
                    .type(PlaceType.TOURIST_ATTRACTION)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return request;
    }
}
