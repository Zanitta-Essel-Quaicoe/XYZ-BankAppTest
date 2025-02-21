package tests;

import org.junit.jupiter.api.*;
import pages.BankManagerPage;
import pages.AddCustomerPage;
import pages.OpenAccountPage;
import pages.CustomersPage;

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

        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        // Debugging: Print actual URL
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after clicking: " + currentUrl);

        assertTrue(currentUrl.contains("manager"), "Failed to navigate to Bank Manager Dashboard.");
    }



    @Test
    @Order(1)
    @DisplayName("Verify that a bank manager can add a new customer")
    void testAddCustomer() {
        loginAsBankManager(); // Reusing LoginTest login method

        bankManagerPage.clickAddCustomer();

        String[] customer = addCustomerPage.generateRandomCustomer();
        firstName = customer[0];
        lastName = customer[1];
        postCode = customer[2];
        fullName = firstName + " " + lastName;

        addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
        addCustomerPage.clickAddCustomer();

        assertTrue(true, "Customer added successfully.");
    }

    @Test
    @Order(2)
    @DisplayName("Verify that a bank manager can open an account for an existing customer")
    void testOpenAccount() {
        loginAsBankManager();
        bankManagerPage.clickAddCustomer();
        String[] customer = addCustomerPage.generateRandomCustomer();
        firstName = customer[0];
        lastName = customer[1];
        postCode = customer[2];
        fullName = firstName + " " + lastName;

        addCustomerPage.enterCustomerDetails(firstName, lastName, postCode);
        addCustomerPage.clickAddCustomer();



        bankManagerPage.clickOpenAccount();

        openAccountPage.selectCustomer(fullName);
        openAccountPage.selectCurrency("Dollar");
        openAccountPage.clickProcessButton();

        assertTrue(true, "Account created successfully.");
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

        bankManagerPage.clickCustomers();

        assertTrue(customersPage.searchCustomer(firstName), "Customer should exist before deletion.");
        customersPage.deleteCustomer(firstName);
        assertFalse(customersPage.searchCustomer(firstName), "Customer should not exist after deletion.");
    }
}
