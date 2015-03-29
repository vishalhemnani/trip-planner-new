package com.vjs.googleplaceswrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

public class GooglePlaces implements GooglePlacesInterface { 
	
	private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
     * Creates a new GooglePlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public GooglePlaces(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new GooglePlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public GooglePlaces(String apiKey) {
        this(apiKey, new DefaultRequestHandler());
    }

    /**
     * Creates a new GooglePlaces object using the specified API key and character encoding. Using a character encoding
     * other than UTF-8 is not advised.
     *
     * @param apiKey            that has been registered on the Google Developer Console
     * @param characterEncoding to parse data with
     */
    public GooglePlaces(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Param... extraParams) {
        for (Param param : extraParams) {
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        }
        return base;
    }

    private static String buildUrl(String method, String params, Param... extraParams) {
        String url = String.format(Locale.ENGLISH, "%s%s/json?%s", API_URL, method, params);
        url = addExtraParams(url, extraParams);
        url = url.replace(' ', '+');
        return url;
    }
    
    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public boolean isDebugModeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDebugModeEnabled(boolean debugModeEnabled) {
		// TODO Auto-generated method stub
		
	}
	
	public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

	
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_RADAR_SEARCH, String.format("key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return parse(requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    private List<Place> parse(String rawJson) {
		// TODO Auto-generated method stub
    	
    	JSONObject json = new JSONObject(rawJson);

        JSONArray result = json.getJSONArray("results");

        List<Place> places = new ArrayList<Place>();

        System.out.println("Places found: " + result.length());
        
        for(int i = 0; i < result.length(); i++)
        {
        	String id = result.getJSONObject(i).getString(STRING_PLACE_ID);
        	String uri = buildUrl(METHOD_DETAILS, String.format("key=%s",
                     apiKey), (new Param("placeid")).value(id));
        	Place place = new Place();
        
        	try {
        		place = place.parseDetails(requestHandler.get(uri));
        		places.add(place);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    
		return places;
	}


	public Place getPlaceById(String placeId, Param... extraParams) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deletePlaceById(String placeId, Param... extraParams) {
		// TODO Auto-generated method stub
		
	}
}
