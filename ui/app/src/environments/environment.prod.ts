export const environment = {
  production: true,
  fieldsS3BucketUrl: process.env.S3BUCKETURL,
  apiURL: process.env.APIURL,
  fieldsAPIUrl: `${process.env.APIURL}/fields`,
  fieldUsersAPIUrl: `${process.env.APIURL}/fieldusers`,
  fieldAttributesAPIUrl: `${process.env.APIURL}/fieldattributes`,
  fieldActivityFileTypesAPIUrl: `${process.env.APIURL}/fieldactivityfiletypes`,
  fieldActivityTypesAPIUrl: `${process.env.APIURL}/fieldactivitytypes`,
  mapboxUrl: process.env.MAPBOXURL
};
