import {Component, OnInit} from '@angular/core';
import {MovieService} from "./movies.service";
import {SearchResults} from "./searchresults";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    searchResults: SearchResults;

    constructor(private movieService: MovieService) {
    }

    ngOnInit(): void {
        // Get movies
        this.movieService.searchMovies("Newton")
            .subscribe(searchResults => this.searchResults = searchResults);
    }
}
