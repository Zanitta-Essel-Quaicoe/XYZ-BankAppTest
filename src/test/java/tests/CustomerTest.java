package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;
import pages.CustomerPage;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private CustomerPage customerPage;
    private final String CUSTOMER_NAME = "Harry Potter"; // Example customer

    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");

        // Initialize Page Objects
        loginPage = new LoginPage(driver);
        customerPage = new CustomerPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Verify that a customer can navigate to the login page and log in successfully")
    void testCustomerLoginNavigation() {
        loginPage.clickCustomerLogin();
        customerPage.selectCustomer(CUSTOMER_NAME);
        customerPage.clickLogin();

        assertTrue(customerPage.isCustomerLoggedIn(), "❌ Customer login failed!");
    }

    @Test
    @Order(2)
    @DisplayName("Verify that a logged-in customer can view account transactions")
    void testViewTransactions() {
        customerPage.clickTransactionsTab();
        assertTrue(customerPage.isTransactionsTableVisible(), "❌ Transactions table not visible!");
    }

    @Test
    @Order(3)
    @DisplayName("Verify that a customer can log out successfully")
    void testCustomerLogout() {
        customerPage.clickLogout();
        assertTrue(loginPage.isLoginPageDisplayed(), "❌ Customer logout failed!");
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
