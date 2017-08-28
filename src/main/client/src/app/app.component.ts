import {Component, OnInit} from '@angular/core';
import {MovieService} from "./movies.service";
import {SearchResults} from "./searchresults";
import "rxjs/add/observable/forkJoin";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/of";
import {Movie} from "./movie";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    error: any;
    searchResults: SearchResults;
    resultsComplete: boolean;
    searching: boolean;

    constructor(private movieService: MovieService) {
    }

    ngOnInit(): void {
    }

    searchMovies(): void {
        this.searchResults = null;
        this.resultsComplete = false;
        this.searching = true;
        this.error = null;

        this.movieService.searchMovies("Newton")
            .subscribe(searchResults => {
                this.searchResults = searchResults;
                this.resetForNextSearch();
            }, (err) => {
                this.resetForNextSearch();
                this.error = "Unable to retrieve movies.  Please check client and server logs for assistance.";
            });
    }

    resetForNextSearch(): void {
        this.searching = false;
        this.resultsComplete = true;
    }

    sortByTitle(a: Movie, b: Movie): number {
        if (a.Title > b.Title) {
            return 1;
        } else if (a.Title < b.Title) {
            return -1;
        } else {
            return 0;
        }
    }
}
