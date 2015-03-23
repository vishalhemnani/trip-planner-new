package com.vjs.tripplanner;

import se.walkercrou.places.*;
import java.io.InputStream;
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

    public void testReq() {
        List<Place> places = client.getPlacesByRadar(TEST_PLACE_LAT, TEST_PLACE_LNG, 400.0, GooglePlaces.MAXIMUM_RESULTS, Param.name("types").value("food%7Ccafe"));

        for(Place place : places) {
            System.out.println(place.getPlaceId());
        }
    }

    public static void main(String arg[]) {
        DataFetcher fetcher = new DataFetcher();

        fetcher.testReq();
    }

}
