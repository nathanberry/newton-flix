package com.newtonflix.service;

import com.newtonflix.model.Movie;
import com.newtonflix.model.SearchResults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class OmdbServiceIT extends OmdbServiceTestBase {
    @Test
    public void testSearchMoviesByTitle() throws Exception {
        SearchResults searchResults = omdbService.searchMoviesByTitle("newton", 1);
        assertNotNull("Total results not set", searchResults.getTotalResults());

        List<Movie> movies = searchResults.getMovies();
        assertEquals("Incorrect number of movies", 10, movies.size());

        // Spot check movies from search
        assertMovie("tt0120769", "The Newton Boys", "1998", movies);
        assertMovie("tt0218433", "Me & Isaac Newton", "1999", movies);
        assertMovie("tt6484982", "Newton", "2017", movies);
    }

    @Test
    public void testSearchMoviesByTitleAndReturnAll() throws Exception {
        SearchResults searchResults = omdbService.searchMoviesByTitleAndReturnAll("newton");
        assertNotNull("Total results not set", searchResults.getTotalResults());

        List<Movie> movies = searchResults.getMovies();
        assertEquals("Incorrect number of movies!", 44, movies.size());
    }
}
