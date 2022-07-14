import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ReviewComponent} from './review/review.component';
import {HistoryComponent} from './history/history.component';
import {RegComponent} from './reg/reg.component';
import {AuthGuard} from './_guard/auth.guard';
import {RankComponent} from './rank/rank.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {ReReviewComponent} from './re-review/re-review.component';
import {CodeSimilarityComponent} from './code-similarity/code-similarity.component';

const routes: Routes = [{path: 'review', component: ReviewComponent, canActivate: [AuthGuard]},
  {path: 'history', component: HistoryComponent, canActivate: [AuthGuard]},
  {path: 'reg', component: RegComponent},
  {path: 'rank', component: RankComponent, canActivate: [AuthGuard]},
  {path: 're-review', component: ReReviewComponent, canActivate: [AuthGuard]},
  {path: '', component: ReviewComponent, canActivate: [AuthGuard], pathMatch: 'full'},
  {path: 'code-similarity', component: CodeSimilarityComponent, canActivate: [AuthGuard]},
  {path: '**', component: PageNotFoundComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
