package anthonyfdev.com.popmovies.discovery.model;

import java.util.ArrayList;

import anthonyfdev.com.popmovies.common.ListResponse;

/**
 * @author Anthony Fermin
 */

public class MovieResponse extends ListResponse<Movie> {
    public MovieResponse() {
        clazz = Movie.class;
        listPropertyName = "results";
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

}
