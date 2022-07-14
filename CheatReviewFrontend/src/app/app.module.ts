import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSliderModule} from '@angular/material/slider';
import {StartComponent} from './start/start.component';
import {LayoutModule} from '@angular/cdk/layout';
import {
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatMenuModule,
    MatInputModule,
    MatTableModule,
    MatButtonToggleModule,
    MatPaginatorModule,
    MatDialogModule, MatGridListModule, MatRadioModule, MatExpansionModule, MatBadgeModule
} from '@angular/material';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {RouterModule} from '@angular/router';
import {AlertComponent} from './alert/alert.component';
import {ReviewComponent} from './review/review.component';
import {HistoryComponent} from './history/history.component';
import {AlertService} from './alert.service';
import {SimpleNotificationsModule} from 'angular2-notifications';
import {AppRoutingModule} from './app-routing.module';

import cpp from 'highlight.js/lib/languages/cpp';
import {HighlightModule} from 'ngx-highlightjs';
import {RegComponent} from './reg/reg.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RankComponent} from './rank/rank.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {ReReviewComponent} from './re-review/re-review.component';
import {QContentDlgComponent} from './qcontent-dlg/qcontent-dlg.component';
import { CodeSimilarityComponent } from './code-similarity/code-similarity.component';
import {AceEditorModule} from 'ng2-ace-editor';

export function hljsLanguages() {
  return [
    {name: 'cpp', func: cpp}
  ];
}

@NgModule({
  declarations: [
    StartComponent,
    AlertComponent,
    ReviewComponent,
    HistoryComponent,
    RegComponent,
    RankComponent,
    PageNotFoundComponent,
    ReReviewComponent,
    QContentDlgComponent,
    CodeSimilarityComponent
  ],
    imports: [
        HttpClientModule,
        BrowserModule,
        BrowserAnimationsModule,
        MatSliderModule,
        LayoutModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatIconModule,
        MatListModule,
        FontAwesomeModule,
        RouterModule,
        SimpleNotificationsModule.forRoot(),
        AppRoutingModule,
        MatCardModule,
        HighlightModule.forRoot({
            languages: hljsLanguages
        }),
        MatProgressBarModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatMenuModule,
        MatInputModule,
        FormsModule,
        MatTableModule,
        MatButtonToggleModule,
        MatPaginatorModule,
        MatDialogModule,
        MatGridListModule,
        AceEditorModule,
        MatRadioModule,
        MatExpansionModule,
        MatBadgeModule
    ],
  providers: [AlertService],
  bootstrap: [StartComponent],
  entryComponents: [QContentDlgComponent]
})
export class AppModule {
}
