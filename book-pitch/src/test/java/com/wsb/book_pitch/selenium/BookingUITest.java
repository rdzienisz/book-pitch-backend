package com.wsb.book_pitch.selenium;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingUITest {
    private static final Logger logger = LoggerFactory.getLogger(BookingUITest.class);
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static WebDriverWait longWait;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    void testCreateBooking() {
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
        dateInput.sendKeys("1-01-2024");
        List<WebElement> availableSlots = driver.findElements(By.xpath("//tr[contains(@style, 'lightgreen')]"));
        if (!availableSlots.isEmpty()) {
            availableSlots.get(0).click();
        }
        else {
            WebElement timeSlot = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[text()='20:00']")));
            timeSlot.click();
        }
        createBookingButton.click();
        longWait.until(ExpectedConditions.alertIsPresent());
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