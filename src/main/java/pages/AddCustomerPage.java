package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.github.javafaker.Faker;
import java.time.Duration;

public class AddCustomerPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;
    private Faker faker;

    // Locators
    private By firstNameInput = By.xpath("//input[@placeholder='First Name']");
    private By lastNameInput = By.xpath("//input[@placeholder='Last Name']");
    private By postCodeInput = By.xpath("//input[@placeholder='Post Code']");
    private By addCustomerButton = By.xpath("//button[text()='Add Customer']");

    // Constructor
    public AddCustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.jsExecutor = (JavascriptExecutor) driver;
        this.faker = new Faker();
    }

    // Fill customer details
    public void enterCustomerDetails(String firstName, String lastName, String postCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(postCodeInput).sendKeys(postCode);
    }

    // Click "Add Customer" button
    public void clickAddCustomer() {
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(addCustomerButton));
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();  // Accept alert after adding customer
    }

    // Generate a random customer (for testing)
    public String[] generateRandomCustomer() {
        return new String[]{faker.name().firstName(), faker.name().lastName(), faker.address().zipCode()};
    }
}
