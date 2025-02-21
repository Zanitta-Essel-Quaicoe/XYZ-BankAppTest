package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By bankManagerLoginButton = By.xpath("//button[contains(text(),'Bank Manager Login')]");
    private By customerLoginButton = By.xpath("//button[contains(text(),'Customer Login')]");
    private By loginPageHeader = By.xpath("//strong[contains(text(),'XYZ Bank')]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Click "Bank Manager Login"
    public void clickBankManagerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(bankManagerLoginButton)).click();
    }

    // ✅ NEW: Click "Customer Login"
    public void clickCustomerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(customerLoginButton)).click();
    }

    // ✅ NEW: Verify if login page is displayed (used for logout validation)
    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginPageHeader)).isDisplayed();
    }
}
