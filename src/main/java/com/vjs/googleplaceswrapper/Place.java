package com.vjs.googleplaceswrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.vjs.googleplaceswrapper.GooglePlaces.*;
import static com.vjs.googleplaceswrapper.GooglePlacesInterface.STRING_ICON;

public class Place {
	
	
	private final List<String> types = new ArrayList<String>();
    // photo_reference of the first photo
    private String photoRef;
    private final List<Review> reviews = new ArrayList<Review>();
    private String placeId;
    private double lat = -1, lng = -1;
    private String name;
    private String addr;
    private String icon;
    private double rating = -1;
    private int numRatings = 0;
    private String phone, internationalPhone;
    private String googleUrl, website;
    private Hours hours;
    
    
    public Place parseDetails(String rawJson) {
        
    	JSONObject json = new JSONObject(rawJson);

        JSONObject result = json.getJSONObject(OBJECT_RESULT);

        //System.out.println(rawJson);
        
        // easy stuff
        name = result.getString(STRING_NAME);
        placeId = result.getString(STRING_PLACE_ID);
        addr = result.optString(STRING_ADDRESS, null);
        phone = result.optString(STRING_PHONE_NUMBER, null);
        internationalPhone = result.optString(STRING_INTERNATIONAL_PHONE_NUMBER, null);
        rating = result.optDouble(DOUBLE_RATING, -1);
        googleUrl = result.optString(STRING_URL, null);
        numRatings = result.optInt(STRING_TOTAL_RATINGS);
        website = result.optString(STRING_WEBSITE, null);
        icon = result.optString(STRING_ICON, null);
        photoRef = "";

        JSONArray photos = result.optJSONArray(ARRAY_PHOTOS);
        if(photos != null) {
            // only 1 photo
            photoRef = photos.getJSONObject(0).getString(STRING_PHOTO_REFERENCE);
        }
        
        //location
        JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
        double lat = location.getDouble(DOUBLE_LATITUDE), lng = location.getDouble(DOUBLE_LONGITUDE);

        // hours of operation
        JSONObject hours = result.optJSONObject(OBJECT_HOURS);
        Hours schedule = new Hours();
        if (hours != null) {
            // periods of operation
            JSONArray jsonPeriods = hours.optJSONArray(ARRAY_PERIODS);
            if (jsonPeriods != null) {
                for (int i = 0; i < jsonPeriods.length(); i++) {
                    JSONObject jsonPeriod = jsonPeriods.getJSONObject(i);

                    // opening information (from)
                    JSONObject opens = jsonPeriod.getJSONObject(OBJECT_OPEN);
                    String openingDay = "" + opens.getInt(INTEGER_DAY);
                    String openingTime = opens.getString(STRING_TIME);

                    // closing information (to)
                    JSONObject closes = jsonPeriod.getJSONObject(OBJECT_CLOSE);
                    String closingTime = closes.getString(STRING_TIME);

                    // add the period to the hours
                    schedule.addPeriod(new Hours.Period().setDay(openingDay).setOpeningTime(openingTime)
                            .setClosingTime(closingTime));
                }
            }
        }

        Place place = new Place();
        
        // types
        JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
        List<String> types = new ArrayList<String>();
        if (jsonTypes != null) {
            for (int i = 0; i < jsonTypes.length(); i++) {
                types.add(jsonTypes.getString(i));
            }
        }

        // reviews
        JSONArray jsonReviews = result.optJSONArray(ARRAY_REVIEWS);
        List<Review> reviews = new ArrayList<Review>();
        if (jsonReviews != null) {
            for (int i = 0; i < jsonReviews.length(); i++) {
                JSONObject jsonReview = jsonReviews.getJSONObject(i);

                String author = jsonReview.optString(STRING_AUTHOR_NAME, null);
                String authorUrl = jsonReview.optString(STRING_AUTHOR_URL, null);
                String lang = jsonReview.optString(STRING_LANGUAGE, null);
                int reviewRating = jsonReview.optInt(INTEGER_RATING, -1);
                String text = jsonReview.optString(STRING_TEXT, null);
                long time = jsonReview.optLong(LONG_TIME, -1);

                reviews.add(new Review().setAuthor(author).setAuthorUrl(authorUrl).setLanguage(lang)
                        .setRating(reviewRating).setText(text).setTime(time));
            }
        }
        
        return place.setPlaceId(placeId).setName(name).setAddress(addr).setLatitude(lat)
        		.setLongitude(lng).addTypes(types).setRating(rating).setPhoneNumber(phone)
        		.setInternationalPhoneNumber(internationalPhone).addPhoto(photoRef).setTotalRatings(numRatings).setGoogleUrl(googleUrl).
        		setWebsite(website).setHours(schedule).addReviews(reviews).setIcon(icon);
    }
    
    /**
     * Returns the unique identifier for this place.
     *
     * @return id
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Sets the unique, stable, identifier for this place.
     *
     * @param placeId to use
     * @return this
     */
    protected Place setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    /**
     * Returns the icon for this place.
     *
     * @return id
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon for this place.
     *
     * @param icon to use
     * @return this
     */
    protected Place setIcon(String icon) {
        this.icon = icon;
        return this;
    }


    /**
     * Returns the total ratings for this place.
     *
     * @return ratings
     */
    public int getTotalRatings() {
        return numRatings;
    }

    /**
     * Sets the total ratings for this place.
     *
     * @param totalRatings to use
     * @return this
     */
    protected Place setTotalRatings(int totalRatings) {
        this.numRatings= totalRatings;
        return this;
    }


    /**
     * Returns the latitude of the place.
     *
     * @return place latitude
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * Sets the latitude of the place.
     *
     * @param lat latitude
     * @return this
     */
    protected Place setLatitude(double lat) {
        this.lat = lat;
        return this;
    }

    /**
     * Returns the longitude of this place.
     *
     * @return longitude
     */
    public double getLongitude() {
        return lng;
    }

    /**
     * Sets the longitude of this place.
     *
     * @param lon longitude
     * @return this
     */
    protected Place setLongitude(double lon) {
        this.lng = lon;
        return this;
    }

    
    /**
     * Returns the  for this place.
     *
     * @return hours of operation
     */
    public Hours getHours() {
        return hours;
    }

    /**
     * Sets the  of this place.
     *
     * @param hours of operation
     * @return this
     */
    protected Place setHours(Hours hours) {
        this.hours = hours;
        return this;
    }

    /**
     * Returns true if this place is always opened.
     *
     * @return true if always opened
     */
    public boolean isAlwaysOpened() {
        return hours.isAlwaysOpened();
    }

    /**
     * Returns this Place's phone number.
     *
     * @return number
     */
    public String getPhoneNumber() {
        return phone;
    }

    /**
     * Sets this Place's phone number.
     *
     * @param phone number
     * @return this
     */
    protected Place setPhoneNumber(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Returns the place's phone number with a country code.
     *
     * @return phone number
     */
    public String getInternationalPhoneNumber() {
        return internationalPhone;
    }

    /**
     * Sets the phone number with an international country code.
     *
     * @param internationalPhone phone number
     * @return this
     */
    protected Place setInternationalPhoneNumber(String internationalPhone) {
        this.internationalPhone = internationalPhone;
        return this;
    }

    /**
     * Returns the Google PLus page for this place.
     *
     * @return plus page
     */
    public String getGoogleUrl() {
        return googleUrl;
    }

    /**
     * Sets the Google Plus page for this place.
     *
     * @param googleUrl google plus page
     * @return this
     */
    protected Place setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
        return this;
    }

    /**
     * Returns the website of this place.
     *
     * @return website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the website url associated with this place.
     *
     * @param website of place
     * @return this
     */
    protected Place setWebsite(String website) {
        this.website = website;
        return this;
    }

        /**
     * Returns the name of this place.
     *
     * @return name of place
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this place.
     *
     * @param name of place
     * @return this
     */
    protected Place setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the address of this place.
     *
     * @return address of this place
     */
    public String getAddress() {
        return addr;
    }

    /**
     * Sets the address of this place.
     *
     * @param addr address
     * @return this
     */
    protected Place setAddress(String addr) {
        this.addr = addr;
        return this;
    }

     /**
     * Adds a photo ref to this place.
     *
     * @param photo to add
     * @return this
     */
    protected Place addPhoto(String photo) {
        this.photoRef = photo;
        return this;
    }

    /**
     * Returns the photo reference for this place.
     *
     * @return photo
     */
    public String getPhoto() {
        return photoRef;
    }

        /**
     * Returns the rating of this place.
     *
     * @return rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the rating of this place.
     *
     * @param rating of place
     * @return this
     */
    protected Place setRating(double rating) {
        this.rating = rating;
        return this;
    }
    
    /**
     * Adds a collection of string "types".
     *
     * @param types to add
     * @return this
     */
    protected Place addTypes(Collection<String> types) {
        this.types.addAll(types);
        return this;
    }

    /**
     * Returns all of this place's types in an unmodifiable list.
     *
     * @return types
     */
    public List<String> getTypes() {
        return Collections.unmodifiableList(types);
    }
    
    /**
     * Adds a collection of reviews to this place.
     *
     * @param reviews to add
     * @return this
     */
    protected Place addReviews(Collection<Review> reviews) {
        this.reviews.addAll(reviews);
        return this;
    }

    /**
     * Returns this place's reviews in an unmodifiable list.
     *
     * @return reviews
     */
    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }
    
    public String toString() {
        return String.format("Place{id=%s}", placeId);
    }

}
