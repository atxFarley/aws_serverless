import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

import {Field} from './field';
import {FieldAttribute} from './fieldAttribute';
import {FieldActivity} from './fieldActivity';
import {FieldActivityFile} from './fieldActivityFile';
import {FieldHistory} from './fieldHistory';
import {environment} from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FieldService {

  private fieldsAPIUrl = environment.fieldsAPIUrl;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }


  /** GET field by id. Return `undefined` when id not found */
  getFieldNo404<Data>(id: number): Observable<Field> {
    const url = `${this.fieldsAPIUrl}/?fieldid=${id}`;
    return this.http.get<Field[]>(url)
      .pipe(
        map(fieldes => fieldes[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          console.log(`${outcome} field id=${id}`);
        }),
        catchError(this.handleError<Field>(`getField id=${id}`))
      );
  }

  /** GET field by id. Will 404 if id not found */
  getField(id: number): Observable<Field> {
    console.log('getField: ' + id);
    const url = `${this.fieldsAPIUrl}/${id}`;
    return this.http.get<Field>(url).pipe(
      tap(_ => console.log(`fetched field id=${id}`)),
      catchError(this.handleError<Field>(`getfield id=${id}`))
    );
  }

  /** DELETE: delete the field from the server */
  deleteField(field: Field | number): Observable<Field> {
    const id = typeof field === 'number' ? field : field.fieldId;
    const url = `${this.fieldsAPIUrl}/${id}`;

    return this.http.delete<Field>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted field id=${id}`)),
      catchError(this.handleError<Field>('deletefield'))
    );
  }

  /** PUT: update the field on the server */
  updateField(field: Field): Observable<any> {
    console.log('in updateField() in fieldService: field: ' + field.fieldId);
    const url = `${this.fieldsAPIUrl}/${field.fieldId}`;
    return this.http.put(url, field, this.httpOptions).pipe(
      tap(_ => console.log(`updated field id=${field.fieldId}`)),
      catchError(this.handleError<any>('updatefield'))
    );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T): any  {
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
