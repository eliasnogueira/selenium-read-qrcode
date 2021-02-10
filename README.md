# Selenium Read QRCode

This project shows how to read the QRCode content from a webapp using Selenium and ZXing.

## Technologies in use
* [Java](https://www.oracle.com/java/technologies/javase-downloads.html) as the programming language
* [Selenium](http://selenium.dev/) as the web test automation tool
* [Zxing](https://github.com/zxing/zxing) as the library to decode the QRCode content
* [AssertJ](https://joel-costigliola.github.io/assertj/) as the assertion library
* [JUnit 5](https://junit.org/junit5/) as the test tool to support the test automation script
* [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) as the browser binary management library

## How to run this project

### Preconditions
- Java JDK >=11
- Google Chrome installed

### Steps

### Running using the command line
1. Go do the project directory and run `mvn verify -Dmaven.test.skip=true`
2. Run `mvn test` to run the test

### Running in your IDE
1. Open this project in your preferred IDE
2. Open the `QRCodeTest` class placed in `src/test/java`
3. Run the test

### Expected result
You can expect a successful execution.
The test will read the QRCode content and assert by its expectation.

## What does the tests do
- Open the browser in a page that has a QRCode
- In the test that reads the image src as path `readQRCodeFromURL`  
  - Get the image path and transform it in a `BufferedImage`
- In the test that reads the image src as Base64 `readQRCodeFromBase64`
    - Get the Base64 content and transform it in a `BufferedImage`  
- Process the image and decode it using ZXing
- Return the QRCode content
- Assert the QRCode content
