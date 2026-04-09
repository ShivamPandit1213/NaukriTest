package login;

<<<<<<< HEAD
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import publicMethod.PublicMethod;
import utils.ConfigReader;
import utils.LocatorReader;

public class Login {

    // 🔹 Step 1: Declare WebDriver (browser instance)
    // This will be received from Test class (NOT created here)
    private WebDriver driver;

    // 🔹 Step 2: Constructor Injection
    // When Login object is created, driver is passed from Test class
    public Login(WebDriver driver) {
        this.driver = driver; // Assign passed driver to class variable
    }

    // 🔹 Step 3: Main Login Method
    public void nokriLogin() throws Throwable {

        // 🔹 Step 4: Create object of reusable methods class
        // Used for waits, screenshots, etc.
        PublicMethod callCustomMethod = new PublicMethod(driver);

        // 🔹 Step 5: Get URL from config.properties
        // Example: naukri.login.url=https://www.naukri.com
        String naukriURL = ConfigReader.getConfig("naukri.login.url");

        // 🔹 Step 6: Open the URL in browser
        driver.get(naukriURL);

        // =========================================================

        // 🔹 Step 7: Click on Login link

        // Get locator from locator file
        String loginLink = LocatorReader.getLocator("loginLink");

        // Wait until element is visible
        WebElement wb = callCustomMethod.waitForElementVisible(loginLink);

        // Click on Login link
        wb.click();

        System.out.println("Login form field is visible.");

        // =========================================================

        // 🔹 Step 8: Enter Email

        // Get email locator
        String emailLocator = LocatorReader.getLocator("emailLocator");

        // Wait until element is clickable
        WebElement email = callCustomMethod.waitUntilElementClickable(emailLocator);

        // Get username from config file
        String user = ConfigReader.getConfig("naukri.user");

        // Enter username
        email.sendKeys(user);

        System.out.println("Entered Email Id: " + user);

        // =========================================================

        // 🔹 Step 9: Enter Password

        // Get password locator
        String passLocator = LocatorReader.getLocator("passLocator");

        // Find password field using driver
        WebElement passElement = driver.findElement(By.xpath(passLocator));

        // Get password from config file
        String pass = ConfigReader.getConfig("naukri.password");

        // Enter password
        passElement.sendKeys(pass);

        System.out.println("Entered Password: " + pass);

        // =========================================================

        // 🔹 Step 10: Take Screenshot (after entering credentials)
        Thread.sleep(3000); // ⚠️ Not recommended in real framework
        callCustomMethod.getScreenshot();

        // =========================================================

        // 🔹 Step 11: Click Login button

        // Get submit button locator
        String submitLogin = LocatorReader.getLocator("submitLogin");

        // Find element
        WebElement submitBtn = driver.findElement(By.xpath(submitLogin));

        // Click Login button
        submitBtn.click();

        System.out.println("Clicked on Login button");
        System.out.println("Login successful");

        // =========================================================
    }
}
=======
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import publicMethod.PublicMethod;
import utils.ConfigReader;

public class Login {
	private WebDriver driver;

	public Login(WebDriver driver) {
		this.driver = driver;
	}

	public void nokriLogin() throws InterruptedException, IOException {
		driver.get(ConfigReader.get("nokri_base.url"));
		
		driver.findElement(By.xpath("//a[text()='Login']")).click();

		String emailLocator = "//label[text()='Email ID / Username']/following-sibling::input";
		WebElement email = PublicMethod.waitUntilElementClickable(driver, emailLocator);
		String user = ConfigReader.get("naukri.user");
		email.sendKeys(user);
		System.out.println("Entered Email Id: " + user);

		String passLocator = "//label[text()='Password']/following-sibling::input";
		WebElement passElement = driver.findElement(By.xpath(passLocator));
		String pass = ConfigReader.get("naukri.password");
		passElement.sendKeys(pass);
		System.out.println("Entered Password: " + pass);

		driver.findElement(By.xpath("//button[text()='Login']")).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);
		System.out.println("Login successful");
	}
}
>>>>>>> 9da8f978834d8a72df51ff5d142f17babef96433
