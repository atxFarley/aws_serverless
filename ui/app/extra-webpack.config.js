const webpack = require('webpack');


module.exports = {
  plugins: [new webpack.DefinePlugin({
    'process.env': {
      APIURL: JSON.stringify(process.env.APIURL),
      MAPBOXURL: JSON.stringify(process.env.MAPBOXURL),
      S3BUCKETURL: JSON.stringify(process.env.S3BUCKETURL)
    }
  })]
}
