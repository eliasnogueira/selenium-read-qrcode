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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.testng.Assert.assertEquals;

public class QRTest {

    @Test
    public void readQRCode() throws IOException {
        // maybe you change this for your local chromedriver
        System.setProperty("webdriver.chrome.driver", "/Users/eliasnogueira/Selenium/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://eliasnogueira.github.io/selenium-read-qrcode/");

            String qrCodeFile = driver.findElement(By.id("qr")).getAttribute("src");

            // get the qr code content and assert the result
            String qrCodeResult = decodeQRCode(new URL(qrCodeFile));
            assertEquals(qrCodeResult, "QR Code output text");

        } catch (Throwable throwable) {
            /*
             * If a com.google.zxing.NotFoundException your image maybe is too large
             * or the url was not found
             */
            Assert.fail(throwable.toString());

        } finally {
            driver.quit();
        }
    }

    /**
     * Decode a QR Code image using zxing
     * @param qrCodeImage the image URL
     * @return the content
     * @throws IOException url not found
     */
    private static String decodeQRCode(URL qrCodeImage) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}
