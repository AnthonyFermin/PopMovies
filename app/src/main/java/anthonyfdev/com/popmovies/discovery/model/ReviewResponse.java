package anthonyfdev.com.popmovies.discovery.model;

import anthonyfdev.com.popmovies.common.ListResponse;

/**
 * @author Anthony Fermin (Fuzz)
 */

public class ReviewResponse extends ListResponse<Review> {

    public ReviewResponse() {
        clazz = Review.class;
        listPropertyName = "results";
    }
}