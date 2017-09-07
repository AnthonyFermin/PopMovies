package anthonyfdev.com.popmovies.discovery.model;

import anthonyfdev.com.popmovies.common.ListResponse;

/**
 * @author Anthony Fermin ()
 */

public class TrailerResponse extends ListResponse<Trailer> {

    public TrailerResponse() {
        clazz = Trailer.class;
        listPropertyName = "results";
    }
}
