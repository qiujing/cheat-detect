import {Component, OnInit} from '@angular/core';
import {CodeSimilarityService} from '../_service/code-similarity.service';
import {CodeSimilarityResult} from '../_interface/code-similarity-result';

@Component({
  selector: 'app-code-similarity',
  templateUrl: './code-similarity.component.html',
  styleUrls: ['./code-similarity.component.css']
})
export class CodeSimilarityComponent implements OnInit {
  code = ['', ''];
  submitting = false;
  result: CodeSimilarityResult = null;

  constructor(private codeSimilarityService: CodeSimilarityService) {
  }

  ngOnInit() {
  }

  start() {
    this.submitting = true;
    this.codeSimilarityService.codeSimilarity(this.code).subscribe(data => {
      this.submitting = false;
      this.result = data;
    }, error => this.submitting = false);
  }
}
