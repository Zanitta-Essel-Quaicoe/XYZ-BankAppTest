package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CustomerPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By transactionButton = By.xpath("//button[contains(text(),'Transactions')]");
    private By depositButton = By.xpath("//button[contains(text(),'Deposit')]");
    private By withdrawButton = By.xpath("//button[contains(text(),'Withdrawl')]");
    private By amountInput = By.xpath("//input[@placeholder='amount']");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By balanceLocator = By.xpath("//div[@ng-hide='noAccount']/strong[2]");
    private By customerNameLabel = By.xpath("//span[contains(@class,'fontBig')]");
    private By transactionTable = By.xpath("//table[contains(@class,'table-bordered')]");
    private By confirmationMessage = By.xpath("//span[contains(text(),'Deposit Successful') or contains(text(),'Transaction successful')]");
    private By errorMessage = By.xpath("//span[contains(text(),'Transaction Failed') or contains(text(),'Insufficient Funds')]");

    // Active tab locators - scoped to customer page
    private By activeDepositTab = By.xpath("//button[contains(text(),'Deposit') and contains(@class,'btn-primary')]");
    private By activeWithdrawTab = By.xpath("//button[contains(text(),'Withdrawl') and contains(@class,'btn-primary')]");

    public CustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isCustomerPageDisplayed() {
        try {
            WebElement nameLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(customerNameLabel));
            System.out.println("✅ Customer page displayed for: " + nameLabel.getText());
            return nameLabel.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("❌ Customer page not displayed.");
            return false;
        }
    }

    public void viewTransactions() {
        System.out.println("📜 Viewing transactions...");
        wait.until(ExpectedConditions.elementToBeClickable(transactionButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(transactionTable));
    }

    public boolean isTransactionPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(transactionTable));
            System.out.println("✅ Transactions page displayed.");
            return true;
        } catch (TimeoutException e) {
            System.out.println("❌ Failed to load transaction page.");
            return false;
        }
    }

    public void depositFunds(int amount) {
        System.out.println("💰 Depositing: " + amount);
        wait.until(ExpectedConditions.elementToBeClickable(depositButton)).click();
        ensureActiveTab(activeDepositTab);

        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(amountInput));
        amountField.clear();
        amountField.sendKeys(String.valueOf(amount));

        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationMessage));
        System.out.println("✅ Deposit successful.");
    }

    public void withdrawFunds(int amount) {
        System.out.println("💸 Withdrawing: " + amount);
        wait.until(ExpectedConditions.elementToBeClickable(withdrawButton)).click();
        ensureActiveTab(activeWithdrawTab);

        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(amountInput));
        amountField.clear();
        amountField.sendKeys(String.valueOf(amount));

        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationMessage));
            System.out.println("✅ Withdrawal successful.");
        } catch (TimeoutException e) {
            if (isTransactionFailed()) {
                System.out.println("❌ Insufficient funds error detected.");
            }
        }
    }

    public String getBalance() {
        try {
            WebElement balanceElement = wait.until(ExpectedConditions.presenceOfElementLocated(balanceLocator));
            String balance = balanceElement.getText().trim();
            System.out.println("📊 Current balance: " + balance);
            return balance;
        } catch (TimeoutException e) {
            System.out.println("❌ Failed to fetch balance.");
            return "N/A";
        }
    }

    public void waitForBalanceToUpdate(String expectedBalance) {
        try {
            System.out.println("⏳ Waiting for balance to update to: " + expectedBalance);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(balanceLocator, expectedBalance));
            System.out.println("✅ Balance updated.");
        } catch (TimeoutException e) {
            System.out.println("❌ Balance did not update to expected value: " + expectedBalance);
        }
    }

    public boolean isTransactionFailed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            System.out.println("❌ Transaction failed (insufficient funds or other error).");
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isDepositTabActive() {
        return isTabActive(activeDepositTab, "Deposit");
    }

    public boolean isWithdrawTabActive() {
        return isTabActive(activeWithdrawTab, "Withdraw");
    }

    private boolean isTabActive(By locator, String tabName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("✅ " + tabName + " tab is active.");
            return true;
        } catch (TimeoutException e) {
            System.out.println("❌ " + tabName + " tab is not active.");
            return false;
        }
    }

    private void ensureActiveTab(By activeTabLocator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(activeTabLocator));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Active tab not found: " + activeTabLocator);
        }
    }
}
