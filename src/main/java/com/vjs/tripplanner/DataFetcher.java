package com.vjs.tripplanner;

import com.vjs.googleplaceswrapper.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Created by vishal on 3/22/15.
 */
public class DataFetcher {
    private static final String API_KEY_FILE_NAME = "places_api.key";
    private static final double TEST_PLACE_LAT = 34.052235, TEST_PLACE_LNG = -118.243683;

    private GooglePlaces client = null;

    public DataFetcher() {
        try {
            InputStream in = DataFetcher.class.getResourceAsStream("/" + API_KEY_FILE_NAME);
            client = new GooglePlaces(IOUtils.toString(in));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<Place> radarSearch(double lat, double lng, double radius, String typeStr) {
        List<Place> places = client.getPlacesByRadar(lat, lng, radius, Param.name("types").value(typeStr));

        return places;
    }
}
