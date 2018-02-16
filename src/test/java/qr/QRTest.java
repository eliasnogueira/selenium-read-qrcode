package qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class QRTest {

    @Test
    public void readQRCode() throws IOException {
        /*
         * setting chromedriver binary to start Google Chrome
         * @see https://sites.google.com/a/chromium.org/chromedriver/downloads
         */
        System.setProperty("webdriver.chrome.driver", "/Users/eliasnogueira/Selenium/chromedriver");
        WebDriver driver = new ChromeDriver();

        driver.get("https://eliasnogueira.github.io/selenium-read-qrcode/");

        String qrCodeFile = driver.findElement(By.id("qr")).getAttribute("src");

        // show the content of QR Code
        System.out.println(decodeQRCode(new URL(qrCodeFile)));

        driver.quit();

    }

    /**
     * Decode a QR Code image using zxing
     * @param qrCodeimage the image URL
     * @return the content
     * @throws IOException url not found
     */
    private static String decodeQRCode(URL qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }
}
