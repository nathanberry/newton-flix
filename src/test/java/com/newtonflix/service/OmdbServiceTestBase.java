package com.newtonflix.service;

import com.newtonflix.Application;
import com.newtonflix.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(classes = Application.class)
abstract class OmdbServiceTestBase {
    @Autowired
    OmdbService omdbService;

    void assertMovie(String imdbId, String expectedTitle, String expectedYear, List<Movie> movies) {
        Movie movie = findMovie(imdbId, movies);
        assertNotNull("Did not find " + imdbId, movie);
        assertEquals("Incorrect title for " + movie.getImdbId(), expectedTitle, movie.getTitle());
        assertEquals("Incorrect year for " + movie.getImdbId(), expectedYear, movie.getYear());
        assertEquals("Incorrect type for " + movie.getImdbId(), "movie", movie.getType());
        assertNotNull("Poster not set for " + movie.getImdbId(), movie.getPoster());
    }

    private Movie findMovie(String imdbId, List<Movie> movies) {
        Movie found = null;

        for (Movie movie : movies) {
            if (movie.getImdbId().equals(imdbId)) {
                found = movie;
                break;
            }
        }

        return found;
    }
}
