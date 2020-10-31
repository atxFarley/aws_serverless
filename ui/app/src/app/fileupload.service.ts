import {Injectable} from '@angular/core';
import * as S3 from 'aws-sdk/clients/s3';
import {Observable, Observer, of, pipe} from "rxjs";
import {FileUpload} from "./fileUpload";
import {FieldActivityFile} from "./fieldActivityFile";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, tap, concatMap} from "rxjs/operators";
import {Field} from "./field";
import {prepareEventListenerParameters} from "@angular/compiler/src/render3/view/template";
import {environment} from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  private fieldsAPIUrl = environment.fieldsAPIUrl;
  private fieldsS3BucketUrl = environment.fieldsS3BucketUrl;

  fieldActivityFile: FieldActivityFile;

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getPresignedURL(file, fieldId: string, fieldActivityId: string): Observable<FileUpload> {
    console.log("getPresignedURL(fieldId: " + fieldId + ", fieldActivityId: " + fieldActivityId + ")");
    const contentType = file.type;
    console.log("contentType: " + contentType);
    const fileSize = file.size;
    console.log("file size: " + file.size);
    let fileKey = fieldId + "/" + fieldActivityId + "/" + file.name;
    let fileUpload = {} as FileUpload;
    fileUpload.fileKey = fileKey;
    fileUpload.fileContentType = contentType;
    const url = `${this.fieldsAPIUrl}/s3url`;
    return this.http.post<FileUpload>(url, fileUpload).pipe(
      tap(_ => console.log(`got presigned url`)),
      catchError(this.handleError<FileUpload>(`got presignedurl failed`))
    );
  }


  uploadFile(fileUpload: FileUpload, file, fieldId: string, fieldActivityId: string): Observable<FieldActivityFile> {
    console.log("uploadFile(fieldId: " + fieldId + ", fieldActivityId: " + fieldActivityId + ")");
    let fieldActivityFile = {} as FieldActivityFile;
    const contentType = file.type;
    console.log("contentType: " + contentType);
    const fileSize = file.size;
    console.log("file size: " + file.size);
    fieldActivityFile.fieldActivityFileSizeMB = (fileSize / 1024 / 1024);
    fieldActivityFile.fieldActivityFilename = file.name;
    fieldActivityFile.fieldActivityId = parseInt(fieldActivityId);
    console.log("fieldActivityFile: size: " + +fieldActivityFile.fieldActivityFileSizeMB);
    let fileKey = fieldId + "/" + fieldActivityId + "/" + file.name;
    let fileURL = null;
    let presignedUrl = fileUpload.presignedUrl;
    console.log("presignedUrl: " + presignedUrl);
    return Observable.create(observer => {
      const upload = this.http.put(presignedUrl, file).toPromise();
      upload.then(data => {
        console.log('upload success => ', data)
        fileURL = this.fieldsS3BucketUrl + "/" +  fileKey;
        console.log("file location: " + fileURL);
        fieldActivityFile.fieldActivityFileLocation = fileURL;
        observer.next(fieldActivityFile);
        observer.complete();
      }).catch(err => console.error('error: ', err))
    });
  }

  addFieldActivityFile(fieldId: string, fieldActivityId: string, fieldActivityFile: FieldActivityFile): Observable<FieldActivityFile> {
    console.log("addFieldActivityFile()");
    console.log("fieldActivityFile: " + JSON.stringify(fieldActivityFile));
    const url = `${this.fieldsAPIUrl}/${fieldId}/activities/${fieldActivityId}/activityfiles`;
    console.log("url: " + url);
    return Observable.create(observer => {
      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(fieldActivityFile)
      })
        .then(function (response) {
          //console.log("response: " + response);
          return response.json();
        })
        .then(function (data) {
          console.log("data: " + JSON.stringify(data));
          //addDataToMap(data, "");
          observer.next(fieldActivityFile);
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


// uploadFile(file, fieldId: string, fieldActivityId: string): Observable<FieldActivityFile> {
//   console.log("uploadFile(fieldId: " + fieldId + ", fieldActivityId: " + fieldActivityId + ")");
//   let fieldActivityFile = {} as FieldActivityFile;
//   const contentType = file.type;
//   console.log("contentType: " + contentType);
//   const fileSize = file.size;
//   console.log("file size: " + file.size);
//   const bucket = new S3(
//     {
//       accessKeyId: '',
//       secretAccessKey: '',
//       region: 'us-east-1'
//     }
//   );
//   const params = {
//     Bucket: 'fieldactivityfiles',
//     Key: fieldId + "/" + fieldActivityId + "/" + file.name,
//     Body: file,
//     ACL: 'public-read',
//     ContentType: contentType
//   };
//   let fileURL = null;
//
//   return Observable.create(observer => {
//     bucket.upload(params).on('httpUploadProgress', function (evt) {
//       //console.log(evt.loaded + ' of ' + evt.total + ' Bytes');
//     }).send(function (err, data) {
//       if (err) {
//         console.log('There was an error uploading your file: ', JSON.stringify(err));
//         observer.error(err);
//       }
//       console.log('Successfully uploaded file.', data);
//       fileURL = data.Location;
//       console.log("file location: " + fileURL);
//       fieldActivityFile.fieldActivityFileLocation = fileURL;
//       fieldActivityFile.fieldActivityFileSizeMB = (fileSize / 1024 / 1024);
//       fieldActivityFile.fieldActivityFilename = file.name;
//       fieldActivityFile.fieldActivityId = parseInt(fieldActivityId);
//       console.log("fieldActivityFile: size: " + +fieldActivityFile.fieldActivityFileSizeMB);
//       observer.next(fieldActivityFile);
//       observer.complete();
//     });
//   });
// }


  private handleError<T>(operation = 'operation', result ?: T) {
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

