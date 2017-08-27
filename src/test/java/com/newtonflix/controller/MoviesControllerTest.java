package com.newtonflix.controller;

import com.newtonflix.model.Movie;
import com.newtonflix.model.SearchResults;
import com.newtonflix.service.OmdbService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {
    private static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OmdbService mockOmdbService;

    private SearchResults mockSearchResults;

    @Before
    public void setUp() throws Exception {
        initMockSearchResults();

        when(mockOmdbService.searchMoviesByTitle(anyString(), anyInt())).thenAnswer(invocationOnMock -> {
            // Ensure a null search string defaults value to newton
            assertEquals("Search string was not newton!", "newton",
                    invocationOnMock.getArgumentAt(0, String.class));
            return mockSearchResults;
        });
    }

    @Test
    public void testSearch_NewtonAsParam() throws Exception {
        ResultActions results = mockMvc.perform(get("/api/movies/?search=newton"));
        assertResponse(results);
    }

    @Test
    public void testSearch_NoSearchParam() throws Exception {
        ResultActions results = mockMvc.perform(get("/api/movies/"));
        assertResponse(results);
    }

    private void assertResponse(ResultActions results) throws Exception {
        results.andExpect(content().contentType(contentType)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.totalResults", is("3")));

        for (int i = 0; i < 3; i++) {
            assertMovie(i, results);
        }
    }

    private void assertMovie(int i, ResultActions results) throws Exception {
        String movieNumber = Integer.toString(i + 1);
        results.andExpect(jsonPath(String.format("$.Search[%d].imdbID", i), is(movieNumber))).
                andExpect(jsonPath(String.format("$.Search[%d].Title", i), is("Newton Movie " + movieNumber))).
                andExpect(jsonPath(String.format("$.Search[%d].Year", i), is("200" + movieNumber))).
                andExpect(jsonPath(String.format("$.Search[%d].Poster", i),
                        is("http://movieposter.com/" + movieNumber)));
    }

    private void initMockSearchResults() {
        mockSearchResults = new SearchResults();
        mockSearchResults.setTotalResults("3");
        mockSearchResults.setMovies(buildMovies(3));
    }

    private List<Movie> buildMovies(int count) {
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            movies.add(buildMovie(i));
        }

        return movies;
    }

    private Movie buildMovie(int i) {
        Movie movie = new Movie();
        String imdbId = Integer.toString(i + 1);
        movie.setImdbId(imdbId);
        movie.setTitle("Newton Movie " + imdbId);
        movie.setType("movie");
        movie.setYear("200" + imdbId);
        movie.setPoster("http://movieposter.com/" + imdbId);
        return movie;
    }
}
