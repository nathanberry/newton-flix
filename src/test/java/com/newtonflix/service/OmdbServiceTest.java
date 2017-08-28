package com.newtonflix.service;

import com.newtonflix.model.Movie;
import com.newtonflix.model.SearchResults;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
public class OmdbServiceTest extends OmdbServiceTestBase {
    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        omdbService.setRestTemplate(restTemplate);
    }

    @Test
    public void testSearchMoviesByTitle() throws Exception {
        String url = String.format("%s/?apikey=%s&type=movie&s=newton&page=1", OmdbService.OMDB_API_URL, OmdbService.API_KEY);
        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTestJson("search-newton.json"), MediaType.APPLICATION_JSON));

        SearchResults searchResults = omdbService.searchMoviesByTitle("newton", 1);
        mockServer.verify();

        assertEquals("Incorrect number of total results", "15", searchResults.getTotalResults());

        List<Movie> movies = searchResults.getMovies();
        assertEquals("Incorrect number of movies", 10, movies.size());

        // Spot check movies from search
        assertMovie("tt0120769", "The Newton Boys", "1998", movies);
        assertMovie("tt0218433", "Me & Isaac Newton", "1999", movies);
        assertMovie("tt6484982", "Newton", "2017", movies);
    }

    @Test
    public void testSearchMoviesByTitleAndReturnAll() throws Exception {
        String url = String.format("%s/?apikey=%s&type=movie&s=newton&page=1", OmdbService.OMDB_API_URL, OmdbService.API_KEY);
        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTestJson("search-newton.json"), MediaType.APPLICATION_JSON));

        url = String.format("%s/?apikey=%s&type=movie&s=newton&page=2", OmdbService.OMDB_API_URL, OmdbService.API_KEY);
        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(getTestJson("search-newton-2.json"), MediaType.APPLICATION_JSON));

        SearchResults searchResults = omdbService.searchMoviesByTitleAndReturnAll("newton");
        mockServer.verify();

        assertEquals("Incorrect number of total results", "15", searchResults.getTotalResults());

        List<Movie> movies = searchResults.getMovies();
        assertEquals("Incorrect number of movies", 15, movies.size());
    }

    private String getTestJson(final String jsonFile) throws IOException {
        try (InputStream resourceAsStream = getClass().getResourceAsStream("/" + jsonFile)) {
            return IOUtils.toString(resourceAsStream, "UTF-8");
        }
    }
}
