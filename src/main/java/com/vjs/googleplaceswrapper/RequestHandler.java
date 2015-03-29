package com.vjs.googleplaceswrapper;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents an interface to handle HTTP traffic between the client and Google Places API.
 */
public interface RequestHandler {

    /**
     * Returns the character encoding used by this handler.
     *
     * @return character encoding
     */
    public String getCharacterEncoding();

    /**
     * Sets the character encoding used by this handler.
     *
     * @param characterEncoding to use
     */
    public void setCharacterEncoding(String characterEncoding);

    /**
     * Returns an InputStream from the specified URI.
     *
     * @param uri to get input stream for
     * @return input stream at uri
     * @throws java.io.IOException
     */
    public InputStream getInputStream(String uri) throws IOException;

    /**
     * Returns the returned data at the specified URI.
     *
     * @param uri to get string data at
     * @return string data at uri
     * @throws java.io.IOException
     */
    public String get(String uri) throws IOException;

    /**
     * Posts new data to the server and returns the response as a string.
     *
     * @param data to post
     * @return string returned
     * @throws java.io.IOException
     */
    public String post(HttpPost data) throws IOException;
}
