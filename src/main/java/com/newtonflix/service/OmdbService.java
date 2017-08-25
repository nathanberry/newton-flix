package com.newtonflix.service;

import com.newtonflix.model.Movie;
import com.newtonflix.model.SearchResults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class OmdbService {
    static final String OMDB_API_URL = "http://www.omdbapi.com";
    static final String API_KEY = "edeb260";
    private RestTemplate restTemplate;

    public List<Movie> searchMoviesByTitle(String title) {
        SearchResults searchResults = getRestTemplate().getForObject(buildUrl("type=movie&s={search}"),
                SearchResults.class, Collections.singletonMap("search", title));
        return searchResults.getMovies();
    }

    private String buildUrl(String uri) {
        return String.format("%s/?apikey=%s&%s", OMDB_API_URL, API_KEY, uri);
    }

    RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
    }

    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
