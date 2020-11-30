import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {FieldAttribute} from './fieldAttribute';
import {environment} from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FieldattributeService {

  private fieldAttributesAPIUrl = environment.fieldAttributesAPIUrl;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }


  /** GET field attributes from the server */
  getFieldAttributes(): Observable<FieldAttribute[]> {
    return this.http.get<FieldAttribute[]>(this.fieldAttributesAPIUrl)
      .pipe(
        tap(_ => console.log('fetched field attributes')),
        catchError(this.handleError<FieldAttribute[]>('getFieldAttributes', []))
      );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T): any {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.error(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
