package com.vjs.googleplaceswrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a place's hours of operation.
 */
public class Hours {
    private final List<Period> periods = new ArrayList<Period>();
    private boolean alwaysOpened;

    protected Hours() {
    }

    /**
     * Returns true if the place is always opened.
     *
     * @return true if always opened
     */
    public boolean isAlwaysOpened() {
        return alwaysOpened;
    }

    /**
     * Sets if the establishment is always opened.
     *
     * @param alwaysOpened if this place is always opened
     * @return this
     */
    protected Hours setAlwaysOpened(boolean alwaysOpened) {
        this.alwaysOpened = alwaysOpened;
        return this;
    }

    /**
     * Adds a period to this schedule's list of periods.
     *
     * @param period to add
     * @return this
     */
    protected Hours addPeriod(Period period) {
        periods.add(period);
        return this;
    }

    /**
     * Returns an unmodifiable list of all the periods of operation.
     *
     * @return periods
     */
    public List<Period> getPeriods() {
        return Collections.unmodifiableList(periods);
    }

    @Override
    public String toString() {
        String str = "";
        for (Period period : periods) {
            str += period + "\n";
        }
        return str;
    }

    /**
     * Represents a period of time in which the place is opened.
     */
    public static class Period {
        private String day;
        private String openingTime, closingTime;

        protected Period() {
        }

        /**
         * Returns the day this period opens.
         *
         * @return opening day
         */
        public String getDay() {
            return day;
        }

        /**
         * Sets the day this period opens.
         *
         * @param openingDay starting day
         * @return this
         */
        protected Period setDay(String day) {
            this.day = new String(day);
            return this;
        }

        /**
         * Returns the time this period opens in hhmm format.
         *
         * @return starting time
         */
        public String getOpeningTime() {
            return openingTime;
        }

        /**
         * Sets the time this period opens. This should be provided in an hhmm format.
         *
         * @param openingTime starting time
         * @return this
         */
        protected Period setOpeningTime(String openingTime) {
            this.openingTime = openingTime;
            return this;
        }

        /**
         * Returns the closing time in hhmm format.
         *
         * @return closing time
         */
        public String getClosingTime() {
            return closingTime;
        }

        /**
         * Sets the closing time of this period.
         *
         * @param closingTime end time
         * @return this
         */
        protected Period setClosingTime(String closingTime) {
            this.closingTime = closingTime;
            return this;
        }

        @Override
        public String toString() {
            return String.format("%s %s:%s -- %s:%s", day, openingTime.substring(0, 2),
                    openingTime.substring(2), closingTime.substring(0, 2), closingTime.substring(2));
        }
    }
}
