import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {FieldActivityFileType} from './fieldActivityFileType';
import {environment} from './../environments/environment';
import {FieldActivity} from './fieldActivity';
import {FieldActivityFile} from './fieldActivityFile';
import {Field} from './field';

@Injectable({
  providedIn: 'root'
})
export class FieldactivityService {

  private fieldsAPIUrl = environment.fieldsAPIUrl;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }



  addFieldActivity(newFieldActivity: FieldActivity): Observable<FieldActivity> {
    console.log('addFieldActivity()');
    console.log('fieldActivity: ' + JSON.stringify(newFieldActivity));
    const url = `${this.fieldsAPIUrl}/${newFieldActivity.fieldId}/activities`;
    console.log('url: ' + url);
    return new Observable<FieldActivity>(observer => {
      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newFieldActivity)
      })
        .then((response) => {
          // console.log('response: ' + response);
          return response.json();
        })
        .then((data) => {
          console.log('data: ' + JSON.stringify(data));
          // addDataToMap(data, '');
          observer.next(newFieldActivity);
          observer.complete();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    });
    // return this.http.post<string>(url, fieldActivityFile, this.httpOptions).pipe(
    //   tap(_ => console.log(`added field activity file =${fieldActivityFile.fieldActivityFileLocation}`)),
    //   catchError(this.handleError<FieldActivityFile>('addFieldActivityFile'))
    // );
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
