describe('Angularjs homepage Hello World', function() {
  it ('Say hello world',function() {
    browser.get('https://angularjs.org');
    element(by.model('yourName')).sendKeys('World');
    var welcomeMessage = element.all(by.tagName('h1'));
    expect(welcomeMessage.get(1).getText()).toEqual('Hello World!');
  });
});
