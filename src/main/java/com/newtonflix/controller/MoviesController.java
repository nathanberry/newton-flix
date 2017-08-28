package com.newtonflix.controller;

import com.newtonflix.model.SearchResults;
import com.newtonflix.service.OmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {
    @Autowired
    OmdbService omdbService;

    @RequestMapping("/")
    public ResponseEntity<SearchResults> search(@RequestParam(value = "search", required = false) String search,
                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "all", required = false) boolean all) {
        if (StringUtils.isEmpty(search)) {
            search = "newton";
        }

        SearchResults searchResults;

        if (all) {
            searchResults = omdbService.searchMoviesByTitleAndReturnAll(search);
        } else {
            searchResults = omdbService.searchMoviesByTitle(search, page);
        }

        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }
}
