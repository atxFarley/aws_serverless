import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

import {environment} from './../environments/environment';
import {FieldActivityFile} from './fieldActivityFile';
import {FileUpload} from './fileUpload';


@Injectable({
  providedIn: 'root'
})
export class FieldactivityfileService {

  private fieldsAPIUrl = environment.fieldsAPIUrl;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }


  deleteBucketFile(fieldId: string, fieldActivityId: string, fieldActivityFile: FieldActivityFile): Observable<FileUpload> {
    console.log('deleteBucketFile(fieldId: ' + fieldId + ', fieldActivityId: ' + fieldActivityId + ')');
    const fileKey = fieldActivityFile.fieldActivityFileLocation.substring(fieldActivityFile.fieldActivityFileLocation.indexOf('fieldactivityfiles.s3.amazonaws.com/') + 36);
    console.log('fileKey: ' + fileKey);
    const url = `${this.fieldsAPIUrl}/s3object`;
    console.log('url: ' + url);
    const bucketObject = {} as FileUpload;
    bucketObject.fileKey = fileKey;
    return new Observable<FileUpload>(observer => {
      fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(bucketObject)
      })
        .then((response) => {
          // console.log('response: ' + response);
          return response.json();
        })
        .then((data) => {
          console.log('data: ' + JSON.stringify(data));
          // addDataToMap(data, '');
          observer.next(bucketObject);
          observer.complete();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    });
  }


  /** DELETE: delete the field activityfile from the server */
  deleteFieldActivityFile(fieldId: string, fieldActivityId: string, fieldActivityFile: FieldActivityFile): Observable<FieldActivityFile> {
    const id = fieldActivityFile.fieldActivityFileId;
    const url = `${this.fieldsAPIUrl}/${fieldId}/activities/${fieldActivityId}/activityfiles/${id}`;
    console.log('delete field activity file url: ' + url);
    return this.http.delete<FieldActivityFile>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted field activity file id=${id}`)),
      catchError(this.handleError<FieldActivityFile>('deletefieldactivityfile'))
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
