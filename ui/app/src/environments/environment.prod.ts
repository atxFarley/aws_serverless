export const environment = {
  production: true,
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
