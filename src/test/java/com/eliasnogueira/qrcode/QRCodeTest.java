/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.eliasnogueira.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class QRCodeTest {

    private static final Logger log = LoggerFactory.getLogger(QRCodeTest.class);
    private static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://eliasnogueira.github.io/selenium-read-qrcode/");
    }

    @Test
    void readQRCodeFromURL() {

        String qrCodeFile = driver.findElement(By.id("qr")).getAttribute("src");

        // get the qr code content and assert the result
        String qrCodeResult = decodeQRCode(qrCodeFile);
        assertThat(qrCodeResult).isEqualTo("Congratulations!");
    }

    @Test
    void readQRCodeFromBase64() {
        String qrCodeFile = driver.findElement(By.id("qr-base64")).getAttribute("src");

        /*
         * Split the content of src attribute from qr-base64 image to get only the Base64 String
         * Because the src starts with 'data:image/png;base64,' and the following text is the Base64 String
         */
        String base64String = qrCodeFile.split(",")[1];

        // get the qr code content and assert the result
        String qrCodeResult = decodeQRCode(base64String);
        assertThat(qrCodeResult).isEqualTo("QR Code Base64 output text");
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    /**
     * Decode a QR Code image using zxing
     * @param qrCodeImage the image URL
     * @return the content
     */
    private static String decodeQRCode(Object qrCodeImage) {
        Result result = null;

        try {
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

            result = new MultiFormatReader().decode(bitmap);
        } catch (NotFoundException | IOException e) {
            log.error("Error reading the QR Code", e);
        }
        return result.getText();
    }
}
