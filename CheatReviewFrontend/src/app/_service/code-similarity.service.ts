import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CodeSimilarityResult} from '../_interface/code-similarity-result';

@Injectable({
  providedIn: 'root'
})
export class CodeSimilarityService {

  constructor(private http: HttpClient) {
  }

  codeSimilarity(code: string[]) {
    return this.http.post<CodeSimilarityResult>('/api/code-similarity', code);
  }
}
