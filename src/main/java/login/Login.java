package login;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import helper.Locator;
import publicMethod.PublicMethod;
import utils.ConfigReader;
import utils.LocatorReader;

public class Login {

	public static void nokriLogin(WebDriver driver) throws IOException, InterruptedException {

		String naukriURL = ConfigReader.getConfig("naukri.login.url");
		driver.get("Naukri.login.url: "+naukriURL);

		// Click on link 'LogIn'
		String loginLink = LocatorReader.getLocator("loginLink");
		PublicMethod.waitForElementVisible(driver, loginLink);
		Locator.callXpathLocator(driver, loginLink).click();
		// loginPage.click();
		System.out.println("Login form field is visible.");

		// Enter on Email
		String emailLocator = LocatorReader.getLocator("emailLocator");
		WebElement email = PublicMethod.waitUntilElementClickable(driver, emailLocator);
		String user = ConfigReader.getConfig("naukri.user");
		email.sendKeys(user);
		System.out.println("Entered Email Id: " + user);

		// Enter on Password
		String passLocator = LocatorReader.getLocator("passLocator");
		WebElement passElement = driver.findElement(By.xpath(passLocator));
		String pass = ConfigReader.getConfig("naukri.password");
		passElement.sendKeys(pass);
		System.out.println("Entered Password: " + pass);

		// Capture screenshot while credentials filled
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);

		// Click on button 'LogIn' via Submit submitLogin
		String submitLogin = LocatorReader.getLocator("submitLogin");
		WebElement submitBtn = Locator.callXpathLocator(driver, submitLogin);
		submitBtn.click();
		System.out.println("Clicked on button 'LogIn' & Login successful");
		System.out.println("===================================================");
	}
}
