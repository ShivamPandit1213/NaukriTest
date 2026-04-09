package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Nokri_Login {
	 WebDriver driver;

	    // Constructor
	    public Nokri_Login(WebDriver driver) {
	        this.driver = driver;
	    }

	    // Locators
	    private By usernameField = By.id("usernameField");    // Replace with actual ID
	    private By passwordField = By.id("passwordField");    // Replace with actual ID
	    private By loginButton = By.id("loginButton");        // Replace with actual ID

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

	    public void loginToNaukri(String username, String password) {
	        enterUsername(username);
	        enterPassword(password);
	        clickLogin();
	    }
}
