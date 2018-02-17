/*
 * Copyright 2018 Elias Nogueira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.testng.Assert.assertEquals;

public class QRTest {

    private WebDriver driver;


    @BeforeTest
    public void setup() {
        // maybe you change this for your local chromedriver
        System.setProperty("webdriver.chrome.driver", "/Users/eliasnogueira/Selenium/chromedriver");
        driver = new ChromeDriver();

        driver.get("https://eliasnogueira.github.io/selenium-read-qrcode/");
    }

    @Test
    public void readQRCodeFromURL() throws IOException, NotFoundException {

        String qrCodeFile = driver.findElement(By.id("qr")).getAttribute("src");

        // get the qr code content and assert the result
        String qrCodeResult = decodeQRCode(qrCodeFile);
        assertEquals(qrCodeResult, "Congratulations!");
    }

    @Test
    public void readQRCodeFromBase64() throws IOException, NotFoundException {
        String qrCodeFile = driver.findElement(By.id("qr-base64")).getAttribute("src");

        /*
         * Split the content of src attribute from qr-base64 image to get only the Base64 String
         * Because the src starts with 'data:image/png;base64,' and following text is the Base64 String
         */
        String base64String = qrCodeFile.split(",")[1];

        // get the qr code content and assert the result
        String qrCodeResult = decodeQRCode(base64String);
        assertEquals(qrCodeResult, "QR Code Base64 output text");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    /**
     * Decode a QR Code image using zxing
     * @param qrCodeImage the image URL
     * @return the content
     * @throws IOException if the image was not found
     */
    private static String decodeQRCode(Object qrCodeImage) throws IOException, NotFoundException {
        BufferedImage bufferedImage;

        // if not (probably it is a URL
        if (((String) qrCodeImage).contains("http")) {
            bufferedImage = ImageIO.read((new URL((String)qrCodeImage)));

            // if is a Base64 String
        } else {
            byte[] decoded = Base64.decodeBase64((String)qrCodeImage);
            bufferedImage = ImageIO.read(new ByteArrayInputStream(decoded));
        }

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}
