import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {SearchResults} from "./searchresults";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map'
import 'rxjs/add/operator/toPromise';

@Injectable()
export class MovieService {
    private moviesApiUrl = "/api/movies/";

    constructor(private http: Http) {
    }

    searchMovies(search: string): Observable<SearchResults> {
        const url = `${this.moviesApiUrl}?search=${search}&all=true`;
        return this.http.get(url).map(res => res.json());
    }
}
