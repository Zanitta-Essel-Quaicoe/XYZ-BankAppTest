package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BankManagerPage;
import pages.AddCustomerPage;
import pages.OpenAccountPage;
import pages.CustomersPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankManagerTest extends LoginTest {  // Extend LoginTest to reuse its setup
    private BankManagerPage bankManagerPage;
    private AddCustomerPage addCustomerPage;
    private OpenAccountPage openAccountPage;
    private CustomersPage customersPage;
    private String firstName, lastName, fullName, postCode;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        System.out.println(driver);

        bankManagerPage = new BankManagerPage(driver);
        addCustomerPage = new AddCustomerPage(driver);
        openAccountPage = new OpenAccountPage(driver);
        customersPage = new CustomersPage(driver);
    }

    @Test
    @Order(0)
    @DisplayName("Verify Bank Manager Login Navigation")
    public void testBankManagerLoginNavigation() {
        loginPage.clickBankManagerLogin();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after clicking: " + currentUrl);
        assertTrue(currentUrl.contains("manager"), "Failed to navigate to Bank Manager Dashboard.");
    }

    @Test
    @Order(1)
    @DisplayName("Verify that a bank manager can add a new customer")
    void testAddCustomer() {
        loginAsBankManager();
        bankManagerPage.clickAddCustomer();

        String[] customer = addCustomerPage.generateRandomCustomer();
        firstName = customer[0];
        lastName = customer[1];
        postCode = customer[2];
        fullName = firstName + " " + lastName;

        addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
        addCustomerPage.clickAddCustomer();

        // Verify alert confirms customer addition
        String alertText = addCustomerPage.getAlertText();
        addCustomerPage.acceptAlertIfPresent();
        assertTrue(alertText.contains("Customer added successfully"),
                "Unexpected alert text: " + alertText);
    }

    @Test
    @Order(2)
    @DisplayName("Verify that a bank manager can open an account for an existing customer")
    void testOpenAccount() {
        loginAsBankManager();
        bankManagerPage.clickAddCustomer();

        // Generate random customer details
        String[] customer = addCustomerPage.generateRandomCustomer();
        firstName = customer[0];
        lastName = customer[1];
        postCode = customer[2];
        fullName = firstName + " " + lastName;

        // Add a new customer
        addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
        addCustomerPage.clickAddCustomer();
        String addCustomerAlert = addCustomerPage.getAlertText();
        addCustomerPage.acceptAlertIfPresent();
        assertTrue(addCustomerAlert.contains("Customer added successfully") || addCustomerAlert.contains("added"),
                "Customer was not added!");

        // Navigate to Open Account
        bankManagerPage.clickOpenAccount();

        // Ensure customer is selectable in the dropdown
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//select[@id='userSelect']"), fullName));

        // Select customer and currency
        openAccountPage.selectCustomer(fullName);
        openAccountPage.selectCurrency("Dollar");

        // Ensure "Process" button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Process']")));

        // Click "Process" — now this already waits for and accepts the alert!
        openAccountPage.clickProcessButton();

        // Remove extra alert handling — it’s already done in clickProcessButton()
        System.out.println("Account creation flow completed successfully.");
    }





    @Test
    @Order(3)
    @DisplayName("Verify that a bank manager can delete a customer")
    void testDeleteCustomer() {
        loginAsBankManager();
        bankManagerPage.clickAddCustomer();

        String[] customer = addCustomerPage.generateRandomCustomer();
        firstName = customer[0];
        lastName = customer[1];
        postCode = customer[2];
        fullName = firstName + " " + lastName;

        addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
        addCustomerPage.clickAddCustomer();
        addCustomerPage.acceptAlertIfPresent();

        bankManagerPage.clickCustomers();

        assertTrue(customersPage.searchCustomer(firstName), "Customer should exist before deletion.");
        customersPage.deleteCustomer(firstName);
        assertFalse(customersPage.searchCustomer(firstName), "Customer should not exist after deletion.");
    }

    @Test
    @Order(4)
    @DisplayName("Verify system behavior for duplicate customer entry")
    void testDuplicateCustomerEntry() {
        loginAsBankManager();
        bankManagerPage.clickAddCustomer();

        // Fixed customer details for duplication test
        String firstName = "John";
        String lastName = "Doe";
        String postCode = "12345";

        // Add the same customer twice
        for (int i = 0; i < 2; i++) {
            addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
            addCustomerPage.clickAddCustomer();

            try {
                String alertText = addCustomerPage.getAlertText();
                addCustomerPage.acceptAlertIfPresent();
                System.out.println("Attempt " + (i + 1) + " - Alert: " + alertText);

                if (i == 1) { // Second attempt should trigger a duplicate error
                    assertTrue(alertText.contains("Customer may be duplicate"),
                            "Expected duplicate customer error, but got: " + alertText);
                }
            } catch (TimeoutException e) {
                Assertions.fail("No alert appeared on attempt " + (i + 1));
            }
        }
    }
}
