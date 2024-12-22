package com.wsb.book_pitch.selenium;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingUITest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofMinutes(2));
    }

    @Test
    void testCreateBooking() {
        driver.get("http://localhost:3000");

        WebElement bookNavLink = driver.findElement(By.id("book-button"));
        bookNavLink.click();

        WebElement emailInput = driver.findElement(By.id("email-input"));
        WebElement dateInput = driver.findElement(By.id("date-input"));
        WebElement pitchSelect = driver.findElement(By.id("pitch-select"));
        WebElement createBookingButton = driver.findElement(By.id("create-booking-button"));

        pitchSelect.click();
        List<WebElement> options = driver.findElements(By.tagName("li")); // Assuming options are <li> elements
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
        wait.until(ExpectedConditions.alertIsPresent());
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