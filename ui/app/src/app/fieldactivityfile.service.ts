import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import {FieldActivityFile} from "./fieldActivityFile";

@Injectable({
  providedIn: 'root'
})
export class FieldactivityfileService {

  private fieldsAPIUrl = "https://ky1bp4f5sl.execute-api.us-east-1.amazonaws.com/Prod/fields";
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  updateFieldActivityFile(fieldActivityFile: FieldActivityFile, fieldId: number): Observable<any> {
    console.log("in updateFieldActivityFile() in fieldActivityFileService: fieldActivityFile: " + fieldActivityFile.fieldActivityFileLocation);
    const url = `${this.fieldsAPIUrl}/${fieldId}/activities/${fieldActivityFile.fieldActivityId}/activityfiles`;
    return this.http.put(url, fieldActivityFile, this.httpOptions).pipe(
      tap(_ => console.log(`added fieldActivityFile=${fieldActivityFile.fieldActivityFileLocation}`)),
      catchError(this.handleError<any>('addfieldactivityfile'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
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
