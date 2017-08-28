package com.newtonflix.service;

import com.newtonflix.model.Movie;
import com.newtonflix.model.SearchResults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class OmdbService {
    static final String OMDB_API_URL = "http://www.omdbapi.com";
    static final String API_KEY = "edeb260";
    private RestTemplate restTemplate;

    public SearchResults searchMoviesByTitleAndReturnAll(String title) {
        SearchResults searchResults = searchMoviesByTitle(title, 1);
        int total = Integer.parseInt(searchResults.getTotalResults());
        int pageSize = searchResults.getMovies().size();

        if (pageSize < total) {
            int pages = Math.floorDiv(total, pageSize) + 1;

            for (int page = 2; pages >= page; page++) {
                SearchResults pagedResults = searchMoviesByTitle(title, page);
                searchResults.getMovies().addAll(pagedResults.getMovies());
            }
        }

        searchResults.getMovies().sort(Comparator.comparing(Movie::getTitle));
        return searchResults;
    }

    public SearchResults searchMoviesByTitle(String title, Integer page) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("search", title);
        paramMap.put("page", page);
        return getRestTemplate().getForObject(buildUrl("type=movie&s={search}&page={page}"),
                SearchResults.class, paramMap);
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
