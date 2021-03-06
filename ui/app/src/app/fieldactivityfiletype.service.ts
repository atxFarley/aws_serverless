import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {FieldActivityFileType} from './fieldActivityFileType';
import {environment} from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FieldactivityfiletypeService {

  private fieldActivityFileTypesAPIUrl = environment.fieldActivityFileTypesAPIUrl;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  /** GET field users from the server */
  getFieldActivityFileTypes(): Observable<FieldActivityFileType[]> {
    return this.http.get<FieldActivityFileType[]>(this.fieldActivityFileTypesAPIUrl)
      .pipe(
        tap(_ => console.log('fetched field activity file types')),
        catchError(this.handleError<FieldActivityFileType[]>('getFieldActivityFileTypes', []))
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
