package com.newtonflix.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResults {
    @JsonProperty("Search")
    private List<Movie> movies;

    @JsonProperty("TotalResults")
    private String totalResults;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
