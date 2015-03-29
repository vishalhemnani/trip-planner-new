package com.vjs.tripplanner;

import com.vjs.googleplaceswrapper.Hours;
import org.postgis.Point;
import com.vjs.googleplaceswrapper.Place;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by vishal on 3/23/15.
 */
public class Main {
    public static final double LA_LAT = 34.052235;
    public static final double LA_LNG = -118.243683;

    public static final double RADIUS = 50000.0;
    public static final int COUNT = 45;

    public static final String DB_NAME = "trip_planner";
    public static final String DB_USER = "vishal";
    public static final String DB_PASS = "momdadgod";

    public static final String PIPE_SEPARATOR = "%7C";

    public static final String RELIGIOUS = "church" + PIPE_SEPARATOR + "hindu_temple" + PIPE_SEPARATOR + "mosque" + PIPE_SEPARATOR + "place_of_worship";
    public static final String ARTS_ENTERTAINMENT = "bowling_alley" + PIPE_SEPARATOR + "casino" + PIPE_SEPARATOR + "museum" + PIPE_SEPARATOR + "zoo" + PIPE_SEPARATOR + "aquarium" + PIPE_SEPARATOR + "amusement_park" + PIPE_SEPARATOR + "art_gallery";
    public static final String NIGHTLIFE = "bar" + PIPE_SEPARATOR + "liquor_store" + PIPE_SEPARATOR + "night_club";
    public static final String SHOPPING = "clothing_store" + PIPE_SEPARATOR + "shopping_mall" + PIPE_SEPARATOR + "shoe_store";
    public static final String FOOD = "bakery" + PIPE_SEPARATOR + "bar" + PIPE_SEPARATOR + "cafe" + PIPE_SEPARATOR + "food" + PIPE_SEPARATOR + "restaurant";
    public static final String BEAUTY = "beauty_salon" + PIPE_SEPARATOR + "hair_care" + PIPE_SEPARATOR + "health" + PIPE_SEPARATOR + "spa";


    public static final ArrayList<String> CATEGORY_QUERIES;
    public static final HashMap<String, ArrayList<String>> SUBCATEGORIES_MAP;

    static {
        CATEGORY_QUERIES = new ArrayList<String>();
        CATEGORY_QUERIES.add(RELIGIOUS);
        CATEGORY_QUERIES.add(ARTS_ENTERTAINMENT);
        CATEGORY_QUERIES.add(NIGHTLIFE);
        CATEGORY_QUERIES.add(SHOPPING);
        CATEGORY_QUERIES.add(FOOD);
        CATEGORY_QUERIES.add(BEAUTY);


        SUBCATEGORIES_MAP = new HashMap<String, ArrayList<String>>();
        SUBCATEGORIES_MAP.put("church", new ArrayList<String>(Arrays.asList("Religious"))); //2
        SUBCATEGORIES_MAP.put("hindu_temple", new ArrayList<String>(Arrays.asList("Religious")));
        SUBCATEGORIES_MAP.put("mosque", new ArrayList<String>(Arrays.asList("Religious")));
        SUBCATEGORIES_MAP.put("place_of_worship", new ArrayList<String>(Arrays.asList("Religious")));
        SUBCATEGORIES_MAP.put("bowling_alley", new ArrayList<String>(Arrays.asList("Arts & Entertainment")));
        SUBCATEGORIES_MAP.put("casino", new ArrayList<String>(Arrays.asList("Arts & Entertainment"))); //4
        SUBCATEGORIES_MAP.put("museum", new ArrayList<String>(Arrays.asList("Arts & Entertainment")));
        SUBCATEGORIES_MAP.put("zoo", new ArrayList<String>(Arrays.asList("Arts & Entertainment")));
        SUBCATEGORIES_MAP.put("aquarium", new ArrayList<String>(Arrays.asList("Arts & Entertainment")));
        SUBCATEGORIES_MAP.put("amusement_park", new ArrayList<String>(Arrays.asList("Arts & Entertainment"))); //7
        SUBCATEGORIES_MAP.put("art_gallery", new ArrayList<String>(Arrays.asList("Arts & Entertainment")));
        SUBCATEGORIES_MAP.put("bar", new ArrayList<String>(Arrays.asList("Nightlife", "Food")));
        SUBCATEGORIES_MAP.put("liquor_store", new ArrayList<String>(Arrays.asList("Nightlife")));
        SUBCATEGORIES_MAP.put("night_club", new ArrayList<String>(Arrays.asList("Nightlife")));
        SUBCATEGORIES_MAP.put("clothing_store", new ArrayList<String>(Arrays.asList("Shopping"))); //5
        SUBCATEGORIES_MAP.put("shopping_mall", new ArrayList<String>(Arrays.asList("Nightlife"))); //5
        SUBCATEGORIES_MAP.put("shoe_store", new ArrayList<String>(Arrays.asList("Nightlife")));
        SUBCATEGORIES_MAP.put("bakery", new ArrayList<String>(Arrays.asList("Food")));
        SUBCATEGORIES_MAP.put("cafe", new ArrayList<String>(Arrays.asList("Food")));
        SUBCATEGORIES_MAP.put("food", new ArrayList<String>(Arrays.asList("Food")));
        SUBCATEGORIES_MAP.put("restaurant", new ArrayList<String>(Arrays.asList("Food")));
        SUBCATEGORIES_MAP.put("beauty_salon", new ArrayList<String>(Arrays.asList("Beauty")));
        SUBCATEGORIES_MAP.put("hair_care", new ArrayList<String>(Arrays.asList("Beauty")));
        SUBCATEGORIES_MAP.put("health", new ArrayList<String>(Arrays.asList("Beauty")));
        SUBCATEGORIES_MAP.put("spa", new ArrayList<String>(Arrays.asList("Beauty")));
    }

    protected DataFetcher fetcher = null;

    public Main() {
        fetcher = new DataFetcher();
    }

    public static void main(String arg[]) {
        Main instance = new Main();

        instance.fetchPlacesData();
    }

    public void fetchPlacesData() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
       /*
        * Load the JDBC driver and establish a connection.
        */
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER, "");

        for(String subCatQuery : CATEGORY_QUERIES) {
            System.out.println("----------------------------------");
            System.out.println("----------------------------------");
            System.out.println("Sending request to Places API for category_query: " + subCatQuery + "\n");
            List<Place> places = fetcher.radarSearch(LA_LAT, LA_LNG, RADIUS, subCatQuery);

            for (Place place : places) {
                System.out.println("Processing Place: " + place.getName() + "\n");

                ArrayList<String> types = new ArrayList<String>(place.getTypes());
                StringBuffer subCatStringBuf = new StringBuffer();
                HashSet<String> categoriesSet = new HashSet<String>();

                for(String type : types) {
                    if(SUBCATEGORIES_MAP.containsKey(type)) {
                        subCatStringBuf.append(type + ",");
                        for(String cat : SUBCATEGORIES_MAP.get(type)) {
                            categoriesSet.add(cat);
                        }
                    }
                }
                subCatStringBuf.setLength(subCatStringBuf.length()-1);

                StringBuffer catStringBuf = new StringBuffer();
                for(String cat : categoriesSet) {
                    catStringBuf.append(cat + ",");
                }
                catStringBuf.setLength(catStringBuf.length()-1);

               String sqlStr = "INSERT INTO PLACES_DETAIL SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? WHERE NOT EXISTS(SELECT 1 FROM PLACES_DETAIL WHERE place_id = ?)";

                // Add Geometry class to Postgres
                ((org.postgresql.PGConnection)conn).addDataType("geometry",org.postgis.PGgeometry.class);

                pstmt = conn.prepareStatement(sqlStr);

                pstmt.setString(1, place.getPlaceId());
                pstmt.setString(2, place.getAddress());
                pstmt.setString(3, place.getPhoneNumber());
                pstmt.setString(4, place.getName());
                pstmt.setString(5, place.getPhoto());
                pstmt.setDouble(6, place.getRating());
                pstmt.setInt(7, place.getTotalRatings());
                pstmt.setString(8, catStringBuf.toString());
                pstmt.setString(9, subCatStringBuf.toString());
                pstmt.setString(10, place.getHours().toString());
                // Ideal Duration
                pstmt.setInt(11, 4);
                pstmt.setString(12, place.getGoogleUrl());
                pstmt.setString(13, place.getWebsite());
                Point point = new Point(place.getLatitude(), place.getLongitude());
                point.setSrid(4326);
                pstmt.setObject(14, new org.postgis.PGgeometry(point));
                pstmt.setString(15, place.getIcon());
                pstmt.setString(16, place.getPlaceId());

                System.out.println("Executing query: " + pstmt.toString() + "\n");
                System.out.println("----------------------------------");
                pstmt.executeUpdate();

                persistHours(conn, place.getPlaceId(), place.getHours());
            }
        }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void persistHours(Connection conn, String placeId, Hours hours) {
        String sqlStr = "INSERT INTO OPEN_PERIODS SELECT ?,?,?,? WHERE NOT EXISTS(SELECT 1 FROM OPEN_PERIODS WHERE place_id = ? AND day = ? AND opening_hour = ? AND closing_hour = ?)";

        PreparedStatement pstmt2 = null;

        try {
            pstmt2 = conn.prepareStatement(sqlStr);

            for(Hours.Period period : hours.getPeriods()) {
                pstmt2.setString(1, placeId);
                pstmt2.setString(2, period.getDay());
                pstmt2.setInt(3, Integer.parseInt(period.getOpeningTime()));
                pstmt2.setInt(4, Integer.parseInt(period.getClosingTime()));

                pstmt2.setString(5, placeId);
                pstmt2.setString(6, period.getDay());
                pstmt2.setInt(7, Integer.parseInt(period.getOpeningTime()));
                pstmt2.setInt(8, Integer.parseInt(period.getClosingTime()));

                System.out.println("Executing query: " + pstmt2.toString() + "\n");
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
