import {Injectable} from '@angular/core';
import * as S3 from 'aws-sdk/clients/s3';
import {Observable, of} from "rxjs";
import {FieldActivityFile} from "./fieldActivityFile";

@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor() {
  }

  uploadFile(file, fieldActivityId: string): Observable<FieldActivityFile> {
    let fieldActivityFile = {} as FieldActivityFile;
    const contentType = file.type;
    const bucket = new S3(
      {
        accessKeyId: 'AKIARMZC7KNTOQO5SY3U',
        secretAccessKey: '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',
        region: 'us-east-1'
      }
    );
    const params = {
      Bucket: 'fieldactivityfiles',
      Key: fieldActivityId + file.name,
      Body: file,
      ACL: 'public-read',
      ContentType: contentType
    };
    let fileURL = null;
    bucket.upload(params).on('httpUploadProgress', function (evt) {
      console.log(evt.loaded + ' of ' + evt.total + ' Bytes');
    }).send(function (err, data) {
      if (err) {
        console.log('There was an error uploading your file: ', JSON.stringify(err));
      }
      console.log('Successfully uploaded file.', data);
      fileURL = data.Location;
      console.log("file location: " + fileURL);


      fieldActivityFile.fieldActivityFileLocation = fileURL;
      fieldActivityFile.fieldActivityFileSizeMB = (file.sizeInGb * 1000);
      fieldActivityFile.fieldActivityFilename = file.name;
      fieldActivityFile.fieldActivityId = parseInt(fieldActivityId);


    });

    return of(fieldActivityFile as FieldActivityFile);
  }

}
