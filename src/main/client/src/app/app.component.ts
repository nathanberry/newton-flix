import {Component, OnInit} from '@angular/core';
import {MovieService} from "./movies.service";
import {SearchResults} from "./searchresults";
import {Observable} from "rxjs/Observable";
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
                let total = parseInt(this.searchResults.totalResults);
                let pageSize = this.searchResults.Search.length;

                // If the total is larger than the number of results.  Retrieve additional pages.
                if (total > pageSize) {
                    let remaining = total - pageSize;
                    let pages = Math.ceil(remaining / pageSize) + 1;
                    let observables = [];

                    for (let page = 2; pages >= page; page++) {
                        let observable = this.movieService.searchMovies("Newton", page);
                        observables.push(observable);
                        observable.subscribe(searchResults => {
                            this.searchResults.Search = this.searchResults.Search.concat(searchResults.Search);
                        });
                    }

                    // Once additional pages are retrieved, sort by Title
                    Observable.forkJoin(...observables).subscribe(() => {
                        this.searchResults.Search.sort(this.sortByTitle);
                        this.resetForNextSearch();
                    });
                } else {
                    this.resetForNextSearch();
                }
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
