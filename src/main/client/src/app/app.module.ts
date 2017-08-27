import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpModule} from '@angular/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {AppComponent} from './app.component';

import {MovieService} from "./movies.service";

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        NgbModule.forRoot(),
        BrowserModule,
        HttpModule
    ],
    providers: [MovieService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
