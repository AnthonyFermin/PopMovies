package anthonyfdev.com.popmovies.discovery.model;

import java.util.ArrayList;

import anthonyfdev.com.popmovies.common.ListResponse;

/**
 * @author Anthony Fermin ()
 */

public class ReviewResponse extends ListResponse<Review> {

    public ReviewResponse() {
        clazz = Review.class;
        listPropertyName = "results";
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}