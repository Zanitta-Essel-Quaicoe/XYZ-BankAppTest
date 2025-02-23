package tests;

import org.junit.jupiter.api.*;
import pages.CustomerPage;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest extends LoginTest {
    private CustomerPage customerPage;

    @BeforeEach
    public void setUpCustomerTest() {
        customerPage = new CustomerPage(driver);
        loginAsCustomer("Harry Potter");
        assertTrue(customerPage.isCustomerPageDisplayed(), "❌ Customer page not loaded.");
    }

    @Test
    @Order(1)
    @DisplayName("View transactions test")
    public void testViewTransactions() {
        customerPage.viewTransactions();
        assertTrue(customerPage.isTransactionPageDisplayed(), "❌ Failed to view transactions.");
    }

    @Test
    @Order(2)
    @DisplayName("Deposit funds test")
    public void testDepositFunds() {
        int depositAmount = 100;
        customerPage.depositFunds(depositAmount);

        // Ensure correct tab is active
        assertTrue(customerPage.isDepositTabActive(), "❌ Deposit tab not active.");

        // Verify balance update
        customerPage.waitForBalanceToUpdate(String.valueOf(depositAmount));
        assertEquals(String.valueOf(depositAmount), customerPage.getBalance(), "❌ Balance did not match deposit.");
    }

    @Test
    @Order(3)
    @DisplayName("Withdraw funds test with sufficient balance")
    public void testWithdrawFunds() {
        int initialDeposit = 200;
        int withdrawAmount = 100;

        // Deposit first
        customerPage.depositFunds(initialDeposit);
        customerPage.waitForBalanceToUpdate(String.valueOf(initialDeposit));
        assertEquals(String.valueOf(initialDeposit), customerPage.getBalance(), "❌ Initial balance mismatch after deposit.");

        // Withdraw funds
        customerPage.withdrawFunds(withdrawAmount);
        String expectedBalance = String.valueOf(initialDeposit - withdrawAmount);

        // Re-check balance
        customerPage.waitForBalanceToUpdate(expectedBalance);
        String actualBalance = customerPage.getBalance();
        System.out.println("✅ Expected balance: " + expectedBalance);
        System.out.println("📊 Final balance after withdrawal: " + actualBalance);

        assertEquals(expectedBalance, actualBalance, "❌ Balance did not update correctly after withdrawal.");
    }

    @Test
    @Order(4)
    @DisplayName("Attempt to withdraw more than available balance")
    public void testWithdrawExceedingBalance() {
        int depositAmount = 50;
        int withdrawAmount = 100;

        // Deposit funds
        customerPage.depositFunds(depositAmount);
        customerPage.waitForBalanceToUpdate(String.valueOf(depositAmount));
        assertEquals(String.valueOf(depositAmount), customerPage.getBalance(), "❌ Initial balance mismatch after deposit.");

        // Withdraw more than balance
        customerPage.withdrawFunds(withdrawAmount);

        // Check if error message appeared
        boolean transactionFailed = customerPage.isTransactionFailed();
        System.out.println("⚠️ Transaction failure detected: " + transactionFailed);

        // Re-check balance
        String currentBalance = customerPage.getBalance();
        System.out.println("📊 Balance after failed withdrawal: " + currentBalance);

        // Assertions
        assertTrue(transactionFailed, "❌ Expected insufficient funds error.");
        assertEquals(String.valueOf(depositAmount), currentBalance, "❌ Balance should remain unchanged after failed withdrawal.");
    }


}
