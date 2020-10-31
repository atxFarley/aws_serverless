// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  //fieldsS3BucketUrl: 'https://fieldactivityfiles.s3.amazonaws.com/',
  fieldsS3BucketUrl: process.env.S3BUCKETURL,
  //apiURL: 'https://ky1bp4f5sl.execute-api.us-east-1.amazonaws.com/Prod',
  apiURL: process.env.APIURL,
  fieldsAPIUrl: `${process.env.APIURL}/fields`,
  fieldUsersAPIUrl: `${process.env.APIURL}/fieldusers`,
  fieldActivityFileTypesAPIUrl: `${process.env.APIURL}/fieldactivityfiletypes`,
  fieldActivityTypesAPIUrl: `${process.env.APIURL}/fieldactivitytypes`,
  //mapboxUrl: 'https://api.mapbox.com/styles/v1/alfarley/ckfu0q0fv07oz19lpffu7cjd6/tiles/256/{z}/{x}/{y}?access_token=pk.eyJ1IjoiYWxmYXJsZXkiLCJhIjoiOWgxTzVWRSJ9.wPUIEFeXYqsWzhTT8LlDng'
  mapboxUrl: process.env.MAPBOXURL

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
