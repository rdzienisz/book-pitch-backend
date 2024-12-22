package com.wsb.book_pitch.selenium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingUITest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    @Test
    void testCreateBooking() throws IOException {
        driver.get("http://localhost:3000");

        WebElement bookNavLink = driver.findElement(By.id("book-button"));
        bookNavLink.click();

        WebElement emailInput = driver.findElement(By.id("email-input"));
        WebElement dateInput = driver.findElement(By.id("date-input"));
        WebElement pitchSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("pitch-select")));
        WebElement createBookingButton = driver.findElement(By.id("create-booking-button"));
        pitchSelect.click();
        List<WebElement> options = driver.findElements(By.tagName("li"));
        for (WebElement option : options) {
            if (option.getText().equals("Pitch 1")) {
                option.click();
                break;
            }
        }

        emailInput.sendKeys("test@example.com");
        dateInput.sendKeys("30-12-2024");
        List<WebElement> availableSlots = driver.findElements(By.xpath("//tr[contains(@style, 'lightgreen')]"));
        if (!availableSlots.isEmpty()) {
            availableSlots.get(0).click();
        }

        createBookingButton.click();
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("screenshots/pitchSelectError.png"));
        wait.until(ExpectedConditions.alertIsPresent());
        File screenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot1, new File("screenshots/pitchSelectError.png"));
        String alertText = driver.switchTo().alert().getText();
        assertTrue(alertText.contains("Booking successful!"));
        driver.switchTo().alert().accept();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}