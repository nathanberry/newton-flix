import {async, ComponentFixture, ComponentFixtureAutoDetect, TestBed} from '@angular/core/testing';

import {AppComponent} from './app.component';
import {MovieService} from "./movies.service";
import {By} from "@angular/platform-browser";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/of";

describe('AppComponent', () => {
    let comp: AppComponent;
    let fixture: ComponentFixture<AppComponent>;
    let movieService: MovieService;

    beforeEach(async(() => {
        let movieServiceStub = {
            searchMovies: (search, page) => {
                console.log("MovieService returning mock data for search=" + search + " and page=" + page);
                return Observable.of({
                    "Search": [
                        {"imdbID": "1", "Title": "Newton 1", "Year": "2015", "Poster": "NewtonPoster1"},
                        {"imdbID": "2", "Title": "Newton 2", "Year": "2016", "Poster": "NewtonPoster2"},
                        {"imdbID": "3", "Title": "Newton 3", "Year": "2017", "Poster": "N/A"}
                    ],
                    "totalResults": "3"
                });
            }
        };

        TestBed.configureTestingModule({
            declarations: [
                AppComponent
            ],
            providers: [
                {provide: ComponentFixtureAutoDetect, useValue: true},
                {provide: MovieService, useValue: movieServiceStub}
            ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AppComponent);
        comp = fixture.componentInstance;
        movieService = TestBed.get(MovieService);
    });

    it('should create the app', async(() => {
        expect(comp).toBeTruthy();
        expect(comp.searchResults).toBeFalsy();
    }));

    it("should display movies on search", function () {
        expect(comp).toBeTruthy();

        comp.searchMovies();
        fixture.detectChanges();

        // query for the title <h1> by CSS element selector
        let de = fixture.debugElement.query(By.css('table'));
        let el = de.nativeElement;
        let tBody: HTMLElement;
        let rows: NodeListOf<HTMLElement>;
        tBody = el.getElementsByTagName("tbody")[0];
        rows = tBody.getElementsByTagName("tr");
        expect(rows.length).toEqual(3);

        assertMovie(rows, 0, "Newton 1", "2015", "NewtonPoster1");
        assertMovie(rows, 1, "Newton 2", "2016", "NewtonPoster2");
        assertMovie(rows, 2, "Newton 3", "2017", null);
    });

    let assertMovie = function (rows: NodeListOf<HTMLElement>, index: number, expectedTitle: string,
                                expectedYear: string, expectedPoster: string) {
        let row: HTMLElement;
        let cells: NodeListOf<HTMLElement>;
        row = rows[index];
        cells = row.getElementsByTagName("td");
        expect(cells.length).toEqual(3);

        if (expectedPoster != null) {
            expect(cells[0].children.length).toEqual(1);
            expect(cells[0].children[0].getAttribute("src")).toEqual(expectedPoster);
        } else {
            expect(cells[0].innerText).toContain("");
        }

        expect(cells[1].innerText).toContain(expectedTitle);
        expect(cells[2].innerText).toContain(expectedYear);
    };
});
