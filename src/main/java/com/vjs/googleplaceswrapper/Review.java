package com.vjs.googleplaceswrapper;

/**
 * Represents a user submitted review.
 */
public class Review {
    private String author, authorUrl, lang, text;
    private int rating;
    private long time;

    protected Review() {
    }

    /**
     * Returns the author of the review.
     *
     * @return the author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the review.
     *
     * @param author of review
     * @return this
     */
    protected Review setAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Returns the url associated with the author.
     *
     * @return url
     */
    public String getAuthorUrl() {
        return authorUrl;
    }

    /**
     * Sets the author's url.
     *
     * @param authorUrl to set
     * @return this
     */
    protected Review setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    /**
     * Returns the language the review is in.
     *
     * @return review language
     */
    public String getLanguage() {
        return lang;
    }

    /**
     * Sets the language the review is in.
     *
     * @param lang language of review
     * @return this
     */
    protected Review setLanguage(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * Returns the content of this review.
     *
     * @return content of review
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the content of this review.
     *
     * @param text content of review
     * @return this
     */
    protected Review setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns the rating of this review between 0 and 5.
     *
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of this review.
     *
     * @param rating of review
     * @return this
     */
    protected Review setRating(int rating) {
        this.rating = rating;
        return this;
    }

    /**
     * Returns the unix-time stamp that the review was posted on.
     *
     * @return unix-timestamp
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the unix-time stamp the review was posted on.
     *
     * @param time unix timestamp
     * @return this
     */
    protected Review setTime(long time) {
        this.time = time;
        return this;
    }

}
