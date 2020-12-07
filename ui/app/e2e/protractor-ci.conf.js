const config = require('./protractor.conf').config;

config.capabilities = {
  browserName: 'chrome',
  'goog:chromeOptions': {
    args: ['--headless', '--no-sandbox', '--disable-gp', '--window-size=800x600']
  }
};

exports.config = config;
