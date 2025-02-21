package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CustomerPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By customerDropdown = By.id("userSelect");
    private By loginButton = By.xpath("//button[text()='Login']");
    private By logoutButton = By.xpath("//button[text()='Logout']");
    private By welcomeMessage = By.className("fontBig");
    private By transactionsTab = By.xpath("//button[contains(text(), 'Transactions')]");
    private By transactionsTable = By.xpath("//table[@class='table-bordered']");

    // Constructor
    public CustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Select customer from dropdown
    public void selectCustomer(String customerName) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(customerDropdown));
        dropdown.click();
        WebElement customerOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[text()='" + customerName + "']")));
        customerOption.click();
    }

    // Click Login button
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // Verify if customer login is successful
    public boolean isCustomerLoggedIn() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage)).isDisplayed();
    }

    // Click Transactions tab
    public void clickTransactionsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(transactionsTab)).click();
    }

    // Verify Transactions table is visible
    public boolean isTransactionsTableVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(transactionsTable)).isDisplayed();
    }

    // Click Logout button
    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
