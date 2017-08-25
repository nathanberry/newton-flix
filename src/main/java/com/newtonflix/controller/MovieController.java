package com.newtonflix.controller;

import com.newtonflix.model.SearchResults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @RequestMapping("/")
    public ResponseEntity<SearchResults> search(@RequestParam("title") String title) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
