package nokriPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NokriLogin {
	WebDriver driver;

	// Constructor
	public NokriLogin(WebDriver driver) {
		this.driver = driver;
	}

	//String usernameField = "";
	// Locators (replace with actual Naukri locators)
	By usernameField = By.xpath("//label[text()='Email ID / Username']/following-sibling::input"); // Replace with correct locator
	By passwordField = By.xpath("//label[text()='Password']/following-sibling::input"); // Replace with correct locator
	By loginButton = By.xpath("//button[text()='Login']"); // Replace with correct locator

	// Actions
	public void enterUsername(String username) {
		driver.findElement(usernameField).sendKeys(username);
	}

	public void enterPassword(String password) {
		driver.findElement(passwordField).sendKeys(password);
	}

	public void clickLogin() {
		driver.findElement(loginButton).click();
	}
}
